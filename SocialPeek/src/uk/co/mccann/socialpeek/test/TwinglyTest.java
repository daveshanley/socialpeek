package uk.co.mccann.socialpeek.test;

import java.util.Calendar;
import java.util.List;

import org.junit.Test;

import quicktime.std.movies.media.TweenMedia;

import uk.co.mccann.socialpeek.exceptions.ParseException;
import uk.co.mccann.socialpeek.interfaces.Data;
import uk.co.mccann.socialpeek.parser.TwinglyParser;
import uk.co.mccann.socialpeek.parser.WordPressParser;
import uk.co.mccann.socialpeek.parser.YahooSearchParser;

public class TwinglyTest {

	@Test public void search() {
		
		TwinglyParser bp = new TwinglyParser();
		
		String[] john = {"lewis","super"};
		
		try {
			List<Data> d =   bp.getMultipleKeywordItems(john,6);
			
			for(Data t : d){
				
				System.out.println("Headline: " + t.getHeadline());
				System.out.println("Description: " + t.getBody());
				System.out.println("Link: " + t.getLink());
				System.out.println("User: " + t.getUser());
				System.out.println("Date: " + t.getDate().get(Calendar.DATE));
				System.out.println("Location: " + t.getLocation());
				System.out.println("Thumbnail URL: " + t.getThumbnail());
				System.out.println();
				System.out.println();
				
			}
				
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
}
