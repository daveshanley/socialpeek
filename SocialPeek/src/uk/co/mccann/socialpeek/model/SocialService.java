package uk.co.mccann.socialpeek.model;

import uk.co.mccann.socialpeek.interfaces.Peekable;
import uk.co.mccann.socialpeek.interfaces.Service;

/**
 * <b>SocialService</b><br/>
 * Interface to all implemented services (which in turn always subclass AbstractSocialService)
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
public interface SocialService extends Peekable, Service {
	
}
