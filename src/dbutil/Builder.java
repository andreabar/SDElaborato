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
			
			//TODO: call generator script
			
			
			
		} catch (SQLException | ClassNotFoundException e) {

			System.out.println("UNABLE TO CONNECT TO DB");
			e.printStackTrace();
		} 
	}

}
