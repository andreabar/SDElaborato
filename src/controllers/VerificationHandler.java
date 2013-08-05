package controllers;

import java.util.List;

import model.Record;

public class VerificationHandler {

	private List<Record> records;
	private int userID;

	public VerificationHandler(List<Record> records, int id) {
		super();
		this.records = records;
		userID = id;
		
	}

	public void initializeResources() throws Exception {

//		RecordController.saveResources(records);
		TaskController.addTasks(records, userID);
	}
	
	
	
}
