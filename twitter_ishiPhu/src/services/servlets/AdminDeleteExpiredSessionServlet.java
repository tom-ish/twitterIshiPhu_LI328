package services.servlets;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONObject;

import services.ServicesAdmin;

public class AdminDeleteExpiredSessionServlet extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest requete, HttpServletResponse response) throws IOException{
		// Lecture des parametres
		String adminID = requete.getParameter("adminID");
		String userID = requete.getParameter("userID");
		
		// Traitement des donnees
		JSONObject json = ServicesAdmin.deleteExpiredSession(adminID, userID, response.getWriter());
		
		// Ecriture des parametres
		PrintWriter writer = response.getWriter();
		response.setContentType("text/plain");
		writer.print(json.toString());
	}
	
}
