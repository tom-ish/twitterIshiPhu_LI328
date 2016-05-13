package services.test;

import bd.DBMail;
import services.mail.MailTools;

public class TestMail {

	public static void main(String[] args){
		/*
		 * Testing mailBox: 
				String from = "ishiPhu@outlook.fr";
				String password = "twitterPhu";
		 */
		String idFriend = "ishiphu";
		String mail = DBMail.getEmailFromID(idFriend, null);
		String message = idFriend + " added you on his/her friendList!";
		System.out.println(mail);
		MailTools.sendAdminMail(mail, "Twitter IshiPhu notifications", message, null);

		//services.mail.MailTools.sendAdminMail("twist0@live.fr","Mailing Test2","This is a testing mail", null);
	}
	
}
