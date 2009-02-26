package uk.co.mccann.socialpeek.xml;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import uk.co.mccann.socialpeek.interfaces.Data;


// Helper Class to allow conversion from Strings to Data Formats

public class XMLDataHelper {

	private SimpleDateFormat dateFormat;

	public XMLDataHelper(){
		dateFormat = null;
	}

	public void setHeadline(Data data, String headline) throws ParseException{

		if (headline == null || data == null)
			data.setHeadline("not available");
		else
			data.setHeadline(headline);
	}

	public void setBody(Data data, String body) throws ParseException{

		if (body == null || data == null)
			data.setHeadline("");
		else
			data.setBody(body);
	}

	public void setLink(Data data, String link) throws ParseException{

		if (link == null || data == null)
			data.setLink("http://socialpeek.com");
		else
			data.setLink(link);
	}

	public void setUser(Data data, String user) throws ParseException{

		if (user == null || data == null)
			data.setLink("not available");
		else
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

	public void setDateFormat(String dateFormat){
		this.dateFormat =  new SimpleDateFormat(dateFormat);
	}

	public void setDateFormat(SimpleDateFormat dateFormat){
		this.dateFormat = dateFormat;
	}

}
