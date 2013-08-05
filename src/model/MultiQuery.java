package model;


import java.util.HashMap;

import fetcher.JSONFetcher;

public class MultiQuery extends Query {

	private HashMap<Query, JSONFetcher> queries = new HashMap<>();
	
	public void addQuery(Query q, JSONFetcher f){
		
		queries.put(q, f);
		
	}
	
	public MultiQuery(String i) {

		super(i);
	}
	


}
