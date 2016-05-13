package services.servlets;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.URL;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONObject;

import services.ServicesImage;

public class DisplayImageServlet extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public void doGet(HttpServletRequest requete, HttpServletResponse response) throws IOException{
		// Lecture des parametres
		String key = requete.getParameter("session_key");
		String url = requete.getParameter("url");
		
		// Traitement des donnees
		JSONObject json = ServicesImage.displayImageFromURL(key, new URL(url), response.getWriter());

		// Ecriture des donnees
		PrintWriter writer = response.getWriter();
		response.setContentType("text/plain");
		writer.print(json.toString());
	}
	
}
