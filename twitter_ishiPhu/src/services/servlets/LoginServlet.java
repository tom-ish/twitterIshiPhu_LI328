package services.servlets;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONObject;

import services.ServicesAuthentification;

public class LoginServlet extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	protected void doPost(HttpServletRequest requete, HttpServletResponse response) throws IOException, ServletException {
		// Lecture des parametres
		String username = requete.getParameter("username");
		String password = requete.getParameter("password");
				
		// Traitement des donnees
		JSONObject json = ServicesAuthentification.login(username,password,response.getWriter());
		
		// Ecriture des donnees
		PrintWriter writer = response.getWriter();
		response.setContentType("text/plain");
	    writer.print(json.toString());
	}
	
	
	
}
