package controllers;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import model.EuropeanaRecord;
import model.Record;
import model.VimeoRecord;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import util.AppData;

import dbutil.DBHelper;

public class RecordController {

	public static List<String> getWebResources(Record r) throws Exception {

		int id = DBHelper.getRecordID(r);

		List<String> list =

		checkForResources(id);

		if (!list.isEmpty())
			return list;
		else {
			downloadResources(r, list);
			return list;

		}

	}

	private static List<String> downloadResources(Record r, List<String> list)
			throws Exception {
		
		BufferedReader in = new BufferedReader(new InputStreamReader(new URL(
				r.getUniqueUrl()).openStream()));

		String inputLine = new String();
		JSONObject o = null;
		while ((inputLine = in.readLine()) != null) 
			o = new JSONObject(inputLine);

		JSONArray array = o.getJSONObject("object")
				.getJSONArray("aggregations");

		try {
			String shownBy = array.getJSONObject(0).getString("edmIsShownBy");
			list.add(shownBy);
		}

		catch (JSONException e) {

			JSONArray webRes = array.getJSONObject(0).getJSONArray(
					"webResources");
			for (int j = 0; j < webRes.length(); j++)
				list.add(webRes.getJSONObject(j).getString("about"));

		}
		return list;

	}

	private static List<String> checkForResources(int id) throws SQLException {

		List<String> resources = new ArrayList<String>();
		String q = "SELECT url FROM resource WHERE record = " + id;

		ResultSet set = DBHelper.getConnection().createStatement()
				.executeQuery(q);
		while (set.next()) {
			resources.add(set.getString("url"));
		}
		
		set.close();

		return resources;
	}

	public static List<Record> saveRecords(ArrayList<Record> records) {

		for (Record r : records) {
			String query = "INSERT INTO record (type, language, url, title, ipr_type, provider) VALUES "
					+ "(?,?,?,?,?,?);";
			java.sql.PreparedStatement statement;
			try {
				statement = DBHelper.getConnection().prepareStatement(query,
						Statement.RETURN_GENERATED_KEYS);

				statement.setString(1, r.getType());
				statement.setString(2, r.getLanguage());
				statement.setString(3, r.getUniqueUrl());
				statement.setString(4, r.getTitle());
				statement.setString(5, r.getRights());
				statement.setString(6, r.getProvider());


				statement.executeUpdate();
				
				ResultSet keys = statement.getGeneratedKeys();
				
				
				if(keys.next()){
					r.setID(keys.getInt(1));	
				}
				statement.close();
				keys.close();
					
			} catch (SQLException e) {

				e.printStackTrace(); 
			}
			
			if(r.getProvider().equals(AppData.VIMEO))
				DBHelper.saveMetadata(r.getID(), (((VimeoRecord)r).getMetadata()));
			
		}
		return records;

	
	}

	public static Record getRecord(int id) throws SQLException {
		String sql = "SELECT * FROM record WHERE id ="  + id;	
		ResultSet set = DBHelper.getConnection().createStatement().executeQuery(sql);
		if(set.next())
			if(set.getString("provider").equals(AppData.EUROPEANA)){
				EuropeanaRecord r = new EuropeanaRecord(set);
				set.getStatement().close();
				set.close();
				return r;

			}else if(set.getString("provider").equals(AppData.VIMEO)){
				VimeoRecord r = new VimeoRecord(set);
				set.getStatement().close();
				set.close();
				return r;
				}
			
		return null;

	}

	

}
