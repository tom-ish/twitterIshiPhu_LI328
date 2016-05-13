package bd;

import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class DBAdmin {
	
	public static void deleteExpiredSession(String userID, PrintWriter printWriter){
		String sql = "DELETE FROM SESSION WHERE login = '" + userID + "';";
		
		try {
			Connection cxn = DBStatic.getMySQLConnection();
			Statement state = cxn.createStatement();
			
			state.executeUpdate(sql);
			
			state.close();
			cxn.close();
		} catch (SQLException e) {
			printWriter.print(e.getMessage());
			e.printStackTrace();
		}
	}

	public static void deleteUserProfile(String username, PrintWriter printWriter){
		String sql = "DELETE FROM USER_PROFILE WHERE login = '" + username + "';";
		
		try {
			Connection cxn = DBStatic.getMySQLConnection();
			Statement state = cxn.createStatement();
			
			state.executeUpdate(sql);
			
			state.close();
			cxn.close();
		} catch (SQLException e) {
			printWriter.print(e.getMessage());
			e.printStackTrace();
		}
	}

}
