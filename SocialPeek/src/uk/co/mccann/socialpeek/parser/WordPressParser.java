package uk.co.mccann.socialpeek.parser;


import java.util.Collections;
import java.util.List;
import uk.co.mccann.socialpeek.exceptions.NoResultsException;
import uk.co.mccann.socialpeek.exceptions.ParseException;
import uk.co.mccann.socialpeek.interfaces.Data;
import uk.co.mccann.socialpeek.rss.RSSHelper;
import uk.co.mccann.socialpeek.rss.RSSReader;

import com.sun.cnpi.rss.elements.Channel;
import com.sun.cnpi.rss.elements.Item;

/**
 * <b>WordPressParser</b><br/>
 * Use the Wordpress API to retrieve blogs
 *
 * <h4>Copyright and License</h4>
 * This code is copyright (c) McCann Erickson Advertising Ltd, 2008 except where
 * otherwise stated. It is released as
 * open-source under the Creative Commons NC-SA license. See
 * <a href="http://creativecommons.org/licenses/by-nc-sa/2.5/">http://creativecommons.org/licenses/by-nc-sa/2.5/</a>
 * for license details. This code comes with no warranty or support.
 *
 *	NOTES:
 *	MAX_RESULTS = 10
 *	No Limit
 *	No Users
 *
 *
 * @author Lewis Taylor <lewis.taylor@europe.mccann.com>
 */
public class WordPressParser extends AbstractParser {

	// Query URL Strings
	private final String BASE_URL = "http://en.blog.wordpress.com/feed/";
	private final String KEYWORD_URL = "http://en.search.wordpress.com/?f=feed&q={keyword}";

	private final String dateFormat = "EEE, d MMM yyyy H:mm:ss z";



	/** {@inheritDoc} */
	public Data getItem() throws ParseException, NoResultsException {

		return getItems(1).get(0);
	}

	/** {@inheritDoc} */
	public List<Data> getItems(int limit) throws ParseException, NoResultsException {

		String query = BASE_URL;

		List<Data> extractedData = this.getData(query);

		// return 'limit' items of shuffled data
		return extractData(extractedData, limit, true);
	}

	/** {@inheritDoc} */
	public Data getKeywordItem(String keyword) throws ParseException, NoResultsException {

		return getKeywordItems(keyword, 1).get(0);
	}

	/** {@inheritDoc} */
	public Data getKeywordItem(String[] keywords) throws ParseException, NoResultsException {

		// Construct query in form: term1+term2+term3
		String query = keywords[0];

		for (int i = 1; i < keywords.length; i++)
			query += "+" + keywords[i];

		return getKeywordItem(query);
	}

	/** {@inheritDoc} */
	public List<Data> getKeywordItems(String keyword, int limit) throws ParseException, NoResultsException {

		String query = KEYWORD_URL.replace("{keyword}", keyword);

		List<Data> extractedData = this.getData(query);

		// return 'limit' items of shuffled data
		return extractData(extractedData, limit, true);
	}

	/** {@inheritDoc} */
	public List<Data> getKeywordItems(String[] keywords, int limit) throws ParseException, NoResultsException {

		// Construct query in form: term1+term2+term3
		String query = keywords[0];

		for (int i = 1; i < keywords.length; i++)
			query += "+" + keywords[i];

		return getKeywordItems(query, limit);
	}

	/** {@inheritDoc} */
	public Data getUserItem(int userId) throws ParseException, NoResultsException {

		return null;
	}

	/** {@inheritDoc} */
	public Data getUserItem(String userId) throws ParseException, NoResultsException {

		return null;
	}

	/** {@inheritDoc} */
	public List<Data> getUserItems(int userId, int limit) throws ParseException, NoResultsException {

		return null;
	}

	/** {@inheritDoc} */
	public List<Data> getUserItems(String userId, int limit) throws ParseException, NoResultsException {

		return null;
	}

	/** {@inheritDoc} */
	public Data getLatestUserItem(int userId) throws ParseException, NoResultsException {

		return null;
	}

	/** {@inheritDoc} */
	public Data getLatestUserItem(String userId) throws ParseException, NoResultsException {

		return null;
	}

	/** {@inheritDoc} */
	public List<Data> getLatestUserItems(int userId, int limit) throws ParseException, NoResultsException {

		return null;
	}

	/** {@inheritDoc} */
	public List<Data> getLatestUserItems(String userId, int limit) throws ParseException, NoResultsException {

		return null;
	}


	/**
	 * Fetch Items from an RSS location and convert the feed a list 
	 * of Data objects
	 *  
	 * @param query
	 * @return List<Data>
	 */
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
			throw new ParseException("Unable to parse WordPress RSS data:" + e.getStackTrace());
		}

		List<Item> items = null;

		if (channel!=null)
			items = (List<Item>) channel.getItems();

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