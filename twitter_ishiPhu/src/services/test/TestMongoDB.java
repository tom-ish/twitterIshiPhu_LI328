package services.test;

import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.Mongo;

import bd.DBMongoStatic;

public class TestMongoDB {
	
	public static void main(String[] args){
		String name = "Tweet";
		Mongo m = DBMongoStatic.getMongoConnection(null);
		DBCollection collection = DBMongoStatic.getCollectionFromMongoDB(m, name);
		DBCursor curseur = collection.find();
		while(curseur.hasNext()){
			System.out.println(curseur.next());
		}
	}	

}
