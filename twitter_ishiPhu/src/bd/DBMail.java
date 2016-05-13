package bd;

import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class DBMail {

	public static String getEmailFromID(String id, PrintWriter printWriter){
		String sql = "SELECT * FROM USER;";
		Statement state;
		String rslt = null;
		try {		
			Connection cxn = DBStatic.getMySQLConnection();
			state = cxn.createStatement();
			ResultSet result = state.executeQuery(sql);

			while(result.next()){
				String s = result.getString(1);
				if(id.equalsIgnoreCase(s)){
					rslt = result.getString(3);
					state.close();
					cxn.close();
					return rslt;
				}
			}
			state.close();
			cxn.close();
		} catch (SQLException e) {
			printWriter.print(e.getMessage());
			e.printStackTrace();
		}
		return rslt;
	}
}
