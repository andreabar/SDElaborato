package fetcher;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;

import org.json.JSONException;

import view.controllers.SearchTabController;
import model.Query;
import model.Record;

public interface JSONFetcher {

	public ArrayList<Record> executeQuery(Query v)
			throws MalformedURLException, NoResultException, ConnectionErrorException, JSONException, IOException;

	public Query buildQuery(SearchTabController v);

	public String getProvider();

	public class NoResultException extends Exception{
		
		public NoResultException() {
		
			super("No result found!");
		}
	}
	
	public class ConnectionErrorException extends Exception{
		
		public  ConnectionErrorException() {

			super("Connection error, retry");
		}
	}
}


