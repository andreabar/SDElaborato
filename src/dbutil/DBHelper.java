package dbutil;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

import com.mysql.jdbc.PreparedStatement;
import com.vaadin.data.validator.EmailValidator;

import org.apache.commons.validator.routines.*;

import model.Record;
import model.Search;

public class DBHelper {

	private static Connection connection;
	
	public static Connection connectToDB() {

//		Builder.buildDB();
		Context initCtx;
		try {
			initCtx = new InitialContext();
			Context envCtx = (Context) initCtx.lookup("java:comp/env");

			DataSource ds = (DataSource) envCtx.lookup("jdbc/sd");

			return ds.getConnection();

		} catch (NamingException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;

	}

	public static Connection getConnection() {

		if (connection == null)
			connection = connectToDB();
		return connection;
	}

	public static void saveRecords(ArrayList<Record> records, String input)
			throws Exception {

		String query = "INSERT INTO record (type, language, url, title) VALUES ";
		for (int i = 0; i < records.size(); i++) {
			query += "(?,?,?,?)";
			if (i < records.size() - 1)
				query += ",";
			else
				query += ";";
		}

		java.sql.PreparedStatement statement = getConnection()
				.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);

		for (int i = 0; i < records.size(); i++) {
			statement.setString(4 * i + 1, records.get(i).getType());
			statement.setString(4 * i + 2, records.get(i).getLanguage());
			statement.setString(4 * i + 3, records.get(i).getUniqueUrl());
			statement.setString(4 * i + 4, records.get(i).getTitle());

		}

		statement.executeUpdate();
		
		saveResources(records);
		saveResearch(records, input);
		
	}

	private static void saveResources(ArrayList<Record> records) throws Exception {

		String query = "INSERT INTO location (record, url) VALUES ";
		int j = 1;

		
		for(Record r : records){
			List<String> resources = r.getWebResources();
			for(String s : resources){
				
				query += "(?,?),";
			}
		}
		
		String update = query.substring(0, query.length()-1) + ";";
		java.sql.PreparedStatement statement = getConnection()
				.prepareStatement(update);
		for(Record r : records){
			int id = getRecordID(r);
			if(id != -1){
			List<String> resources = r.getWebResources();
			for(String s : resources){
				statement.setInt(j, id);
				j++;
				statement.setString(j, s);
				j++;			}
			}
		}
			statement.executeUpdate();

		}
	
	private static void saveResearch (ArrayList<Record> records, String input) throws SQLException {
		Date now = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String currentTime = sdf.format(now);
		String query = "INSERT INTO search (record, date, keyword) VALUES ";

		for (int i = 0; i < records.size(); i++) {
			query += "(?,?,?)";
			if (i < records.size() - 1)
				query += ",";
			else
				query += ";";
		}

		java.sql.PreparedStatement statement = getConnection()
				.prepareStatement(query);

		int j = 1;

		for(Record r : records){
			int id = getRecordID(r);
			if(id != -1){
				statement.setInt(j, id);
				j++;
				statement.setString(j, currentTime);
				j++;
				statement.setString(j, input);
				j++;
			}

		}
		statement.executeUpdate();
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
			
	public static ArrayList<Search> getSearches() throws SQLException, ParseException{
		ArrayList<Search> searches = new ArrayList<Search>();
		ArrayList<Integer> records = new ArrayList<Integer>();
		
		String q = new String("SELECT * FROM search");
		
		java.sql.PreparedStatement statement = getConnection()
				.prepareStatement(q);
		
		ResultSet result = statement.executeQuery();
		Date firstDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse("1970-01-01 00:00:00");
		Date oldDate = firstDate;
		String keyword = new String();
		
		while(result.next()){

			Search s = new Search(records, result.getDate("date"), result.getString("keyword"));
			records.add(result.getInt("record"));
			searches.add(s);
		}
		
//		while (result.next()) {
//			Date newDate = (result.getDate("date"));
//			if(newDate.after(oldDate) && !(oldDate.equals(firstDate))){
//				Search newSearch = new Search(records, oldDate, keyword);
//				searches.add(newSearch);
//				records = new ArrayList<Integer>();
//			}
//			keyword = result.getString("keyword");
//			oldDate = newDate;
//			
//		}
//		Search newSearch = new Search(records, oldDate, keyword);
//		searches.add(newSearch);

		return searches;
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
			// TODO Auto-generated catch block
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
			// TODO Auto-generated catch block
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
