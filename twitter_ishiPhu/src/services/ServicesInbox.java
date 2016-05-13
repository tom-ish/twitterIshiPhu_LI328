package services;

import java.io.PrintWriter;
import java.net.UnknownHostException;

import org.json.JSONObject;

import com.mongodb.MongoException;

import bd.DBAuthentification;
import bd.DBInbox;
import bd.DBSessionKey;

public class ServicesInbox {
	
	public static JSONObject postInbox(String key, String dest, String message, PrintWriter printWriter){
		if(ServicesTools.isNullParameter(key) || ServicesTools.isNullParameter(dest) || ServicesTools.isNullParameter(message))
			return ServicesTools.error(1, "One of the parameter is null");
		if(DBSessionKey.sessionKeyExpire(key,printWriter))
			return ServicesTools.error(21, "The session_key is expired");
		if(!DBAuthentification.existeLogin(dest,printWriter))
			return ServicesTools.error(12, "The userDest specified doesn't exist in the database!");
		
		String username = DBSessionKey.getUsernameByKey(key);
		if(!DBAuthentification.existeLogin(username, printWriter))
			return ServicesTools.error(11, "The userName specified doesn't exist in the database!");
		
		try {
			DBInbox.postInbox(username,key,dest,message,printWriter);
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (MongoException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return ServicesTools.ok();
	}

}
