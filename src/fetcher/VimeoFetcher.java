package fetcher;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import org.apache.commons.lang.CharEncoding;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.scribe.builder.ServiceBuilder;
import org.scribe.builder.api.VimeoApi;
import org.scribe.model.OAuthRequest;
import org.scribe.model.Response;
import org.scribe.model.Token;
import org.scribe.model.Verb;
import org.scribe.oauth.OAuthService;

import controllers.RecordController;

import shared.PropertiesReader;
import util.AppData;
import view.controllers.SearchTabController;
import model.Query;
import model.Record;
import model.VimeoQuery;
import model.VimeoRecord;

public class VimeoFetcher implements JSONFetcher {

	private static final String  API_ACCESS_POINT = "http://vimeo.com/api/rest/v2?format=json&method=vimeo.videos.search&query=";
	
	private OAuthService service = new ServiceBuilder().apiKey(PropertiesReader.getVimeoAPIKey())
			.apiSecret(PropertiesReader.getVimeoAPISecret()).provider(VimeoApi.class).build();

	@Override
	public ArrayList<Record> executeQuery(Query v) throws MalformedURLException, JSONException, NoResultException, ConnectionErrorException
			{

		ArrayList<Record> records = new ArrayList<>();
		
		VimeoQuery q = new VimeoQuery(v.getInput());
		q.setLimit(v.getLimit());
		for (int i = 1; i <= q.getPages(); i++) {
			
			URL url = buildQueryRequest(q, i);
			OAuthRequest req = new OAuthRequest(Verb.POST, url.toString());
			req.setCharset(CharEncoding.UTF_8);
			service.signRequest(new Token(PropertiesReader.getVimeoToken(), PropertiesReader.getVimeoTokenSecret()), req);
			Response response = req.send();

			if(i==q.getPages())
				records.addAll(saveRecords(response.getBody(), q.getLimit()-records.size())); //in the last page, retrieve 50 but save just the remaining
			else
				records.addAll(saveRecords(response.getBody(), -1));
			
		
		}
		return records;

	}

	private ArrayList<Record> saveRecords(String response, int remainingResult)
			throws JSONException, NoResultException, ConnectionErrorException {

		JSONObject o = new JSONObject(response);
		if (!o.getString("stat").equals("ok"))
			throw new ConnectionErrorException();
		if (o.getJSONObject("videos").getInt("total") == 0)
			throw new NoResultException();

		JSONArray items = o.getJSONObject("videos").getJSONArray("video");
		ArrayList<Record> records = getRecordList(items, remainingResult);
		return records;

	}

	private ArrayList<Record> getRecordList(JSONArray items, int remainingResult)
			throws JSONException {

		ArrayList<Record> list = new ArrayList<Record>();
		int limit = remainingResult != -1? remainingResult : items.length();
			
		for (int i = 0; i < limit; i++) {

			Record item = new VimeoRecord();

			JSONObject jsonItem = items.getJSONObject(i);
			
			Record previous = RecordController.getRecord(AppData.VIMEO_URL + jsonItem.getInt("id"));
			
			if(null != previous){
					item = previous;
					((VimeoRecord) item).setMetadata(jsonItem);

			}

			
			else{
				((VimeoRecord) item).setMetadata(jsonItem);

			item.setTitle(jsonItem.getString("title"));
			item.setUniqueUrl(AppData.VIMEO_URL + jsonItem.getInt("id"));
			item.setPortalLink(item.getUniqueUrl());
			
			}
			list.add(item);
		}

		return list;

	}

	private URL buildQueryRequest(Query q, int page)
			throws MalformedURLException {

		String urlTarget = API_ACCESS_POINT + q.getInput() + "&summary_response=1";


		if (q.getLimit() > 50) {
			urlTarget += "&page=" + page + "&per_page="
					+ VimeoQuery.MAXIMUM_RPP;

		}
		
		else 
			urlTarget += "&per_page=" + q.getLimit();

		System.out.println("QUERY: " + urlTarget);
		return new URL(urlTarget.replaceAll(" ", "%20"));

	}

	@Override
	public Query buildQuery(SearchTabController v) {

		VimeoQuery q = new VimeoQuery(v.getMainView().getTextfield().getValue()
				.toString().trim());
		q.setLimit((Integer) v.getMainView().getStepper().getValue());
		
		q.setProvider(getProvider());

		return q;
	}

	@Override
	public String getProvider() {
		return AppData.VIMEO;
	}



}
