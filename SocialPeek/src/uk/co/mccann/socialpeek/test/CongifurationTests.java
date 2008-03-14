package uk.co.mccann.socialpeek.test;


import static org.junit.Assert.fail;

import org.junit.Before;
import org.junit.Test;

import uk.co.mccann.socialpeek.SocialPeek;
import uk.co.mccann.socialpeek.SocialPeekConfiguration;
import uk.co.mccann.socialpeek.SocialPeekFactory;
import uk.co.mccann.socialpeek.exceptions.SocialPeekException;
import uk.co.mccann.socialpeek.interfaces.PeekFactory;
import uk.co.mccann.socialpeek.model.SocialService;
import uk.co.mccann.socialpeek.service.AbstractSocialService;

public class CongifurationTests {

	@Before
	public void setUp() throws Exception {
	}

	@Test public void config() {
		
		SocialService service = new AbstractSocialService();
		service.setUsername("shanmantest");
		service.setPassword("fofcowb");
		
		SocialPeekConfiguration config = new SocialPeekConfiguration();
		config.setFeedType(SocialPeek.RETURN_XML);
		config.registerService(service);
		
		/* set up our main engine */
		SocialPeek socialPeek = new SocialPeek(config);
		PeekFactory peekFactory = socialPeek.getPeekingFactory();
		
	}
	
}
