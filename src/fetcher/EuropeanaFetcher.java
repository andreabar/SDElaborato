package fetcher;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Set;

import org.apache.commons.lang.CharEncoding;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import dbutil.DBHelper;

import shared.PropertiesReader;
import util.AppData;
import util.Languages;
import view.controllers.SearchTabController;
import model.EuropeanaRecord;
import model.EuropeanaQuery;
import model.Query;
import model.Record;

public class EuropeanaFetcher implements JSONFetcher {

	private static final String API_ACCESS_POINT = "http://europeana.eu/api//v2/search.json?wskey=";

	@Override
	public ArrayList<Record> executeQuery(Query v) throws ConnectionErrorException, NoResultException, JSONException, IOException, MalformedURLException {

		URL request = buildQueryRequest(v);
		if (request != null) {
			System.out.println("Query: " + request.toString());
			ArrayList<Record> response = fetchResponse(request);
			return response;
		}

		else
			throw new ConnectionErrorException();

	}

	private ArrayList<Record> fetchResponse(URL request) throws NoResultException, ConnectionErrorException, IOException, JSONException {

		BufferedReader in = new BufferedReader(new InputStreamReader(
				request.openStream(), CharEncoding.UTF_8));

		String inputLine = new String();
		while ((inputLine = in.readLine()) != null) {

			JSONObject o = new JSONObject(inputLine);
			if (!o.getBoolean("success"))
				throw new ConnectionErrorException();
			if (o.getInt("totalResults") == 0)
				throw new NoResultException();
			

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
			try {
				item.setType(jsonItem.getString("type"));
			} catch (JSONException e) {
				item.setType("unknown");
			}
			try {

				item.setTitle(jsonItem.getJSONArray("title").getString(0));
			} catch (JSONException e) {

				item.setTitle("unknown");
			}
			try {

				item.setLanguage(jsonItem.getJSONArray("language").getString(0));
			} catch (JSONException e) {

				item.setLanguage("unknown");
			}
			try {

				item.setRights(jsonItem.getJSONArray("rights").getString(0));
			} catch (JSONException e) {

				item.setRights("unknown");
			}
			try {

				item.setUniqueUrl(jsonItem.getString("link"));
			} catch (JSONException e) {

			}

			list.add(item);
		}

		return list;

	}

	private URL buildQueryRequest(Query q) throws MalformedURLException {

		String urlTarget = API_ACCESS_POINT + PropertiesReader.europeanaAPIKey
				+ "&query=" + q.getInput();
		if (-1 != q.getLimit())
			urlTarget += "&rows=" + q.getLimit();
		if (q.hasLanguageFilter())
			urlTarget += "&qf=LANGUAGE:" + q.getLanguage();
		if (null != q.getIprType()) {
			for (String s : q.getIprType())
				urlTarget += "&qf=RIGHTS:" + s;

		}

		if (q.hasDataFilter())
			urlTarget += "&qf=TYPE:" + q.getDataType().toUpperCase();

		return new URL(urlTarget.replaceAll(" ", "%20"));

	}

	@SuppressWarnings("unchecked")
	@Override
	public Query buildQuery(SearchTabController v) {

		EuropeanaQuery query = new EuropeanaQuery(v.getMainView()
				.getTextfield().getValue().toString());

		query.setLimit((Integer) v.getMainView().getStepper().getValue());

		if (v.getMainView().getIprSelector().getValue() != null)
			query.setIprType(((Set<Object>) (v.getMainView().getIprSelector()
					.getValue())));

		if (!v.getMainView().getTypeSelect().getValue()
				.equals(AppData.ANY_TYPE))
			query.setDataType(v.getMainView().getTypeSelect().getValue()
					.toString());
		else
			query.setDataType(AppData.ANY_TYPE);

		if (!v.getMainView().getLanguageSelect().getValue()
				.equals(AppData.ANY_TYPE))
			query.setLanguage(Languages.get(v.getMainView().getLanguageSelect()
					.getValue().toString()));
		else
			query.setLanguage(AppData.ANY_TYPE);

		query.setProvider(getProvider());

		return query;

	}

	@Override
	public String getProvider() {

		return AppData.EUROPEANA;
	}

}
