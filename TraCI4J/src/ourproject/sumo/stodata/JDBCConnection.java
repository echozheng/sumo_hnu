/*
 * Class:JDBCConnection
 * Usage: connection to the database
 * **/
package sumo.stodata;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class JDBCConnection {
	
	private static final String DRIVER = "com.mysql.jdbc.Driver";
	private static final String URL = "jdbc:mysql://localhost:3306/sumodata";
	private static final String USER = "root";
	private static final String PASSWORD = "root";
	
	public static Connection conn;
	
	//create the connection to database while return only one connection.
	public static synchronized Connection getConnection(){
		if(conn != null){
			return conn;
		}
		
		try{
			Class.forName(DRIVER);
			conn = DriverManager.getConnection(URL,USER,PASSWORD);
			if(!conn.isClosed()){
				System.out.println("succeeded connecting to the database!");
			}
		}catch(Exception ex){
			ex.printStackTrace();
		}
		return conn;
	}
	
	//close the connection to mysql
	public static void closeConnectionToMysql(){
		try {
			conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}
