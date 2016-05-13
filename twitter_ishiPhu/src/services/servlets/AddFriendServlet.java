package services.servlets;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONObject;

import services.ServicesFriend;

public class AddFriendServlet extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public void doGet(HttpServletRequest requete, HttpServletResponse response) throws IOException{
		// Lecture des parametres
		String key = requete.getParameter("session_key");
		String friendID = requete.getParameter("friendID");
		
		// Traitement des donnees
		JSONObject json = ServicesFriend.AddFriend(key, friendID, response.getWriter());

		// Ecriture des donnees
		PrintWriter writer = response.getWriter();
		response.setContentType("text/plain");
		writer.print(json.toString());
	}

}
