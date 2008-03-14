package uk.co.mccann.socialpeek.parser;

import java.util.Random;

import org.w3c.dom.Document;

import uk.co.mccann.socialpeek.model.SocialService;

/**
* <b>AbstractParser</b><br/>
* All parsers for services need to extend this class
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
public abstract class AbstractParser {
	
	protected SocialService service;
	protected Random random;
	
	public AbstractParser() { }
	
	/**
     * Called by subclasses to set the SocialService for the parser
   	 * @param service
     * @see SocialService
     */
	public AbstractParser(SocialService service) {
	
		this.service = service;
	}
	
	/**
     * Set the SocialService for the parser
   	 * @param service
     * @see SocialService
     */
	public void setSocialService(SocialService service) {
		
		this.service = service;
	}
	
	/**
     * Get the SocialService for the parser
   	 * @return the SocialService for the parser
     * @see SocialService
   	 */
	protected SocialService getSocialService() {
		
		return this.service;
	}
	

}
