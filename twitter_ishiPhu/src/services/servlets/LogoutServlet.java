package services.servlets;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONObject;

import services.ServicesAuthentification;

public class LogoutServlet extends HttpServlet {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest requete, HttpServletResponse response) throws IOException, ServletException
	{
		// Lecture des parametres
		String key = requete.getParameter("session_key");
		
		// Traitement des donnees
		JSONObject json = ServicesAuthentification.logout(key, response.getWriter());
		
		// Ecriture des donnees
		PrintWriter writer = response.getWriter();
		response.setContentType("text/plain");
	    writer.print(json.toString());
	}

}
