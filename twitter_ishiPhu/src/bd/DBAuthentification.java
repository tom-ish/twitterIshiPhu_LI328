package bd;

import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.servlet.http.Part;

import bd.image.DBImage;

public class DBAuthentification {
	
	public static boolean existeLogin(String username, PrintWriter printWriter) {
		String sql = "SELECT * FROM USER;";
		Statement state;

		try {		
			Connection cxn = DBStatic.getMySQLConnection();
			state = cxn.createStatement();
			ResultSet result = state.executeQuery(sql);

			while(result.next()){
				String s = result.getString(1);
				if(username.equalsIgnoreCase(s))
					return true;
			}
			state.close();
			cxn.close();
		} catch (SQLException e) {
			printWriter.print(e.getMessage());
			e.printStackTrace();
		}
		return false;
	}

	public static boolean existeLoginProfile(String username,PrintWriter printWriter) {
		String sql = "SELECT * FROM USER_PROFILE;";
		Statement state;

		try {		
			Connection cxn = DBStatic.getMySQLConnection();
			state = cxn.createStatement();
			ResultSet result = state.executeQuery(sql);

			while(result.next()){
				String s = result.getString(1);
				if(username.equalsIgnoreCase(s))
					return true;
			}
			state.close();
			cxn.close();
		} catch (SQLException e) {
			printWriter.print(e.getMessage());
			e.printStackTrace();
		}
		return false;
	}

	public static boolean passwordMatch(String username, String password, PrintWriter printWriter) {
		String sql = "SELECT * FROM USER;";
		Statement state;
		
		try {
			Connection cxn = DBStatic.getMySQLConnection();
			state = cxn.createStatement();
			ResultSet result = state.executeQuery(sql);

			while(result.next()){
				String user = result.getString(1);
				String pass = result.getString(2);
				if(username.equalsIgnoreCase(user))
					if(password.equalsIgnoreCase(pass))
						return true;
			}
			state.close();
			cxn.close();
		} catch (SQLException e) {
			printWriter.print(e.getMessage());
			e.printStackTrace();
		}
		return false;
	}

	public static boolean passwordValide(String password, PrintWriter printWriter) {
		String acceptable[] = {"a","b","c","d","e","f","g","h","i","j","k","l","m","n","o","p","q","r","s","t","u","v","w","x","y","z",
		            "A","B","C","D","E","F","G","H","I","J","K","L","M","N","O","P","Q","R","S","T","U","V","W","X","Y","Z",
		            "0","1","2","3","4","5","6","7","8","9"};
		
		if(password.length() < 6) return false;
		else {
			for(int i = 0; i < acceptable.length; i++){
				for(int j = 0; j < password.length(); j++){
					if(Character.toString(password.charAt(j)).equals(acceptable[i]))
						return true;
				}
			}
		}
		return false;
	}

	public static void createUser(String username, String password, String email, PrintWriter printWriter) {
		String sql = "INSERT INTO USER VALUES('" + username + "','" + password + "','" + email + "');";
		
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

	public static void createUserProfile(String username, String firstName, String lastName,
			String birthDate, String email, Part imgPart, PrintWriter printWriter) {
		
		String oid = null;
		if(imgPart != null)
			oid = DBImage.addPartFileToGridFS(username, imgPart, printWriter);
		String sql = "INSERT INTO USER_PROFILE VALUES('" + username + "','" + firstName + "','" + lastName + "','" + birthDate + "','" + email + "','" + oid +"');";
		
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

	public static void deleteSessionKey(String username, String session_key, PrintWriter printWriter) {
		String sql = "DELETE FROM SESSION WHERE login = '" + username + "' AND sessionKey = '" + session_key + "';";

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