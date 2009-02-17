package uk.co.mccann.socialpeek.parser;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;


import org.apache.commons.httpclient.HttpURL;
import org.apache.log4j.Logger;

import uk.co.mccann.socialpeek.SocialPeek;
import uk.co.mccann.socialpeek.exceptions.ParseException;
import uk.co.mccann.socialpeek.interfaces.Data;
import uk.co.mccann.socialpeek.model.PeekData;
import uk.co.mccann.socialpeek.model.SocialService;
import uk.co.mccann.socialpeek.service.DeliciousService;
import uk.co.mccann.socialpeek.service.LastFMService;
import yarfraw.core.datamodel.ChannelFeed;
import yarfraw.core.datamodel.ItemEntry;
import yarfraw.core.datamodel.YarfrawException;
import yarfraw.io.FeedReader;
import yarfraw.io.FeedWriter;

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
* @author Dave Shanley <david.shanley@europe.mccann.com>
*/
public class DeliciousParser extends AbstractParser  {
	
	private FeedReader reader;
	private ChannelFeed channel;
	private SimpleDateFormat deliciousDateFormat;
	private long expireLengthMillis = 1800000; // 30  minutes;.
	
	/**
     *  Default constructor, calls super constructor from AbstractParser
     *  @param service set's the social service for the parser
     */
	public DeliciousParser(SocialService service) {
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
	
	private void createRSSCachedFile(File file, ChannelFeed feed) throws ParseException {
	
		try {
			
			FeedWriter w = new FeedWriter(file);
			w.writeChannel(feed);
		
		} catch (YarfrawException e) {
		
			throw new ParseException("unable to write file: " + e.getMessage());
		}
		
		
	}
	
	
	public DeliciousParser() { }
	
	
	public Data getKeywordItem(String keyword) throws ParseException {
		
		try {
			
			/* check cache file */
			File cachedRSSFile = new File(getSocialService().getConfiguration().getRSSCacheLocation() + "delicious.rss.key."+ keyword.toLowerCase() + ".xml");
			
			if(!this.checkRSSCachedFileValid(cachedRSSFile)) {
			
				/* set up a new RSS reader from source */
				this.reader = new FeedReader(new HttpURL(DeliciousService.DELICIOUS_URL + "rss/tag/" + keyword));
				this.channel = this.reader.readChannel();   
				
				/* write file to cache */
				this.createRSSCachedFile(cachedRSSFile, this.channel);
			
			} else {
				
				/* set up a new RSS reader from file */
				this.reader = new FeedReader(cachedRSSFile);
				this.channel = this.reader.readChannel();   
				
			}
				
				
			/* get a list of RSS items and then shuffle them up for a random peek! */
			List<ItemEntry> items = this.channel.getItems();
			
			/* shuffle it up for some randomness */
			Collections.shuffle(items);
			
			return this.compileDeliciousData(items.get(this.random.nextInt(items.size()-1)));
			
			
		} catch (Exception exp) {
			throw new ParseException("unable to parse del.icio.us RSS data: " + exp.getMessage());
			
		}
		
		
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
				this.reader = new FeedReader(new HttpURL(DeliciousService.DELICIOUS_URL + "rss/" + userId));
				this.channel = this.reader.readChannel();   
				
				/* write file to cache */
				this.createRSSCachedFile(cachedRSSFile, this.channel);
			
			} else {
				
				/* set up a new RSS reader from file */
				this.reader = new FeedReader(cachedRSSFile);
				this.channel = this.reader.readChannel();   
				
			}
			
			List<Data> extractedData = new ArrayList<Data>();
			
			
			/* get a list of RSS items and then shuffle them up for a random peek! */
			List<ItemEntry> items = this.channel.getItems();
			
			for(ItemEntry item : items) {
			
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
				this.reader = new FeedReader(new HttpURL(DeliciousService.DELICIOUS_URL + "rss/" + userId));
				this.channel = this.reader.readChannel();   
				
				/* write file to cache */
				this.createRSSCachedFile(cachedRSSFile, this.channel);
			
			} else {
				
				/* set up a new RSS reader from file */
				this.reader = new FeedReader(cachedRSSFile);
				this.channel = this.reader.readChannel();   
				
			}
			
			
			/* get a list of RSS items and then shuffle them up for a random peek! */
			List<ItemEntry> items = this.channel.getItems();
			
			/* get the first item back */
			ItemEntry rssItem = items.get(0);
			
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
				this.reader = new FeedReader(new HttpURL(DeliciousService.DELICIOUS_URL + "rss/recent"));
				this.channel = this.reader.readChannel();   
				
				/* write file to cache */
				this.createRSSCachedFile(cachedRSSFile, this.channel);
			
			} else {
				
				/* set up a new RSS reader from file */
				this.reader = new FeedReader(cachedRSSFile);
				this.channel = this.reader.readChannel();   
				
			}
			
		
			/* get a list of RSS items and then shuffle them up for a random peek! */
			List<ItemEntry> items = this.channel.getItems();
			
			for(ItemEntry item : items) {
			
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
				this.reader = new FeedReader(new HttpURL(DeliciousService.DELICIOUS_URL + "rss/tag/" + keyword));
				this.channel = this.reader.readChannel();   
				
				/* write file to cache */
				this.createRSSCachedFile(cachedRSSFile, this.channel);
			
			} else {
				
				/* set up a new RSS reader from file */
				this.reader = new FeedReader(cachedRSSFile);
				this.channel = this.reader.readChannel();   
				
			}
			
			
			/* get a list of RSS items and then shuffle them up for a random peek! */
			List<ItemEntry> items = this.channel.getItems();
			
