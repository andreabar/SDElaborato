package controllers;

import java.io.File;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import shared.PropertiesReader;
import util.AppData;

import dbutil.DBHelper;

import model.Record;
import model.Status;

public class TaskController {

	public static void addTasks(List<Record> records, int userID, int queryID)
			throws SQLException {

		String query = "INSERT INTO task (date, user, record, resource, type, provider, query) VALUES ";
		int j = 1;
		
		for(Record r : records){
			
			query += "(?,?,?,?,?,?,?)";

			if (records.indexOf(r) == records.size() - 1)
				query += ";";
			else query += ",";
		}
		
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
						statement.setInt(j, queryID);
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
		statement.close();

	}
	
	public static ResultSet getScheduledTasks(int userID) throws SQLException{
		

		String sql = "SELECT * FROM scheduled_task WHERE user = " + userID + " AND (status NOT LIKE '" + Status.DOWNLOADED + "' AND status NOT LIKE '" + Status.NOT_DOWNLOADABLE+"')";
		
		
		java.sql.PreparedStatement statement = DBHelper.getConnection()
				.prepareStatement(sql);
		
		ResultSet result = statement.executeQuery();
				
		return result;
	}
	
	public static ResultSet getDownloadedFiles(int userID) throws SQLException {
		String sql = "SELECT * FROM scheduled_task WHERE user = " + userID + " AND (status LIKE " + "'" + Status.DOWNLOADED + "' OR status LIKE '" + Status.NOT_DOWNLOADABLE +"')";
		
		
		java.sql.PreparedStatement statement = DBHelper.getConnection()
				.prepareStatement(sql);
		
		ResultSet result = statement.executeQuery();
				
		return result;

	}

	public static ResultSet getDownload(int taskId) {

		try {
			String sql = "SELECT * FROM download WHERE task = " + taskId + ";";
			ResultSet set = DBHelper.getConnection().createStatement().executeQuery(sql);
			return set;
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
		
		
		
	}
	
	public static void removeNotDownloadableTasks(int userID){
		String s = "DELETE FROM scheduled_task WHERE user = " + userID + " AND status = '" + Status.NOT_DOWNLOADABLE + "';"; 
		
		java.sql.PreparedStatement statement;
		try {
			statement = DBHelper.getConnection()
					.prepareStatement(s);
			statement.executeUpdate();
			statement.close();
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
					statement.close();
					result.close();
					return true;
				} else {
					statement.close();
					result.close();
					return false;
				}
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
			
		}
		return false;
		
	}

	public static ResultSet getWaitingTasks(int userID) throws SQLException {
		String sql = "SELECT * FROM task WHERE user = " + userID;
		
		
		java.sql.PreparedStatement statement = DBHelper.getConnection()
				.prepareStatement(sql);
		
		ResultSet result = statement.executeQuery();
		
		
		return result;
		
	}
	
	public static void deleteTask(int taskID){
		String s = "DELETE FROM scheduled_task WHERE id = " +taskID+ " ;";
		try {
			Statement statement = DBHelper.getConnection().createStatement();

			statement.execute("DELETE FROM file WHERE scheduled_task = " + taskID);			
			statement.execute(s);
			DBHelper.getConnection().commit();

		} catch (SQLException e) {
			e.printStackTrace();
			try {
				DBHelper.getConnection().rollback();
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
		
		
		

	}

	

}
