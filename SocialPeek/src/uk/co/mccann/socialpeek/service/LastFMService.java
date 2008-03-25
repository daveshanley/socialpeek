package uk.co.mccann.socialpeek.service;

import uk.co.mccann.socialpeek.interfaces.Parser;
import uk.co.mccann.socialpeek.parser.LastFMParser;

public class LastFMService extends AbstractSocialService {

	public static String API_URL = "http://ws.audioscrobbler.com/1.0/";	
	public static String UK_LOCATION = "United+Kingdom";
	public static String US_LOCATION = "United+States";
	
	public static String CURRENT_CHART = API_URL + "place/" + UK_LOCATION + "/toptracks.xml";
	public static String ARTIST_API = API_URL + "artist/";
	public static String USER_API = API_URL + "user/";
	
	public LastFMService() {
		
		this(new LastFMParser());
		this.parser.setSocialService(this);
		this.parser.setUpParser();
		
		/* this service does not require authentication */
		this.setRequireAuthentication(false);
	}

	public LastFMService(Parser parser) {
		super(parser);
	}
	
}
