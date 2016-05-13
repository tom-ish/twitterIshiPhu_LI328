package services.test;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class TestDate {

	public static void main(String[] args) {
		//CreateUserServlet cUS = Create
		GregorianCalendar calendar = new java.util.GregorianCalendar();
		Date date_du_jour = new Date();
		String date = SimpleDateFormat.getDateTimeInstance(SimpleDateFormat.SHORT, SimpleDateFormat.SHORT).format(date_du_jour);
		System.out.println("date/Time = " + date);

		
		
		// Date d'expiration
		calendar.add(Calendar.HOUR, 1);
		Date date_expiration = calendar.getTime();
		
		// On recupere la date et le temps sous la forme: "JJ/MM/AA HH:MM:SS"
		System.out.println("date expiration : " + SimpleDateFormat.getDateTimeInstance(SimpleDateFormat.SHORT, SimpleDateFormat.MEDIUM).format(date_expiration));
		
	}

}
