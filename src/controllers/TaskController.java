package controllers;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import dbutil.DBHelper;

import model.Record;
import model.Status;

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

						Date dt = new Date();
						SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
						String currentTime = sdf.format(dt);
						statement.setString(j, currentTime);
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
		
		String q = "select * from scheduled_task, record, result, query where user = " + userID + 
				" AND scheduled_task.record = record.id AND record.id IN (select record from result" + 
						" where query IN (select query from user_history where user = " + userID + ")) AND result.query = query.id AND result.record = record.id";

		
		java.sql.PreparedStatement statement = DBHelper.getConnection()
				.prepareStatement(q);
		
		ResultSet result = statement.executeQuery();
		
		return result;
	}

	public static ResultSet getDownload(int taskId) {

		try {
			ResultSet set = DBHelper.getConnection().createStatement().executeQuery("SELECT * FROM download WHERE task = " + taskId + ";");
			return set;
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
		
		
		
	}
	
	public static void removeNotDownloadableTask(int userID){
		String s = "DELETE FROM scheduled_task WHERE user = " + userID + " AND status = '" + Status.NOT_DOWNLOADABLE + "';"; 
		
		java.sql.PreparedStatement statement;
		try {
			statement = DBHelper.getConnection()
					.prepareStatement(s);
			statement.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
	}
	
	public static boolean isNotDownloadable(int scheduledTaskId){
		String q = "SELECT status FROM scheduled_task WHERE id = " + scheduledTaskId + ";";
		
		java.sql.PreparedStatement statement;
		try {
			statement = DBHelper.getConnection()
					.prepareStatement(q);
			ResultSet result = statement.executeQuery(q);
			if(result.next()){
				if(result.getString("status").equals(Status.NOT_DOWNLOADABLE)){
					return true;
				} else {
					return false;
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
			
		}
		return false;
		
	}

}
