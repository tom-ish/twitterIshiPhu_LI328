package bd;

import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import org.json.JSONException;
import org.json.JSONObject;

public class DBSessionKey {
	
	public static String generateKey(){
		return DBSessionKey.generateKey(32);
	}
	
	public static String generateKey(int length){
		String s = "";
		String dictionary = "";

		dictionary = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890";
		
		for (int i = 0; i < length; i++) {
			s += dictionary.charAt((int) (Math.random() * dictionary.length()));
		}
		return s;
	}
	
	public static String getUsernameByKey(String key){
		String sql = "SELECT * FROM SESSION;";
		String rslt = "";
		try {
			Connection cxn = DBStatic.getMySQLConnection();
			Statement state = cxn.createStatement();
			ResultSet result = state.executeQuery(sql);
			while(result.next()){
				String sessionKey = result.getString(2);
				if(key.equalsIgnoreCase(sessionKey)){
					rslt = result.getString(1);
					return rslt;
				}
			}
			if(rslt.equalsIgnoreCase("")){
				return null;
			}
			state.close();
			cxn.close();
		}
		catch(SQLException e){
			e.printStackTrace();
		}
		return rslt;
	}
	
	public static JSONObject getUserJSONByKey(String key){
		String sql = "SELECT * FROM SESSION;";
		JSONObject rslt = new JSONObject();
		try {
			Connection cxn = DBStatic.getMySQLConnection();
			Statement state = cxn.createStatement();
			ResultSet result = state.executeQuery(sql);
			while(result.next()){
				String sessionKey = result.getString(2);
				if(key.equalsIgnoreCase(sessionKey)){
					String login = result.getString(1);
					rslt.put("username", login);
					rslt.put("sessionKey", sessionKey);
					return rslt;
				}
			}
			state.close();
			cxn.close();
		}
		catch(SQLException e1){
			e1.printStackTrace();
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return rslt;
	}

	public static String getKey(String username) {
		String sql = "SELECT * FROM SESSION;";
		String rslt = "";
		try {
			Connection cxn = DBStatic.getMySQLConnection();
			Statement state = cxn.createStatement();
			ResultSet result = state.executeQuery(sql);
			while(result.next()){
				String login = result.getString(1);
				if(username.equalsIgnoreCase(login)){
					rslt = result.getString(2);
					return rslt;
				}
			}
			if(rslt.equalsIgnoreCase("")){
				return null;
			}
			state.close();
			cxn.close();
		}
		catch(SQLException e){
			e.printStackTrace();
		}
		return rslt;
	}

	public static void addSessionKey(String username, String session_key) {
		GregorianCalendar calendar = new java.util.GregorianCalendar();
		
		// Date d'expiration
		calendar.add(Calendar.HOUR, 1);
		Date date_expiration = calendar.getTime();
		
		// On recupere la date et le temps expiratoire sous la forme: "JJ/MM/AA HH:MM:SS"
		String dateNow = SimpleDateFormat.getDateTimeInstance(SimpleDateFormat.SHORT, SimpleDateFormat.MEDIUM).format(date_expiration);
		
		String sql = "INSERT INTO SESSION VALUES('" + username + "','" + session_key + "','" + dateNow + "');";
		try {
			Connection cxn = DBStatic.getMySQLConnection();
			Statement state = cxn.createStatement();
			state.executeUpdate(sql);
			state.close();
			cxn.close();
		} catch (SQLException e){
			e.printStackTrace();
		}
	}

	public static boolean sessionKeyValide(String session_key, PrintWriter printWriter) {
		if(!DBSessionKey.uniqueSessionKey(session_key, printWriter)) return false;
		return true;
	}

	public static boolean uniqueSessionKey(String session_key, PrintWriter printWriter) {
		String sql = "SELECT S.sessionKey FROM SESSION S;";
		try {
			Connection cxn = DBStatic.getMySQLConnection();
			Statement state = cxn.createStatement();
			
			ResultSet result = state.executeQuery(sql);
			
			while(result.next()){
				if(session_key.equalsIgnoreCase(result.getString(1)))
					return false;
			}
			state.close();
			cxn.close();
		} catch (SQLException e) {
			printWriter.print(e.getMessage());
			e.printStackTrace();
		}
		return true;
	}
	
	public static boolean sessionKeyExpire(String session_key, PrintWriter printWriter) {
		GregorianCalendar calendar = new java.util.GregorianCalendar();
		Date date_du_jour = calendar.getTime();
		
		String sql = "SELECT * FROM SESSION;";
		try {
			Connection cxn = DBStatic.getMySQLConnection();
			Statement state = cxn.createStatement();
			
			ResultSet result = state.executeQuery(sql);
			
			while(result.next()){
				String date = result.getString(3);
				SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yy hh:mm:ss");
				Date date_expiration = sdf.parse(date);
				
				if(session_key.equalsIgnoreCase(result.getString(2)))
					if(date_du_jour.getTime() > date_expiration.getTime()){
						printWriter.print("Session expiree!");
						return true;
					}
					else return false;
			}
			state.close();
			cxn.close();
		} catch (SQLException e) {
			printWriter.print(e.getMessage());
			e.printStackTrace();
		} catch ( ParseException e){
			printWriter.print(e.getMessage());
			e.printStackTrace();
		}
		return true;
	}
	
	public static Date getDateExpiration(String user, PrintWriter printWriter) {
		Date date_expiration = null;
		String sql = "SELECT * FROM SESSION;";
		try {
			Connection cxn = DBStatic.getMySQLConnection();
			Statement state = cxn.createStatement();
			
			ResultSet result = state.executeQuery(sql);
			
			while(result.next()){
				String login1 = result.getString(1);
				String login2 = result.getString(2);
				if(login1.equalsIgnoreCase(user) || login2.equalsIgnoreCase(user)){
					date_expiration = new SimpleDateFormat("dd/MM/yy HH:mm:ss").parse(result.getString(3));
					if(date_expiration != null) printWriter.print("-----------" + date_expiration.toString() + "-----------");
					else printWriter.print("date not null");
				}
			}
			state.close();
			cxn.close();
			return date_expiration;
		} catch (SQLException e) {
			printWriter.print(e.getMessage());
			e.printStackTrace();
		} catch ( ParseException e){
			printWriter.print(e.getMessage());
			e.printStackTrace();
		}
		return date_expiration;
	}


}
