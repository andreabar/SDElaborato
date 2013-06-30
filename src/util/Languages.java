package util;

import java.util.HashMap;


public class Languages {

	public static HashMap<String, String> map = new HashMap<String, String>();
	
	public static void buildMap(){
		
		map.put("Italian", "IT");
		map.put("French", "FR");
		map.put("German", "DE");
		map.put("English", "EN");
		map.put("any", "MUL");
		
	}
	
	public static String get(String language){
		
		return map.get(language);
	}
	
	
}
