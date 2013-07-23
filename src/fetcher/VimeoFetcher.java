package fetcher;

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


import view.controllers.ViewController;
import model.Query;
import model.Record;
import model.VimeoQuery;
import model.VimeoRecord;

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
		q.setLimit(records.size());
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

			Record item = new VimeoRecord();
			
			JSONObject jsonItem = items.getJSONObject(i);
		
			item.setTitle(jsonItem.getString("title"));
			item.setUniqueUrl("http://vimeo.com/" + jsonItem.getInt("id"));
			
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
	public Query buildQuery(ViewController v) {

		VimeoQuery q = new VimeoQuery(v.getMainView().getTextfield().getValue().toString().trim());
		q.setLimit((Integer)v.getMainView().getStepper().getValue());
		q.setDataType("VIDEO");
		q.setLanguage("unknown");
		q.setProvider("VIMEO");
		
		return q;
	}
	
	

}