package services;

import org.json.JSONObject;

public class ServicesTools {
	
	// ok() <=> serviceAccepted()
	static JSONObject ok()
	{
		return new JSONObject();
	}
	
	// error(int code, String mesaqge) <=> serviceRefused(String message, int codeErreur)
	static JSONObject error(int code, String message) 
	{
		try
		{
			JSONObject json = new JSONObject();
			json.put(message,code);
			return json;
		}
		catch(Exception e)
		{
			return(null);
		}
	}

	static boolean isNullParameter(String parameter){
		if(parameter == null)	return true;
		return false;
	}


}


