package controllers;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;

import dbutil.DBHelper;

import model.Record;

public class TaskController {

	public static void addTasks(List<Record> records, int userID)
			throws SQLException {

		String query = "INSERT INTO task (date, user, record, resource, type, provider) VALUES ";
		int j = 1;
		
		for(Record r : records){
			
			query += "(?,?,?,?,?,?)";

			if (records.indexOf(r) == records.size() - 1)
				query += ";";
			else query += ",";
		}

//		for (Record r : records) {
//			try {
//				for (String res : r.getWebResources()) {
//					query += "(?,?,?,?,?)";
//
//					if (records.indexOf(r) == records.size() - 1
//							&& r.getWebResources().indexOf(res) == r
//									.getWebResources().size() - 1)
//						query += ";";
//					else
//						query += ",";
//				}
//			} catch (Exception e) {
//				return;
//			}
//
//		}

		java.sql.PreparedStatement statement = DBHelper.getConnection()
				.prepareStatement(query);
		
		for (Record r : records) {
			int id = DBHelper.getRecordID(r);
			if (id != -1) {

				try {
//					for (String s : r.getWebResources()) {
						statement.setDate(j,
								new java.sql.Date(((new Date()).getTime())));
						j++;
						statement.setInt(j, userID);
						j++;
						statement.setInt(j, id);
						j++;
						statement.setString(j, r.getUniqueUrl());
						j++;
						statement.setString(j, r.getType());
						j++;
						statement.setString(j, r.getProvider());
						j++;

					}
				 catch (Exception e) {
					e.printStackTrace();
				}
			}

			}
		
		
		
//		for (Record r : records) {
//			int id = DBHelper.getRecordID(r);
//			if (id != -1) {
//
//				try {
//					for (String s : r.getWebResources()) {
//						statement.setDate(j,
//								new java.sql.Date(((new Date()).getTime())));
//						j++;
//						statement.setInt(j, userID);
//						j++;
//						statement.setInt(j, id);
//						j++;
//						statement.setString(j, s);
//						j++;
//						statement.setString(j, r.getType());
//						j++;
//
//					}
//				} catch (Exception e) {
//					e.printStackTrace();
//				}
//
//			}
//		}

		statement.executeUpdate();

	}
	
	public static ResultSet getResults(int userID) throws SQLException{
		String q = "SELECT scheduled_task.resource, record.title, record.type, scheduled_task.status, scheduled_task.id, scheduled_task.date FROM scheduled_task inner join " +
				"record on scheduled_task.record = record.id WHERE scheduled_task.user = " + userID + ";";
		
		java.sql.PreparedStatement statement = DBHelper.getConnection()
				.prepareStatement(q);
		
		ResultSet result = statement.executeQuery();
		
		return result;
	}

	public static ResultSet getDownload(int taskId) {

		try {
			System.out.println("SELECT * FROM sd.download WHERE task = " + taskId + ";");
			ResultSet set = DBHelper.getConnection().createStatement().executeQuery("SELECT * FROM sd.download WHERE task = " + taskId + ";");
			return set;
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
		
		
		
	}

}
