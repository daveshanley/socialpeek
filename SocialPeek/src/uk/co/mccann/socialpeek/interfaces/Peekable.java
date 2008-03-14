package uk.co.mccann.socialpeek.interfaces;

import java.util.List;

import uk.co.mccann.socialpeek.exceptions.SocialPeekException;
import uk.co.mccann.socialpeek.model.SocialService;

/**
 * Peekable
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
public interface Peekable {
	
	/**
     * Get a single random peek
   	 * 
     * @throws SocialPeekException
     * @return formatted string data produced by generator
     */
	public String getRandomPeek() throws SocialPeekException;
	
	/**
     * Get multiple peeks
     * 
   	 * @param limit limit the number of items returned.
   	 * @return formatted string data produced by generator
     * @throws SocialPeekException
     */
	public String getRandomPeek(int limit) throws SocialPeekException;
	
	/**
     * Get a single random peek from a service user
   	 * 
   	 * @param userId the integer id of the service user (if implemented)
   	 * @return formatted string data produced by generator
     * @throws SocialPeekException
     */
	public String getUserPeek(int userId)  throws SocialPeekException;
	
	/**
     * Get a single random peek from a service user
   	 * 
   	 * @param userId the username/user account name of the service user (if implemented)
     * @throws SocialPeekException
     */
	public String getUserPeek(String userId) throws SocialPeekException;
	
	/**
     * Get multiple items from a service user
   	 * 
   	 * @param userId the user id of the service user (if implemented)
   	 * @param limit limit the number of items returned
   	 * @return formatted string data produced by generator
     * @throws SocialPeekException
     */
	public String getUserPeek(int userId, int limit) throws SocialPeekException;
	
	/**
     * Get multiple items from a service user
   	 * 
   	 * @param userId the user id of the service user (if implemented)
   	 * @param limit limit the number of items returned
   	 * @return formatted string data produced by generator
     * @throws SocialPeekException
     */
	public String getUserPeek(String userId, int limit)  throws SocialPeekException;
	
	/**
     * Get the latest single posted item from a service user
   	 * 
   	 * @param userId the user id of the service user (if implemented)
   	 * @return formatted string data produced by generator
     * @throws SocialPeekException
     */
	public String getLatestUserPeek(String userId) throws SocialPeekException;
	
	/**
     * Get multiple latest posted items from a service user
   	 * 
   	 * @param userId the user id of the service user (if implemented)
   	 * @return formatted string data produced by generator
   	 * @param limit limit the number of items returned
     * @throws SocialPeekException
     */
	public String getLatestUserPeek(int userId, int limit) throws SocialPeekException;
	
	/**
     * Get multiple latest posted items from a service user
   	 * 
   	 * @param userId the user id of the service user (if implemented)
   	 * @return formatted string data produced by generator
   	 * @param limit limit the number of items returned
     * @throws SocialPeekException
     */
	public String getLatestUserPeek(String userId, int limit)  throws SocialPeekException;
	
	/**
     * Get a single random peek without running the result through a generator
   	 * 
     * @throws SocialPeekException
     * @return POJO Data object for your own manipulation
     */
	public Data getRawDataRandomPeek() throws SocialPeekException;
	
	
	/**
     * Get multiple peeks without running the result through a generator
   	 * 
     * @throws SocialPeekException
     * @return List filled with POJO Data objects for your own manipulation
     */
	public List<Data> getRawDataRandomPeek(int limit) throws SocialPeekException;
	
	/**
     * Get single peek from a service user without running the result through a generator
   	 * 
     * @throws SocialPeekException
     * @param userId the user id of the service user (if implemented)
     * @return POJO Data object for your own manipulation
     */
	public Data getRawDataUserPeek(int userId)  throws SocialPeekException;
	
	/**
     * Get single peek from a service user without running the result through a generator
   	 * 
     * @throws SocialPeekException
     * @param userId the user id of the service user (if implemented)
     * @return POJO Data object for your own manipulation
     */
	public Data getRawDataUserPeek(String userId) throws SocialPeekException;
	
	/**
     * Get multiple peeks from a service user without running the result through a generator
   	 * 
     * @throws SocialPeekException
     * @param userId the user id of the service user (if implemented)
     * @param limit limit the number of items returned
     * @return List filled with POJO Data objects for your own manipulation
     */
	public List<Data> getRawDataUserPeek(int userId, int limit) throws SocialPeekException;
	
	/**
     * Get multiple peeks from a service user without running the result through a generator
   	 * 
     * @throws SocialPeekException
     * @param userId the user id of the service user (if implemented)
     * @param limit limit the number of items returned
     * @return List filled with POJO Data objects for your own manipulation
     */
	public List<Data> getRawDataUserPeek(String userId, int limit)  throws SocialPeekException;

}
