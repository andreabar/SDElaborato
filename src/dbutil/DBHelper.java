package dbutil;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

import model.Record;
import model.Search;

public class DBHelper {

	private static Connection connection;
	
	public static Connection connectToDB() {

		Builder.buildDB();
		Context initCtx;
		try {
			initCtx = new InitialContext();
			Context envCtx = (Context) initCtx.lookup("java:comp/env");

			DataSource ds = (DataSource)

			envCtx.lookup("jdbc/ElaboratoSD");

			return ds.getConnection();

		} catch (NamingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;

	}

	private static Connection getConnection() {

		if (connection == null)
			connection = connectToDB();
		return connection;
	}

	public static void saveRecords(ArrayList<Record> records, String input)
			throws SQLException {

		String query = "INSERT INTO record (type, language, link, title) VALUES ";
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
			statement.setString(4 * i + 3, records.get(i).getEuropeanaId());
			statement.setString(4 * i + 4, records.get(i).getTitle());

		}

		statement.executeUpdate();
		System.out.println(statement.getResultSetType());
		
		saveResources(records);
		saveResearch(records, input);
		
	}

	private static void saveResources(ArrayList<Record> records) throws SQLException {

		String query = "INSERT INTO location (record, url) VALUES ";
		int j = 1;

		
		for(Record r : records){
			ArrayList<String> resources = r.getWebResources();
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
			ArrayList<String> resources = r.getWebResources();
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

	private static int getRecordID(Record r) {

		String q = new String("SELECT rid FROM record WHERE STRCMP(link, '" + r.getEuropeanaId() + "') = 0;");
		System.out.println(q);
		try {
			ResultSet resultSet = getConnection().createStatement().executeQuery(q);
			int id = -1;
			while(resultSet.next())
				id = resultSet.getInt("rid");
			
			return id;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
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
		
		while (result.next()) {
			Date newDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(result.getString("date"));
			if(newDate.after(oldDate) && !(oldDate.equals(firstDate))){
				Search newSearch = new Search(records, oldDate, keyword);
				searches.add(newSearch);
				records = new ArrayList<Integer>();
			}
			records.add(result.getInt("record"));
			keyword = result.getString("keyword");
			oldDate = newDate;
			
		}
		Search newSearch = new Search(records, oldDate, keyword);
		searches.add(newSearch);

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
	

}
