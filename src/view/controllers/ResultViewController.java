package view.controllers;

import java.io.Serializable;
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.XML;

import refresher.Refresher;
import shared.PropertiesReader;
import model.Query;
import model.Record;
import model.Status;

import controllers.QueryController;
import controllers.RecordController;
import controllers.TaskController;

import util.AppData;
import view.views.ResultTab;


import com.vaadin.data.Property;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.terminal.ExternalResource;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button;
import com.vaadin.ui.Component;
import com.vaadin.ui.Label;
import com.vaadin.ui.Link;
import com.vaadin.ui.ProgressIndicator;

import dbutil.DBHelper;

public class ResultViewController implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5972665301072207312L;
	private ResultTab resultView;
	private Refresher refresher;

	public ResultViewController(ResultTab r) {
		setResultView(r);
		refresher = new Refresher(this);
		resultView.addComponent(refresher);
		
		resultView.getClear().addListener(new ClearListener(this));
		resultView.getDeleteSelected().addListener(
				new DeleteSelectedListener(this));
		resultView.getSeeMetadata().addListener(new SeeMetadataListener(this));
		resultView.getFileTable().addListener(
				new Property.ValueChangeListener() {

					/**
			 * 
			 */
					private static final long serialVersionUID = -5800779526323410471L;

					@Override
					public void valueChange(ValueChangeEvent event) {
						if (event.getProperty().getValue() != null)
							resultView.getDeleteSelected().setEnabled(true);
						else
							resultView.getDeleteSelected().setEnabled(false);
					}
				});

		resultView.getDownloadedFileTable().addListener(
				new Property.ValueChangeListener() {

					/**
			 * 
			 */
					private static final long serialVersionUID = -5800779526323410471L;

					@Override
					public void valueChange(ValueChangeEvent event) {
						if (event.getProperty().getValue() != null)
							resultView.getSeeMetadata().setEnabled(true);
						else
							resultView.getSeeMetadata().setEnabled(false);
					}
				});

	}

	public Object loadTableItem(ResultSet result) {

		try {
			Record record = RecordController.getRecord(result.getInt("record"));
			Query q = QueryController.getQuery(result.getInt("query"));
			String title = record.getTitle();
			String type = record.getType();
			String keyword = q.getKeyword();
			String provider = record.getProvider();
			String sDateQuery = q.getDate();

			Date dateQuery = null;
			Date dateDownload = null;
			try {
				dateQuery = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
						.parse(sDateQuery);
				String sDateDownload = result.getString("date_download");
				dateDownload = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
						.parse(sDateDownload);
			} catch (ParseException e) {
				e.printStackTrace();
			} catch (SQLException e) {

				dateDownload = null;
			}

			Object rowItem[] = new Object[] { title, type, keyword, provider,
					new String(), new Label(), dateQuery, dateDownload };
			Integer scheduledTaskId = result.getInt("id");

			this.resultView.getFileTable().addItem(rowItem, scheduledTaskId);
			
			return scheduledTaskId;

		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}

	}

	public void loadResultTable() {
		this.resultView.getFileTable().removeAllItems();

		try {
			ResultSet result = TaskController.getScheduledTasks(AppData.userID);
			ResultSet tasks = TaskController.getWaitingTasks(AppData.userID);
			while (tasks.next()) {

				Object id = loadTableItem(tasks);
				resultView.getFileTable().getItem(id).getItemProperty("Status")
						.setValue("waiting");
				resultView.getFileTable().getItem(id)
						.getItemProperty("Progress").setValue(new Label());

			}

			while (result.next()) {

				Object id = loadTableItem(result);
				Integer scheduledTaskId = result.getInt("id");
				Component c = null;
				String statusCol = null;

				String status = result.getString("status");

				if (status.equals(Status.SCHEDULED)
						|| status.equals(Status.PROCESSING)) {

					c = new Label("");
					statusCol = status;
				}

				else if (status.equals(Status.DOWNLOADING)) {
					ArrayList<Long> list = getDownloadStatus(scheduledTaskId);
					c = new ProgressIndicator((float) list.get(0)
							/ (float) list.get(1));
					statusCol = "Downloading..("
							+ Math.floor(list.get(1) / ((float) 1024 * 1024))
							+ " MB)";
				}

				else if (status.equals(Status.NOT_DOWNLOADABLE)) {
					c = new Link("Click to see online", new ExternalResource(
							result.getString("resource"), "_blank"));
					statusCol = "Not downloadable";
				}

				this.resultView.getFileTable().getItem(id)
						.getItemProperty("Status").setValue(statusCol);
				this.resultView.getFileTable().getItem(id)
						.getItemProperty("Progress").setValue(c);

				resultView.getFileTable().setSortContainerPropertyId("Status");
				resultView.getFileTable().setSortAscending(true);
				resultView.getFileTable().sort();
			}
			result.getStatement().close();
			tasks.getStatement().close();
			this.resultView.getFileTable().setCaption("Files in Download (Total items : " + 
			this.resultView.getFileTable().size() + ")");

		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public void loadDownloadedFileTable() {
		this.resultView.getDownloadedFileTable().removeAllItems();
		try {
			ResultSet result = TaskController
					.getDownloadedFiles(AppData.userID);
			while (result.next()) {

				Record record = RecordController.getRecord(result
						.getInt("record"));
				Query q = QueryController.getQuery(result.getInt("query"));

				String title = record.getTitle();
				String type = record.getType();
				String keyword = q.getKeyword();
				String provider = record.getProvider();
				String sDateQuery = q.getDate();
				String sDateDownload = result.getString("date_download");
				Date dateQuery = null;
				Date dateDownload = null;
				try {
					dateQuery = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
							.parse(sDateQuery);
					dateDownload = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
							.parse(sDateDownload);
				} catch (ParseException e) {
					e.printStackTrace();
				}
				Integer scheduledTaskId = result.getInt("id");

				Component c = buildLinkFile(result, null);

				Object rowItem[] = new Object[] { title, type, keyword,
						provider, c, dateQuery, dateDownload };

				this.resultView.getDownloadedFileTable().addItem(rowItem,
						scheduledTaskId);

				resultView.getDownloadedFileTable().setSortContainerPropertyId(
						"Date Download");
				resultView.getDownloadedFileTable().setSortAscending(false);
				resultView.getDownloadedFileTable().sort();
			}
			this.resultView.getDownloadedFileTable().setCaption("Downloaded Files (Total items : " + 
					this.resultView.getDownloadedFileTable().size() + ")");
			result.getStatement().close();
			result.close();
			} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public boolean isJunkDataInTable() {
		@SuppressWarnings("unchecked")
		Collection<Integer> ids = (Collection<Integer>) resultView
				.getFileTable().getItemIds();
		for (Integer i : ids) {
			if (getResultView().getFileTable().getItem(i)
					.getItemProperty("Status").getValue().toString()
					.equals(Status.NOT_DOWNLOADABLE)) {
				return true;
			}
		}
		return false;
	}

	public Component buildLinkFile(ResultSet result, Component c)
			throws SQLException {
		try {
			ArrayList<URL> urls = getFileLink(result.getInt("id"));
			if (urls.size() > 0) {
				c = new Link("Click to open", new ExternalResource(urls.get(0)
						.toString()));

			}
		} catch (MalformedURLException e) {
			System.out.println("out " + e.getMessage());
		}
		return c;
	}

	private ArrayList<URL> getFileLink(int id) throws SQLException,
			MalformedURLException {

		String sql = "SELECT * from file WHERE scheduled_task = " + id + ";";
		ArrayList<URL> urls = new ArrayList<>();
		ResultSet query = DBHelper.getConnection().createStatement()
				.executeQuery(sql);
		while (query.next()) {
			urls.add(new URL("http://" + PropertiesReader.getFilesHost() + "/"
					+ query.getString("path").replace(" ", "%20")));
		}
		
		query.close();

		return urls;

	}

	public ArrayList<Long> getDownloadStatus(int taskId) {

		try {

			ResultSet task = TaskController.getDownload(taskId);

			if (task.next()) {

				long total = task.getLong("size");
				long temp = task.getLong("temp_size");

				ArrayList<Long> list = new ArrayList<>();
				list.add(temp);
				list.add(total);
				
				task.close();

				return list;

			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		return new ArrayList<>();

	}

	public ResultTab getResultView() {
		return resultView;
	}

	public void setResultView(ResultTab resultView) {
		this.resultView = resultView;
	}

	public Refresher getRefresher() {
		return refresher;
	}

	public void setRefresher(Refresher refresher) {
		this.refresher = refresher;
	}

	public void enableClear() {
		resultView.getClear().setEnabled(isJunkDataInTable());
	}

}

class ClearListener implements Button.ClickListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8248905226288671342L;

	private ResultViewController rvc;

	public ClearListener(ResultViewController rvc) {
		this.rvc = rvc;
	}

	@Override
	public void buttonClick(ClickEvent event) {
		try {
			TaskController.removeNotDownloadableTasks(AppData.userID);
			DBHelper.getConnection().commit();
			rvc.loadResultTable();
			rvc.getResultView().getClear().setEnabled(false);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

}


class DeleteSelectedListener implements Button.ClickListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7854115335214054453L;
	ResultViewController controller;

	public DeleteSelectedListener(ResultViewController rvc) {
		controller = rvc;
	}

	@Override
	public void buttonClick(ClickEvent event) {

		Object selected = controller.getResultView().getFileTable().getValue();
		try {
			DBHelper.deleteTask((int) selected);
			DBHelper.getConnection().commit();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		controller.loadResultTable();

	}

}

class SeeMetadataListener implements Button.ClickListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2550182749932062225L;
	ResultViewController controller;

	public SeeMetadataListener(ResultViewController rvc) {
		controller = rvc;
	}

	@Override
	public void buttonClick(ClickEvent event) {

		Object task = controller.getResultView().getDownloadedFileTable()
				.getValue();
		JSONObject o;
		try {
			o = DBHelper.getMetadata((int) task);
			com.vaadin.ui.Window w = new com.vaadin.ui.Window("Metadata");
			Label data = new Label(XML.toString(o.toString()));
			w.center();
			w.setWidth("50%");

			w.setHeight("50%");
			data.setSizeFull();

			data.setContentMode(Label.CONTENT_XML);
			w.addComponent(data);

			controller.getResultView().getApplication().getMainWindow()
					.addWindow(w);

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
