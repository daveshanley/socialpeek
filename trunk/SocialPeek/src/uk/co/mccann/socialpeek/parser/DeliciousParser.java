package uk.co.mccann.socialpeek.parser;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import org.apache.commons.httpclient.HttpURL;
import uk.co.mccann.socialpeek.exceptions.ParseException;
import uk.co.mccann.socialpeek.interfaces.Data;
import uk.co.mccann.socialpeek.interfaces.Parser;
import uk.co.mccann.socialpeek.model.PeekData;
import uk.co.mccann.socialpeek.model.SocialService;
import uk.co.mccann.socialpeek.service.DeliciousService;
import yarfraw.core.datamodel.ChannelFeed;
import yarfraw.core.datamodel.ItemEntry;
import yarfraw.io.FeedReader;

public class DeliciousParser extends AbstractParser implements Parser {
	
	private FeedReader reader;
	private ChannelFeed channel;
	private SimpleDateFormat deliciousDateFormat;
	
	/**
     *  Default constructor, calls super constructor from AbstractParser
     *  @param service set's the social service for the parser
     */
	public DeliciousParser(SocialService service) {
		super(service);
	}
	
	public DeliciousParser() { }
	
	
	public Data getKeywordItem(String keyword) throws ParseException {
		// TODO Auto-generated method stub
		return null;
	}

	public List<Data> getLatestMultipleUserItems(int userId, int limit)
			throws ParseException {
		// TODO Auto-generated method stub
		return null;
	}

	public List<Data> getLatestMultipleUserItems(String userId, int limit) throws ParseException {
		
		try {
			
			List<Data> extractedData = new ArrayList<Data>();
			
			/* set up a new RSS reader */
			this.reader = new FeedReader(new HttpURL(DeliciousService.DELICIOUS_URL + "rss/" + userId));
			this.channel = this.reader.readChannel();   
		
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
			
			/* set up a new RSS reader */
			this.reader = new FeedReader(new HttpURL(DeliciousService.DELICIOUS_URL + "rss/recent"));
			this.channel = this.reader.readChannel();   
		
			/* get a list of RSS items and then shuffle them up for a random peek! */
			List<ItemEntry> items = this.channel.getItems();
			
			/* get the first item back */
			ItemEntry rssItem = items.get(0);
			
			return this.compileDeliciousData(rssItem);
			
		} catch (Exception exp) {
			
			throw new ParseException("unable to parse del.icio.us RSS data: " + exp.getMessage());
			
		}
	}

	public List<Data> getMultipleItems(int limit) throws ParseException {
		
		try {
			
			List<Data> extractedData = new ArrayList<Data>();
			
			/* set up a new RSS reader */
			this.reader = new FeedReader(new HttpURL(DeliciousService.DELICIOUS_URL + "rss/recent"));
			this.channel = this.reader.readChannel();   
		
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
			
			/* set up a new RSS reader */
			this.reader = new FeedReader(new HttpURL(DeliciousService.DELICIOUS_URL + "rss/tag/" + keyword));
			this.channel = this.reader.readChannel();   
		
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

	public List<Data> getMultipleKeywordItems(String[] keywords, int limit)
			throws ParseException {
		// TODO Auto-generated method stub
		return null;
	}

	public List<Data> getMultipleUserItems(int userId, int limit) throws ParseException {
	
		return this.getMultipleUserItems(String.valueOf(userId), limit);
	
	}

	public List<Data> getMultipleUserItems(String userId, int limit) throws ParseException {
		try {
			
			List<Data> extractedData = new ArrayList<Data>();
			
			/* set up a new RSS reader */
			this.reader = new FeedReader(new HttpURL(DeliciousService.DELICIOUS_URL + "rss/" + userId));
			this.channel = this.reader.readChannel();   
		
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
	
	
	private Data compileDeliciousData(ItemEntry rssItem) throws java.text.ParseException {
		
		Data data = new PeekData();
		
		data.setBody(rssItem.getDescriptionOrSummaryText());
		
		/* create a new calendar object */
		Calendar pubDate = Calendar.getInstance();
		
		/* parse the date */
		pubDate.setTime(this.deliciousDateFormat.parse(rssItem.getPubDate()));
		
		data.setDate(pubDate);
		data.setHeadline(rssItem.getTitleText());
		data.setLink(rssItem.getLinks().get(0).getHref()); // only take the first link
		data.setUser(rssItem.getAuthorOrCreator().get(0).getEmailOrText()); // only take the first creator
		
		return data;
		
	}
	
	public Data getSingleItem() throws ParseException {
		
		try {
		
			/* set up a new RSS reader */
			this.reader = new FeedReader(new HttpURL(DeliciousService.DELICIOUS_URL + "rss/recent"));
			this.channel = this.reader.readChannel();   
		
			/* get a list of RSS items and then shuffle them up for a random peek! */
			List<ItemEntry> items = this.channel.getItems();
			
			/* shuffle up the items */
			Collections.shuffle(items);
			
			/* get a random item back from the shuffled list */
			ItemEntry rssItem = items.get(this.random.nextInt(items.size()-1));
			
			return this.compileDeliciousData(rssItem);
			
		} catch (Exception exp) {
			
			throw new ParseException("unable to parse del.icio.us RSS data: " + exp.getMessage());
			
		}
		
	}

	public Data getSingleUserItem(int userId) throws ParseException {
		
		return this.getSingleUserItem(String.valueOf(userId));
		
	}

	public Data getSingleUserItem(String userId) throws ParseException {
		try {
			
			/* set up a new RSS reader */
			this.reader = new FeedReader(new HttpURL(DeliciousService.DELICIOUS_URL + "rss/" + userId));
			this.channel = this.reader.readChannel();   
		
			/* get a list of RSS items and then shuffle them up for a random peek! */
			List<ItemEntry> items = this.channel.getItems();
			
			/* shuffle up the items */
			Collections.shuffle(items);
			
			/* get a random item back from the shuffled list */
			ItemEntry rssItem = items.get(this.random.nextInt(items.size()-1));
			
			return this.compileDeliciousData(rssItem);
			
		} catch (Exception exp) {
			
			throw new ParseException("unable to parse del.icio.us RSS data: " + exp.getMessage());
			
		}
		
	}

	public void setUpParser() {
		this.random = new Random();
		this.deliciousDateFormat = new SimpleDateFormat("yyyy-mm-dd'T'kk:mm:ss'Z'");
	}

	public Data getKeywordItem(String[] keywords) throws ParseException {
		// TODO Auto-generated method stub
		return null;
	}

}
