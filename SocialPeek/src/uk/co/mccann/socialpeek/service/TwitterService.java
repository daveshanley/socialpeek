package uk.co.mccann.socialpeek.service;

import uk.co.mccann.socialpeek.interfaces.Parser;
import uk.co.mccann.socialpeek.parser.TwitterParser;

public class TwitterService extends AbstractSocialService {

	public static final String TWITTER_URL ="http://twitter.com/";
	
	public TwitterService() {
		
		this(new TwitterParser());
		this.parser.setSocialService(this);
		this.parser.setUpParser();
	
	}
	
	public TwitterService(Parser parser) {
		super(parser);
	}
	
	/* override anything that needs it */
}
