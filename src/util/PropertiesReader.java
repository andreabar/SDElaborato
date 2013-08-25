package util;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class PropertiesReader {

	public static String dbName;
	public static String filesHost;
	public static String vimeoToken;
	public static String vimeoTokenSecret;
	public static String vimeoAPIKey;
	public static String vimeoAPISecret;
	public static String getDbName() {
		return dbName;
	}


	public static String getFilesHost() {
		return filesHost;
	}


	public static String getVimeoToken() {
		return vimeoToken;
	}


	public static String getVimeoTokenSecret() {
		return vimeoTokenSecret;
	}


	public static String getVimeoAPIKey() {
		return vimeoAPIKey;
	}


	public static String getVimeoAPISecret() {
		return vimeoAPISecret;
	}


	public static String getEuropeanaAPIKey() {
		return europeanaAPIKey;
	}


	public static String europeanaAPIKey;


	public static void initProperties(File f){
		
		Properties p = new Properties();
		try {
			p.load(new FileInputStream(f));
			dbName = p.getProperty("dbName");
			filesHost = p.getProperty("filesHost");
			vimeoToken = p.getProperty("vimeoToken");
			vimeoTokenSecret = p.getProperty("vimeoTokenSecret");
			vimeoAPIKey = p.getProperty("vimeoAPIKey");
			vimeoAPISecret = p.getProperty("vimeoAPISecret");
			europeanaAPIKey = p.getProperty("europeanaAPIKey");

		} catch (IOException e) {
			e.printStackTrace();
		}

		
	}
	

}
