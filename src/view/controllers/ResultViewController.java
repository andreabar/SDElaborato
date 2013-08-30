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

import model.Query;
import model.Record;
import model.Status;

import controllers.QueryController;
import controllers.RecordController;
import controllers.TaskController;

import util.AppData;
import util.PropertiesReader;
import view.views.ResultTab;


import com.github.wolfie.refresher.Refresher;
import com.vaadin.terminal.ExternalResource;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button;
import com.vaadin.ui.Component;
import com.vaadin.ui.Label;
import com.vaadin.ui.Link;
import com.vaadin.ui.ProgressIndicator;

import dbutil.DBHelper;


public class ResultViewController implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -5972665301072207312L;
	private ResultTab resultView;
	private Refresher refresher;
	public ResultViewController(ResultTab r){
		setResultView(r);

		resultView.getClear().addListener(new ClearListener(this));
	}
	
	public Object loadTableItem(ResultSet result){
		
		try{
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
			dateQuery = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(sDateQuery);
			String sDateDownload = result.getString("date_download");
			dateDownload = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(sDateDownload);
		} catch (ParseException e) {
			e.printStackTrace();
		} catch(SQLException e){
			
			dateDownload = null;
		}
		
		Object rowItem[] = new Object[]{title, type, keyword, provider, new String(), new Label(), dateQuery, dateDownload};
		Integer scheduledTaskId = result.getInt("id");
			
		this.resultView.getFileTable().addItem(rowItem, scheduledTaskId);
		return scheduledTaskId;
		
		} catch(SQLException e){
			e.printStackTrace();
			return null;
		}

		
		
		
	}
	
	public void loadResultTable(){
		this.resultView.getFileTable().removeAllItems();
		
		try {
			ResultSet result = TaskController.getScheduledTasks(AppData.userID);
			ResultSet tasks = TaskController.getWaitingTasks(AppData.userID);
			
			while(tasks.next()){
				
				Object id = loadTableItem(tasks );
				resultView.getFileTable().getItem(id).getItemProperty("Status").setValue("waiting");
				resultView.getFileTable().getItem(id).getItemProperty("Progress").setValue(new Label());
				
			}
			
			while(result.next()){

				Object id = loadTableItem(result);
				Integer scheduledTaskId = result.getInt("id");
				Component c = null;
				String statusCol = null;	

				
				String status = result.getString("status");
				
				if(status.equals(Status.SCHEDULED) || status.equals(Status.PROCESSING)){
						
						c = new Label("");
						statusCol = status;
					}
					

					else if(status.equals(Status.DOWNLOADING)){
						ArrayList<Long> list = getDownloadStatus(scheduledTaskId);
						c = new ProgressIndicator((float)list.get(0)/(float)list.get(1));
						statusCol = "Downloading..(" + Math.floor(list.get(1)/((float)1024*1024)) + " MB)";
					}
					
					
					else if (status.equals(Status.NOT_DOWNLOADABLE)) {
						c = new Link("Click to see online", new ExternalResource(result.getString("resource")));
						statusCol = "Not downloadable";
					}
					

					this.resultView.getFileTable().getItem(id).getItemProperty("Status").setValue(statusCol);
					this.resultView.getFileTable().getItem(id).getItemProperty("Progress").setValue(c);

					resultView.getFileTable().setSortContainerPropertyId("Status");
					resultView.getFileTable().setSortAscending(true);
					resultView.getFileTable().sort();
				}
				
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public void loadDownloadedFileTable(){
		this.resultView.getDownloadedFileTable().removeAllItems();
		try {
			ResultSet result = TaskController.getDownloadedFiles(AppData.userID);
			while(result.next()){
				
				Record record = RecordController.getRecord(result.getInt("record"));
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
					dateQuery = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(sDateQuery);
					dateDownload = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(sDateDownload);
				} catch (ParseException e) {
					e.printStackTrace();
				}
				Integer scheduledTaskId = result.getInt("id");

				Component c = buildLinkFile(result, null);

					Object rowItem[] = new Object[]{title, type, keyword, provider, c, dateQuery, dateDownload};
					
					this.resultView.getDownloadedFileTable().addItem(rowItem, scheduledTaskId);
					
					resultView.getDownloadedFileTable().setSortContainerPropertyId("Date Download");
					resultView.getDownloadedFileTable().setSortAscending(false);
					resultView.getDownloadedFileTable().sort();
				}
				
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public boolean isJunkDataInTable(){
		@SuppressWarnings("unchecked")
		Collection<Integer> ids = (Collection<Integer>) resultView.getFileTable().getItemIds();
		for(Integer i : ids){
			if(TaskController.isNotDownloadable(i)){
				return true;
			}
		}
		return false;
	}

	public Component buildLinkFile(ResultSet result, Component c)
			throws SQLException {
		try {
			ArrayList<URL> urls = getFileLink(result.getInt("id"));
			if(urls.size()>0){
				c = new Link("Click to open", new ExternalResource(
						urls.get(0).toString()));
			}
		} catch (MalformedURLException e) {
			System.out.println("out " + e.getMessage() );
		}
		return c;
	}

	private ArrayList<URL> getFileLink(int id) throws SQLException, MalformedURLException {

		String sql = "SELECT * from file WHERE scheduled_task = " + id + ";";
		ArrayList<URL> urls = new ArrayList<>();
		ResultSet query = DBHelper.getConnection().createStatement().executeQuery(sql);
			while(query.next()){
				urls.add(new URL("http://" + PropertiesReader.filesHost +  "/" + query.getString("path").replace(" ", "%20")));
			}
			
			return urls;
		
		
	}
	
	public ArrayList<Long> getDownloadStatus(int taskId){
		
		 try {
			 
			 ResultSet task = TaskController.getDownload(taskId);

			 if(task.next()){
				 
				long total = task.getLong("size");
				long temp = task.getLong("temp_size");

				ArrayList<Long> list = new ArrayList<>();
				list.add(temp);
				list.add(total);
				
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

}

class ClearListener implements Button.ClickListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8248905226288671342L;
	
	private ResultViewController rvc;

	public ClearListener(ResultViewController rvc){
		this.rvc = rvc;
	}
	
	@Override
	public void buttonClick(ClickEvent event) {
		TaskController.removeNotDownloadableTask(AppData.userID);
		rvc.loadResultTable();
		rvc.getResultView().getClear().setEnabled(false);
	}
	
}




