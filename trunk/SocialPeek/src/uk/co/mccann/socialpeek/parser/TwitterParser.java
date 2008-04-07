package uk.co.mccann.socialpeek.parser;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;

import java.util.Random;

import thinktank.twitter.Twitter;
import thinktank.twitter.TwitterException;
import thinktank.twitter.Twitter.Status;
import thinktank.twitter.Twitter.User;
import uk.co.mccann.socialpeek.exceptions.ParseException;
import uk.co.mccann.socialpeek.interfaces.Data;
import uk.co.mccann.socialpeek.interfaces.Parser;
import uk.co.mccann.socialpeek.model.PeekData;
import uk.co.mccann.socialpeek.model.SocialService;
import uk.co.mccann.socialpeek.service.TwitterService;


/**
 * <b>TwitterParser</b><br/>
 * Uses ThinkTank's jTwitter library to collect and process twitter information
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
public class TwitterParser extends AbstractParser implements Parser {
	
	Twitter twitter;
	
	/**
     *  Default constructor, calls super constructor from AbstractParser
     *  @param service set's the social service for the parser
     */
	public TwitterParser(SocialService service) {
		super(service);
	}
	
	public TwitterParser() { }
	
	/**
     *  Configures the parser for any properties that need initializing
     *  
     */
	public void setUpParser() {
		
		/* create a new twitter engine, using thinktank's jTwitter lib */
		this.twitter = new Twitter(this.getSocialService().getUsername(),this.getSocialService().getPassword());
		
	}
	
	private Data compileTwitterData(Status status) {
	
		
		Data twitterUser = new PeekData();
		twitterUser.setUser(status.getUser().getName());
		twitterUser.setLink(TwitterService.TWITTER_URL + status.getUser().getScreenName() + "/statuses/" + status.getId());
			
		/* set up calendar */
		Calendar cal = Calendar.getInstance();
		cal.setTime(status.getCreatedAt());
		twitterUser.setDate(cal);
			
		try {
		       
			byte[] utf8Bytes = status.getText().getBytes("UTF8");
		        
			/* convert to UTF-8*/
		    String newBody = new String(utf8Bytes, "UTF-8");
		        
		    twitterUser.setBody("said '<strong>" + status.getUser().getScreenName() + "</strong>'");
		    twitterUser.setHeadline(newBody);
		    
		    /* check to see if there is a photo attached to the user */
			if(status.getUser().getProfileImageUrl()!=null) {
				twitterUser.setUserProfilePhoto(status.getUser().getProfileImageUrl().toString());
			}
			if(status.getUser().getLocation()!=null) {
				twitterUser.setLocation(status.getUser().getLocation());
			}
				
			return twitterUser;
		    	
		} catch(UnsupportedEncodingException e) {
		    	
			return null;
		    	 
		}
	
	}
	
	public List<Data> getMultipleItems(int limit) throws ParseException {
		
		this.random = new Random();
		
		/* implementation code! */
		try {
			
			List<Data> extractedData = new ArrayList<Data>();
			
			/* get featured users */
			List<User> featuredUsers = this.twitter.getFeatured(); 
			
			/* iterate through all the users and add their posts to the extracted data (we will shuffle it all up later) */
			Collections.shuffle(featuredUsers);
			
			for(User user : featuredUsers) {
				
				Status status = user.getStatus();
				
				extractedData.add(this.compileTwitterData(status));
				
			}
			
			/* grab the public timeline as well so we have a truely random view of what is going on. */
			List<Status> publicUsers = this.twitter.getPublicTimeline();
			
			/* iterate through all the users and add their posts to the extracted data (we will shuffle it all up later) */
			Collections.shuffle(publicUsers);
			
			for(Status status : publicUsers) {
				
				extractedData.add(this.compileTwitterData(status));
				
			}
			
			/* shuffle it up for some randomness */
			Collections.shuffle(extractedData);
			
			List<Data> compactedData = new ArrayList<Data>();
			
			/* now trim it up */
			if(limit > extractedData.size()) limit = extractedData.size(); // make sure we don't go out of bounds!
			for(int x = 0; x < limit; x++) {
				compactedData.add(extractedData.get(x));
			}
			return compactedData;
		
		} catch (TwitterException e) {
			
			throw new ParseException("twitter parsing failed : " + e.getMessage());
		}
	}

	public Data getSingleItem() throws ParseException {
		
		List<Data> extractedData = this.getMultipleItems(20);
		this.random = new Random();
		return extractedData.get(this.random.nextInt(extractedData.size()-1));
	}

	public Data getKeywordItem(String keyword) throws ParseException {
		
		List<Data> extractedData = this.getMultipleItems(50);
		this.random = new Random();
		
		/* look through extracted data and search for a particular keyword */
		List<Data> matchingData = new ArrayList<Data>();
		
		for(Data data : extractedData) {
			boolean add = false;
			if(data.getHeadline().contains(keyword)) add = true;
			if(data.getBody().contains(keyword)) add = true;
			if(data.getUser().contains(keyword)) add = true;
			if(add) {
				if(!matchingData.contains(data)) matchingData.add(data);
			}
		}
		
		if(matchingData.size() > 0) {
			int randomNum = this.random.nextInt(matchingData.size()-1);
			if(randomNum >= 0 ) {
				return matchingData.get(randomNum);
			} else {
				return matchingData.get(++randomNum);
			}
		} else {
			
			throw new ParseException("keyword was not found in peek");
		}
	}

	public List<Data> getMultipleKeywordItems(String keyword, int limit) throws ParseException {
		/* not supported in this parser */
		return null;	
	}

	public List<Data> getMultipleKeywordItems(String[] keywords, int limit) throws ParseException {
		List<Data> extractedData = this.getMultipleItems(limit);
		this.random = new Random();
		
		/* look through extracted data and search for a particular keyword */
		List<Data> matchingData = new ArrayList<Data>();
		
		for(Data data : extractedData) {
			
			for(String keyword : keywords) {
				boolean add = false;
				if(data.getHeadline().contains(keyword)) add = true;
				if(data.getBody().contains(keyword)) add = true;
				if(data.getUser().contains(keyword)) add = true;
				if(add) {
					if(!matchingData.contains(data)) matchingData.add(data);
				}
			}
		}
		
		if(matchingData.size() > 0) {
		
			return matchingData;
		
		} else {
			
			throw new ParseException("keyword was not found in peek");
		}
	}

	public List<Data> getMultipleUserItems(int userId, int limit) throws ParseException {
		
		/* use string based overloaded method */
		return this.getMultipleUserItems(String.valueOf(userId), limit);
		
	}

	public List<Data> getMultipleUserItems(String userId, int limit) throws ParseException {
		
		
		/* implementation code! */
		try {
			
			List<Data> extractedData = new ArrayList<Data>();
			List<Status> list = this.twitter.getUserTimeline(userId, limit, null);
			
			/* counter */
			int counter = 0;
			
			for(Status status : list) {
				if(counter < limit) {
					
					extractedData.add(this.compileTwitterData(status));
				
				}
			}
			
			return extractedData;
		
		} catch (TwitterException e) {
			
			/* check for a user not found message */
			if(e.getMessage().contains("404")) {
				throw new ParseException("twitter parsing failed : username '" + userId + "' does not exist!");
			} else {		
				throw new ParseException("twitter parsing failed : " + e.getMessage());
			}
		}
		
	}

	public Data getSingleUserItem(int userId) throws ParseException {
		List<Data> extractedData = this.getMultipleUserItems(String.valueOf(userId), 20);
		this.random = new Random();
		return extractedData.get(this.random.nextInt(extractedData.size()-1));
	}

	public Data getSingleUserItem(String userId) throws ParseException {
		List<Data> extractedData = this.getMultipleUserItems(userId, 20);
		this.random = new Random();
		return extractedData.get(this.random.nextInt(extractedData.size()-1));
	}

	public List<Data> getLatestMultipleUserItems(int userId, int limit) throws ParseException {
		return this.getMultipleUserItems(String.valueOf(userId), limit);
	}

	public List<Data> getLatestMultipleUserItems(String userId, int limit) throws ParseException {
		return this.getMultipleUserItems(String.valueOf(userId), limit);
	}

	public Data getLatestSingleUserItem(int userId) throws ParseException {
		
		String user = String.valueOf(userId);	
		return this.getSingleUserItem(user);
		
	}

	public Data getLatestSingleUserItem(String userId) throws ParseException {
		/* implementation code! */
		
		try {
			
			Status userStatus = this.twitter.getStatus(userId);
			Data userItem = this.compileTwitterData(userStatus);
			
			return userItem;
			
		} catch (TwitterException e) {
			
			throw new ParseException("twitter parsing failed : " + e.getMessage());
		}
	}

	public Data getKeywordItem(String[] keywords) throws ParseException {
		
		List<Data> extractedData = this.getMultipleItems(50);
		this.random = new Random();
		
		/* look through extracted data and search for a particular keyword */
		List<Data> matchingData = new ArrayList<Data>();
		
		for(Data data : extractedData) {
			
			for(String keyword : keywords) {
				boolean add = false;
				if(data.getHeadline().contains(keyword)) add = true;
				if(data.getBody().contains(keyword)) add = true;
				if(data.getUser().contains(keyword)) add = true;
				if(add) {
					if(!matchingData.contains(data)) matchingData.add(data);
				}
			}
		}
		
		if(matchingData.size() > 0) {
			return matchingData.get(this.random.nextInt(matchingData.size()-1));
		
		} else {
			
			throw new ParseException("keyword was not found in peek");
		}
		
	}

	
}
