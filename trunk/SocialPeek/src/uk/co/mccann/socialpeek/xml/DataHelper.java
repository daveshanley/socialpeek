package uk.co.mccann.socialpeek.xml;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;

import uk.co.mccann.socialpeek.interfaces.Data;
import uk.co.mccann.socialpeek.model.PeekData;

// Helper Class to allow conversion from Strings to Data Formats

public class DataHelper {

	private SimpleDateFormat dateFormat;

	public DataHelper(){
		dateFormat = null;
	}

	public void setHeadline(Data data, String headline) throws ParseException{

		if (headline == null || data == null)
			return;

		data.setHeadline(headline);
	}
	
	public void setBody(Data data, String body) throws ParseException{

		if (body == null || data == null)
			return;
		
	
		data.setBody(body);
	}
	
	public void setLink(Data data, String link) throws ParseException{

		if (link == null || data == null)
			return;
	

		data.setLink(link);
	}
	
	public void setUser(Data data, String user) throws ParseException{

		if (user == null || data == null)
			return;
	
		data.setUser(user);
	}
	
	
	public void setDate(Data data, String date) throws ParseException{

		if (date == null || data == null)
			return;
		
		Calendar cal =  Calendar.getInstance();
		
		if (dateFormat!=null)
			cal.setTime(dateFormat.parse(date));

		data.setDate(cal);
	}

	public static void main(String[] args){

		DataHelper xmlc = new DataHelper();
		xmlc.setDateFormat("EEE, d MMM yyyy H:mm:ss z");
		try {
			xmlc.setDate(new PeekData(), "MON, 11 Feb 2009 20:46:02 GMT");
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public void setDateFormat(String dateFormat){
		this.dateFormat =  new SimpleDateFormat(dateFormat);
	}

}
