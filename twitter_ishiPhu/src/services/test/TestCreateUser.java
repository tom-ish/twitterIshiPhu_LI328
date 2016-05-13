package services.test;

import org.json.JSONException;
import org.json.JSONObject;

import services.ServicesAuthentification;
import services.ServicesProfile;
import services.ServicesSearch;

public class TestCreateUser {

	public static void main(String[] args) throws JSONException {
		String username = "titoto";
		String password = "tttttt";
		String password2 = "tttttt";
		String email = "twist0@live.fr";
		String firstName= "tito";
		String lastName = "tito";
		String birthDate = "123456";
		JSONObject json = ServicesAuthentification.createNewUser(username, password, password2, email, firstName, lastName, birthDate, null);
		JSONObject json2 = ServicesProfile.createUserProfile(username, password, password2, firstName, lastName, birthDate, email, null, null);
		System.out.println(json.toString(3));
		System.out.println(json2.toString(3));
	}

}
