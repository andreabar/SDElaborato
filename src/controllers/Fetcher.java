package controllers;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

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

	public String searchMetaData(String t){
		URL url;
		HttpURLConnection connection = null;
		//it's just for testing
		String urlTarget = "http://europeana.eu/api//v2/search.json?wskey=" + APIKEY;
		urlTarget = urlTarget + "&query=" + t + "&start=1&rows=1&profile=standard&qf=TYPE:VIDEO";
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
			return response.toString();

		}catch(Exception e){
			e.printStackTrace();
			return null;

		} finally {

			if(connection != null) {
				connection.disconnect(); 
			}
		}
	}

}
