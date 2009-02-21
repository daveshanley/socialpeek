package uk.co.mccann.socialpeek.test;

import java.util.Calendar;

import org.junit.Test;

import uk.co.mccann.socialpeek.exceptions.ParseException;
import uk.co.mccann.socialpeek.interfaces.Data;
import uk.co.mccann.socialpeek.parser.FlickrParser;

public class FlickrTest {

	@Test public void search() {
		
		FlickrParser bp = new FlickrParser();
		
		String[] john = {"balls"};
		
		try {
			Data t =  bp.getKeywordItem(john);
			
	
			System.out.println("Headline: " + t.getHeadline());
			System.out.println("Description: " + t.getBody());
			System.out.println("Link: " + t.getLink());
			System.out.println("User: " + t.getUser());
			System.out.println("Date: " + t.getDate().get(Calendar.DATE));
			System.out.println("Location: " + t.getLocation());
			System.out.println("Thumbnail URL: " + t.getThumbnail());
			System.out.println();
			System.out.println();
			
				
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
}
