package services.servlets;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONObject;

import services.ServicesFriend;

public class ConnectedFriendsServlet extends HttpServlet {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * @throws IOException 
	 * 
	 */
	
	public void doGet(HttpServletRequest requete, HttpServletResponse response) throws IOException{
		// Lecture des parametres
		String session_key = requete.getParameter("session_key");
		
		// Traitement des donnees
		JSONObject json = ServicesFriend.getConnectedFriends(session_key, response.getWriter());

		// Ecriture des donnees
		PrintWriter writer = response.getWriter();
		response.setContentType("text/plain");
		writer.print(json.toString());
	}

}
