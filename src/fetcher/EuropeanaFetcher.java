package fetcher;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Set;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import util.Languages;
import view.controllers.ViewController;
import model.EuropeanaRecord;
import model.EuropeanaQuery;
import model.Query;
import model.Record;

public class EuropeanaFetcher implements JSONFetcher {

	private static final String API_KEY = "NFUAy4RDa";
	private static final String API_ACCESS_POINT = "http://europeana.eu/api//v2/search.json?wskey=";
	public static final String PROVIDER = "EUROPEANA";
	public EuropeanaFetcher() {

	}

	@Override
	public ArrayList<Record> executeQuery(Query v) throws Exception {

		URL request = buildQueryRequest(v);
		if (request != null) {
			System.out.println("Query: " + request.toString());
			ArrayList<Record> response = fetchResponse(request);
			v.setLimit(response.size());
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

			Record item = new EuropeanaRecord();
			
			JSONObject jsonItem = items.getJSONObject(i);
			
			item.setType(jsonItem.getString("type"));
			item.setTitle(jsonItem.getJSONArray("title").getString(0));
			item.setLanguage(jsonItem.getJSONArray("language").getString(0));
			try{
			item.setRights(jsonItem.getJSONArray("rights").getString(0));
			} catch(JSONException e){
				item.setRights("unknown");
			}
			item.setUniqueUrl(jsonItem.getString("link"));

			list.add(item);
		}

		return list;

	}

	
	private URL buildQueryRequest(Query q) throws MalformedURLException {

		String urlTarget = API_ACCESS_POINT + API_KEY + "&query=" + q.getInput();
		if (-1 != q.getLimit())
			urlTarget += "&rows=" + q.getLimit();
		if (q.hasLanguageFilter())
			urlTarget += "&qf=LANGUAGE:" + q.getLanguage();
		if (null != q.getIprType()){
			for(String s : q.getIprType())
				urlTarget += "&qf=RIGHTS:" + s;
		
		}
			
		if (q.hasDataFilter())
			urlTarget += "&qf=TYPE:" + q.getDataType().toUpperCase();

		return new URL(urlTarget.replaceAll(" ", "%20"));

	}

	@SuppressWarnings("unchecked")
	@Override
	public Query buildQuery(ViewController v) {
	
		EuropeanaQuery query = new EuropeanaQuery(v.getMainView().getTextfield().getValue().toString());
		
		query.setLimit((Integer)v.getMainView().getStepper().getValue());
		
		if(v.getMainView().getIprSelector().getValue() != null )
			query.setIprType(((Set<Object>)(v.getMainView().getIprSelector().getValue())));
		
		if(!v.getMainView().getTypeSelect().getValue().equals("any"))
			query.setDataType(v.getMainView().getTypeSelect().getValue().toString());
		else query.setDataType("any");

		if(!v.getMainView().getLanguageSelect().getValue().equals("any"))
			query.setLanguage(Languages.get(v.getMainView().getLanguageSelect().getValue().toString()));
		else 
			query.setLanguage("any");
		
		query.setProvider(getProvider());
		
		
		return query;
		
	}

	@Override
	public String getProvider() {
		
		return PROVIDER;
	}

	
}
