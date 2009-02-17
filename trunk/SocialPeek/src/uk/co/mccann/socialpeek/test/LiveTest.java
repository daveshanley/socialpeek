package uk.co.mccann.socialpeek.test;

import java.util.Calendar;
import java.util.List;

import uk.co.mccann.socialpeek.exceptions.ParseException;
import uk.co.mccann.socialpeek.interfaces.Data;
import uk.co.mccann.socialpeek.parser.LiveParser;

public class LiveTest {

	public static void main(String[] args){
		
		LiveParser bp = new LiveParser();
		
		String[] john = {"john"};
		
		try {
			Data t = bp.getKeywordItem(john);
			
			
				
				System.out.println("Headline: " + t.getHeadline());
				System.out.println("Description: " + t.getBody());
				System.out.println("Link: " + t.getLink());
				System.out.println("User: " + t.getUser());
				System.out.println("Date: " + t.getDate().get(Calendar.DATE));
				System.out.println();
				System.out.println();
				
			
				
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
}
