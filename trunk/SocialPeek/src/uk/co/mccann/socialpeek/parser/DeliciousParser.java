package uk.co.mccann.socialpeek.parser;


import java.io.File;
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
 * <b>YouTubeParser</b><br/>
 * Use the WWF API to read and parse feelings and thoughts from around the web
 *
 * <h4>Copyright and License</h4>
 * This code is copyright (c) McCann Erickson Advertising Ltd, 2008 except where
 * otherwise stated. It is released as
 * open-source under the Creative Commons NC-SA license. See
 * <a href="http://creativecommons.org/licenses/by-nc-sa/2.5/">http://creativecommons.org/licenses/by-nc-sa/2.5/</a>
 * for license details. This code comes with no warranty or support.
 *
 *	Limit parameter not known
 *
 *
 * @author Lewis Taylor <lewis.taylor@europe.mccann.com>
 */
public class DeliciousParser extends AbstractParser {

	// RSS Caching variables
	private final String xmlKey = "delicious.rss.";
	private final long expireLengthMillis = 1800000; // 30  minutes
	
	
	// &sa=N 						provides less relevant results
	// q=query+inpostauthor:john	provides author search
	private final String BASE_URL = "http://feeds.delicious.com/v2/rss/";

	private final String KEYWORD_SUFFIX = "tag/";
	private final String LIMIT_SUFFIX = "?count=";
	private final int DEFAULT_LIMIT = 10;

	private final String dateFormat = "EEE, d MMM yyyy H:mm:ss z";
	
	public void setUpParser(){
		this.random = new Random();
	}


	public Data getSingleItem() throws ParseException, NoResultsException {

		return getMultipleItems(1).get(0);

	}


	public List<Data> getMultipleItems(int limit) throws ParseException, NoResultsException {

		String query = BASE_URL + LIMIT_SUFFIX + DEFAULT_LIMIT;

		List<Data> extractedData = this.getData(query);

		// return 'limit' items of shuffled data
		return extractData(extractedData, limit, true);
	}


	public Data getKeywordItem(String keyword) throws ParseException, NoResultsException {

		return getMultipleKeywordItems(keyword, 1).get(0);
	}

	
	public Data getKeywordItem(String[] keywords) throws ParseException, NoResultsException {

		// Construct query in form: term1+term2+term3
		String query = keywords[0];

		for (int i = 1; i < keywords.length; i++)
			query += "+" + keywords[i];

		return getKeywordItem(query);
	}

	
	public List<Data> getMultipleKeywordItems(String keyword, int limit) throws ParseException, NoResultsException {

		String query = BASE_URL + KEYWORD_SUFFIX + keyword + LIMIT_SUFFIX + DEFAULT_LIMIT;

		List<Data> extractedData = this.getData(query);

		// return 'limit' items of shuffled data
		return extractData(extractedData, limit, true);
	}


	public List<Data> getMultipleKeywordItems(String[] keywords, int limit) throws ParseException, NoResultsException {

		// Construct query in form: term1+term2+term3
		String query = keywords[0];

		for (int i = 1; i < keywords.length; i++)
			query += "+" + keywords[i];

		return getMultipleKeywordItems(query, limit);
	}

	
	public Data getLatestSingleUserItem(int userId) throws ParseException, NoResultsException {

		return getLatestSingleUserItem(String.valueOf(userId));
	}


	public Data getLatestSingleUserItem(String userId) throws ParseException, NoResultsException {

		return getLatestMultipleUserItems(userId, 1).get(0);
	}


	public List<Data> getLatestMultipleUserItems(int userId, int limit) throws ParseException, NoResultsException {

		return getLatestMultipleUserItems(String.valueOf(userId), limit);
	}

	
	public List<Data> getLatestMultipleUserItems(String userId, int limit) throws ParseException, NoResultsException {

		String query = BASE_URL + userId + LIMIT_SUFFIX + DEFAULT_LIMIT;
		
		List<Data> extractedData = this.getData(query);

		return extractData(extractedData, limit, false);
	}

	
	public Data getSingleUserItem(int userId) throws ParseException, NoResultsException {

		return getSingleUserItem(String.valueOf(userId));
	}

	
	public Data getSingleUserItem(String userId) throws ParseException, NoResultsException {

		return getMultipleUserItems(userId, 1).get(0);
	}

	
	public List<Data> getMultipleUserItems(int userId, int limit) throws ParseException, NoResultsException {
		return getMultipleUserItems(String.valueOf(userId), limit);

	}


	public List<Data> getMultipleUserItems(String userId, int limit) throws ParseException, NoResultsException {

		String query = BASE_URL + userId + LIMIT_SUFFIX + DEFAULT_LIMIT;

		List<Data> extractedData = this.getData(query);

		return extractData(extractedData, limit, true);
	}

	
	private String doCacheInspection(String suffix){
		
		File file = new File(getSocialService().getConfiguration().getRSSCacheLocation() + xmlKey + suffix + ".xml");
		
		if(file.exists()) {

			long time = System.currentTimeMillis();
			
			if(file.lastModified() > (time - expireLengthMillis)) 
				return file.getAbsolutePath();
			else 
				return null;
		} else {
			return null;
		}
		
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
			throw new ParseException("Unable to parse Blogger RSS data:" + e.getStackTrace());
		}
		
		
		/* get a list of RSS items and then shuffle them up for a random peek! */
		List<Item> items = (List<Item>) channel.getItems();
	
		if (items==null && items.size()==0)
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

