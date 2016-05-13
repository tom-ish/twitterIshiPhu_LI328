package services;

import java.io.PrintWriter;

import org.json.JSONException;
import org.json.JSONObject;

import bd.DBAuthentification;
import bd.DBFavori;
import bd.DBSessionKey;
import bd.TweetTools;

public class ServicesFavori {
	
	public static JSONObject addFavori(String key, String tweet_id, PrintWriter printWriter){
		if(ServicesTools.isNullParameter(key) || ServicesTools.isNullParameter(tweet_id))
			return ServicesTools.error(1, "One of the parameter is null");
		if(DBSessionKey.sessionKeyExpire(key, printWriter))
			return ServicesTools.error(21, "The session_key is expired!");
		
		String user = DBSessionKey.getUsernameByKey(key);
		if(!DBAuthentification.existeLogin(user, printWriter))
			return ServicesTools.error(11, "The userName specified doesn't exist in the database!");
		
		String tweet_txt = TweetTools.getTweetByID(tweet_id, printWriter);
		String author_login = TweetTools.getTweetAuthor(tweet_id, printWriter);
		DBFavori.addFavori(author_login, user, tweet_txt, printWriter);
		
		JSONObject json = new JSONObject();
		JSONObject author = new JSONObject();
		
		
		try {
			author.put("login", user);
			author.put("session_key", key);
			
			json.put("author", author);
			json.put("tweet_id", tweet_id);
			json.put("tweet_txt", tweet_txt);
			json.put("add to the favorites", "success!");
		} catch (JSONException e) {
			printWriter.print(e.getMessage());
			e.printStackTrace();
		}
		return json;
	}

}
