package controllers;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

import model.Record;

public class DBHelper {

	private static Connection connection;
	
	public static Connection connectToDB() {

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

	public static void saveRecords(ArrayList<Record> records)
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
			
		
	

}
