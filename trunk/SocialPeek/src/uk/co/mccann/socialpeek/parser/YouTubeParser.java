package uk.co.mccann.socialpeek.parser;


import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

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
public class YouTubeParser extends AbstractParser {

	// &sa=N 						provides less relevant results
	// q=query+inpostauthor:john	provides author search
	private final String BASE_URL = "http://gdata.youtube.com/feeds/base/videos?alt=rss&v=2&q=";

	private final String KEYWORD_SUFFIX = null;
	private final String USER_SUFFIX = null;
	private final String LIMIT_SUFFIX = "&num=";
	
	private final String dateFormat = "EEE, d MMM yyyy H:mm:ss z";


	public void setUpParser(){
		this.random = new Random();
	}

	
	// Fetch Items from an RSS feed and return a list of Data objects
	// with an agreed limit (maybe added in future - limit parameter.
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
			throw new ParseException("Unable to parse Blogger RSS data:" + e.getStackTrace());
		}

		/* get a list of RSS items and then shuffle them up for a random peek! */
		List<Item> items = (List<Item>) channel.getItems();

		if (items==null)
			return new ArrayList<Data>();

		return rssHelper.convertYouTubeToData(items);
		
	}


	public Data getItem() throws ParseException {

		/* not implemented */
		return null;

	}


	public List<Data> getItems(int limit) throws ParseException {

		/* not implemented */
		return null;
	}


	public Data getKeywordItem(String keyword) throws ParseException {

		String query = BASE_URL;
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
			query += "+" + keywords[i];

		return getKeywordItem(query);
	}

	public List<Data> getKeywordItems(String keyword, int limit) throws ParseException {

		String query = BASE_URL;
		query += keyword;
		query += LIMIT_SUFFIX + limit;
		
		List<Data> extractedData = this.getData(query);

		if (extractedData.size() > limit)
			return extractedData.subList(0,limit);
		else
			return extractedData;
	}


	public List<Data> getKeywordItems(String[] keywords, int limit) throws ParseException {

		// Construct query in form: term1+term2+term3
		String query = keywords[0];

		for (int i = 1; i < keywords.length; i++)
			query += "+" + keywords[i];

		return getKeywordItems(query, limit);

	}


	public Data getLatestUserItem(int userId) throws ParseException {

		/* not implemented */
		return null;
	}


	public Data getLatestUserItem(String userId) throws ParseException {

		/* not implemented */
		return null;
	}


	public List<Data> getLatestUserItems(int userId, int limit) throws ParseException {

		/* not implemented */
		return null;
	}

	public List<Data> getLatestUserItems(String userId, int limit) throws ParseException {

		/* not implemented */
		return null;
	}

	public Data getUserItem(int userId) throws ParseException {

		return getUserItem(String.valueOf(userId));
	}

	public Data getUserItem(String userId) throws ParseException {

		/* not implemented */
		return null;
	}

	public List<Data> getUserItems(int userId, int limit) throws ParseException {

		/* not implemented */
		return null;

	}


	public List<Data> getUserItems(String userId, int limit) throws ParseException {

		/* not implemented */
		return null;

	}

}

