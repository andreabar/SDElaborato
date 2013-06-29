package controllers;

import java.net.MalformedURLException;
import java.util.ArrayList;

import model.Query;
import model.Record;

public abstract class JSONFetcher {

	public String API_KEY;
	public String API_URL;
	
	public abstract ArrayList<Record> executeQuery(Query q) throws MalformedURLException, Exception; 
	
}