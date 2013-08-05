package view.controllers;

import java.net.MalformedURLException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import com.github.wolfie.refresher.Refresher;
import com.github.wolfie.refresher.Refresher.RefreshListener;
import com.vaadin.event.LayoutEvents.LayoutClickEvent;
import com.vaadin.event.LayoutEvents.LayoutClickListener;
import com.vaadin.terminal.ExternalResource;
import com.vaadin.ui.AbstractComponent;
import com.vaadin.ui.Component;
import com.vaadin.ui.Link;
import com.vaadin.ui.ProgressIndicator;

import controllers.TaskController;

public class DownloadThread extends Thread{

	private Component progressBar;
	int taskId;
	private ResultSet result;
	ResultViewController view; 
	public DownloadThread(ResultViewController resultViewController, ResultSet result, ProgressIndicator p, int id) {
		
		view = resultViewController;
		this.result = result;
		progressBar = p;
		((AbstractComponent) progressBar).setImmediate(true);
		((ProgressIndicator) progressBar).setPollingInterval(1000);
		taskId = id;
		
	}
	 @Override
	public void run() {

		 
		 System.out.println("Started thread...");
		 
		 while((Float)((ProgressIndicator) progressBar).getValue() < 1f){
		 
			 System.out.println("INTO while");
		 try {
			 
			 ResultSet task = TaskController.getDownload(taskId);

			 if(task.next()){
				 
				long total = task.getLong("size");
				long temp = task.getLong("temp_size");
				
				
				((ProgressIndicator) progressBar).setValue(new Float((float)temp/(float)total));
				 
			 }
			 
			 
			
			Thread.sleep(1000);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
			try {
				progressBar = view.buildLinkFile(result, progressBar);
			} catch (SQLException e) {
				// TODO Auto-generated catch block
			}

		 }
	 }
}


