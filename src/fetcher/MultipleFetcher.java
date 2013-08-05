package fetcher;

import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.Set;

import com.google.gwt.dev.util.collect.HashMap;

import model.MultiQuery;
import model.Query;
import model.Record;
import util.Languages;
import view.controllers.ViewController;

public class MultipleFetcher extends JSONFetcher{

	private ArrayList<JSONFetcher> fetchers = new ArrayList<>();

	public void addFetcher(JSONFetcher f){
			fetchers.add(f);
		
	}
	
	@Override
	public ArrayList<Record> executeQuery(Query v)
			throws MalformedURLException, Exception {

		ArrayList<Record> records = new ArrayList<>();

		for(JSONFetcher f : fetchers)
			records.addAll(f.executeQuery(v));
	
		return records;
	}

	@SuppressWarnings("unchecked")
	@Override
	public Query buildQuery(ViewController v) {
			
		Query query = new MultiQuery(v.getMainView().getTextfield().getValue().toString());
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
