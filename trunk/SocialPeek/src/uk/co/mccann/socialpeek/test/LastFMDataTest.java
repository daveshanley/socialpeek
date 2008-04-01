package uk.co.mccann.socialpeek.test;
import static org.junit.Assert.fail;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;

import org.junit.Test;

import uk.co.mccann.socialpeek.SocialPeek;
import uk.co.mccann.socialpeek.SocialPeekConfiguration;
import uk.co.mccann.socialpeek.interfaces.PeekFactory;
import uk.co.mccann.socialpeek.model.SocialService;
import uk.co.mccann.socialpeek.parser.LastFMParser;
import uk.co.mccann.socialpeek.service.LastFMService;
import uk.co.mccann.socialpeek.service.TwitterService;

public class LastFMDataTest {
	
	@Test public void singleTest() {
		try {
			
			SocialService service = new LastFMService();
			
			SocialPeekConfiguration config = new SocialPeekConfiguration();
			config.setFeedType(SocialPeek.RETURN_RSS);
			
			config.registerService(service);
			
			SocialPeek socialPeek = new SocialPeek(config);
			PeekFactory peekFactory = socialPeek.getPeekingFactory();
		
			String data = peekFactory.getPeeker(LastFMService.class).getMultiplePeekUsingTags(new String[]{"proud","happy","confusing"},50);
			
			File file = new File("/usr/local/apache2/htdocs/socialpeek/feed.rss");
			file.delete();
		
			OutputStreamWriter osw = new OutputStreamWriter(new FileOutputStream("/usr/local/apache2/htdocs/socialpeek/feed.rss",true),"UTF-8");
			
			System.out.print(data);
			
			osw.write(data);
			osw.close();
			
			
		} catch (Exception exp) {
			exp.printStackTrace();
			fail("failed");
		}
	}

}
