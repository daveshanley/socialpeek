package uk.co.mccann.socialpeek.parser;


import java.io.File;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.List;


import org.apache.commons.httpclient.HttpURL;
import org.apache.log4j.Logger;

import com.sun.cnpi.rss.elements.Channel;
import com.sun.cnpi.rss.elements.Item;
import com.sun.cnpi.rss.elements.Rss;
import com.sun.cnpi.rss.parser.RssParser;
import com.sun.cnpi.rss.parser.RssParserFactory;

import uk.co.mccann.socialpeek.SocialPeek;
import uk.co.mccann.socialpeek.exceptions.ParseException;
import uk.co.mccann.socialpeek.interfaces.Data;
import uk.co.mccann.socialpeek.model.PeekData;
import uk.co.mccann.socialpeek.model.SocialService;
import uk.co.mccann.socialpeek.service.DeliciousService;
import uk.co.mccann.socialpeek.service.DiggService;
import uk.co.mccann.socialpeek.service.LastFMService;
import uk.co.mccann.socialpeek.rss.*;

/**
 * <b>DeliciousParser</b><br/>
 * extract and process data from audio scrobbler web services
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
public class DiggParser extends AbstractParser  {

	
	RSSReader parser = new RSSReader();
	RSSWriter feedWriter = new RSSWriter();
	Rss rssFeed;
	Channel channel;

	private SimpleDateFormat deliciousDateFormat;
	private long expireLengthMillis = 1800000; // 30  minutes;.

	/**
	 *  Default constructor, calls super constructor from AbstractParser
	 *  @param service set's the social service for the parser
	 */
	public DiggParser(SocialService service) {
		super(service);
	}

	private boolean checkRSSCachedFileValid(File file) {

		if(file.exists()) {

			long time = System.currentTimeMillis();
			if(file.lastModified() > (time - this.expireLengthMillis)) {

				return true;

			} else {

				return false;
			}

		} else {

			return false;
		}

	}

	private void createRSSCachedFile(File file, Channel feed) throws ParseException {

			RSSWriter rw = new RSSWriter();
			rw.writeRSSToFile(file, feed);
	}


	public DiggParser() { }


	public Data getKeywordItem(String keyword) throws ParseException {

		try {

			/* check cache file */
			File cachedRSSFile = new File(getSocialService().getConfiguration().getRSSCacheLocation() + "delicious.rss.key."+ keyword.toLowerCase() + ".xml");

			if(!this.checkRSSCachedFileValid(cachedRSSFile)) {

				/* set up a new RSS reader from source */
				channel = parser.parseRSSFeed(new URL(DiggService.TOPIC_URL_URL + "v2/rss/tag/" + keyword));

				/* write file to cache */
				this.createRSSCachedFile(cachedRSSFile, this.channel);

			} else {

				/* set up a new RSS reader from file */
				channel = parser.parseRSSFeed(cachedRSSFile.toURL());

			}

		} catch (Exception exp) {
			throw new ParseException("unable to parse del.icio.us RSS data: " + exp.getMessage());

		}
		
		/* get a list of RSS items and then shuffle them up for a random peek! */
		List<Item> items = (List) channel.getItems();

		/* shuffle it up for some randomness */
		Collections.shuffle(items);

		return this.compileDeliciousData(items.get(this.random.nextInt(items.size()-1)));


	}

	public List<Data> getLatestMultipleUserItems(int userId, int limit) throws ParseException {
		return this.getLatestMultipleUserItems(String.valueOf(userId), limit);
	}

	public List<Data> getLatestMultipleUserItems(String userId, int limit) throws ParseException {

		try {

			/* check cache file */
			File cachedRSSFile = new File(getSocialService().getConfiguration().getRSSCacheLocation() + "delicious.rss.user."+ userId.toLowerCase() + ".xml");


			if(!this.checkRSSCachedFileValid(cachedRSSFile)) {

				/* set up a new RSS reader from source */
				channel = parser.parseRSSFeed(new URL(DiggService.DIGG_URL + "v2/rss/" + userId));

				/* write file to cache */
				this.createRSSCachedFile(cachedRSSFile, this.channel);

			} else {

				/* set up a new RSS reader from file */
				channel = parser.parseRSSFeed(cachedRSSFile.toURL());

			}

			List<Data> extractedData = new ArrayList<Data>();


			/* get a list of RSS items and then shuffle them up for a random peek! */
			List<Item> items = (List<Item>) channel.getItems();

			for(Item item : items) {

				/* compile item */
				extractedData.add(this.compileDeliciousData(item));

			}

			List<Data> compactedData = new ArrayList<Data>();

			/* now trim it up */
			if(limit > extractedData.size()) limit = extractedData.size(); // make sure we don't go out of bounds!
			for(int x = 0; x < limit; x++) {
				compactedData.add(extractedData.get(x));
			}

			return compactedData;


		} catch (Exception exp) {

			throw new ParseException("unable to parse del.icio.us RSS data: " + exp.getMessage());

		}

	}

	public Data getLatestSingleUserItem(int userId) throws ParseException {

		return this.getLatestSingleUserItem(String.valueOf(userId));

	}

	public Data getLatestSingleUserItem(String userId) throws ParseException {

		try {

			/* check cache file */
			File cachedRSSFile = new File(getSocialService().getConfiguration().getRSSCacheLocation() + "delicious.rss.user."+ userId.toLowerCase() + ".xml");


			if(!this.checkRSSCachedFileValid(cachedRSSFile)) {

				/* set up a new RSS reader from source */
				channel = parser.parseRSSFeed(new URL(DiggService.DIGG_URL + "v2/rss/" + userId));

				/* write file to cache */
				this.createRSSCachedFile(cachedRSSFile, this.channel);

			} else {

				/* set up a new RSS reader from file */
				channel = parser.parseRSSFeed(cachedRSSFile.toURL());

			}


			/* get a list of RSS items and then shuffle them up for a random peek! */
			List<Item> items = (List<Item>) channel.getItems();

			/* get the first item back */
			Item rssItem = items.get(0);

			return this.compileDeliciousData(rssItem);

		} catch (Exception exp) {
			exp.printStackTrace();
			throw new ParseException("unable to parse del.icio.us RSS data: " + exp.getMessage());

		}
	}

	public List<Data> getMultipleItems(int limit) throws ParseException {

		try {

			List<Data> extractedData = new ArrayList<Data>();

			/* check cache file */
			File cachedRSSFile = new File(getSocialService().getConfiguration().getRSSCacheLocation() + "delicious.rss.recent.xml");


			if(!this.checkRSSCachedFileValid(cachedRSSFile)) {

				/* set up a new RSS reader from source */
				channel = parser.parseRSSFeed(new URL(DiggService.DIGG_URL + "v2/rss/recent"));

				
				/* write file to cache */
				this.createRSSCachedFile(cachedRSSFile, this.channel);

			} else {

				/* set up a new RSS reader from file */
				channel = parser.parseRSSFeed(cachedRSSFile.toURL());

			}


			/* get a list of RSS items and then shuffle them up for a random peek! */
			List<Item> items = (List<Item>) channel.getItems();

			for(Item item : items) {

				/* compile item */
				extractedData.add(this.compileDeliciousData(item));

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


		} catch (Exception exp) {

			throw new ParseException("unable to parse del.icio.us RSS data: " + exp.getMessage());

		}


	}

	public List<Data> getMultipleKeywordItems(String keyword, int limit) throws ParseException {

		try {

			List<Data> extractedData = new ArrayList<Data>();

			/* check cache file */
			File cachedRSSFile = new File(getSocialService().getConfiguration().getRSSCacheLocation() + "delicious.rss.key."+ keyword.toLowerCase() + ".xml");

			if(!this.checkRSSCachedFileValid(cachedRSSFile)) {

				/* set up a new RSS reader from source */
				channel = parser.parseRSSFeed(new URL(DiggService.DIGG_URL + "v2/rss/tag/" + keyword));

				/* write file to cache */
				this.createRSSCachedFile(cachedRSSFile, this.channel);

			} else {

				/* set up a new RSS reader from file */
				channel = parser.parseRSSFeed(cachedRSSFile.toURL());

			}


			/* get a list of RSS items and then shuffle them up for a random peek! */
			List<Item> items = (List<Item>) channel.getItems();

			for(Item item : items) {

				/* compile item */
				extractedData.add(this.compileDeliciousData(item));

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


		} catch (Exception exp) {

			throw new ParseException("unable to parse del.icio.us RSS data: " + exp.getMessage());

		}


	}

	public List<Data> getMultipleKeywordItems(String[] keywords, int limit) throws ParseException {

		try {

			/* first we need to work out the ratio of keywords to limited returns */
			int ratio = limit  / keywords.length;

			List<Data> extractedData = new ArrayList<Data>();

			for(int x = 0; x < keywords.length; x++) {

				/* check cache file */
				File cachedRSSFile = new File(getSocialService().getConfiguration().getRSSCacheLocation() + "delicious.rss.key."+ keywords[x] + ".xml");

				if(!this.checkRSSCachedFileValid(cachedRSSFile)) {

					/* set up a new RSS reader from source */
					channel = parser.parseRSSFeed(new URL(DiggService.DIGG_URL + "v2/rss/tag/" + keywords[x]));

					/* write file to cache */
					this.createRSSCachedFile(cachedRSSFile, this.channel);

				} else {

					/* set up a new RSS reader from file */
					channel = parser.parseRSSFeed(cachedRSSFile.toURL());

				}


				/* get a list of RSS items and then shuffle them up for a random peek! */
				List<Item> items = (List<Item>) channel.getItems();

				for(Item item : items) {

					/* compile item */
					extractedData.add(this.compileDeliciousData(item));

				}

				/* shuffle them all up for good measure! */
				Collections.shuffle(items);

				int counter = 0;
				for(Item item : items) {
					if(counter < ratio) {
						/* compile item */
						extractedData.add(this.compileDeliciousData(item));
					} else {
						break;
					}
					counter++;
				}
			}

			Collections.shuffle(extractedData);

			List<Data> compactedData = new ArrayList<Data>();

			/* now trim it up */
			if(limit > extractedData.size()) limit = extractedData.size(); // make sure we don't go out of bounds!
			for(int x = 0; x < limit; x++) {
				compactedData.add(extractedData.get(x));
			}

			return compactedData;


		} catch (Exception exp) {

			throw new ParseException("unable to parse del.icio.us RSS data: " + exp.getMessage());

		}



	}

	public List<Data> getMultipleUserItems(int userId, int limit) throws ParseException {

		return this.getMultipleUserItems(String.valueOf(userId), limit);

	}

	public List<Data> getMultipleUserItems(String userId, int limit) throws ParseException {
		try {

			List<Data> extractedData = new ArrayList<Data>();

			/* check cache file */
			File cachedRSSFile = new File(getSocialService().getConfiguration().getRSSCacheLocation() + "delicious.rss.user."+ userId.toLowerCase() + ".xml");

			if(!this.checkRSSCachedFileValid(cachedRSSFile)) {

				/* set up a new RSS reader from source */
				channel = parser.parseRSSFeed(new URL(DiggService.DIGG_URL + "v2/rss/" + userId));

				/* write file to cache */
				this.createRSSCachedFile(cachedRSSFile, this.channel);

			} else {

				/* set up a new RSS reader from file */
				channel = parser.parseRSSFeed(cachedRSSFile.toURL());

			}


			/* get a list of RSS items and then shuffle them up for a random peek! */
			List<Item> items = (List<Item>) channel.getItems();

			for(Item item : items) {

				/* compile item */
				extractedData.add(this.compileDeliciousData(item));

			}

			List<Data> compactedData = new ArrayList<Data>();

			/* shuffle it up */
			Collections.shuffle(extractedData);

			/* now trim it up */
			if(limit > extractedData.size()) limit = extractedData.size(); // make sure we don't go out of bounds!
			for(int x = 0; x < limit; x++) {
				compactedData.add(extractedData.get(x));
			}

			return compactedData;


		} catch (Exception exp) {

			throw new ParseException("unable to parse del.icio.us RSS data: " + exp.getMessage());

		}
	}


	private Data compileDeliciousData(Item rssItem) throws java.text.ParseException {

		this.setUpParser(); // re-set things!

		Data data = new PeekData();

		if(rssItem.getDescription()!=null && !rssItem.getDescription().equals("null")) {
			data.setBody(rssItem.getDescription().getText());
		} else {
			data.setBody("");
		}
		/* create a new calendar object */
		Calendar pubDate = Calendar.getInstance();

		/* parse the date */

		try {

			/* try first publication date type */
			pubDate.setTime(this.deliciousDateFormat.parse(rssItem.getPubDate().getText()));

		} catch(java.text.ParseException exp) {


			this.deliciousDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");

			/* try second publication date type */
			pubDate.setTime(this.deliciousDateFormat.parse(rssItem.getPubDate().getText()));
		}
		if(pubDate!=null) {
			data.setDate(pubDate);
		} else {
			data.setDate(Calendar.getInstance());
		}
		if(rssItem.getTitle()!=null) {
			data.setHeadline(rssItem.getTitle().getText());
		} else {
			data.setHeadline("not available");
		}
		if(rssItem.getLink()!= null) {
			data.setLink(rssItem.getLink().getText()); 
		} else {
			data.setLink("http://socialpeek.com");
		}
		if(rssItem.getAuthor()!=null) {
			data.setUser(rssItem.getAuthor().getText()); // only take the first creator
		} else {
			data.setUser("not available");
		}
		return data;

	}

	public Data getSingleItem() throws ParseException {

		try {

			/* check cache file */
			File cachedRSSFile = new File(getSocialService().getConfiguration().getRSSCacheLocation() + "delicious.rss.recent.xml");

			if(!this.checkRSSCachedFileValid(cachedRSSFile)) {

				/* set up a new RSS reader from source */
				channel = parser.parseRSSFeed(new URL(DiggService.DIGG_URL + "v2/rss/recent"));

				/* write file to cache */
				this.createRSSCachedFile(cachedRSSFile, this.channel);

			} else {

				/* set up a new RSS reader from file */
				channel = parser.parseRSSFeed(cachedRSSFile.toURL());

			}


			/* get a list of RSS items and then shuffle them up for a random peek! */
			List<Item> items = (List<Item>) channel.getItems();

			/* shuffle up the items */
			Collections.shuffle(items);

			/* get a random item back from the shuffled list */
			Item rssItem = items.get(this.random.nextInt(items.size()-1));

			return this.compileDeliciousData(rssItem);

		} catch (Exception exp) {
			exp.printStackTrace();
			throw new ParseException("unable to parse del.icio.us RSS data: " + exp.getMessage());

		}

	}

	public Data getSingleUserItem(int userId) throws ParseException {

		return this.getSingleUserItem(String.valueOf(userId));

	}

	public Data getSingleUserItem(String userId) throws ParseException {
		try {

			if(SocialPeek.logging) {
				this.logger.info("[loading RSS data : " + DiggService.DIGG_URL + "rss/" + userId);
			}


			/* check cache file */
			File cachedRSSFile = new File(getSocialService().getConfiguration().getRSSCacheLocation() + "delicious.rss.user."+ userId.toLowerCase() + ".xml");

			if(!this.checkRSSCachedFileValid(cachedRSSFile)) {

				/* set up a new RSS reader from source */
				channel = parser.parseRSSFeed(new URL(DiggService.DIGG_URL + "v2/rss/" + userId));

				/* write file to cache */
				this.createRSSCachedFile(cachedRSSFile, this.channel);

			} else {

				/* set up a new RSS reader from file */
				channel = parser.parseRSSFeed(cachedRSSFile.toURL());

			}


			/* get a list of RSS items and then shuffle them up for a random peek! */
			List<Item> items = (List<Item>) channel.getItems();


			/* shuffle up the items */
			Collections.shuffle(items);


			/* get a random item back from the shuffled list */
			Item rssItem = items.get(this.random.nextInt(items.size()-1));

			return this.compileDeliciousData(rssItem);

		} catch (Exception exp) {
			exp.printStackTrace();
			throw new ParseException("unable to parse del.icio.us RSS data: " + exp.getMessage());

		}

	}

	public void setUpParser() {

		super.setUpParser();
		this.logger = Logger.getLogger(DeliciousParser.class);
		this.deliciousDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'kk:mm:ss'Z'");
	}

	public Data getKeywordItem(String[] keywords) throws ParseException {

		List<Data> data = this.getMultipleKeywordItems(keywords, 20);
		return data.get(this.random.nextInt(data.size()-1));

	}

}
