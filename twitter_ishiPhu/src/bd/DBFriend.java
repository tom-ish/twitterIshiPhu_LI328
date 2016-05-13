package bd;

import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;

public class DBFriend {
	
	public static void addFriend(String username, String idFriend, String date, PrintWriter printWriter){
		String sql = "INSERT INTO FRIEND VALUE('" + username + "','" + idFriend + "','" + date + "');";
		
		try {
			Connection cxn = DBStatic.getMySQLConnection();
			Statement state = cxn.createStatement();

			// L'existence de l'idFriend a ete prealablement teste dans ServicesFriend.addFriend()
			state.executeUpdate(sql);
			
			cxn.close();
			state.close();
			cxn.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			printWriter.print(e.getMessage());
			e.printStackTrace();
		}
	}
	
	public static void deleteFriend(String username, String idFriend, PrintWriter printWriter){
		String sql = "DELETE FROM FRIEND WHERE userLogin = '" + username + "' AND friendLogin = '" + idFriend + "';";
		
		try {
			Connection cxn = DBStatic.getMySQLConnection();
			Statement state = cxn.createStatement();
			
			// L'existence de l'idFriend a ete prealablement teste dans ServicesFriend.addFriend()
			state.executeUpdate(sql);

			cxn.close();
			state.close();
			cxn.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			printWriter.print(e.getMessage());
			e.printStackTrace();
		}
	}
	
	public static ArrayList<String> getFriends(String user, PrintWriter printWriter){
		ArrayList<String> listeFriends = new ArrayList<String>();
		int cpt = 0;
		String sql = "SELECT * FROM FRIEND f WHERE f.userLogin = '" + user + "' OR f.friendLogin = '" + user + "';";
		try {
			Connection cxn = DBStatic.getMySQLConnection();
			Statement state = cxn.createStatement();
			
			ResultSet result = state.executeQuery(sql);
			
			while(result.next()){
				String friendID = null;
				if(result.getString(1).equalsIgnoreCase(user))
					friendID = result.getString(2);
				else if(result.getString(2).equalsIgnoreCase(user))
					friendID = result.getString(1);
				listeFriends.add(friendID);
				cpt++;
			}
			state.close();
			cxn.close();
			if(cpt != 0)
				return listeFriends;
			else return null;
		} catch (SQLException e) {
			printWriter.print(e.getMessage());
			e.printStackTrace();
		}
		return listeFriends;
	}

	public static ArrayList<String> getConnectedFriends(String user, PrintWriter printWriter) {
		ArrayList<String> listeConnected = new ArrayList<String>();
		
		GregorianCalendar calendar = new java.util.GregorianCalendar();
		Date date_du_jour = calendar.getTime();
		Date dateExpiration = null;
		
		ArrayList<String> listeFriends = getFriends(user, printWriter);
		if(!listeFriends.isEmpty()){
			for(int i = 0; i < listeFriends.size(); i++){
				dateExpiration = DBSessionKey.getDateExpiration(listeFriends.get(i), printWriter);
				if(date_du_jour.getTime() < dateExpiration.getTime())
					listeConnected.add(listeFriends.get(i));
			}
		}
		return listeConnected;
	}

}
