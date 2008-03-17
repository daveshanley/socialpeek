package uk.co.mccann.socialpeek.service;

import uk.co.mccann.socialpeek.interfaces.Parser;
import uk.co.mccann.socialpeek.parser.TwitterParser;

/**
 * <b>TwitterService</b><br/>
 * Service class for Twitter Peeking
 *
 * <h4>Copyright and License</h4>
 * This code is copyright (c) McCann Erickson Advertising Ltd, 2008 except where
 * otherwise stated. It is released as
 * open-source under the LGPL license. See
 * <a href="http://www.gnu.org/licenses/lgpl.html">http://www.gnu.org/licenses/lgpl.html</a>
 * for license details. This code comes with no warranty or support.
 *
 * @author Dave Shanley <david.shanley@europe.mccann.com>
 */
public class TwitterService extends AbstractSocialService {

	public static final String TWITTER_URL ="http://twitter.com/";
	
	/**
     *  Set up service and configure parser.
     */
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
