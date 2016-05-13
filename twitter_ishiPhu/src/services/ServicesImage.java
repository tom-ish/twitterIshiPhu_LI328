package services;

import java.awt.FlowLayout;
import java.io.PrintWriter;
import java.net.MalformedURLException;
import java.net.URL;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;

import org.json.JSONException;
import org.json.JSONObject;

import bd.DBAuthentification;
import bd.DBSessionKey;
import bd.image.BytesTools;
import bd.image.ImagesTools;

public class ServicesImage {

	public static JSONObject postImageFromURL(String key, String lien, PrintWriter printWriter){
		if(ServicesTools.isNullParameter(key) || ServicesTools.isNullParameter(lien))
			return ServicesTools.error(1, "One of the parameter is null!");
		
		if(DBSessionKey.sessionKeyExpire(key,printWriter))
			return ServicesTools.error(21, "The sessionKey is expired");
		
		String username = DBSessionKey.getUsernameByKey(key);
		if(!DBAuthentification.existeLogin(username, printWriter))
			return ServicesTools.error(11, "The userName specified doesn't exist in the database!");
		
		JSONObject json = null;
		URL url;
		try {
			url = new URL(lien);	
			json = new JSONObject();
			byte[] data = ImagesTools.saveImageFromURLToDB(username, url, printWriter);
			JSONObject author = new JSONObject();
			
			
			try {
				author.put("login", username);
				author.put("session_key", key);
				
				json.put("author", author);
				json.put("url", lien);
				json.put("data", BytesTools.getStringFromBytes(data));
				json.put("postImage","success!");
			} catch (JSONException e) {
				printWriter.print(e.getMessage());
				e.printStackTrace();
			}
		} catch (MalformedURLException e) {
			printWriter.print(e.getMessage());
			e.printStackTrace();
		}
		return json;
	}
	
	public static JSONObject displayImageFromURL(String key, URL url, PrintWriter printWriter){
		if(ServicesTools.isNullParameter(key) || ServicesTools.isNullParameter(url.toString()))
			return ServicesTools.error(1, "One of the parameter is null!");
		
		if(DBSessionKey.sessionKeyExpire(key,printWriter))
			return ServicesTools.error(21, "The sessionKey is expired");
		
		String username = DBSessionKey.getUsernameByKey(key);
		if(!DBAuthentification.existeLogin(username, printWriter))
			return ServicesTools.error(11, "The userName specified doesn't exist in the database!");
		
		byte[] data = BytesTools.getByteFromURL(url, null);
		
		JFrame frame = new JFrame();
		frame.getContentPane().setLayout(new FlowLayout());
		frame.getContentPane().add(new JLabel(new ImageIcon(data)));
		frame.pack();
		frame.setVisible(true);
		
		JSONObject json = new JSONObject();
		try {
			json.put("displayImage", "success!");
			json.put("New JFrame", "success !");
		} catch (JSONException e) {
			printWriter.print(e.getMessage());
			e.printStackTrace();
		}
		return json;
	}
	
}
