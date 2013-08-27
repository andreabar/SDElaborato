package dbutil;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

import util.AppData;
import util.PropertiesReader;

import model.Record;

public class DBHelper {

	private static Connection connection;
	
	public static Connection connectToDB() {


		try {

		
		Context initCtx;
			initCtx = new InitialContext();
			Context envCtx = (Context) initCtx.lookup("java:comp/env");

			DataSource ds = (DataSource) envCtx.lookup("jdbc/" + PropertiesReader.dbName);

			connection =  ds.getConnection();
			return connection;

		} catch (NamingException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;

	}

	public static Connection getConnection() {

		return connection;
	}

	
	
	

	public static int getRecordID(Record r) {

		String q = new String("SELECT id FROM record WHERE STRCMP(url, '" + r.getUniqueUrl() + "') = 0;");
		try {
			ResultSet resultSet = getConnection().createStatement().executeQuery(q);
			int id = -1;
			while(resultSet.next())
				id = resultSet.getInt("id");
			
			return id;
		} catch (SQLException e) {
			e.printStackTrace();
			return -1;
		}
		
	}
			
	
	
	public static ResultSet getDetails(Date d, String k) throws SQLException{
		
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String date = sdf.format(d);
		
		String q = new String("SELECT title, type, language, url FROM record inner join " +
				"search on record.rid = search.record inner join location on location.record = record.rid" +
				" WHERE search.date = '" + date + "' AND search.keyword = '" + k + "' ;");
		java.sql.PreparedStatement statement = getConnection()
				.prepareStatement(q);
		
		ResultSet result = statement.executeQuery();	
		
		return result;
		
	}

	public static ResultSet executePreparedStatement(String q, String[] values) {

		try {
			java.sql.PreparedStatement stat = getConnection().prepareStatement(q);

			for(int i = 0; i<values.length; i++)
				stat.setString(i+1, values[i]);
			
			System.out.println(stat.toString());
			ResultSet resultSet = stat.executeQuery();
			return resultSet;
			
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
		
	}
	
	public static String getUserName(int uid){
		String q = "SELECT username FROM user WHERE id = " + uid + ";";
		
		String name = null;
		java.sql.PreparedStatement statement;
		try {
			statement = getConnection()
					.prepareStatement(q);
			ResultSet result = statement.executeQuery();	

			if(result.next()){
				name = result.getString("username");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return name;
	}
	
	public static int handleLogin(String u, String p) {

		String q = "SELECT id FROM user WHERE username = ? and password = ?";
		
		ResultSet set = DBHelper.executePreparedStatement(q, new String[]{u,p});
		
		if(set == null)
			return -1;
		
		try {
			if(set.next())
				return set.getInt("id");
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return -1;
		
		
		
	}
	
	public static boolean newUser(String email, String pass) throws SQLException{
		if(!org.apache.commons.validator.routines.EmailValidator.getInstance().isValid(email)){
			return false;
		}
		
		String s = "INSERT INTO user (username, password) VALUES ('" + email + "', '" + pass + "');";
		
		java.sql.PreparedStatement stat = getConnection().prepareStatement(s);
		
		stat.executeUpdate();
		
		return true;
	}
	

}
