package uk.co.mccann.socialpeek.interfaces;

import java.util.Calendar;

/**
 * <b>Data</b><br/>
 * Interface for PeekData objects.
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
public interface Data {
	
	/**
     *  Set the headline
     */
	public void setHeadline(String headline);
	
	/**
     *  Set the body or description information
     */
	public void setBody(String body);
	
	/**
     *  Set the date the item was created or posted
     */
	public void setDate(Calendar date);
	
	/**
     *  Set the http URL of the source of the information, or the link it refers to.
     */
	public void setLink(String link);
	
	/**
     *  Set the name or username of the user who originally submitted the information
     */
	public void setUser(String user);
	
	/**
     *  Set the http URL of a photo of the user who posted the link.
     */
	public void setUserProfilePhoto(String photo);
	
	/**
     *  Get the headline
     */
	public String getHeadline();
	
	/**
     *  Get the body or description information
     */
	public String getBody();
	
	/**
     *  Get the http URL of the source of the information, or the link it refers to.
     */
	public Calendar getDate();
	
	/**
     *  Get the name or username of the user who originally submitted the information
     */
	public String getLink();
	
	/**
     *  Get the name or username of the user who originally submitted the information
     */
	public String getUser();
	
	/**
     * Get the http URL of a photo of the user who posted the link.
     */
	public String getUserProfilePhoto();
	

}
