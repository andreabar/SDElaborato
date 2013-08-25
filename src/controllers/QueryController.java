package controllers;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import util.AppData;


import dbutil.DBHelper;

import model.EuropeanaRecord;
import model.Query;
import model.Record;
import model.VimeoRecord;

public class QueryController {

	public static ArrayList<Query> getQueries(int uid) throws SQLException,
			ParseException {
		ArrayList<Query> searches = new ArrayList<Query>();

		String q = new String(
				"SELECT * FROM query WHERE id IN (SELECT query FROM user_history WHERE user = "
						+ uid + ")");

		java.sql.PreparedStatement statement = DBHelper.getConnection()
				.prepareStatement(q);

		ResultSet result = statement.executeQuery();

		while (result.next()) {

			Query query = new Query(result.getInt("id"),
					result.getString("provider"), result.getString("type"),
					result.getString("keyword"), result.getString("language"),
					result.getInt("results"), result.getString("date"));

			searches.add(query);
		}

		return searches;
	}

	public static Query saveQuery(Query query, int uid) {

		try {
			Statement statement = DBHelper.getConnection().createStatement();


			Date dt = new Date();
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			String currentTime = sdf.format(dt);

			String i = "INSERT INTO query(date, provider, type, language, keyword, results) VALUES ('" +currentTime+ "','" + query.getProvider() + "','" + query.getDataType() + "','" +
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
			e.printStackTrace();
		}

		return query;
	}

	public static ArrayList<Record> getRecords(Query q) throws SQLException {

		
		String sql = "SELECT record.* FROM result, record WHERE result.record = record.id AND result.query = " + q.getId() + ";";
		
		

		Statement statement = DBHelper.getConnection().createStatement();

		ResultSet set = statement.executeQuery(sql);
		ArrayList<Record> list = new ArrayList<Record>();

		while (set.next()) {
			String provider = set.getString("provider");
			Record r;

			if (provider.equals(AppData.EUROPEANA))
				r = new EuropeanaRecord(set);
			else 
				r = new VimeoRecord(set);

			list.add(r);

		}

		return list;

	}

	public static void deleteSearch(Query q) throws SQLException {
		String s = "DELETE FROM query WHERE id = " + q.getId() + ";";

		Statement statement = DBHelper.getConnection().createStatement();

		statement.executeUpdate(s);
	}

	public static void addQueryResult(List<Record> records, Query updated) {

		String sql = "INSERT INTO result (query, record) VALUES ";
		for (Record r : records) {
			sql += "(?,?)";
			if (records.indexOf(r) == records.size() - 1)
				sql += ";";
			else
				sql += ",";

		}

		try {

			java.sql.PreparedStatement statement = DBHelper.getConnection()
					.prepareStatement(sql);
			int j = 1;
			for (Record r : records) {

				statement.setInt(j, updated.getId());
				j++;
				statement.setInt(j, DBHelper.getRecordID(r));
				j++;
			}

			statement.execute();
		}

		catch (Exception e) {
			e.printStackTrace();
		}

	}

	public static Query getQuery(int id) throws SQLException {
		String sql = "SELECT * FROM query WHERE id ="  + id;	
		ResultSet set = DBHelper.getConnection().createStatement().executeQuery(sql);
	
		if(set.next())
			return new  Query(id, set.getString("provider"), set.getString("type"), set.getString("keyword"), set.getString("language"), set.getInt("results"), set.getString("date"));
		return null;
		
	}

}
