package services;

import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;

import org.json.JSONException;
import org.json.JSONObject;

import services.mail.MailTools;
import bd.DBAuthentification;
import bd.DBFriend;
import bd.DBMail;
import bd.DBSessionKey;

public class ServicesFriend {
	
	public static JSONObject AddFriend(String key, String idFriend, PrintWriter printWriter){
		GregorianCalendar calendar = new java.util.GregorianCalendar();
		Date date_du_jour = calendar.getTime();
		String date = SimpleDateFormat.getDateTimeInstance(SimpleDateFormat.SHORT, SimpleDateFormat.MEDIUM).format(date_du_jour);
		
		if(ServicesTools.isNullParameter(key) || ServicesTools.isNullParameter(idFriend))
			return ServicesTools.error(1, "One of the parameter is null");
		if(!DBAuthentification.existeLogin(idFriend, printWriter))
			return ServicesTools.error(11, "The idFriend specified doesn't exist in the database!");
		if(DBSessionKey.sessionKeyExpire(key, printWriter))
			return ServicesTools.error(21, "The session_key is expired!");
		
		String user = DBSessionKey.getUsernameByKey(key);
		if(!DBAuthentification.existeLogin(user, printWriter))
			return ServicesTools.error(11, "The userName specified doesn't exist in the database!");

		DBFriend.addFriend(user ,idFriend, date, printWriter);
		
		// Envoi d'un mail de confirmation
		String message = idFriend + " added you on his/her friendList!";
		String mail = DBMail.getEmailFromID(idFriend, printWriter);
		MailTools.sendAdminMail(mail , "Twitter IshiPhu notifications", message, printWriter);
		
		JSONObject json = new JSONObject();
		JSONObject author = new JSONObject();
		
		
		try {
			author.put("login", user);
			author.put("session_key", key);
			
			json.put("author", author);
			json.put("idFriend", idFriend);
			json.put("idFriend mail", mail);
			json.put("AddFriend Message", "send");
			json.put("AddFriend", "success");
		} catch (JSONException e) {
			printWriter.print(e.getMessage());
			e.printStackTrace();
		}
		return json;
	}
	
	public static JSONObject RemoveFriend(String key, String idFriend, PrintWriter printWriter){
		if(ServicesTools.isNullParameter(key) || ServicesTools.isNullParameter(idFriend))
			return ServicesTools.error(1, "One of the parameter is null");
		if(DBSessionKey.sessionKeyExpire(key, printWriter))
			return ServicesTools.error(21, "The session_key is expired!");
		if(!DBAuthentification.existeLogin(idFriend, printWriter))
			return ServicesTools.error(11, "The idFriend specified doesn't exist in the database!");
		

		String user = DBSessionKey.getUsernameByKey(key);
		if(!DBAuthentification.existeLogin(user, printWriter))
			return ServicesTools.error(11, "The userName specified doesn't exist in the database!");

		DBFriend.deleteFriend(user, idFriend, printWriter);
		
		// Envoi d'un mail de confirmation
		String message = idFriend + " deleted you from his/her friendList!";
		String mail = DBMail.getEmailFromID(idFriend, printWriter);
		MailTools.sendAdminMail(mail , "Twitter IshiPhu notifications", message, printWriter);
		
		JSONObject json = new JSONObject();
		JSONObject author = new JSONObject();
		
		
		try {
			author.put("login", user);
			author.put("session_key", key);
			
			json.put("author", author);
			json.put("idFriend", idFriend);
			json.put("idFriend mail", mail);
			json.put("deleteFriend Message", "send");
			json.put("deleteFriend", "success");
		} catch (JSONException e) {
			printWriter.print(e.getMessage());
			e.printStackTrace();
		}
		return json;
	}

	public static JSONObject getConnectedFriends(String key, PrintWriter printWriter) {
		if(ServicesTools.isNullParameter(key))
			return ServicesTools.error(1, "SessionKey is null");
		if(DBSessionKey.sessionKeyExpire(key, printWriter))
			return ServicesTools.error(21, "The session_key is expired!");

		String user = DBSessionKey.getUsernameByKey(key);
		if(!DBAuthentification.existeLogin(user, printWriter))
			return ServicesTools.error(11, "The userName specified doesn't exist in the database!");

		ArrayList<String> listeFriends = DBFriend.getConnectedFriends(user, printWriter);
		
		JSONObject json = new JSONObject();
		JSONObject author = new JSONObject();
		
		
		try {
			author.put("login", user);
			author.put("session_key", key);
			
			json.put("author", author);
			JSONObject friend = new JSONObject();
			JSONObject tmp = new JSONObject();
			for(int i = 0; i < listeFriends.size(); i++){
				tmp.put("friendName", listeFriends.get(i).toString());
				tmp.put("nbConnected", i);
				friend.put("friend", tmp);
			}
			json.put("listeFriends", friend);
		} catch (JSONException e) {
			printWriter.print(e.getMessage());
			e.printStackTrace();
		}
		return json;
	}

}
