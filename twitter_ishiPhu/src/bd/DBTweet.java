package bd;

import java.io.PrintWriter;
import java.net.UnknownHostException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;

import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.Mongo;
import com.mongodb.MongoException;

public class DBTweet {
	
	// Retourne la date a laquelle le tweet a ete poste
	public static String postTweet(String username, String key, String tweet, PrintWriter printWriter) throws UnknownHostException, MongoException, ParseException{
		GregorianCalendar calendar = new java.util.GregorianCalendar();
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
		Date date_du_jour = calendar.getTime();
		String date_str = sdf.format(date_du_jour);
				
		Mongo m = DBMongoStatic.getMongoConnection(printWriter);
		DBCollection collection = DBMongoStatic.getCollectionFromMongoDB(m, "Tweet");
		
		BasicDBObject object = new BasicDBObject();
		BasicDBObject author = new BasicDBObject();
		author.put("login",username);
		author.put("session_key",key);
		object.put("author", author);
		object.put("tweet", tweet);
		object.put("date", date_str);
		object.put("retweeted",false);
		collection.insert(object);
		
		/*
		object.put("author_login", username);
		DBCursor curseur = collection.find(object);
		DBObject rslt = curseur.next();
		
		DBObject obj = new BasicDBObject();
		DBObject tweet_obj = new BasicDBObject();
		if(rslt == null){
			obj.put("author_login", username);
			obj.put("session_key", key);
			tweet_obj.put("tweet_id", 1);
			tweet_obj.put("tweet_txt", tweet);
			obj.put("tweet", tweet_obj);
			collection.insert(obj);
		}
		else{
			while(curseur.hasNext()){
				DBObject tmp = new BasicDBObject();
				tmp.put("tweet_id", ((int)((DBObject) rslt.get("tweet")).get("tweet_id")+1));
				tmp.put("tweet_txt", tweet);
				collection.update((DBObject) rslt.get("tweet"), tmp);
			}
		}*/
		return date_str;
	}
	
	public static void deleteAllTweet(PrintWriter printWriter){
		Mongo m = DBMongoStatic.getMongoConnection(printWriter);
		DBCollection collection = DBMongoStatic.getCollectionFromMongoDB(m, "Tweet");
		
		collection.remove(new BasicDBObject());
	}
	
	public static void deleteTweet(String username, String key, Date date_from, Date date_to, PrintWriter printWriter){
		Mongo m = DBMongoStatic.getMongoConnection(printWriter);
		DBCollection collection = DBMongoStatic.getCollectionFromMongoDB(m, "Tweet");
		
		DBObject query = new BasicDBObject();
		query.put("author_login", username);
		query.put("session_key", key);
		DBCursor curseur = collection.find(query);
		while(curseur.hasNext()){
			try {
				String date = (String) curseur.next().get("date");
				SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yy hh:mm:ss");
				Date date_tmp = sdf.parse(date);
				
				if(date_tmp.after(date_from) && date_tmp.before(date_to))
					collection.remove(curseur.next());
			} catch (ParseException e) {
				printWriter.print(e.getMessage());
				e.printStackTrace();
			}
		}
	}

	public static void reTweet(String key, String srcID, String user, String tweet, PrintWriter printWriter) {
		GregorianCalendar calendar = new java.util.GregorianCalendar();
		Date date_du_jour = calendar.getTime();
		String date = SimpleDateFormat.getDateTimeInstance(SimpleDateFormat.SHORT, SimpleDateFormat.MEDIUM).format(date_du_jour);
		
		Mongo m = DBMongoStatic.getMongoConnection(printWriter);
		DBCollection collection = DBMongoStatic.getCollectionFromMongoDB(m, "Tweet");
		
		DBObject query = new BasicDBObject();
		query.put("author_login", srcID);
		query.put("tweet", tweet);
		DBCursor curseur = collection.find(query);
		while(curseur.hasNext()){
			curseur.next().put("retweeted", true);
		}
		
		DBObject object = new BasicDBObject();
		BasicDBObject author = new BasicDBObject();
		author.put("login",srcID);
		author.put("session_key",key);
		object.put("author", author);
		object.put("reTweeted by", user);
		object.put("tweet", tweet);
		object.put("date", date);
		
		collection.insert(object);
	}

	public static ArrayList<ArrayList<String>> getUserTweet(String user, PrintWriter printWriter) {
		ArrayList<ArrayList<String>> rslt = new ArrayList<ArrayList<String>>();
		
		Mongo m = DBMongoStatic.getMongoConnection(printWriter);
		DBCollection collection = DBMongoStatic.getCollectionFromMongoDB(m, "Tweet");
		
		DBObject query = new BasicDBObject();
		query.put("author_login", user);
		
		DBCursor curseur = collection.find(query);
		curseur.sort(new BasicDBObject("date", -1));
		
		while(curseur.hasNext()){
			ArrayList<String> list = new ArrayList<String>();
			DBObject c = curseur.next();
			String tweet = (String) c.get("tweet");
			String date = (String) c.get("date");
			list.add(0, tweet);
			list.add(1, date);
			rslt.add(list);
		}

		/* Tri en fonction de la date
		Collections.sort(rslt);
		Collections.reverse(rslt);*/
		/*
		ArrayList<ArrayList<String>> sortedList = new ArrayList<ArrayList<String>>();
		String dateString0 = rslt.get(0).get(1);
		System.out.println(dateString0);
		int cpt = 0;
		for(ArrayList<String> tmp_list : rslt){
			if(!tmp_list.isEmpty()){
				String dateString1 = tmp_list.get(1);
				SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yy hh:mm:ss");
				Date date_tmp0 = sdf.parse(dateString0);
				Date date_tmp1 = sdf.parse(dateString1);

				if(date_tmp0.compareTo(date_tmp1) <= 0){
					// date_tmp0 earlier than date_tmp1
					sortedList.add(tmp_list);
					dateString0 = dateString1;
				}
				else{
					sortedList.add(cpt,tmp_list);
				}
				cpt++;
			}
		}
		*/
		return rslt;
	}

}
