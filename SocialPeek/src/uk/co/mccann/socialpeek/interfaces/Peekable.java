package uk.co.mccann.socialpeek.interfaces;

import java.util.Calendar;
import java.util.List;

import uk.co.mccann.socialpeek.exceptions.SocialPeekException;

public interface Peekable {

	public String getRandomPeek() throws SocialPeekException;
	
	public String getRandomPeek(int limit) throws SocialPeekException;
	
	public String getUserPeek(int userId)  throws SocialPeekException;
	
	public String getUserPeek(String userId) throws SocialPeekException;
	
	public String getUserPeek(int userId, int limit) throws SocialPeekException;
	
	public String getUserPeek(String userId, int limit)  throws SocialPeekException;
	
	public String getLatestUserPeek(String userId) throws SocialPeekException;
	
	public String getLatestUserPeek(int userId, int limit) throws SocialPeekException;
	
	public String getLatestUserPeek(String userId, int limit)  throws SocialPeekException;
	
	public Data getRawDataRandomPeek() throws SocialPeekException;
	
	public List<Data> getRawDataRandomPeek(int limit) throws SocialPeekException;
	
	public Data getRawDataUserPeek(int userId)  throws SocialPeekException;
	
	public Data getRawDataUserPeek(String userId) throws SocialPeekException;
	
	public List<Data> getRawDataUserPeek(int userId, int limit) throws SocialPeekException;
	
	public List<Data> getRawDataUserPeek(String userId, int limit)  throws SocialPeekException;

}
