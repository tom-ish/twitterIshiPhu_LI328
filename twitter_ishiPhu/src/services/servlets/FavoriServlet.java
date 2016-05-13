package services.servlets;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONObject;

import services.ServicesFavori;

public class FavoriServlet extends HttpServlet{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest requete, HttpServletResponse response) throws IOException{
		// Lecture des parametres
		String session_key = requete.getParameter("session_key");
		String tweet_id = requete.getParameter("tweet_id");
		
		// Traitement des donnees
		JSONObject json = ServicesFavori.addFavori(session_key, tweet_id, response.getWriter());
		
		// Ecriture des donnees
		PrintWriter writer = response.getWriter();
		response.setContentType("text/plain");
	    writer.print(json.toString());
	}

}
