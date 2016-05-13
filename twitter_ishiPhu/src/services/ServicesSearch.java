package services;

import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;

import org.json.JSONException;
import org.json.JSONObject;

import bd.DBFriend;
import bd.DBSearch;
import bd.DBSessionKey;

public class ServicesSearch {

	public static JSONObject search(String key, String query, String byFriends, PrintWriter writer) {
		ArrayList<String> listeMsg = null;
		ArrayList<String> listeFriends;
		String username = "unauthentified user";
		boolean contact_only = false;
		
		// Resultat
		JSONObject json = new JSONObject();
			
		if(query.equalsIgnoreCase("")){
			// Recherche en mode non authentifie
			if(ServicesTools.isNullParameter(key)){
				listeMsg = DBSearch.searchAllDB(writer);
			}
			// Recherche en mode authentifie
			else{
				username = DBSessionKey.getUsernameByKey(key);
				// Recherche parmi les amis
				if(byFriends.equalsIgnoreCase("1")){
					listeFriends = DBFriend.getFriends(username,writer);
					listeMsg = DBSearch.searchAllTweetsFromFriends(listeFriends, writer);
					contact_only = true;
				}
				
				// Recherche globale
				else{
					listeMsg = DBSearch.searchAllDB(writer);
				}
					
			}
		}
		else {
			
		}
		
		GregorianCalendar calendar = new java.util.GregorianCalendar();
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
		Date date_du_jour = calendar.getTime();
		String date_str = sdf.format(date_du_jour);
		
		JSONObject author = new JSONObject();
		
		
		try {
			author.put("login", username);
			author.put("session_key", key);
			
			json.put("author", author);
			json.put("results", listeMsg);
			json.put("query", query);
			json.put("contact_only",contact_only);
			json.put("date", date_str);
			json.put("search", "success!");
		} catch (JSONException e) {
			writer.print(e.getMessage());
			e.printStackTrace();
		}
		
		return json;
	}
	


}
