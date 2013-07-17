package controllers;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import model.Record;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import dbutil.DBHelper;

public class RecordController {

	
	public static ArrayList<String> getWebResources(Record r)
			 {

		ArrayList<String> list = new ArrayList<String>();
		BufferedReader in;
		try {
			in = new BufferedReader(new InputStreamReader(new URL(
					r.getJSONLink()).openStream()));
		
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
			
		
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return list;

		
		
	}

	public static void saveRecords(ArrayList<Record> records) throws SQLException {

		
			String query = "INSERT INTO record (query, type, language, url, title, ipr_type) VALUES ";
			for (int i = 0; i < records.size(); i++) {
				query += "(?,?,?,?,?,?)";
				if (i < records.size() - 1)
					query += ",";
				else
					query += ";";
			}

			java.sql.PreparedStatement statement = DBHelper.getConnection()
					.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);

			for (int i = 0; i < records.size(); i++) {
				statement.setInt(6 * i + 1, records.get(i).getQueryID());
				statement.setString(6 * i + 2, records.get(i).getType());
				statement.setString(6 * i + 3, records.get(i).getLanguage());
				statement.setString(6 * i + 4, records.get(i).getJSONLink());
				statement.setString(6 * i + 5, records.get(i).getTitle());
				statement.setString(6 * i + 6, records.get(i).getRights());
				

			}

			statement.executeUpdate();
			
//			saveResources(records);
	}


	private static void saveResources(ArrayList<Record> records) throws SQLException {

		
		String query = "INSERT INTO resource (record, url) VALUES ";
		int j = 1;

		
		for(Record r : records){
			ArrayList<String> resources = r.getWebResources();
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

	
}
