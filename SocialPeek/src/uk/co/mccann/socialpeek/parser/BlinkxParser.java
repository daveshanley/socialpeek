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
 * <b>WordPressParser</b><br/>
 * Use the WWF API to read and parse feelings and thoughts from around the web
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
 *
 *
 * @author Lewis Taylor <lewis.taylor@europe.mccann.com>
 */
public class BlinkxParser extends AbstractParser {

	// Query URL Strings
	private final String BASE_URL = "http://www.blinkx.com/rss?apicall=action%3Dquery%26databasematch%3Dmedia%26totalresults%3Dtrue%26clientip%3D199.4.27.122%26text%3Dlewis%26start%3D1%26maxresults%3D10%26sortby%3Drelevance%26fieldtext%3D%26printfields%3Dsummary_link_text%2Csummary_link_href_field%2Cplaylistpath%2Cmedia_source_url%2Ccategory%2Cmedia_bitrate%2Cuse_lightning_cast%2Cuse_adult_full_video_adverts%2Cadditional_info%2Cmedia_duration%2Cdrecontent%2Ctranscription_ctm%2Csection_start%2Clink%2Cmedia_type%2Cdefault_hit_image_location%2Cexternal_player_url%2Cnum_dpflvs%2Ctitle%2Csource_page_url%2Csummary%2Cowner_id%2Cdefault_wide_image_location%2Cwide_image_link%2Cdefault_footer_image_location%2Cchannel%2Cmedia_publish_date%2Cid%2Cmedia_location%2Cnum_swfs%2Caverage_vote%2Cnum_views%2Cnum_preflvs%2Cnum_hdflvs%2Csafe_flag%2Cdisplay_name%2Cnum_comments%2Cnum_dpmp4s%2Cstaticpreview%26adultlinkwords%3Dtrue%26clientregion%3DH9%26newsresults%3Dtrue%26characters%3D10000&query=";
	private final String RECENT_URL = "http://en.blog.wordpress.com/feed/";
	private final String KEYWORD_SUFFIX = "&q=";
	private final String USER_SUFFIX = null;
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
			throw new ParseException("Unable to parse WordPress RSS data:" + e.getStackTrace());
		}

		/* get a list of RSS items and then shuffle them up for a random peek! */
		List<Item> items = (List<Item>) channel.getItems();

		if (items==null)
			return new ArrayList<Data>();

		return rssHelper.convertToData(items);
		
	}


	public Data getItem() throws ParseException {

		String query = RECENT_URL;

		List<Data> extractedData = getData(query);
		
		if (extractedData==null || extractedData.size()==0)
			return null;
		
		// Shuffle Result
		Collections.shuffle(extractedData);
		return extractedData.get(0);

	}


	public List<Data> getItems(int limit) throws ParseException {

		String query = RECENT_URL;
		
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
			query += "+" + keywords[i];

		return getKeywordItem(query);
	}

	public List<Data> getKeywordItems(String keyword, int limit) throws ParseException {

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


	public List<Data> getKeywordItems(String[] keywords, int limit) throws ParseException {

		// Construct query in form: term1+term2+term3
		String query = keywords[0];

		for (int i = 1; i < keywords.length; i++)
			query += "+" + keywords[i];

		return getKeywordItems(query, limit);

	}


	public Data getLatestUserItem(int userId) throws ParseException {

		return getLatestUserItem(String.valueOf(userId));
	}


	public Data getLatestUserItem(String userId) throws ParseException {

//		String query = BASE_URL + USER_SUFFIX;
//		query += userId;
//
//		List<Data> extractedData = getData(query);
//		
//		if (extractedData==null || extractedData.size()==0)
//			return null;
//		
//		return extractedData.get(0);
		
		return null;
	}


	public List<Data> getLatestUserItems(int userId, int limit) throws ParseException {

		return getLatestUserItems(String.valueOf(userId), limit);
	}

	public List<Data> getLatestUserItems(String userId, int limit) throws ParseException {

//		String query = BASE_URL + USER_SUFFIX;
//		query += userId;
//		
//		List<Data> extractedData = this.getData(query);
//
//		/* shuffle it up for some randomness */
//		if (extractedData==null || extractedData.size()==0)
//			return null;
//		
//		if (extractedData.size() > limit)
//			return extractedData.subList(0,limit);
//		else
//			return extractedData;
		
		return null;
	}

	public Data getUserItem(int userId) throws ParseException {

		return getUserItem(String.valueOf(userId));
	}

	public Data getUserItem(String userId) throws ParseException {

//		String query = BASE_URL + USER_SUFFIX;
//		query += userId;
//
//		List<Data> extractedData = getData(query);
//		
//		if (extractedData==null || extractedData.size()==0)
//			return null;
//		
//		Collections.shuffle(extractedData);
//		return extractedData.get(0);
		
		return null;
	}

	public List<Data> getUserItems(int userId, int limit) throws ParseException {

		return getUserItems(String.valueOf(userId), limit);
	}


	public List<Data> getUserItems(String userId, int limit) throws ParseException {

//		String query = BASE_URL + USER_SUFFIX;
//		query += userId;
//		
//		List<Data> extractedData = this.getData(query);
//
//		/* shuffle it up for some randomness */
//		if (extractedData==null || extractedData.size()==0)
//			return null;
//		
//		Collections.shuffle(extractedData);
//		
//		if (extractedData.size() > limit)
//			return extractedData.subList(0,limit);
//		else
//			return extractedData;
		
		return null;

	}
	
	public void checkLimit(int limit) throws KeywordLimitException{
		if (limit>20)
			throw new KeywordLimitException();
	}

}
