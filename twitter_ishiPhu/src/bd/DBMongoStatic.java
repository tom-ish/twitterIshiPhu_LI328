package bd;

import java.io.PrintWriter;
import java.net.UnknownHostException;

import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.Mongo;
import com.mongodb.MongoException;

public class DBMongoStatic {
	
	public static Mongo getMongoConnection(PrintWriter printWriter) {
		Mongo m = null;
		try {
			m = new Mongo("132.227.201.129",27130);
			return m;
		} catch (UnknownHostException e) {
			printWriter.print(e.getMessage());
			e.printStackTrace();
		} catch (MongoException e){
			printWriter.print(e.getMessage());
			e.printStackTrace();
		}
		return m;
	}
	
	public static DBCollection getCollectionFromMongoDB(Mongo m, String name){
		DB db = m.getDB("gr1_phu_ishiwata");
		return db.getCollection(name);
	}

}
