package controllers;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import model.Record;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.vaadin.Application;

import dbutil.DBHelper;

public class RecordController {

	
	public static List<String> getWebResources(Record r) throws Exception
			 {
		
		int id = DBHelper.getRecordID(r);

		List<String> list = 

		checkForResources(id);
		
		if(!list.isEmpty())
			return list;
		else
		{
			downloadResources(r, list);
			return list;

		}

		
		
	}

	private static List<String> downloadResources(Record r, List<String> list) throws Exception {
		BufferedReader in;
		
		in = new BufferedReader(new InputStreamReader(new URL(
				r.getUniqueUrl()).openStream()));
	
		String inputLine = new String();
		JSONObject o = null;
		while ((inputLine = in.readLine()) != null) {

			 o = new JSONObject(inputLine);
			}
		
		JSONArray array = o.getJSONObject("object").getJSONArray(
				"aggregations");
		JSONArray webRes = array.getJSONObject(0).getJSONArray("webResources");
		for (int j = 0; j < webRes.length(); j++)
					list.add(webRes.getJSONObject(j).getString("about"));
		
		return list;
		
		
	}

	private static List<String> checkForResources(int id) throws SQLException {

		List<String> resources = new ArrayList<String>();
		String q = "SELECT url FROM resource WHERE record = " + id;
		
		ResultSet set = DBHelper.getConnection().createStatement().executeQuery(q);
		while(set.next()){
			 resources.add(set.getString("url"));
		}
		
		return resources;
	}

	public static void saveRecords(ArrayList<Record> records) throws SQLException {

		
			String query = "INSERT INTO record (query, type, language, url, title, ipr_type, provider) VALUES ";
			for (int i = 0; i < records.size(); i++) {
				query += "(?,?,?,?,?,?,?)";
				if (i < records.size() - 1)
					query += ",";
				else
					query += ";";
			}

			java.sql.PreparedStatement statement = DBHelper.getConnection()
					.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);

			for (int i = 0; i < records.size(); i++) {
				statement.setInt(7 * i + 1, records.get(i).getQueryID());
				statement.setString(7 * i + 2, records.get(i).getType());
				statement.setString(7 * i + 3, records.get(i).getLanguage());
				statement.setString(7 * i + 4, records.get(i).getUniqueUrl());
				statement.setString(7 * i + 5, records.get(i).getTitle());
				statement.setString(7 * i + 6, records.get(i).getRights());
				statement.setString(7 * i + 7, records.get(i).getProvider());

				

			}

			statement.executeUpdate();
			
//			saveResources(records);
	}


	public static void saveResources(List<Record> records) throws Exception {

		
		String query = "INSERT INTO resource (record, url) VALUES ";
		int j = 1;

		
		for(Record r : records){
			List<String> resources = r.getWebResources();
			for(String s : resources){
				
				query += "(?,?),";
			}
		}
		
		String update = query.substring(0, query.length()-1) + ";";
		java.sql.PreparedStatement statement = DBHelper.getConnection()
				.prepareStatement(update);
		for(Record r : records){
			int id = DBHelper.getRecordID(r);
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

	
}
