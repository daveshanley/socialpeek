package uk.co.mccann.socialpeek.parser;

import java.util.Collections;
import java.util.List;
import java.util.Random;

import uk.co.mccann.socialpeek.exceptions.NoResultsException;
import uk.co.mccann.socialpeek.exceptions.ParseException;
import uk.co.mccann.socialpeek.interfaces.Data;
import uk.co.mccann.socialpeek.rss.RSSHelper;
import uk.co.mccann.socialpeek.rss.RSSReader;

import com.sun.cnpi.rss.elements.Channel;
import com.sun.cnpi.rss.elements.Item;

/**
* <b>TechnoratiParser</b><br/>
* Use technorati RSS feeds to generate data
*
* <h4>Copyright and License</h4>
* This code is copyright (c) McCann Erickson Advertising Ltd, 2008 except where
* otherwise stated. It is released as
* open-source under the Creative Commons NC-SA license. See
* <a href="http://creativecommons.org/licenses/by-nc-sa/2.5/">http://creativecommons.org/licenses/by-nc-sa/2.5/</a>
* for license details. This code comes with no warranty or support.
* 
* Query Notes:
* 
* Limit cannot be set
* Users don't exist
* 
* @author Dave Shanley <david.shanley@europe.mccann.com>
*/
public class TechnoratiParser extends AbstractParser {
	
	// RSS Caching variables
	private final String xmlKey = "delicious.rss.";
	private final long expireLengthMillis = 1800000; // 30  minutes
	
	// Query URLs
	// http://api.technorati.com/search?key=b888a12e033abe371165e7605ffd1642&query=hello+john&format=rss&limit=1&claim=1
	// b888a12e033abe371165e7605ffd1642
	
	private final String BASE_URL = "http://feeds.technorati.com/?language=en";
	private final String KEYWORD_URL = "http://feeds.technorati.com/search/{keyword}?language=en&count={limit}";
	private final String USER_URL = "http://feeds.delicious.com/v2/rss/{user}?language=en&count={limit}";	
	
	private final int DEFAULT_LIMIT = 10;

	private final String dateFormat = "EEE, d MMM yyyy H:mm:ss z";

	
	public void setUpParser(){
		this.random = new Random();
	}


	public Data getItem() throws ParseException, NoResultsException {

		return getItems(1).get(0);

	}


	public List<Data> getItems(int limit) throws ParseException, NoResultsException {

		int itemLimit = (limit>DEFAULT_LIMIT) ? limit : DEFAULT_LIMIT; 
		String query = BASE_URL.replace("{limit}", String.valueOf(itemLimit));

		List<Data> extractedData = this.getData(query);

		// return 'limit' items of shuffled data
		return extractData(extractedData, limit, true);
	}


	public Data getKeywordItem(String keyword) throws ParseException, NoResultsException {

		return getKeywordItems(keyword, 1).get(0);
	}


	public Data getKeywordItem(String[] keywords) throws ParseException, NoResultsException {

		// Construct query in form: term1+term2+term3
		String query = keywords[0];

		for (int i = 1; i < keywords.length; i++)
			query += "+" + keywords[i];

		return getKeywordItem(query);
	}


	public List<Data> getKeywordItems(String keyword, int limit) throws ParseException, NoResultsException {

		int itemLimit = (limit>DEFAULT_LIMIT) ? limit : DEFAULT_LIMIT; 

		String query = KEYWORD_URL.replace("{keyword}", keyword);
		query = query.replace("{limit}", String.valueOf(itemLimit));

		List<Data> extractedData = this.getData(query);

		// return 'limit' items of shuffled data
		return extractData(extractedData, limit, true);
	}


	public List<Data> getKeywordItems(String[] keywords, int limit) throws ParseException, NoResultsException {

		// Construct query in form: term1+term2+term3
		String query = keywords[0];

		for (int i = 1; i < keywords.length; i++)
			query += "+" + keywords[i];

		return getKeywordItems(query, limit);
	}


	public Data getUserItem(int userId) throws ParseException, NoResultsException {
	
		return getUserItem(String.valueOf(userId));
	}


	public Data getUserItem(String userId) throws ParseException, NoResultsException {
	
		return getUserItems(userId, 1).get(0);
	}


	public List<Data> getUserItems(int userId, int limit) throws ParseException, NoResultsException {
		return getUserItems(String.valueOf(userId), limit);
	
	}


	public List<Data> getUserItems(String userId, int limit) throws ParseException, NoResultsException {
	
		int itemLimit = (limit>DEFAULT_LIMIT) ? limit : DEFAULT_LIMIT; 
	
		String query = USER_URL.replace("{user}", userId);
		query = query.replace("{limit}", String.valueOf(itemLimit));
	
		List<Data> extractedData = this.getData(query);
	
		return extractData(extractedData, limit, true);
	}


	public Data getLatestUserItem(int userId) throws ParseException, NoResultsException {

		return getLatestUserItem(String.valueOf(userId));
	}


	public Data getLatestUserItem(String userId) throws ParseException, NoResultsException {

		return getLatestUserItems(userId, 1).get(0);
	}


	public List<Data> getLatestUserItems(int userId, int limit) throws ParseException, NoResultsException {

		return getLatestUserItems(String.valueOf(userId), limit);
	}


	public List<Data> getLatestUserItems(String userId, int limit) throws ParseException, NoResultsException {

		String query = USER_URL.replace("{user}", userId);
		query = query.replace("{limit}", String.valueOf(limit));

		List<Data> extractedData = this.getData(query);

		return extractData(extractedData, limit, false);
	}


	// Fetch Items from an RSS feed and return a list of Data objects
	// with an agreed limit (maybe added in future - limit parameter.
	private List<Data> getData(String query) throws ParseException, NoResultsException {
		
		// RSS Helper object to map RSS Items
		// to Data objects
		RSSHelper rssHelper = new RSSHelper();
	
		// Set up date format
		rssHelper.setDateFormat(dateFormat);
	
		RSSReader parser = new RSSReader();
	
		// Set the URL for the RSS reader to point to
		parser.setURL(query);
	
		// Parse the Feed and get the feed's channel
		Channel channel = null;
		try {
			channel = parser.parseFeed();
		} catch (Exception e) {
			throw new ParseException("Unable to parse Deilicious RSS data:" + e.getStackTrace());
		}
		
		
		/* get a list of RSS items and then shuffle them up for a random peek! */
		List<Item> items = (List<Item>) channel.getItems();
	
		if (items==null || items.size()==0)
			throw new NoResultsException();
	
		return rssHelper.convertToData(items);
	}

	
	/**
	 * 
	 * Receives a list of data and extracts the amount required
	 * If a random element is to be selected, shuffle is set to true
	 * 
	 * @param data
	 * @param limit
	 * @param shuffle
	 * @return
	 */
	private List<Data> extractData(List<Data> data, int limit, boolean shuffle){

		if (shuffle)
			Collections.shuffle(data);

		if (data.size() > limit)
			return data.subList(0,limit);
		else
			return data;
	}

}