package dbutil;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import com.mysql.jdbc.Driver;
import com.mysql.jdbc.Statement;

public class Builder {


	public static void buildDB(){
		
		try {
		      Class.forName("com.mysql.jdbc.Driver");
			Connection c = DriverManager.getConnection("jdbc:mysql://localhost?user=root&password=-36degrees-");
			
			c.createStatement().executeUpdate("CREATE SCHEMA elab;");
			c.createStatement().execute("CREATE TABLE elab.record(" +
					"" +
					"" +
					"");
			
			//ETC ETC
			
			
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

}
