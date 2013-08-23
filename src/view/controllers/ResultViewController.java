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

import model.Status;

import controllers.TaskController;

import util.AppData;
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
	private static final String downloadHost = "192.168.1.1";
	public ResultViewController(ResultTab r){
		setResultView(r);

		resultView.getClear().addListener(new ClearListener(this));
	}
	
	public void loadResultTable(){
		this.resultView.getFileTable().removeAllItems();
		try {
			ResultSet result = TaskController.getResults(AppData.userID);
			while(result.next()){
				String title = result.getString("title");
				String type = result.getString("type");
				String keyword = result.getString("keyword");
				String provider = result.getString("provider");
				String status = result.getString("status");
				String sDateQuery = result.getString("date");
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

				Component c = null;
				String statusCol = null;
				
				if(status.equals(Status.SCHEDULED) || status.equals(Status.PROCESSING)){
					
					c = new Label("");
					statusCol = status;
				}
				
				else if(status.equals(Status.DOWNLOADED)) {
					
					c = buildLinkFile(result, c);
					statusCol = "Downloaded";
					
				}
				else if(status.equals(Status.DOWNLOADING)){
					c = new ProgressIndicator();
					statusCol = "Downloading..";
					DownloadThread d = new DownloadThread(this, (ProgressIndicator) c, scheduledTaskId);
					d.start();
				}
				
				
				else if (status.equals(Status.NOT_DOWNLOADABLE)) {
					c = new Link("Click to see online", new ExternalResource(result.getString("resource")));
					statusCol = "Not downloadable";
				}
				

				Object rowItem[] = new Object[]{title, type, keyword, provider, statusCol, c, dateQuery, dateDownload};
				
				this.resultView.getFileTable().addItem(rowItem, scheduledTaskId);
				
				resultView.getFileTable().setSortContainerPropertyId("Status");
				resultView.getFileTable().setSortAscending(true);
				resultView.getFileTable().sort();
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
				System.out.println(urls.get(0).toString().replace(" ", "%20"));
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
		System.out.println(sql);
			ResultSet query = DBHelper.getConnection().createStatement().executeQuery(sql);
			while(query.next()){
				urls.add(new URL("http://" + downloadHost +  "/" + query.getString("path").replace(" ", "%20")));
			}
			
			return urls;
		
		
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




