package uk.co.mccann.socialpeek.test;

import java.util.List;

import uk.co.mccann.socialpeek.exceptions.ParseException;
import uk.co.mccann.socialpeek.interfaces.Data;
import uk.co.mccann.socialpeek.parser.BloglinesParser;
import uk.co.mccann.socialpeek.service.BloglinesService;

public class BloglinesTest {

	public static void main(String[] args){
		
		BloglinesParser blp = new BloglinesParser();
		
		try {
			List<Data> hello = blp.parseXML(BloglinesService.BLOGLINES_URL + "lewis");
			
			for (Data d : hello){
				
				System.out.println(d.getHeadline());
				System.out.println(d.getBody());
				System.out.println(d.getLink());
				System.out.println(d.getUser());
				System.out.println(d.getDate());
			}
				
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
}
