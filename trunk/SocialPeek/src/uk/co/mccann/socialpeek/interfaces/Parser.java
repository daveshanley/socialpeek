package uk.co.mccann.socialpeek.interfaces;

import java.util.List;

import uk.co.mccann.socialpeek.exceptions.ParseException;
import uk.co.mccann.socialpeek.model.SocialService;


/**
 * Configurable
 * Configuation classes implementation
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

public interface Parser {
	
	/**
     * Sets the social service for a parser
   	 * 
     * @param	service	the SocialService to be used for the parser
     * @see SocialService
     */
	public void setSocialService(SocialService service);
	
	/**
     * Sets the parser up
   	 */
	public void setUpParser();
	
	/**
     * Get a single PeekData Object
     * 
     * @return single Data object 
     * @throws ParseException
   	 */
	public Data getSingleItem() throws ParseException;
	
	/**
     * Get a multiple PeekData Objects
     * 
     * @return a List filled with Data objects 
     * @throws ParseException
     * @param limit integer limit of the number of results you want back.
   	 */
	public List<Data> getMultipleItems(int limit) throws ParseException;
	
	/**
     * Get a single PeekData object from a service user
     * 
     * This method may not be implemented by all services, should not be called if so.
     * 
     * @param userId integer value of the user's ID (if the service uses integer based id's)
     * @return Data object 
     * @throws ParseException
   	 */
	public Data getSingleUserItem(int userId) throws ParseException;
	
	/**
     * Get a single PeekData object from a service user
     * 
     * This method may not be implemented by all services, should not be called if so.
     * 
     * @param userId String value of the user's ID (if the service uses String based id's)
     * @return Data object 
     * @throws ParseException
   	 */
	public Data getSingleUserItem(String userId) throws ParseException;
	
	/**
     * Get latest single PeekData object from a service user
     * 
     * This method may not be implemented by all services, should not be called if so.
     * 
     * @param userId integer value of the user's ID (if the service uses integer based id's)
     * @return Data object 
     * @throws ParseException
   	 */
	public Data getLatestSingleUserItem(int userId) throws ParseException;
	
	/**
     * Get latest single PeekData object from a service user
     * 
     * This method may not be implemented by all services, should not be called if so.
     * 
     * @param userId String value of the user's ID (if the service uses String based id's)
     * @return Data object 
     * @throws ParseException
   	 */
	public Data getLatestSingleUserItem(String userId) throws ParseException;	
	
	/**
     * Get multiple PeekData objects from a service user
     * 
     * This method may not be implemented by all services, should not be called if so.
     * 
     * @param userId integer value of the user's ID (if the service uses integer based id's)
     * @param limit limit the returned results
     * @return Single Data Object
     * @throws ParseException
   	 */
	public List<Data> getMultipleUserItems(int userId, int limit) throws ParseException;
	
	/**
     * Get multiple PeekData objects from a service user
     * 
     * This method may not be implemented by all services, should not be called if so.
     * 
     * @param userId String value of the user's ID (if the service uses String based id's)
     * @param limit limit the returned results
     * @return List of Data objects 
     * @throws ParseException
   	 */
	public List<Data> getMultipleUserItems(String userId, int limit) throws ParseException;
	
	/**
     * Get latest multiple PeekData objects from a service user 
     * 
     * This method may not be implemented by all services, should not be called if so.
     * 
     * @param userId integer value of the user's ID (if the service uses integer based id's)
     * @param limit limit the returned results
     * @return List of Data objects 
     * @throws ParseException
   	 */
	public List<Data> getLatestMultipleUserItems(int userId, int limit) throws ParseException;
	
	/**
     * Get latest multiple PeekData objects from a service user
     * 
     * This method may not be implemented by all services, should not be called if so.
     * 
     * @param userId String value of the user's ID (if the service uses String based id's)
     * @param limit limit the returned results
     * @return List of Data objects 
     * @throws ParseException
   	 */
	public List<Data> getLatestMultipleUserItems(String userId, int limit) throws ParseException;
	
	/**
     * Get a single PeekData object from a service user depending on a tag or a keyword
     * 
     * This method may not be implemented by all services, should not be called if so.
     * 
     * @param keyword name of the tag you want to use.
     * @return Single Data Object
     * @throws ParseException
   	 */
	public Data getKeywordItem(String keyword) throws ParseException;
	
	/**
     * Get a single PeekData object from a service user depending multiple tags or keywords
     * 
     * This method may not be implemented by all services, should not be called if so.
     * 
     * @param keyword name of the tag you want to use.
     * @return Single Data Object
     * @throws ParseException
   	 */
	public Data getKeywordItem(String[] keywords) throws ParseException;
	
	/**
     * Get multiple PeekData object from a service filtered by a tag or a keyword
     * 
     * This method may not be implemented by all services, should not be called if so.
     * 
     * @param keyword name of the tag you want to use.
     * @param limit limits the number of results
     * @return List of Data objects 
     * @throws ParseException
   	 */
	public List<Data> getMultipleKeywordItems(String keyword, int limit) throws ParseException;
	
	/**
     * Get multiple PeekData object from a service filtered by multiple tags or keywords
     * 
     * This method may not be implemented by all services, should not be called if so.
     * 
     * @param keywords an array of the keywords you want to use.
     * @param limit limits the number of results
     * @return List of Data objects 
     * @throws ParseException
   	 */
	public List<Data> getMultipleKeywordItems(String[] keywords, int limit) throws ParseException;
		
}
