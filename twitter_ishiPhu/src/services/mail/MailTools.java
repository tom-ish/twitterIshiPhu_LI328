package services.mail;

import java.io.PrintWriter;
import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.NoSuchProviderException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

/*
 * Testing mailBox: 
		String from = "ishiPhu@outlook.fr";
		String password = "twitterPhu";
 */


public class MailTools {
	
	private static Properties getProperties(String host){
		// Get system properties
		Properties proprietes = System.getProperties();

		// Setup mail server
		proprietes.setProperty("mail.smtp.host", host);
		proprietes.put("mail.smtp.port", "587");
		proprietes.put("mail.smtp.auth", "true");
		proprietes.put("mail.smtp.starttls.enable", "true");
		return proprietes;
	}
	
	private static Session getSession(Properties properties){
		// Get the default Session object.
		Session session = Session.getDefaultInstance(properties);
		session.setDebug(true);
		return session;
	}
	
	public static void sendAdminMail(String to, String subjectTitle, String message_txt, PrintWriter printWriter){
		MailTools.sendMail("ishiPhu@outlook.fr", "twitterPhu", to, subjectTitle, message_txt, printWriter);
	}

	public static void sendMail(String from, String mailPassword, String to, String subjectTitle, String message_txt, PrintWriter printWriter){
		// Assuming you are sending email from localhost
		String host = "smtp-mail.outlook.com";	
		Properties properties = getProperties(host);
		Session session = getSession(properties);
		/*
		String subject = "This is the Subject Line!";
		String txt = "This is actual message";
		*/
		try {
			MimeMessage message = new MimeMessage(session);								// Create a default MimeMessage object.
			message.setFrom(new InternetAddress(from));									// Set From: header field of the header.
			message.addRecipient(Message.RecipientType.TO, new InternetAddress(to));	// Set To: header field of the header.
			message.setSubject(subjectTitle /*subject*/);								// Set Subject: header field
			message.setText(message_txt /*txt*/);										// Now set the actual message

			// Send message
			Transport transport = session.getTransport("smtp");
			transport.connect(host, from, mailPassword);
			message.saveChanges();

			transport.sendMessage(message, message.getAllRecipients());
			// System.out.println("------------------ Sent message successfully.... ------------------");
			printWriter.print("------------------ Sent message successfully....");
			transport.close();
		} catch (NoSuchProviderException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		catch (MessagingException mex) {
			mex.printStackTrace();
		}
	}
	
}
