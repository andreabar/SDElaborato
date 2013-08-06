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

public class VimeoFetcher implements JSONFetcher {

	public static final String PROVIDER = "VIMEO";
	private static final String VIMEO_URL = "http://vimeo.com/";
	private static final String TOKEN = "1353bf8aabd8b200d0e4b9d7a9a2b514";
	private static final String  TOKEN_SECRET = "eac01f5d3305858b2f4e1a93a5bc77b0fe54d7f6";
	private static final String  API_ACCESS_POINT = "http://vimeo.com/api/rest/v2?format=json&method=vimeo.videos.search&query=";
	private static final String API_KEY = "815936f63c257234e191aba5e70304abe4c3ac8c";
	private static final String  API_SECRET = "294e76518159aa7017f610dbd0922f0cf9f17643";
	
	private OAuthService service = new ServiceBuilder().apiKey(API_KEY)
			.apiSecret(API_SECRET).provider(VimeoApi.class).build();

	@Override
	public ArrayList<Record> executeQuery(Query v)
			throws MalformedURLException, Exception {

		ArrayList<Record> records = new ArrayList<>();
		
		VimeoQuery q = new VimeoQuery(v.getInput());
		q.setLimit(v.getLimit());
		for (int i = 1; i <= q.getPages(); i++) {
			
			URL url = buildQueryRequest(q, i);
			OAuthRequest req = new OAuthRequest(Verb.POST, url.toString());
			service.signRequest(new Token(TOKEN, TOKEN_SECRET), req);
			Response response = req.send();

			records.addAll(saveRecords(response.getBody()));

		}
		return records;

	}

	private ArrayList<Record> saveRecords(String response)
			throws JSONException, Exception {

		JSONObject o = new JSONObject(response);
		if (!o.getString("stat").equals("ok"))
			throw new Exception("Request couldn't be satisfied.");
		if (o.getJSONObject("videos").getInt("total") == 0)
			throw new Exception("No result found.");

		JSONArray items = o.getJSONObject("videos").getJSONArray("video");
		ArrayList<Record> records = getRecordList(items);
		return records;

	}

	private ArrayList<Record> getRecordList(JSONArray items)
			throws JSONException {

		ArrayList<Record> list = new ArrayList<Record>();

		for (int i = 0; i < items.length(); i++) {

			Record item = new VimeoRecord();

			JSONObject jsonItem = items.getJSONObject(i);

			item.setTitle(jsonItem.getString("title"));
			item.setUniqueUrl(VIMEO_URL + jsonItem.getInt("id"));

			list.add(item);
		}

		return list;

	}

	private URL buildQueryRequest(Query q, int page)
			throws MalformedURLException {

		String urlTarget = API_ACCESS_POINT + q.getInput();
		
		int total = q.getLimit();
		int missingResults = total - (page-1)*VimeoQuery
				.MAXIMUM_RPP;
		
		if (q.getLimit() > 50) {
			urlTarget += "&page=" + page + "&per_page="
					+ (missingResults<VimeoQuery.MAXIMUM_RPP? missingResults : VimeoQuery.MAXIMUM_RPP);
			;

		}
		
		else 
			urlTarget += "&per_page=" + q.getLimit();

		System.out.println("QUERY: " + urlTarget);
		return new URL(urlTarget.replaceAll(" ", "%20"));

	}

	@Override
	public Query buildQuery(ViewController v) {

		VimeoQuery q = new VimeoQuery(v.getMainView().getTextfield().getValue()
				.toString().trim());
		q.setLimit((Integer) v.getMainView().getStepper().getValue());
		
		q.setProvider(getProvider());

		return q;
	}

	@Override
	public String getProvider() {
		return PROVIDER;
	}



}
