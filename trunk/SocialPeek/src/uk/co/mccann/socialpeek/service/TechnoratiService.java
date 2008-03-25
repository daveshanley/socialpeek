package uk.co.mccann.socialpeek.service;

import java.util.List;

import uk.co.mccann.socialpeek.exceptions.SocialPeekException;
import uk.co.mccann.socialpeek.interfaces.Data;
import uk.co.mccann.socialpeek.interfaces.Parser;
import uk.co.mccann.socialpeek.parser.TechnoratiParser;

/**
 * <b>TechnoratiService</b><br/>
 * Generate data from Technorati
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
public class TechnoratiService extends AbstractSocialService {

public static String TECHNORATI_URL = "http://feeds.technorati.com/";
	
	public TechnoratiService() {
		
		this(new TechnoratiParser());
		this.parser.setSocialService(this);
		this.parser.setUpParser();
		
		/* this service does not require authentication */
		this.setRequireAuthentication(false);
	}

	public TechnoratiService(Parser parser) {
		super(parser);
	}
	
	/* overridden methods */
	public String getUserPeek(int userId) throws SocialPeekException {
		
		/* not implemented */
		throw new SocialPeekException("this method is not implemented technorati service!");
	
	}
	
	public String getUserPeek(String userId) throws SocialPeekException {
		
		/* not implemented */
		throw new SocialPeekException("this method is not implemented technorati service!");
		
	}

	public String getUserPeek(int userId, int limit) throws SocialPeekException {
		
		/* not implemented */
		throw new SocialPeekException("this method is not implemented technorati service!");
		
	}

	public String getUserPeek(String userId, int limit) throws SocialPeekException {
		
		/* not implemented */
		throw new SocialPeekException("this method is not implemented technorati service!");
		
	}
	
	public Data getRawDataUserPeek(int userId) throws SocialPeekException {
		
		/* not implemented */
		throw new SocialPeekException("this method is not implemented technorati service!");
		
	}

	public Data getRawDataUserPeek(String userId) throws SocialPeekException {
		
		/* not implemented */
		throw new SocialPeekException("this method is not implemented technorati service!");
	}

	public List<Data> getRawDataUserPeek(int userId, int limit) throws SocialPeekException {
		
		/* not implemented */
		throw new SocialPeekException("this method is not implemented technorati service!");
	}

	public List<Data> getRawDataUserPeek(String userId, int limit) throws SocialPeekException {
		
		/* not implemented */
		throw new SocialPeekException("this method is not implemented technorati service!");
	}

	public String getLatestUserPeek(String userId) throws SocialPeekException {
		
		/* not implemented */
		throw new SocialPeekException("this method is not implemented technorati service!");
		
	}

	public String getLatestUserPeek(int userId, int limit) throws SocialPeekException {
		/* not implemented */
		throw new SocialPeekException("this method is not implemented technorati service!");
		
	}

	public String getLatestUserPeek(String userId, int limit) throws SocialPeekException {
		
		/* not implemented */
		throw new SocialPeekException("this method is not implemented technorati service!");
	}
	
	
}
