package util;

import java.util.HashMap;


public class Languages {

	public static HashMap<String, String> map = new HashMap<String, String>();
	
	public static void buildMap(){
		
		map.put("Italian", "it");
		map.put("French", "fr");
		map.put("German", "de");
		map.put("English", "en");
		map.put("any", "mul");
		
	}
	
	public static String get(String language){
		
		return map.get(language);
	}
	
	
}
