package uk.co.mccann.socialpeek.test;


import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Set;

import org.apache.commons.httpclient.HttpURL;
import org.json.JSONArray;
import org.junit.Test;
import org.w3c.dom.Element;

import uk.co.mccann.socialpeek.SocialPeek;
import uk.co.mccann.socialpeek.SocialPeekConfiguration;
import uk.co.mccann.socialpeek.exceptions.SocialPeekException;
import uk.co.mccann.socialpeek.generator.RSSGenerator;
import uk.co.mccann.socialpeek.interfaces.Data;
import uk.co.mccann.socialpeek.interfaces.Generator;
import uk.co.mccann.socialpeek.interfaces.PeekFactory;
import uk.co.mccann.socialpeek.model.PeekData;
import uk.co.mccann.socialpeek.model.SocialService;
import uk.co.mccann.socialpeek.service.DeliciousService;
import uk.co.mccann.socialpeek.service.TwitterService;
import yarfraw.core.datamodel.CategorySubject;
import yarfraw.core.datamodel.ChannelFeed;
import yarfraw.core.datamodel.FeedFormat;
import yarfraw.core.datamodel.ItemEntry;
import yarfraw.io.FeedReader;
import org.junit.Test;
import static org.junit.Assert.fail;

public class DeliciousTest {
	/*
	@Test public void rssFeedTest() {
	
		try {
	
		FeedReader r = new FeedReader(new HttpURL("http://del.icio.us/rss/tag/fear"));
		r.setFormat(FeedFormat.RSS10);
		ChannelFeed c = r.readChannel();   
		
		Data data = new PeekData();
		
		
		
		//System.out.println(c);
		int counter = 0;
		for(ItemEntry entry : c.getItems()){
			if(counter < 1 ) {
			//System.out.println("Title: " + entry.getTitleText());
			//System.out.println("Link: " +entry.getLinks().get(0).getHref());
			//System.out.println("Author: " +entry.getAuthorOrCreator().get(0).getEmailOrText());
			//System.out.println("Description: " +entry.getDescriptionOrSummaryText());
			
			data.setBody(entry.getDescriptionOrSummaryText());
			
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-mm-dd'T'kk:mm:ss'Z'");
			Calendar cal = Calendar.getInstance();
			cal.setTime(sdf.parse(entry.getPubDate()));
			
			data.setDate(cal);
			data.setHeadline(entry.getTitleText());
			data.setLink(entry.getLinks().get(0).getHref());
			data.setUser(entry.getAuthorOrCreator().get(0).getEmailOrText());
			//data.setUserProfilePhoto("http://www.yahoo.com/");
			
			
			//Set<CategorySubject> subjects = entry.getCategorySubjects();
			
			//for(CategorySubject subject : subjects) {
			//	System.out.println("Tags: " +subject.getCategoryOrSubjectOrTerm());
			//}
			//System.out.println("Published: " + entry.getPubDate());
			//System.out.println("-----------------------------------------------------------");
			
			
			//Element thumnail = entry.getElementByNS("http://purl.org/rss/1.0/", "creator");
			 //	System.out.println(thumnail);
		       // System.out.println(thumnail.getAttribute("width"));
		        //System.out.println(thumnail.getAttribute("height")); 
		       
			//System.out.println(entry.getAttributeValueByLocalName("creator"));
			} else {
				break;
			}
			counter++;
			
		}
		
		Generator generator = new RSSGenerator();	
		System.out.println(generator.generate(data));
	
		} catch (Exception exp) {
		exp.printStackTrace();
		fail("failed"  + exp.getMessage());
		
		}
	
	}
	*/
	
	@Test public void randomPeek() {
		
		SocialService twitterService = new TwitterService();
		twitterService.setUsername("shanmantest");
		twitterService.setPassword("fofcowb");
		
		SocialService deliciousService = new DeliciousService();
		deliciousService.setUsername("test");
		deliciousService.setPassword("test");
		
		SocialPeekConfiguration config = new SocialPeekConfiguration();
		config.setFeedType(SocialPeek.RETURN_XML);
		
		config.registerService(twitterService);
		config.registerService(deliciousService);
		
		/* set up our main engine */
		SocialPeek socialPeek = new SocialPeek(config);
		PeekFactory peekFactory = socialPeek.getPeekingFactory();
		
		/* start peeking! */
		
		try {
		
			System.out.println(peekFactory.getPeeker(DeliciousService.class).getRandomPeek());
		
		} catch (SocialPeekException e) {
			e.printStackTrace();
			fail(e.getMessage());
		}
		
	}
	
	@Test public void userPeek() {
		
		SocialService twitterService = new TwitterService();
		twitterService.setUsername("shanmantest");
		twitterService.setPassword("fofcowb");
		
		SocialService deliciousService = new DeliciousService();
		deliciousService.setUsername("test");
		deliciousService.setPassword("test");
		
		SocialPeekConfiguration config = new SocialPeekConfiguration();
		config.setFeedType(SocialPeek.RETURN_XML);
		
		config.registerService(twitterService);
		config.registerService(deliciousService);
		
		/* set up our main engine */
		SocialPeek socialPeek = new SocialPeek(config);
		PeekFactory peekFactory = socialPeek.getPeekingFactory();
		
		/* start peeking! */
		
		try {
		
			System.out.println(peekFactory.getPeeker(DeliciousService.class).getUserPeek("theshanman"));
		
		} catch (SocialPeekException e) {
			e.printStackTrace();
			fail(e.getMessage());
		}
		
	}
	
	
}