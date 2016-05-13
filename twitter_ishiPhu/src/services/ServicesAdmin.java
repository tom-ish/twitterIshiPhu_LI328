package services;

import java.io.PrintWriter;

import org.json.JSONException;
import org.json.JSONObject;

import bd.DBAdmin;
import bd.DBAuthentification;
import bd.DBSessionKey;

public class ServicesAdmin {
	
	private static String MDP = "admin$";
	
	public static JSONObject deleteExpiredSession(String adminID, String userID, PrintWriter printWriter){
		if(ServicesTools.isNullParameter(adminID) || ServicesTools.isNullParameter(userID))
			return ServicesTools.error(1, "One of the parameter is null!");
		if(!DBAuthentification.existeLogin(userID, printWriter))
			return ServicesTools.error(11, "The user specified doesn't exist!");
		
		if(!adminID.equalsIgnoreCase(MDP))
			return ServicesTools.error(101, "AdminID is not correct!");
		
		if(!DBSessionKey.sessionKeyExpire(DBSessionKey.getKey(userID), printWriter))
			return ServicesTools.error(105, "Admin attempts to delete an active session");
		DBAdmin.deleteExpiredSession(userID, printWriter);
		
		JSONObject json = new JSONObject();
		try {
			json.put("adminID", adminID);
			json.put("userID", userID);
			json.put("Deleted expired session", "success!");
		} catch (JSONException e) {
			printWriter.print(e.getMessage());
			e.printStackTrace();
		}
		return json;
	}
	
	public static JSONObject deleteUserProfile(String adminID, String username, PrintWriter printWriter){
		if(ServicesTools.isNullParameter(adminID) || ServicesTools.isNullParameter(username))
			return ServicesTools.error(1, "One of the parameter is null!");
		if(!DBAuthentification.existeLogin(username, printWriter))
			return ServicesTools.error(11, "The user specified doesn't exist!");
		
		if(!adminID.equalsIgnoreCase(MDP))
			return ServicesTools.error(101, "AdminID is not correct ==> Access denied");
		
		DBAdmin.deleteUserProfile(username, printWriter);
		
		JSONObject json = new JSONObject();
		try {
			json.put("adminID", adminID);
			json.put("userID", username);
			json.put("Deleted User Profile", "success!");
		} catch (JSONException e) {
			printWriter.print(e.getMessage());
			e.printStackTrace();
		}
		return json;
	}

}
