package util;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class PropertiesReader {

	static String dbName;
	static String filesHost;
	static String vimeoToken;
	static String vimeoTokenSecret;
	static String vimeoAPIKey;
	static String vimeoAPISecret;
	static int refreshingTime;

	public static String getDbName() {
		return dbName;
	}

	public static String getFilesHost() {
		return filesHost;
	}

	public static int getRefreshingTime() {
		return refreshingTime;
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

	public static void initProperties(File f) {

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
			try{
			refreshingTime = Integer.parseInt(p.getProperty("refreshingTime"));
			}catch(NumberFormatException e){
				
				refreshingTime = 20;
				
			}

		} catch (IOException e) {
			e.printStackTrace();
		}

	}

}
