package uk.co.mccann.socialpeek.interfaces;

import uk.co.mccann.socialpeek.exceptions.SocialPeekException;
import uk.co.mccann.socialpeek.model.SocialService;

/**
 * <b>PeekFactory</b><br/>
 * Services should implement this interface.
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
public interface PeekFactory {
	
	/**
     * get the peeker required to grab data, all 'peekers' are subclasses of AbstractSocialService
   	 * 
     * @throws SocialPeekException
     * @see uk.co.mccann.socialpeek.service.AbstractSocialService
     * @return service that you want to peek into
     */
	public SocialService getPeeker(Class service) throws SocialPeekException;
	
}
