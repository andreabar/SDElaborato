package fetcher;

import java.net.MalformedURLException;
import java.util.ArrayList;

import view.controllers.ViewController;
import model.Query;
import model.Record;

public interface JSONFetcher {

	public ArrayList<Record> executeQuery(Query v)
			throws MalformedURLException, Exception;

	public Query buildQuery(ViewController v);

	public String getProvider();

}