			for(ItemEntry item : items) {
			
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
					this.reader = new FeedReader(new HttpURL(DeliciousService.DELICIOUS_URL + "rss/tag/" + keywords[x]));
					this.channel = this.reader.readChannel();   
					
					/* write file to cache */
					this.createRSSCachedFile(cachedRSSFile, this.channel);
				
				} else {
					
					/* set up a new RSS reader from file */
					this.reader = new FeedReader(cachedRSSFile);
					this.channel = this.reader.readChannel();   
					
				}
				
				/* get a list of RSS items and then shuffle them up for a random peek! */
				List<ItemEntry> items = this.channel.getItems();
				
				/* shuffle them all up for good measure! */
				Collections.shuffle(items);
				
				int counter = 0;
				for(ItemEntry item : items) {
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
				this.reader = new FeedReader(new HttpURL(DeliciousService.DELICIOUS_URL + "rss/" + userId));
				this.channel = this.reader.readChannel();   
				
				/* write file to cache */
				this.createRSSCachedFile(cachedRSSFile, this.channel);
			
			} else {
				
				/* set up a new RSS reader from file */
				this.reader = new FeedReader(cachedRSSFile);
				this.channel = this.reader.readChannel();   
				
			}
		
			
			/* get a list of RSS items and then shuffle them up for a random peek! */
			List<ItemEntry> items = this.channel.getItems();
			
			for(ItemEntry item : items) {
			
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
	
	
	private Data compileDeliciousData(ItemEntry rssItem) throws java.text.ParseException {
		
		this.setUpParser(); // re-set things!
		
		Data data = new PeekData();
		
		if(rssItem.getDescriptionOrSummaryText()!=null && !rssItem.getDescriptionOrSummaryText().equals("null")) {
			data.setBody(rssItem.getDescriptionOrSummaryText());
		} else {
			data.setBody("");
		}
		/* create a new calendar object */
		Calendar pubDate = Calendar.getInstance();
		
		/* parse the date */
		
		try {
		
			/* try first publication date type */
			pubDate.setTime(this.deliciousDateFormat.parse(rssItem.getPubDate()));
		
		} catch(java.text.ParseException exp) {
			
			
			this.deliciousDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
			
			/* try second publication date type */
			pubDate.setTime(this.deliciousDateFormat.parse(rssItem.getPubDate()));
		}
		if(pubDate!=null) {
			data.setDate(pubDate);
		} else {
			data.setDate(Calendar.getInstance());
		}
		if(rssItem.getTitleText()!=null) {
			data.setHeadline(rssItem.getTitleText());
		} else {
			data.setHeadline("not available");
		}
		if(rssItem.getLinks()!= null && rssItem.getLinks().get(0).getHref()!=null) {
			data.setLink(rssItem.getLinks().get(0).getHref()); // only take the first link
		} else {
			data.setLink("http://socialpeek.com");
		}
		if(rssItem.getAuthorOrCreator()!=null && rssItem.getAuthorOrCreator().get(0).getEmailOrText()!=null) {
			data.setUser(rssItem.getAuthorOrCreator().get(0).getEmailOrText()); // only take the first creator
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
				this.reader = new FeedReader(new HttpURL(DeliciousService.DELICIOUS_URL + "rss/recent"));
				this.channel = this.reader.readChannel();   
				
				/* write file to cache */
				this.createRSSCachedFile(cachedRSSFile, this.channel);
			
			} else {
				
				/* set up a new RSS reader from file */
				this.reader = new FeedReader(cachedRSSFile);
				this.channel = this.reader.readChannel();   
				
			}
		
			/* get a list of RSS items and then shuffle them up for a random peek! */
			List<ItemEntry> items = this.channel.getItems();
			
			/* shuffle up the items */
			Collections.shuffle(items);
			
			/* get a random item back from the shuffled list */
			ItemEntry rssItem = items.get(this.random.nextInt(items.size()-1));
			
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
				this.logger.info("[loading RSS data : " + DeliciousService.DELICIOUS_URL + "rss/" + userId);
			}
			
			
			/* check cache file */
			File cachedRSSFile = new File(getSocialService().getConfiguration().getRSSCacheLocation() + "delicious.rss.user."+ userId.toLowerCase() + ".xml");
			
			if(!this.checkRSSCachedFileValid(cachedRSSFile)) {
			
				/* set up a new RSS reader from source */
				this.reader = new FeedReader(new HttpURL(DeliciousService.DELICIOUS_URL + "rss/" + userId));
				this.channel = this.reader.readChannel();   
				
				/* write file to cache */
				this.createRSSCachedFile(cachedRSSFile, this.channel);
			
			} else {
				
				/* set up a new RSS reader from file */
				this.reader = new FeedReader(cachedRSSFile);
				this.channel = this.reader.readChannel();   
				
			}
		
			/* get a list of RSS items and then shuffle them up for a random peek! */
			List<ItemEntry> items = this.channel.getItems();
			
			/* shuffle up the items */
			Collections.shuffle(items);
			
			
			/* get a random item back from the shuffled list */
			ItemEntry rssItem = items.get(this.random.nextInt(items.size()-1));
			
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