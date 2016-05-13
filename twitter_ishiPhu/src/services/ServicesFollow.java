package services;

import java.io.PrintWriter;

import org.json.JSONException;
import org.json.JSONObject;

import services.mail.MailTools;
import bd.DBAuthentification;
import bd.DBFollow;
import bd.DBMail;
import bd.DBSessionKey;

public class ServicesFollow {

	public static JSONObject follow(String key, String followDest, PrintWriter printWriter){
		if(ServicesTools.isNullParameter(key) || ServicesTools.isNullParameter(followDest))
			return ServicesTools.error(1, "One of the parameter is null!");
		if(DBSessionKey.sessionKeyExpire(key, printWriter))
			return ServicesTools.error(21, "The session_key is expired!");
		
		String user = DBSessionKey.getUsernameByKey(key);
		if(!DBAuthentification.existeLogin(user, printWriter))
			return ServicesTools.error(11, "The userName specified doesn't exist in the database!");
		
		DBFollow.follow(user, followDest, printWriter);
		
		// Envoi d'une notification par mail
		String mail = DBMail.getEmailFromID(followDest, printWriter);
		String txt = "You have been followed by " + user + "!";
		MailTools.sendAdminMail(mail, "Following notification", txt, printWriter);
		
		JSONObject json = new JSONObject();
		JSONObject author = new JSONObject();
		
		
		try {
			author.put("login", user);
			author.put("session_key", key);
			
			json.put("author", author);
			json.put("session_key", key);
			json.put("followDestination", followDest);
			json.put("follow","success!");
		} catch (JSONException e) {
			printWriter.print(e.getMessage());
			e.printStackTrace();
		}
		return json;
	}

	public static JSONObject unfollow(String session_key, String userDest, PrintWriter printWriter) {
		if(ServicesTools.isNullParameter(session_key) || ServicesTools.isNullParameter(userDest))
			return ServicesTools.error(1, "One of the parameter is null!");
		if(DBSessionKey.sessionKeyExpire(session_key, printWriter))
			return ServicesTools.error(21, "The session_key is expired!");
		
		String user = DBSessionKey.getUsernameByKey(session_key);
		if(!DBAuthentification.existeLogin(user, printWriter))
			return ServicesTools.error(11, "The userName specified doesn't exist in the database!");
		
		DBFollow.unfollow(user,userDest,printWriter);
		
		JSONObject json = new JSONObject();
		JSONObject author = new JSONObject();
		
		
		try {
			author.put("login", user);
			author.put("session_key", session_key);
			
			json.put("author", author);
			json.put("session_key",session_key);
			json.put("has followed", userDest);
			json.put("delete follower","success!");
		} catch (JSONException e) {
			printWriter.print(e.getMessage());
			e.printStackTrace();
		}
		return json;
	}
}
