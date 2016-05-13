package bd;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;

import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.Mongo;

public class DBSearch {

	public static ArrayList<String> searchAllDB(PrintWriter printWriter) {
		Mongo m = DBMongoStatic.getMongoConnection(printWriter);
		DBCollection collection = DBMongoStatic.getCollectionFromMongoDB(m, "Tweet");
		DBCursor curseur = collection.find();
		

		BasicDBObject date = new BasicDBObject();
		date.put("date", -1);
		curseur.sort(date);
		
		ArrayList<String> rslt = new ArrayList<String>();
		while(curseur.hasNext()){
			rslt.add(curseur.next().toString());
		}
		
		Collections.sort(rslt);
		Collections.reverse(rslt);

		return rslt;
	}

	public static ArrayList<String> searchAllTweetsFromFriends(ArrayList<String> listeFriends, PrintWriter writer) {
		Mongo m = DBMongoStatic.getMongoConnection(writer);
		DBCollection collection = DBMongoStatic.getCollectionFromMongoDB(m, "Tweet");
		
		BasicDBObject query = new BasicDBObject();
		BasicDBObject author = new BasicDBObject();
		author.put("login", new BasicDBObject("$in", listeFriends));
		query.put("author", author);
		
		
		DBCursor curseur = collection.find(query);
		
		BasicDBObject date = new BasicDBObject();
		date.put("date", -1);
		curseur.sort(date);
		

		ArrayList<String> rslt = new ArrayList<String>();
		while(curseur.hasNext()){
			rslt.add(curseur.next().toString());
		}
		
		Collections.sort(rslt);
		Collections.reverse(rslt);

		return rslt;
	}

}
