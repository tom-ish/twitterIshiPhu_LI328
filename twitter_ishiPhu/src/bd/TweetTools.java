package bd;

import java.io.PrintWriter;

import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.Mongo;

public class TweetTools {

	public static String getTweetByID(String tweet_id, PrintWriter printWriter) {
		Mongo m = DBMongoStatic.getMongoConnection(printWriter);
		DBCollection collection = DBMongoStatic.getCollectionFromMongoDB(m, "Tweet");
		DBObject query = new BasicDBObject();
		query.put("_id", tweet_id);
		DBCursor curseur = collection.find(query);
		
		String tweet = curseur.next().get("tweet").toString();
		return tweet;
	}
	
	public static int getTweetIDbyTweet(String tweet_txt, PrintWriter printWriter){
		Mongo m = DBMongoStatic.getMongoConnection(printWriter);
		DBCollection collection = DBMongoStatic.getCollectionFromMongoDB(m, "Tweet");
		DBObject query = new BasicDBObject();
		query.put("tweet", tweet_txt);
		DBCursor curseur = collection.find();
		
		String id_str = curseur.next().get("_id").toString();
		Integer id = Integer.parseInt(id_str);
		return id;
	}

	public static String getTweetAuthor(String tweet_id, PrintWriter printWriter) {
		Mongo m = DBMongoStatic.getMongoConnection(printWriter);
		DBCollection collection = DBMongoStatic.getCollectionFromMongoDB(m, "Tweet");
		DBObject query = new BasicDBObject();
		query.put("_id",tweet_id);
		DBCursor curseur = collection.find();
		
		String author_login = curseur.next().get("author_login").toString();
		return author_login;
	}

}
