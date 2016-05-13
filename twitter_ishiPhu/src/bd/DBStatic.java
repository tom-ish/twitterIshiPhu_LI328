package bd;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBStatic {
	
	private static String mysql_host = "132.227.201.129:33306";	// from home: 132.227.201.129
	private static String mysql_db= "gr1_phu_ishi";
	private static String mysql_username = "gr1_phu_ishi";
	private static String mysql_password = "gr1_phu_ishi$";
	private static boolean mysql_pooling = false;
	private static Database database = null;
	
	
	public static Connection getMySQLConnection() throws SQLException {
		try {
			Class.forName("com.mysql.jdbc.Driver").newInstance();
			if(DBStatic.mysql_pooling == false)
				return (DriverManager.getConnection("jdbc:mysql://" + DBStatic.mysql_host + "/" + DBStatic.mysql_db,DBStatic.mysql_username,DBStatic.mysql_password));
			else{
				if(database == null)
					database = new Database("jdbc/db");
				return (database.getConnection());
			}
		} catch (InstantiationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return (DriverManager.getConnection("jdbc:mysql://" + DBStatic.mysql_host + "/" + DBStatic.mysql_db,DBStatic.mysql_username,DBStatic.mysql_password));
	}
	
	
}
