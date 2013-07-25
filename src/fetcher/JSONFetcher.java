package fetcher;

import java.net.MalformedURLException;
import java.util.ArrayList;

import view.controllers.ViewController;
import views.MainView;

import model.Query;
import model.Record;

public abstract class JSONFetcher {

	public String API_KEY;
	public String API_URL;
	
	public abstract ArrayList<Record> executeQuery(Query q) throws MalformedURLException, Exception;

	public abstract Query buildQuery(ViewController v); 
	
}
