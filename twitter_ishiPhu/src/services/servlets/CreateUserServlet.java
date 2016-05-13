package services.servlets;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.*;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;

import org.json.JSONObject;


import services.ServicesAuthentification;
import services.ServicesProfile;

@MultipartConfig(maxFileSize = 16177215)

public class CreateUserServlet extends HttpServlet {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	protected void doPost(HttpServletRequest requete, HttpServletResponse response) throws ServletException, IOException{
		// Lecture des parametres
		String username = requete.getParameter("username");
		String password = requete.getParameter("password");
		String password2 = requete.getParameter("password2");
		String email = requete.getParameter("email");
		String firstName = requete.getParameter("firstName");
		String lastName = requete.getParameter("lastName");
		String birthDate = requete.getParameter("birthDate");
		Part profilePicture = null;
			
		JSONObject json = new JSONObject();
		JSONObject json2 = new JSONObject();
		
		try {
			// Traitement des donnees
			profilePicture = requete.getPart("profilePicture");
			json = ServicesAuthentification.createNewUser(username, password, password2, email, firstName, lastName, birthDate, response.getWriter());
			json2 = ServicesProfile.createUserProfile(username, password, password2, firstName, lastName, birthDate, email, profilePicture, response.getWriter());
		} catch (IOException e) {
			response.getWriter().print(e.getMessage());
			e.printStackTrace();
		}
	
		// Ecriture des donnees
		PrintWriter writer = response.getWriter();
		response.setContentType("text/plain");
		writer.println(json.toString());
		writer.println(json2.toString());
	}

}