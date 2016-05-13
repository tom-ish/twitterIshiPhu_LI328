package services;

import java.io.PrintWriter;
import java.net.UnknownHostException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import org.json.JSONException;
import org.json.JSONObject;

import com.mongodb.MongoException;

import bd.DBAuthentification;
import bd.DBSessionKey;
import bd.DBTweet;

public class ServicesTweet {
	
	public static JSONObject postTweet(String key, String tweet, PrintWriter printWriter) throws UnknownHostException, MongoException{
		if(ServicesTools.isNullParameter(key) || ServicesTools.isNullParameter(tweet))
			return ServicesTools.error(1, "One of the parameter is null!");
		if(DBSessionKey.sessionKeyExpire(key, printWriter))
			return ServicesTools.error(21, "The session_key is expired!");
		
		String user = DBSessionKey.getUsernameByKey(key);
		if(!DBAuthentification.existeLogin(user, printWriter))
			return ServicesTools.error(11, "The userName specified doesn't exist in the database!");
		
		String date = null;
		try {
			date = DBTweet.postTweet(user, key, tweet, printWriter);
		} catch (ParseException e1) {
			printWriter.println(e1.getMessage());
			e1.printStackTrace();
		}
		
		JSONObject json = new JSONObject();
		JSONObject author = new JSONObject();
		
		
		try {
			author.put("login", user);
			author.put("session_key", key);
			
			json.put("author", author);
			json.put("tweet", tweet);
			json.put("postTweet","success!");
			json.put("date", date);
		} catch (JSONException e) {
			printWriter.print(e.getMessage());
			e.printStackTrace();
		}
		return json;
	}
	
	public static JSONObject deleteTweet(String key, String date_from, String date_to, PrintWriter printWriter){
		if(ServicesTools.isNullParameter(key) || ServicesTools.isNullParameter(date_from) || ServicesTools.isNullParameter(date_to))
			return ServicesTools.error(1, "One of the parameter is null!");
		if(DBSessionKey.sessionKeyExpire(key, printWriter))
			return ServicesTools.error(21, "The session_key is expired!");
		
		String user = DBSessionKey.getUsernameByKey(key);
		if(!DBAuthentification.existeLogin(user, printWriter))
			return ServicesTools.error(11, "The userName specified doesn't exist in the database!");
		
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yy hh:mm:ss");
		Date dateFrom = null, dateTo = null;
		try {
			dateFrom = sdf.parse(date_from);
			dateTo = sdf.parse(date_to);
		} catch (ParseException e) {
			printWriter.print(e.getMessage());
			e.printStackTrace();
		}
		
		DBTweet.deleteTweet(user, key, dateFrom, dateTo, printWriter);
		
		JSONObject json = new JSONObject();
		JSONObject author = new JSONObject();
		
		
		try {
			author.put("login", user);
			author.put("session_key", key);
			
			json.put("author", author);
			json.put("deleted message from", dateFrom);
			json.put("deleted message to", dateTo);
		} catch (JSONException e) {
			printWriter.print(e.getMessage());
			e.printStackTrace();
		}
		return json;
	}
	
	public static JSONObject deleteAllTweet(String key, PrintWriter printWriter){
		if(ServicesTools.isNullParameter(key))
			return ServicesTools.error(1, "One of the parameter is null!");
		if(DBSessionKey.sessionKeyExpire(key, printWriter))
			return ServicesTools.error(21, "The session_key is expired!");
		
		String user = DBSessionKey.getUsernameByKey(key);
		if(!DBAuthentification.existeLogin(user, printWriter))
			return ServicesTools.error(11, "The userName specified doesn't exist in the database!");
		JSONObject json = new JSONObject();
		
		DBTweet.deleteAllTweet(printWriter);
		
		JSONObject author = new JSONObject();
		
		try {
			author.put("login", user);
			author.put("session_key", key);
			
			json.put("author", author);
			json.put("deleteAllTweet", "success!");
		} catch (JSONException e) {
			printWriter.print(e.getMessage());
			e.printStackTrace();
		}
			
		return json;
	}

	public static JSONObject reTweet(String key, String srcID, String tweet, PrintWriter printWriter) {
		if(ServicesTools.isNullParameter(key) || ServicesTools.isNullParameter(srcID) || ServicesTools.isNullParameter(tweet))
			return ServicesTools.error(1, "One of the parameter is null!");
		if(DBSessionKey.sessionKeyExpire(key, printWriter))
			return ServicesTools.error(21, "The session_key is expired!");
		
		String user = DBSessionKey.getUsernameByKey(key);
		if(!DBAuthentification.existeLogin(user, printWriter))
			return ServicesTools.error(11, "The userName specified doesn't exist in the database!");
		
		DBTweet.reTweet(key, srcID, user, tweet, printWriter);
		
		JSONObject json = new JSONObject();
		JSONObject author = new JSONObject();
		
		
		try {
			author.put("login", user);
			author.put("session_key", key);
			
			json.put("author", author);
			json.put("reTweeted_username", user);
			json.put("reTweet","success!");
		} catch (JSONException e) {
			printWriter.print(e.getMessage());
			e.printStackTrace();
		}
		return json;
	}

	public static JSONObject getUserTweet(String key, PrintWriter printWriter) {
		if(ServicesTools.isNullParameter(key))
			return ServicesTools.error(1, "Session_key is null!");
		if(DBSessionKey.sessionKeyExpire(key, printWriter))
			return ServicesTools.error(21, "The session_key is expired!");
		
		String user = DBSessionKey.getUsernameByKey(key);
		if(!DBAuthentification.existeLogin(user, printWriter))
			return ServicesTools.error(11, "The userName specified doesn't exist in the database!");
		
		ArrayList<ArrayList<String>> tweets = DBTweet.getUserTweet(user, printWriter);
		
		JSONObject json = new JSONObject();
		JSONObject author = new JSONObject();
		
		
		try {
			author.put("login", user);
			author.put("session_key", key);
			
			json.put("author", author);
			json.put("tweet", tweets);
			json.put("getUserTweet","success!");
		} catch (JSONException e) {
			printWriter.print(e.getMessage());
			e.printStackTrace();
		}
		return json;
	}

}
