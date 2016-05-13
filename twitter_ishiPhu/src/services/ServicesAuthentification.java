package services;

import java.io.PrintWriter;

import org.json.JSONException;
import org.json.JSONObject;

import services.ServicesTools;
import services.mail.MailTools;
import bd.DBAuthentification;
import bd.DBSessionKey;

public class ServicesAuthentification {
	

	public static JSONObject createNewUser(String username, String password, String password2, String email, String firstName, String lastName, String birthDate, PrintWriter printWriter) {
		// TODO Auto-generated method stub
		if(ServicesTools.isNullParameter(username) || ServicesTools.isNullParameter(password) || ServicesTools.isNullParameter(email))
			return ServicesTools.error(1, "One of the parameter is null");
		if (DBAuthentification.existeLogin(username,printWriter))
			return ServicesTools.error(10,"User already exists");
		if(!password.equalsIgnoreCase(password2))
			return ServicesTools.error(17, "Please confirm your password");
		if (!DBAuthentification.passwordValide(password,printWriter))
			return ServicesTools.error(15, "Password is not in a valide format");
		
		JSONObject json = new JSONObject();
		
		// BD
		try {
			DBAuthentification.createUser(username,password,email,printWriter);
			
			if(ServicesTools.isNullParameter(firstName) || ServicesTools.isNullParameter(lastName) || ServicesTools.isNullParameter(birthDate)){
				json.put("createNewProfile","success, but information is missing");
				String txt = "Thank you for joining us in this platform!\n Welcome to the new social network! We hope you will enjoy your social experience!\nPS: Some information in your profile are missing";
				MailTools.sendAdminMail(email, "Welcome to the community!", txt, printWriter);
			}
			else{
				json.put("createNewProfile","success!");
				String txt = "Thank you for joining us in this platform!\n Welcome to the new social network! We hope you will enjoy your social experience!\n";
				MailTools.sendAdminMail(email, "Welcome to the community!", txt, printWriter);
			}

			json.put("userName", username);
			json.put("email", email);
			json.put("firstName", firstName);
			json.put("lastName", lastName);
			json.put("birthDate", birthDate);
			json.put("createNewUser", "success!");
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			printWriter.print(e.getMessage());
			e.printStackTrace();
		}
		
		return json;
	}

	public static JSONObject login(String username, String password, PrintWriter printWriter) {
		JSONObject json = new JSONObject();
		if(ServicesTools.isNullParameter(username) || ServicesTools.isNullParameter(password))
			return ServicesTools.error(1, "One of the parameter is null");
		if (!DBAuthentification.existeLogin(username,printWriter))
			return ServicesTools.error(11, "The userName specified doesn't exist in the database!");
		if (!DBAuthentification.passwordMatch(username,password,printWriter)){
			return ServicesTools.error(16, "Password doesn't match with the specified username");
		}
		
		String session_key = DBSessionKey.generateKey();
		if(!DBSessionKey.sessionKeyExpire(session_key,printWriter))
			return ServicesTools.error(21, "The sessionKey is not valide");
		DBSessionKey.addSessionKey(username, session_key);
				
		try {
			json.put("userName", username);
			json.put("session_key", session_key);
			json.put("login","success!");
		} catch (JSONException e) {
			printWriter.print(e.getMessage());
			e.printStackTrace();
		}
		return json;
	}

	public static JSONObject logout(String key, PrintWriter printWriter) {
		JSONObject json = new JSONObject();
		if(ServicesTools.isNullParameter(key))
			return ServicesTools.error(1, "One of the parameter is null!");
		if(DBSessionKey.sessionKeyExpire(key, printWriter))
			return ServicesTools.error(21, "The session_key is expired");
		
		String user = DBSessionKey.getUsernameByKey(key);
		if(!DBAuthentification.existeLogin(user, printWriter))
			return ServicesTools.error(11, "The userName specified doesn't exist in the database!");
		
		DBAuthentification.deleteSessionKey(user, key, printWriter);
		
		JSONObject author = new JSONObject();
		
		
		try {
			author.put("login", user);
			author.put("session_key", key);
			
			json.put("author", author);
			json.put("logout", "success!");
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			printWriter.print(e.getMessage());
			e.printStackTrace();
		}
		
		return json;
	}

}