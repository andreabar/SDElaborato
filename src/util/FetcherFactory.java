package util;


import fetcher.EuropeanaFetcher;
import fetcher.JSONFetcher;
import fetcher.VimeoFetcher;

public class FetcherFactory {

	private static FetcherFactory factory = null;
	public JSONFetcher getFetcher(String provider){
		
		if(provider.equals(AppData.EUROPEANA))
			return new EuropeanaFetcher();
		else if (provider.equals(AppData.VIMEO))
			return new VimeoFetcher();
		
		return null;
		
	}

	public static FetcherFactory getFetcherFactory() {
		if(factory == null)
			factory = new FetcherFactory();
		return factory;
	}
	
}
