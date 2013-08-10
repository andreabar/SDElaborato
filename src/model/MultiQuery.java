package model;


import java.util.HashMap;

import fetcher.JSONFetcher;

public class MultiQuery extends Query {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1067913481718583725L;
	private HashMap<Query, JSONFetcher> queries = new HashMap<>();
	
	public void addQuery(Query q, JSONFetcher f){
		
		queries.put(q, f);
		
	}
	
	public MultiQuery(String i) {

		super(i);
	}
	


}
