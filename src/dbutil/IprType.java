package dbutil;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import com.mysql.jdbc.PreparedStatement;

public class IprType {

	public static HashMap<String, String> types;

	public static void build() throws SQLException {

		String sql = "INSERT INTO ipr(url, description) VALUES ";
		types = new HashMap<String, String>();
		types.put("Restricted Access - Right Reserved",
				"http://www.europeana.eu/rights/rr-r/");
		types.put("Free Access - Right Reserved",
				"http://www.europeana.eu/rights/rr-f/");
		types.put("Restricted Access - Right Reserved",
				"http://www.europeana.eu/rights/rr-r/");
		types.put("CC BY-SA", "http://creativecommons.org/licenses/by-sa/*");
		types.put("CC0", "http://creativecommons.org/publicdomain/zero/1.0/");
		types.put("CC BY", "http://creativecommons.org/licenses/by/*");
		types.put("CC BY-NC-ND",
				"http://creativecommons.org/licenses/by-nc-nd/*");
		types.put("CC BY-NC",
				"\"http://creativecommons.org/licenses/by-nc/2.0/at/legalcode\"");
		types.put("CC BY-NC-SA",
				"\"http://creativecommons.org/licenses/by-nc-sa/\"");
		types.put("Public Domain marked",
				"\"http://creativecommons.org/publicdomain/mark/1.0/\"");
		types.put("Unknown copyright",
				"\"http://www.europeana.eu/rights/unknown/\"");
		types.put("Paid Access - Right Reserved",
				"http://www.europeana.eu/rights/rr-p/");

		Iterator<String> it = types.keySet().iterator();
		while (it.hasNext()) {

			sql += "(?,?)";
			it.next();
			if (it.hasNext())
				sql += ",";
			else
				sql += ";";

		}

		int j = 1;
		java.sql.PreparedStatement st = DBHelper.getConnection()
				.prepareStatement(sql);
		for (String t : types.keySet()) {

			st.setString(j, types.get(t));
			j++;
			st.setString(j, t);
			j++;

		}

		st.execute();
	}

	public static String getIprUrl(String k) {

		String sql = "SELECT url FROM ipr WHERE description = '" + k + "';";

		ResultSet set;
		try {
			set = DBHelper.getConnection().createStatement().executeQuery(sql);
			if (set.next())
				return set.getString("url");

		} catch (SQLException e) {
			e.printStackTrace();
		}

		return null;

	}

	public static List<String> getTypes() {

		String sql = "SELECT description FROM ipr ;";
		List<String> types = new ArrayList<>();
		ResultSet set;
		try {
			set = DBHelper.getConnection().createStatement().executeQuery(sql);
			while (set.next()) {

				types.add(set.getString("description"));

			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return types;

	}

}
