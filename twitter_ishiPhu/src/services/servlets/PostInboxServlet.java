package services.servlets;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONObject;

import services.ServicesInbox;

public class PostInboxServlet extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest requete, HttpServletResponse response) throws IOException, ServletException {
		// Lecture des parametres
		String session_key = requete.getParameter("session_key");
		String dest = requete.getParameter("dest");
		String inbox = requete.getParameter("inbox");
				
		// Traitement des donnees
		JSONObject json = ServicesInbox.postInbox(session_key,dest,inbox,response.getWriter());
		
		// Ecriture des donnees
		PrintWriter writer = response.getWriter();
		response.setContentType("text/plain");
	    writer.print(json.toString());
	}
	
	
	
}
