package uk.co.mccann.socialpeek.service;

import uk.co.mccann.socialpeek.interfaces.Parser;
import uk.co.mccann.socialpeek.parser.DeliciousParser;

public class DeliciousService extends AbstractSocialService {

public static final String DELICIOUS_URL ="http://del.icio.us/";
	
	/**
     *  Set up service and configure parser.
     */
	public DeliciousService() {
		
		this(new DeliciousParser());
		this.parser.setSocialService(this);
		this.parser.setUpParser();
		
		/* this service does not require authentication */
		this.setRequireAuthentication(false);
	
	}
	
	public DeliciousService(Parser parser) {
		super(parser);
	}
	
	/* override anything that needs it */
	
}
