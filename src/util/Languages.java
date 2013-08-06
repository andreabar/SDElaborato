package util;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;

import dbutil.DBHelper;


public class Languages {

	
	public static void buildMap(){
		
		String sql = "INSERT INTO language(id, full_language) VALUES " +
		"('it', 'Italian'),('fr','French'),('de','German'),('en','English'),('mul','any');";
		
		try {
			DBHelper.getConnection().createStatement().execute(sql);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	public static String get(String language){
		
		String sql = "SELECT id FROM language WHERE full_language = '" + language + "';";
		
		ResultSet set;
		try {
			set = DBHelper.getConnection().createStatement().executeQuery(sql);
			if(set.next())
				return set.getString("id");

		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return null;
	}
	
	
}
