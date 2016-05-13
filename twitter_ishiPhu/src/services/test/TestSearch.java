package services.test;

import org.json.JSONException;
import org.json.JSONObject;

import services.ServicesSearch;
import bd.DBMongoStatic;

import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.Mongo;

public class TestSearch {

	public static void main(String[] args) throws JSONException {
		String byFriends = "0";
		String query = "";
		String key = "null";
		JSONObject json = ServicesSearch.search(key, query, byFriends, null);
		System.out.println(json.toString(3));
	}

}
