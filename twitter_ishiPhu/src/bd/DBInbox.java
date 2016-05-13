package bd;

import java.io.PrintWriter;
import java.net.UnknownHostException;

import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.Mongo;
import com.mongodb.MongoException;

public class DBInbox {
	
	public static void postInbox(String key, String username, String dest, String note, PrintWriter printWriter) throws UnknownHostException, MongoException {
		Mongo m = DBMongoStatic.getMongoConnection(printWriter);
		DBCollection collection = DBMongoStatic.getCollectionFromMongoDB(m, "Comments");
		BasicDBObject object = new BasicDBObject();
		object.put("author_login", username);
		object.put("user_sessionKey", key);
		object.put("dest", dest);
		BasicDBObject message = new BasicDBObject();
		message.put("text", note);
		object.put("message", message);
		collection.insert(object);
	}
	
}
