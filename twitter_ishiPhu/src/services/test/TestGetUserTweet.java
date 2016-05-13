package services.test;


import org.json.JSONException;
import org.json.JSONObject;

import services.ServicesTweet;

public class TestGetUserTweet {

	public static void main(String[] args) throws JSONException {
		String key = "U9yYBQ1ebtee4O25htkBwJ2i511cVknO";
		JSONObject json = ServicesTweet.getUserTweet(key, null);
		
		System.out.println(json.toString(3));
	}

}
