package util;


import fetcher.EuropeanaFetcher;
import fetcher.JSONFetcher;
import fetcher.VimeoFetcher;

public class FetcherFactory {

	private static FetcherFactory factory = null;
	public JSONFetcher getFetcher(String provider){
		
		if(provider.equals("Europeana"))
			return new EuropeanaFetcher();
		else if (provider.equals("Vimeo"))
			return new VimeoFetcher();
		
		return null;
		
	}

	public static FetcherFactory getFetcherFactory() {
		// TODO Auto-generated method stub
		if(factory == null)
			factory = new FetcherFactory();
		return factory;
	}
	
}
