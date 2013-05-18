package controllers;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import model.Item;
import model.SearchResponse;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Fetcher {
	
	private static Fetcher instance = null;
	private static final String APIKEY = "NFUAy4RDa";
	
	private Fetcher(){
		
	}
	
	public static synchronized Fetcher getFetcher(){
		if(instance == null){
			instance = new Fetcher();
		}
		return instance;
	}

	public SearchResponse searchMetaData(String t){
		URL url;
		HttpURLConnection connection = null;
		//it's just for testing
		String urlTarget = "http://europeana.eu/api//v2/search.json?wskey=" + APIKEY;
		urlTarget = urlTarget + "&query=" + t + "&start=1&rows=1&profile=standard";
		try{
			url = new URL(urlTarget);
			connection = (HttpURLConnection)url.openConnection();

			connection.setUseCaches (false);
			connection.setDoInput(true);
			connection.setDoOutput(true);


			InputStream is = connection.getInputStream();
			BufferedReader rd = new BufferedReader(new InputStreamReader(is));
			String line;
			StringBuffer response = new StringBuffer(); 
			while((line = rd.readLine()) != null) {
				response.append(line);
				response.append('\r');
			}
			rd.close();
			
			System.out.print(response.toString());
			
			ArrayList<Item> items = null;
			JSONObject jsonResponse = new JSONObject(response.toString());
			Integer itemsCount = jsonResponse.getInt("itemsCount");
			Integer totalResults = jsonResponse.getInt("totalResults");
			if(jsonResponse.getBoolean("success") && 
					totalResults > 0){
				items = parseItems(jsonResponse.getJSONArray("items"));
			}
			SearchResponse searchResponse = new SearchResponse(items, itemsCount, totalResults);
			
			return searchResponse;

		}catch(Exception e){
			e.printStackTrace();
			return null;

		} finally {

			if(connection != null) {
				connection.disconnect(); 
			}
		}
	}
	
	
	private ArrayList<Item> parseItems(JSONArray array){
		ArrayList<Item> items = new ArrayList<Item>();
		//System.out.print(array.toString());

		for(int i = 0; i < array.length(); i++){
			
				Item item = new Item();
				JSONObject jsonItem = array.getJSONObject(i);
				item.setCompleteness(jsonItem.getInt("completeness"));
				item.setDataProvider(convertJSONArrayToStringArray(jsonItem.getJSONArray("dataProvider")));
				item.setEuropeanaCollectionName(convertJSONArrayToStringArray
						(jsonItem.getJSONArray("europeanaCollectionName")));
				item.setGuid(jsonItem.getString("guid"));
				item.setId(jsonItem.getString("id"));
				item.setLink(jsonItem.getString("link"));
				item.setProvider(convertJSONArrayToStringArray(jsonItem.getJSONArray("provider")));
				item.setType(jsonItem.getString("type"));
				try{
					item.setRights(convertJSONArrayToStringArray(jsonItem.getJSONArray("rights")));
				}catch(JSONException e){
					item.setRights(convertJSONArrayToStringArray(null));
				}
				try{
					item.setEdmConceptLabel(jsonItem.getString("edmConceptLabel"));
				}catch(JSONException e){
					item.setEdmConceptLabel("null");
				}
				try{
					item.setEdmPreview(jsonItem.getString("edmPreview"));
				}catch(JSONException e){
					item.setEdmPreview("null");
				}
				try{
					item.setDcCreator(convertJSONArrayToStringArray(jsonItem.getJSONArray("dcCreator")));
				}catch(JSONException e){
					item.setDcCreator(convertJSONArrayToStringArray(null));
				}
				try{
					item.setEdmTimespanLabel(jsonItem.getString("edmTimespanLabel"));
				}catch(JSONException e){
					item.setEdmTimespanLabel("null");
				}
				try{
					item.setEuropeanaCompleteness(jsonItem.getInt("europeanaCompleteness"));
				}catch(JSONException e){
					item.setEuropeanaCompleteness(0);
				}
				try{
					item.setLanguage(convertJSONArrayToStringArray(jsonItem.getJSONArray("language")));
				}catch(JSONException e){
					item.setLanguage(convertJSONArrayToStringArray(null));
				}
				try{
					item.setTitle(jsonItem.getString("title"));
				}catch(JSONException e){
					item.setTitle("null");
				}				
				try{
					item.setYear(jsonItem.getString("year"));
				}catch(JSONException e){
					item.setYear("null");
				}					
				items.add(item);
		}
		
		return items;
		
	}
	
	private ArrayList<String> convertJSONArrayToStringArray(JSONArray jsonArray){
		ArrayList<String> list = new ArrayList<String>();
		if (jsonArray != null) { 
			   int len = jsonArray.length();
			   for (int i=0;i<len;i++){ 
			    list.add(jsonArray.get(i).toString());
			   } 
		} else {
			list.add("null");
		}
		return list;
	}
	

}
