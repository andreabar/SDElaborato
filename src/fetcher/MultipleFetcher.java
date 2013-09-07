package fetcher;

import java.net.MalformedURLException;
import java.nio.channels.NoConnectionPendingException;
import java.util.ArrayList;
import java.util.Set;

import org.json.JSONException;

import com.google.gwt.dev.json.JsonException;

import model.MultiQuery;
import model.Query;
import model.Record;
import util.AppData;
import util.Languages;
import view.controllers.SearchTabController;


public class MultipleFetcher implements JSONFetcher{

	private ArrayList<JSONFetcher> fetchers = new ArrayList<>();

	public void addFetcher(JSONFetcher f){
			fetchers.add(f);
		
	}
	
	@Override
	public ArrayList<Record> executeQuery(Query v)
			throws MalformedURLException, NoResultException, ConnectionErrorException, JSONException {

		ArrayList<Record> records = new ArrayList<>();

		for(JSONFetcher f : fetchers){
		
			try{
			records.addAll(f.executeQuery(v));
			} catch(Exception e){
				
				//thrown if one fetcher finds no results
				
			}
			
		}
	
		if(records.isEmpty())
			throw new NoResultException();
		
		return records;
	}

	@SuppressWarnings("unchecked")
	@Override
	public Query buildQuery(SearchTabController v) {
			
		Query query = new MultiQuery(v.getMainView().getTextfield().getValue().toString());
		query.setLimit((Integer)v.getMainView().getStepper().getValue());
		
		if(v.getMainView().getIprSelector().getValue() != null )
			query.setIprType(((Set<Object>)(v.getMainView().getIprSelector().getValue())));
		
		if(!v.getMainView().getTypeSelect().getValue().equals(AppData.ANY_TYPE))
			query.setDataType(v.getMainView().getTypeSelect().getValue().toString());
		else query.setDataType("any");

		if(!v.getMainView().getLanguageSelect().getValue().equals(AppData.ANY_TYPE))
			query.setLanguage(Languages.get(v.getMainView().getLanguageSelect().getValue().toString()));
		else 
			query.setLanguage(AppData.ANY_TYPE);
		
		String provider = new String();
		for(JSONFetcher f : fetchers){
			provider+= f.getProvider();
			if(fetchers.indexOf(f) < fetchers.size()-1)
				provider+=" - ";
				
		}
		query.setProvider(provider);
		
		return query;

		
	}

	@Override
	public String getProvider() {
		
		return null;
	}

}
