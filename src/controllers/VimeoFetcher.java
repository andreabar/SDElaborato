package controllers;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

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

import views.MainView;

import model.Query;
import model.Record;
import model.VimeoQuery;

public class VimeoFetcher extends JSONFetcher{

	private String token = "1353bf8aabd8b200d0e4b9d7a9a2b514";
	private String token_secret = "eac01f5d3305858b2f4e1a93a5bc77b0fe54d7f6";
	private String endpoint = "http://vimeo.com/api/rest/v2?format=json&method=vimeo.videos.search&query=";
	
	
	private OAuthService service = new ServiceBuilder()
	.apiKey("815936f63c257234e191aba5e70304abe4c3ac8c")
	.apiSecret("294e76518159aa7017f610dbd0922f0cf9f17643")
	.provider(VimeoApi.class)
	.build();
	
	@Override
	public ArrayList<Record> executeQuery(Query q)
			throws MalformedURLException, Exception {

		URL query = buildQueryRequest(q);
		OAuthRequest req = new OAuthRequest(Verb.POST, query.toString());
		service.signRequest(new Token(token, token_secret), req);
		Response response = req.send();
	
		ArrayList<Record> records = saveRecords(response.getBody());
		DBHelper.saveRecords(records);
		
		return records;

	}
	
	
	private ArrayList<Record> saveRecords(String response) throws JSONException, Exception {

		JSONObject o = new JSONObject(response);
		if (!o.getString("stat").equals("ok") )
			throw new Exception("Request couldn't be satisfied.");
		if( o.getJSONObject("videos").getInt("total") == 0)
			throw new Exception("No result found.");
		
		JSONArray items = o.getJSONObject("videos").getJSONArray("video");
		ArrayList<Record> records = getRecordList(items);
		return records;
		
	}


	private ArrayList<Record> getRecordList(JSONArray items) throws JSONException {

		ArrayList<Record> list = new ArrayList<Record>();

		for (int i = 0; i < items.length(); i++) {

			Record item = new Record();
			
			JSONObject jsonItem = items.getJSONObject(i);
		
			item.setId(jsonItem.get("id").toString());
			item.setType("VIDEO");
			item.setTitle(jsonItem.getString("title"));
			item.setLanguage("unknown");
			item.setWebResources(new ArrayList<String>());
			item.getWebResources().add("http://vimeo.com/" + item.getEuropeanaId());

			list.add(item);
		}

		return list;
		
	}


	private URL buildQueryRequest(Query q) throws MalformedURLException {

		String urlTarget = endpoint + q.getInput();
		if (-1 != q.getLimit()){
			urlTarget += "&page=" + ((VimeoQuery)q).getPages() + "&per_page=" + ((VimeoQuery)q).getRpp();;
			
		}
		
		System.out.println("QUERY: " + urlTarget);
		return new URL(urlTarget.replaceAll(" ", "%20"));

	}


	@Override
	public Query buildQuery(MainView mainView) {

		VimeoQuery q = new VimeoQuery(mainView.getTextfield().getValue().toString().trim());
		q.setLimit((Integer)mainView.getStepper().getValue());
		return q;
	}
	
	

}
