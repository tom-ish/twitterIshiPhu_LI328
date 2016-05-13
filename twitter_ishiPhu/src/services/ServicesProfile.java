package services;

import java.io.PrintWriter;

import javax.servlet.http.Part;

import org.json.JSONException;
import org.json.JSONObject;

import bd.DBAuthentification;
import bd.DBProfile;
import bd.DBSessionKey;

public class ServicesProfile {

	public static JSONObject createUserProfile(String username, String password, String password2,
			String firstName, String lastName, String birthDate, String email, Part imgPart,
			PrintWriter printWriter) {

		if(ServicesTools.isNullParameter(username) || ServicesTools.isNullParameter(email))
			return ServicesTools.error(2, "At least username and email are required to create a profile!");
		if(!password.equalsIgnoreCase(password2))
			return ServicesTools.error(17, "Please confirm your password");
		if (!DBAuthentification.passwordValide(password,printWriter))
			return ServicesTools.error(15, "Password is not in a valide format");
		
		DBAuthentification.createUserProfile(username, firstName, lastName, birthDate, email, imgPart, printWriter);
			
		JSONObject json = new JSONObject();
		try {
			json.put("username", username);
			json.put("email", email);
			
			if(!firstName.isEmpty())
				json.put("firstName", firstName);
			else
				json.put("firstName", "null");
			if(!lastName.isEmpty())
				json.put("lastName", lastName);
			else
				json.put("lastName", "null");
			if(!birthDate.isEmpty())
				json.put("birthDate", birthDate);
			else
				json.put("birthDate", "null");
			
			json.put("create a new Profile", "success");
		} catch (JSONException e) {
			printWriter.print(e.getMessage());
			e.printStackTrace();
		}
		return json;
	}
	
	public static JSONObject addProfilePicture(String key, String username,
			String email, Part imgPart, PrintWriter printWriter) {
		
		if(ServicesTools.isNullParameter(key) || ServicesTools.isNullParameter(username) || ServicesTools.isNullParameter(email))
			return ServicesTools.error(1, "One of the parameter is null!");
		if(DBSessionKey.sessionKeyExpire(key, printWriter))
			return ServicesTools.error(21, "The session_key is expired!");
		
		String user = DBSessionKey.getUsernameByKey(key);
		if(!DBAuthentification.existeLogin(user, printWriter))
			return ServicesTools.error(11, "The userName specified doesn't exist in the database!");
		
		DBProfile.addProfilePicture(key, user, email, imgPart, printWriter);
		
		JSONObject json = new JSONObject();
		JSONObject author = new JSONObject();
		
		
		try {
			author.put("login", user);
			author.put("session_key", key);
			
			json.put("author", author);
			json.put("email",email);
		} catch (JSONException e) {
			printWriter.print(e.getMessage());
			e.printStackTrace();
		}
		return json;
		
	}

}
