package uk.co.mccann.socialpeek.generator;

import uk.co.mccann.socialpeek.SocialPeek;
import uk.co.mccann.socialpeek.interfaces.Generator;
import uk.co.mccann.socialpeek.model.SocialService;

/**
 * GeneratorFactory
 * Instantiate correct data generator depending on feed type. Should not be instantiated directly, however again - so do if you want.
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

public class GeneratorFactory {
	
	private int feedType;
	
	/**
     * 
     *
     * @return List of services registered
     * @see SocialService
     */
	public GeneratorFactory(int feedType) {
		
		this.feedType = feedType;
	
	}
	
	public Generator getGenerator() {
		
		
		if(this.feedType == SocialPeek.RETURN_XML) {
			
			return new XMLGenerator();
			
		} else if(this.feedType == SocialPeek.RETURN_JSON) {
			
			return new JSONGenerator();
		
		} else if(this.feedType == SocialPeek.RETURN_RSS) {
			
			return new RSSGenerator();	
			
		} else {
			
			return null;
		}
		
	}
	
	
	
}
