package services.servlets;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONObject;

import services.ServicesTweet;

public class TweetServlet extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	protected void doGet(HttpServletRequest requete, HttpServletResponse response) throws IOException{
		// Lecture des parametres
		String key = requete.getParameter("session_key");
		String tweet = requete.getParameter("tweet");
		
		// Traitement des donnees
		JSONObject json = ServicesTweet.postTweet(key, tweet, response.getWriter());
		
		// Ecriture des donnees
		PrintWriter writer = response.getWriter();
		response.setContentType("text/plain");
	    writer.print(json.toString());
	}

}
