package bd;

import java.io.PrintWriter;

import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.Mongo;

public class DBFollow {
	
	public static void follow(String username, String followDest, PrintWriter printWriter){
		Mongo m = DBMongoStatic.getMongoConnection(printWriter);
		DBCollection followCollection = DBMongoStatic.getCollectionFromMongoDB(m, "Follow");
		
		DBObject object = new BasicDBObject();
		object.put("author_login", username);
		object.put("followDestination", followDest);
		
		followCollection.insert(object);
	}

	public static void unfollow(String username, String userDest, PrintWriter printWriter) {
		Mongo m = DBMongoStatic.getMongoConnection(printWriter);
		DBCollection followCollection = DBMongoStatic.getCollectionFromMongoDB(m, "Follow");
		
		DBObject query = new BasicDBObject();
		query.put("author_login", username);
		query.put("followDestination", userDest);
		DBCursor curseur = followCollection.find(query);
		
		followCollection.remove(curseur.next());
	}

}
