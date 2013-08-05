package view.controllers;

import java.io.Serializable;
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import controllers.TaskController;

import util.AppData;
import views.ResultView;

import com.github.wolfie.refresher.Refresher;
import com.github.wolfie.refresher.Refresher.RefreshListener;
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
	private Refresher refresher;
	
	public ResultViewController(ResultView r){
		setResultView(r);
		
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
				
				if(status.equals("scheduled")){
					
					c = new Label("Processing url..");
				}
				
				else if(status.equals("downloaded")) {
					
					c = buildLinkFile(result, c);
					
				}
				else if(status.equals("downloading")){
					c = new ProgressIndicator();
				
					DownloadThread d = new DownloadThread(this, result, (ProgressIndicator) c, scheduledTaskId);
					d.start();
				}
				
				
				else if (status.equals("Not downloadable")) {
					c = new Link(status + ": click to see online", new ExternalResource(result.getString("resource")));
					
				}
				
				Object rowItem[] = new Object[]{title, type, keyword, provider, c, dateQuery, dateDownload};
				
				this.resultView.getFileTable().addItem(rowItem, scheduledTaskId);
				
				resultView.getFileTable().setSortContainerPropertyId("Date");
				resultView.getFileTable().setSortAscending(true);
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
				c = new Link("Downloaded: click to open", new ExternalResource(
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

	public Refresher getRefresher() {
		return refresher;
	}

	public void setRefresher(Refresher refresher) {
		this.refresher = refresher;
	}

}





