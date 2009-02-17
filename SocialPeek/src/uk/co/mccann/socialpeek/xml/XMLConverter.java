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

	public void setDate(Data data, String date) throws ParseException{

		Calendar cal =  Calendar.getInstance();
		
		if (dateFormat!=null)
			cal.setTime(dateFormat.parse(date));

		data.setDate(cal);
	}

	public static void main(String[] args){

		XMLConverter xmlc = new XMLConverter();
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
