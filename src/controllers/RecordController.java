package controllers;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import model.Record;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

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


	
}
