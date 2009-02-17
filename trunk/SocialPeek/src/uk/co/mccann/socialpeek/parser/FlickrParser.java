package uk.co.mccann.socialpeek.parser;


import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import uk.co.mccann.socialpeek.exceptions.KeywordLimitException;
import uk.co.mccann.socialpeek.exceptions.ParseException;
import uk.co.mccann.socialpeek.interfaces.Data;
import uk.co.mccann.socialpeek.rss.RSSHelper;
import uk.co.mccann.socialpeek.rss.RSSReader;

import com.sun.cnpi.rss.elements.Channel;
import com.sun.cnpi.rss.elements.Item;

/**
 * <b>WeFeelFineParser</b><br/>
 * Use the WWF API to read and parse feelings and thoughts from around the web
 *
 * <h4>Copyright and License</h4>
 * This code is copyright (c) McCann Erickson Advertising Ltd, 2008 except where
 * otherwise stated. It is released as
 * open-source under the Creative Commons NC-SA license. See
 * <a href="http://creativecommons.org/licenses/by-nc-sa/2.5/">http://creativecommons.org/licenses/by-nc-sa/2.5/</a>
 * for license details. This code comes with no warranty or support.
 *
 * @author Lewis Taylor <lewis.taylor@europe.mccann.com>
 */
public class FlickrParser extends AbstractParser {

	// Query URL Strings
	private final String BASE_URL = "http://api.flickr.com/services/feeds/photos_public.gne?format=rss2";
	private final String KEYWORD_SUFFIX = "&tags=";
	private final String USER_SUFFIX = "&id=";
	private final String LIMIT_SUFFIX = null;

	// Date format - Dates parsed to calendar objects
	private final String dateFormat = "EEE, d MMM yyyy H:mm:ss z";


	public void setUpParser(){
		this.random = new Random();
	}

	public List<Data> getData(String query) throws ParseException {

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
			throw new ParseException("Unable to parse Live Search RSS data:" + e.getStackTrace());
		}

		/* get a list of RSS items and then shuffle them up for a random peek! */
		List<Item> items = (List<Item>) channel.getItems();

		if (items==null)
			return new ArrayList<Data>();

		return rssHelper.convertToFlickrData(items);
		
	}


	public Data getSingleItem() throws ParseException {

		String query = BASE_URL;

		List<Data> extractedData = getData(query);
		
		if (extractedData==null || extractedData.size()==0)
			return null;
		
		// Shuffle Result
		Collections.shuffle(extractedData);
		return extractedData.get(0);

	}


	public List<Data> getMultipleItems(int limit) throws ParseException {

		String query = BASE_URL;
		
		List<Data> extractedData = this.getData(query);

		/* shuffle it up for some randomness */
		if (extractedData==null || extractedData.size()==0)
			return null;
		
		if (extractedData.size() > limit)
			return extractedData.subList(0,limit);
		else
			return extractedData;
	}


	public Data getKeywordItem(String keyword) throws ParseException {

		String query = BASE_URL + KEYWORD_SUFFIX;
		query += keyword;

		List<Data> extractedData = getData(query);
		
		if (extractedData==null || extractedData.size()==0)
			return null;
		
		// Shuffle Result
		Collections.shuffle(extractedData);
		return extractedData.get(0);
		
	}

	public Data getKeywordItem(String[] keywords) throws ParseException {

		// Construct query in form: term1+term2+term3
		String query = keywords[0];

		for (int i = 1; i < keywords.length; i++)
			query += "," + keywords[i];

		return getKeywordItem(query);
	}

	public List<Data> getMultipleKeywordItems(String keyword, int limit) throws ParseException {

		String query = BASE_URL + KEYWORD_SUFFIX;
		query += keyword;
		
		List<Data> extractedData = this.getData(query);

		/* shuffle it up for some randomness */
		if (extractedData==null || extractedData.size()==0)
			return null;
		
		if (extractedData.size() > limit)
			return extractedData.subList(0,limit);
		else
			return extractedData;
	}


	public List<Data> getMultipleKeywordItems(String[] keywords, int limit) throws ParseException {

		// Construct query in form: term1+term2+term3
		String query = keywords[0];

		for (int i = 1; i < keywords.length; i++)
			query += "," + keywords[i];

		return getMultipleKeywordItems(query, limit);

	}


	public Data getLatestSingleUserItem(int userId) throws ParseException {

		return getLatestSingleUserItem(String.valueOf(userId));
	}


	public Data getLatestSingleUserItem(String userId) throws ParseException {

		String query = BASE_URL + USER_SUFFIX;
		query += userId;

		List<Data> extractedData = getData(query);
		
		if (extractedData==null || extractedData.size()==0)
			return null;
		
		return extractedData.get(0);
	}


	public List<Data> getLatestMultipleUserItems(int userId, int limit) throws ParseException {

		return getLatestMultipleUserItems(String.valueOf(userId), limit);
	}

	public List<Data> getLatestMultipleUserItems(String userId, int limit) throws ParseException {

		String query = BASE_URL + USER_SUFFIX;
		query += userId;
		
		List<Data> extractedData = this.getData(query);

		/* shuffle it up for some randomness */
		if (extractedData==null || extractedData.size()==0)
			return null;
		
		if (extractedData.size() > limit)
			return extractedData.subList(0,limit);
		else
			return extractedData;
	}

	public Data getSingleUserItem(int userId) throws ParseException {

		return getSingleUserItem(String.valueOf(userId));
	}

	public Data getSingleUserItem(String userId) throws ParseException {

		String query = BASE_URL + USER_SUFFIX;
		query += userId;

		List<Data> extractedData = getData(query);
		
		if (extractedData==null || extractedData.size()==0)
			return null;
		
		Collections.shuffle(extractedData);
		return extractedData.get(0);
	}

	public List<Data> getMultipleUserItems(int userId, int limit) throws ParseException {

		return getMultipleUserItems(String.valueOf(userId), limit);
	}


	public List<Data> getMultipleUserItems(String userId, int limit) throws ParseException {

		String query = BASE_URL + USER_SUFFIX;
		query += userId;
		
		List<Data> extractedData = this.getData(query);

		/* shuffle it up for some randomness */
		if (extractedData==null || extractedData.size()==0)
			return null;
		
		Collections.shuffle(extractedData);
		
		if (extractedData.size() > limit)
			return extractedData.subList(0,limit);
		else
			return extractedData;

	}
	
	public void checkLimit(int limit) throws KeywordLimitException{
		if (limit>20)
			throw new KeywordLimitException();
	}

}
