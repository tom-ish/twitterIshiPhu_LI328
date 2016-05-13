package bd;

import java.io.PrintWriter;

import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import com.mongodb.Mongo;

public class DBFavori {

	public static void addFavori(String author_login, String user, String tweet_txt, PrintWriter printWriter) {
		Mongo m = DBMongoStatic.getMongoConnection(printWriter);
		DBCollection followCollection = DBMongoStatic.getCollectionFromMongoDB(m, "Favori");
		
		DBObject object = new BasicDBObject();
		object.put("username", user);
		object.put("author_login", author_login);
		object.put("tweet", tweet_txt);
		
		followCollection.insert(object);
	}

}
