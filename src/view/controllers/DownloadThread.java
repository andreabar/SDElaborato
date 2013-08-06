package view.controllers;

import java.sql.ResultSet;
import java.sql.SQLException;

import com.vaadin.ui.AbstractComponent;
import com.vaadin.ui.Component;
import com.vaadin.ui.ProgressIndicator;

import controllers.TaskController;

public class DownloadThread extends Thread{

	private Component progressBar;
	int taskId;
	ResultViewController view; 
	public DownloadThread(ResultViewController resultViewController, ProgressIndicator p, int id) {
		
		view = resultViewController;
		progressBar = p;
		((AbstractComponent) progressBar).setImmediate(true);
		((ProgressIndicator) progressBar).setPollingInterval(1000);
		taskId = id;
		
	}
	 @Override
	public void run() {

		 
		 System.out.println("Started thread...");
		 
		 while((Float)((ProgressIndicator) progressBar).getValue() < 1f){
		 
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

		 }
	 }
}


