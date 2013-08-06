package view.controllers;

import java.io.Serializable;
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;

import controllers.TaskController;

import util.AppData;
import views.ResultView;

import com.vaadin.event.LayoutEvents.LayoutClickEvent;
import com.vaadin.event.LayoutEvents.LayoutClickListener;
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
	private ResultView resultView;
	
	public ResultViewController(ResultView r){
		setResultView(r);
		this.getResultView().getRefreshButton().addListener(new RefreshButtonListener(this));
		loadResultTable();
		resultView
		.setImmediate(true);
	
		
	}
	
	public void loadResultTable(){
		this.resultView.getFileTable().removeAllItems();
		try {
			ResultSet result = TaskController.getResults(AppData.userID);
			while(result.next()){
				String title = result.getString("title");
				String type = result.getString("type");
				String status = result.getString("status");
				Date date = result.getDate("date");
				Integer scheduledTaskId = result.getInt("id");

				Component c = null;
				String statusCol = null;
				
				if(status.equals("scheduled")){
					
					c = new Label("");
					statusCol = "Processing url..";
				}
				
				else if(status.equals("downloaded")) {
					
					c = buildLinkFile(result, c);
					statusCol = "Downloaded";
					
				}
				else if(status.equals("downloading")){
					c = new ProgressIndicator();
					statusCol = "Downloading..";
					DownloadThread d = new DownloadThread(this, (ProgressIndicator) c, scheduledTaskId);
					d.start();
				}
				
				
				else if (status.equals("Not downloadable")) {
					c = new Link("Click to see online", new ExternalResource(result.getString("resource")));
					statusCol = "Not downloadable";
				}
				
				Object rowItem[] = new Object[]{title, type, statusCol, c, date};
				
				this.resultView.getFileTable().addItem(rowItem, scheduledTaskId);
				
				resultView.getFileTable().setSortContainerPropertyId("Date");
				resultView.getFileTable().setSortAscending(false);
				resultView.getFileTable().sort();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
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

		String sql = "SELECT * from sd.file WHERE scheduled_task = " + id + ";";
		ArrayList<URL> urls = new ArrayList<>();
		System.out.println(sql);
			ResultSet query = DBHelper.getConnection().createStatement().executeQuery(sql);
			while(query.next()){
				urls.add(new URL("http://" + query.getString("path").replace(" ", "%20")));
			}
			
			return urls;
		
		
	}

	public ResultView getResultView() {
		return resultView;
	}

	public void setResultView(ResultView resultView) {
		this.resultView = resultView;
	}

}

class RefreshButtonListener implements Button.ClickListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6367448598090263990L;
	private ResultViewController resultViewController;
	
	public RefreshButtonListener(ResultViewController rvc){
		this.resultViewController = rvc;
	}

	public void buttonClick(ClickEvent event) {
		this.resultViewController.loadResultTable();
	}
	
}


