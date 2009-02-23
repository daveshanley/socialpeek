package uk.co.mccann.socialpeek.parser;


import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.io.StringWriter;
import java.net.URL;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Random;

import org.apache.xerces.parsers.DOMParser;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import uk.co.mccann.socialpeek.exceptions.NoResultsException;
import uk.co.mccann.socialpeek.exceptions.ParseException;
import uk.co.mccann.socialpeek.interfaces.Data;
import uk.co.mccann.socialpeek.model.PeekData;
import uk.co.mccann.socialpeek.rss.RSSHelper;
import uk.co.mccann.socialpeek.rss.RSSReader;
import uk.co.mccann.socialpeek.xml.XMLDataHelper;

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
public class DiggParser extends AbstractParser {


	// RSS Caching variables
	private final String xmlKey = "digg.rss.";
	private final long expireLengthMillis = 1800000; // 30  minutes

	// query variables
	private final String BASE_URL = "http://services.digg.com/stories?appkey=http://socialpeek.com&type=xml&count={limit}";
	private final String KEYWORD_URL = "http://services.digg.com/stories?appkey=http://socialpeek.com&type=xml&search={keyword}&count={limit}";
	private final String USER_URL = "http://services.digg.com/user/{user}/submissions?appkey=http://socialpeek.com&type=xml&count={limit}";

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
	private List<Data> getData(String query) throws ParseException {

		System.out.println(query + "\n");

		List<Data> dataList = new ArrayList<Data>();

		DOMParser parser = new DOMParser();

		XMLDataHelper dataHelp = new XMLDataHelper();
		dataHelp.setDateFormat(dateFormat);

		try {

			/* get XML source and parse instead of using URL direct, Xerces gets upset 
			 * when it finds ampersands, and for some reason the WWF API allows 
			 * ampersands in attributes to XML nodes!!! yikes 
			 */
			String xml = this.parseDodgyCharacters(this.getXMLSource(query));
			BufferedReader reader = new  BufferedReader(new StringReader(xml));
			InputSource source = new InputSource(reader);

			/* parse the data! after all that formatting */
			parser.parse(source);

			/* get DOM doc from parsed remote URL */
			Element root = parser.getDocument().getDocumentElement();

			NodeList stories = root.getElementsByTagName("story");

			// If result from query is >0 create Data Object(s)
			if (stories!=null && stories.getLength() > 0){

				// Cycle through every result and create
				// a PeekData Object containing the values
				// returned in the XML file
				for (int i = 0; i < stories.getLength(); i++){

					Data data = new PeekData();

					//get the employee element
					Element story =  (Element) stories.item(i);

					// Extract fields from XML
					String headline, body, link, date, user;

					headline = getNodeText(story,"title",1);
					body = getNodeText(story,"description",1);
					link = story.getAttribute("link");
					user = ((Element) (story.getElementsByTagName("user")).item(0)).getAttribute("name");
					date = story.getAttribute("submit_date");

					dataHelp.setHeadline(data, headline);
					dataHelp.setBody(data, body);
					dataHelp.setLink(data, link);
					dataHelp.setUser(data, user);

					Calendar cal = Calendar.getInstance();
					cal.setTimeInMillis(Long.valueOf(date)*1000);

					data.setDate(cal);

					//add it to list
					dataList.add(data);
				}
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		return dataList;

	}

	/**
	 * Get XML Source, no longer required as Xerces will grab the XML remote
	 * automatically
	 * 
	 * @throws ParseException
	 */
	private String getXMLSource(String url) throws ParseException {

		try {

			/* open stream */
			URL urlObj = new URL(url);
			InputStream in = urlObj.openStream();

			/* wrap a reader around the stream */
			BufferedReader buff = new BufferedReader(new InputStreamReader(in));

			/* populate writer with xml data from API */
			StringWriter writer = new StringWriter();

			String xmlData;

			while ((xmlData = buff.readLine()) != null) {
				writer.append(xmlData + "\n");
			}

			return writer.toString();

		} catch (IOException e) {

			throw new ParseException("unable to retrive from BL API: "
					+ e.getMessage());
		}

	}

	private String parseDodgyCharacters(String xml) {

		return xml.replaceAll("&", "%amp%");

	}

	private String getNodeText(Element node, String tagName, int occurence){
		String textVal = null;

		NodeList tags = node.getElementsByTagName(tagName);

		if(tags != null && tags.getLength() > 0) {
			Element el;
			if(occurence > 0 && occurence <= tags.getLength())
				el = (Element) tags.item(occurence-1);
			else
				el = (Element) tags.item(0);

			if (el.getFirstChild()!=null)
				textVal = el.getFirstChild().getNodeValue();
			else
				textVal = "";
		}

		return textVal;
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

