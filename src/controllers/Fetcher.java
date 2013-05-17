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
			if(jsonResponse.get("success").equals("true") && 
					totalResults > 0){
				//TODO: Call parseItems
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
	
	/*
	private ArrayList<Item> parseItems(String source){
		JSONArray array = new JSONArray(source);
		
	}
	*/

}
