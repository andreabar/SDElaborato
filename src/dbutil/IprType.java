package dbutil;

import java.util.HashMap;

public class IprType {

	
	public static HashMap<String, String> types;
	
	public static void build(){
		
		types = new HashMap<String, String>();
		types.put("Restricted Access - Right Reserved", "http://www.europeana.eu/rights/rr-r/");
		types.put("Free Access - Right Reserved", "http://www.europeana.eu/rights/rr-f/");
		types.put("Restricted Access - Right Reserved", "http://www.europeana.eu/rights/rr-r/");
		types.put("CC BY-SA", "http://creativecommons.org/licenses/by-sa/*");
		types.put("CC0", "http://creativecommons.org/publicdomain/zero/1.0/");
		types.put("CC BY", "http://creativecommons.org/licenses/by/*");
		types.put("CC BY-NC-ND", "http://creativecommons.org/licenses/by-nc-nd/*");
		types.put("CC BY-NC", "\"http://creativecommons.org/licenses/by-nc/2.0/at/legalcode\"");
		types.put("CC BY-NC-SA", "\"http://creativecommons.org/licenses/by-nc-sa/\"");
		types.put("Public Domain marked", "\"http://creativecommons.org/publicdomain/mark/1.0/\""); 
		types.put("Unknown copyright", "\"http://www.europeana.eu/rights/unknown/\""); 
		types.put("Paid Access - Right Reserved", "http://www.europeana.eu/rights/rr-p/");

	}
	
	public static String getIprUrl(String k){
		
		return types.get(k);
	}
	
	public static HashMap<String, String> getTypes(){
		
		build();
		return types;
	}
}
