package uk.co.mccann.socialpeek.service;

import java.util.List;

import uk.co.mccann.socialpeek.exceptions.SocialPeekException;
import uk.co.mccann.socialpeek.interfaces.Data;
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
	
	public String getMultiplePeekUsingTag(String tag, int limit) throws SocialPeekException {
		
		/* not implemented */
		throw new SocialPeekException("method not implemented for twitter service");
	}

	public String getMultiplePeekUsingTags(String[] tags, int limit) throws SocialPeekException {
		
		/* not implemented */
		throw new SocialPeekException("method not implemented for twitter service");
	}

	public String getRandomPeekUsingTag(String tag) throws SocialPeekException {
		
		/* not implemented */
		throw new SocialPeekException("method not implemented for twitter service");
		
	}

	public String getRandomPeekUsingTags(String[] tags) throws SocialPeekException {
		
		/* not implemented */
		throw new SocialPeekException("method not implemented for twitter service");
	}

	public List<Data> getRawMultiplePeekUsingTag(String tag, int limit) throws SocialPeekException {
		
		/* not implemented */
		throw new SocialPeekException("method not implemented for twitter service");
	}

	public List<Data> getRawMultiplePeekUsingTags(String[] tags, int limit) throws SocialPeekException {
		/* not implemented */
		throw new SocialPeekException("method not implemented for twitter service");
	}

	public Data getRawRandomPeekUsingTag(String tag) throws SocialPeekException {
		/* not implemented */
		throw new SocialPeekException("method not implemented for twitter service");
	}

	public Data getRawRandomPeekUsingTags(String[] tags) throws SocialPeekException {
		
		/* not implemented */
		throw new SocialPeekException("method not implemented for twitter service");
	}
}
