package controllers;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import model.Query;
import model.Record;

public class EuropeanaFetcher extends JSONFetcher {

	private static final String API_KEY = "NFUAy4RDa";
	private static final String API_URL = "http://europeana.eu/api//v2/search.json?wskey=";

	public EuropeanaFetcher() {

	}

	@Override
	public ArrayList<Record> executeQuery(Query q) throws Exception {

		URL request = buildQueryRequest(q);
		if (request != null) {
			System.out.println("Query: " + request.toString());
			ArrayList<Record> response = fetchResponse(request);
			DBHelper.saveRecords(response);

			return response;
		}

		else
			throw new Exception("Can't encode url.");

	}

	private ArrayList<Record> fetchResponse(URL request) throws Exception {

		BufferedReader in = new BufferedReader(new InputStreamReader(
				request.openStream()));

		String inputLine = new String();
		while ((inputLine = in.readLine()) != null) {

			JSONObject o = new JSONObject(inputLine);
			if (!o.getBoolean("success") )
				throw new Exception("Request couldn't be satisfied.");
			if( o.getInt("totalResults") == 0)
				throw new Exception("No result found.");
			
			JSONArray items = o.getJSONArray("items");
			ArrayList<Record> records = getRecordList(items);
			return records;
		}

		return null;

	}

	private ArrayList<Record> getRecordList(JSONArray items)
			throws JSONException {

		ArrayList<Record> list = new ArrayList<Record>();

		for (int i = 0; i < items.length(); i++) {

			Record item = new Record();
			
			JSONObject jsonItem = items.getJSONObject(i);
			item.setCompleteness(jsonItem.getInt("completeness"));
			item.setDataProvider(convertJSONArrayToStringArray(jsonItem
					.getJSONArray("dataProvider")));
			item.setEuropeanaCollectionName(convertJSONArrayToStringArray(jsonItem
					.getJSONArray("europeanaCollectionName")));
			item.setGuid(jsonItem.getString("guid"));
			item.setId(jsonItem.getString("id"));
			item.setLink(jsonItem.getString("link"));
			item.setProvider(convertJSONArrayToStringArray(jsonItem
					.getJSONArray("provider")));
			item.setType(jsonItem.getString("type"));
			item.setTitle(jsonItem.getJSONArray("title").getString(0));
			item.setLanguage(jsonItem.getJSONArray("language").getString(0));

			// item.setRights(convertJSONArrayToStringArray(jsonItem
			// .getJSONArray("rights")));

			// item.setEdmConceptLabel(jsonItem.getString("edmConceptLabel"));
			//
			// item.setEdmPreview(jsonItem.getString("edmPreview"));
			//
			// item.setDcCreator(convertJSONArrayToStringArray(jsonItem
			// .getJSONArray("dcCreator")));
			// item.setDcCreator(convertJSONArrayToStringArray(null));
			//
			// item.setEdmTimespanLabel(jsonItem.getString("edmTimespanLabel"));
			//
			// item.setEuropeanaCompleteness(jsonItem
			// .getInt("europeanaCompleteness"));
			//
			// item.setEuropeanaCompleteness(0);
			// item.setLanguage(convertJSONArrayToStringArray(jsonItem
			// .getJSONArray("language")));
			//
			//
			// item.setYear(jsonItem.getString("year"));

			list.add(item);
		}

		return list;

	}

	private ArrayList<String> convertJSONArrayToStringArray(JSONArray jsonArray)
			throws JSONException {
		ArrayList<String> list = new ArrayList<String>();
		if (jsonArray != null) {
			int len = jsonArray.length();
			for (int i = 0; i < len; i++) {
				list.add(jsonArray.getString(i));
			}
		}

		return list;
	}

	private URL buildQueryRequest(Query q) throws MalformedURLException {

		String urlTarget = API_URL + API_KEY + "&query=" + q.getInput();
		if (-1 != q.getLimit())
			urlTarget += "&rows=" + q.getLimit();
		if (null != q.getLanguage())
			urlTarget += "&qf=LANGUAGE:" + q.getLanguage();
		if (null != q.getIprType())
			urlTarget += "&qf=RIGHTS:" + q.getIprType();
		if (null != q.getDataType())
			urlTarget += "&qf=TYPE:" + q.getDataType().toUpperCase();

		return new URL(urlTarget.replaceAll(" ", "%20"));

	}

	
}
