package controllers;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import dbutil.DBHelper;

import model.Query;
import model.Record;
import model.Search;

public class QueryController {

	
	public static ArrayList<Query> getSearches(int uid) throws SQLException, ParseException{
		ArrayList<Query> searches = new ArrayList<Query>();
		
		String q = new String("SELECT * FROM query WHERE id IN (SELECT query FROM user_history WHERE user = " + uid + ")" );
		
		
		java.sql.PreparedStatement statement = DBHelper.getConnection()
				.prepareStatement(q);
		
		ResultSet result = statement.executeQuery();
	
		while(result.next()){

			Query query = new Query(result.getInt("id"), result.getString("provider"), result.getString("type"), result.getString("keyword"),
							result.getString("language"), result.getInt("results"));
	
			searches.add(query);
		}
		
		return searches;
	}

	public static void saveQuery(Query query, int uid) {

		try {
			Statement statement = DBHelper.getConnection().createStatement();
		
		
		String i = "INSERT INTO query(provider, type, language, keyword, results) VALUES ( '" + query.getProvider() + "','" + query.getDataType() + "','" +
				query.getLanguage() + "','" + query.getKeyword() + "', " + query.getResults() + ");";
		
		statement.executeUpdate(i, Statement.RETURN_GENERATED_KEYS);
		ResultSet keys = statement.getGeneratedKeys();
		if(keys.next()){
			query.setId(keys.getInt(1));

			String uh = "INSERT INTO user_history(user, query) VALUES (" + uid +", " +  
					query.getId() +");";
			statement.executeUpdate(uh);
		
		}
		
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static ArrayList<Record> getRecords(Query q) throws SQLException{
	
		String s = "SELECT * FROM record WHERE query = " + q.getId() +";";
		
		Statement statement = DBHelper.getConnection().createStatement();
		
		ResultSet set = statement.executeQuery(s);
		ArrayList<Record> list = new ArrayList<Record>();
		
		while(set.next()){
			
			Record r = new Record();
			r.setQueryID(q.getId());
			r.setType(set.getString("type"));
			r.setLanguage(set.getString("language"));
			r.setTitle(set.getString("title"));
			r.setLink(set.getString("url"));
			if(q.getProvider().equals("VIMEO")){
				r.setWebResources(new ArrayList<String>());
				r.getWebResources().add("http://vimeo.com/" + r.getJSONLink());
				
			}
			r.setRights(set.getString("ipr_type"));
			list.add(r);
			
		}
		
		return list;
	

	}
	
}
