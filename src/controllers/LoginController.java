package controllers;

import java.sql.ResultSet;
import java.sql.SQLException;

import dbutil.DBHelper;

public class LoginController {

	public static int handleLogin(String u, String p) {

		String q = "SELECT id FROM user WHERE username = ? and password = ?";
		
		ResultSet set = DBHelper.executePreparedStatement(q, new String[]{u,p});
		
		if(set == null)
			return -1;
		
		try {
			if(set.next())
				return set.getInt("id");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return -1;
		
		
		
	}

	
}
