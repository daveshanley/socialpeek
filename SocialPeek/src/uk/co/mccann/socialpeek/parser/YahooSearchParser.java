package uk.co.mccann.socialpeek.parser;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.io.StringWriter;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import org.apache.xerces.parsers.DOMParser;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import uk.co.mccann.socialpeek.exceptions.KeywordLimitException;
import uk.co.mccann.socialpeek.exceptions.ParseException;
import uk.co.mccann.socialpeek.interfaces.Data;
import uk.co.mccann.socialpeek.model.PeekData;
import uk.co.mccann.socialpeek.xml.XMLDataHelper;

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
 *
 *	MAX_RESULTS = 10
 *
 *
 * @author Lewis Taylor <lewis.taylor@europe.mccann.com>
 */
public class YahooSearchParser extends AbstractParser {

	// Query URL Strings
	private final String BASE_URL = "http://boss.yahooapis.com/ysearch/web/v1/";
	private final String KEYWORD_SUFFIX = null;
	private final String USER_SUFFIX = null;
	private final String LIMIT_SUFFIX = "&count=";
	private final String BASE_SUFFIX = "?appid=david.shanley@ymail.com&format=xml";

	// Date format - Dates parsed to calendar objects
	private final String dateFormat = "yyyy/MM/dd";

	public void setUpParser(){
		this.random = new Random();
	}

	public Data getItem() throws ParseException {

		return null;

	}


	public List<Data> getItems(int limit) throws ParseException {

		return null;
		
	}


	public Data getKeywordItem(String keyword) throws ParseException {

		String query = BASE_URL;
		query += keyword;
		query += BASE_SUFFIX;

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
		query += BASE_SUFFIX;
		query += LIMIT_SUFFIX;
		query += limit;
		
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
	
	private List<Data> getData(String query) throws ParseException {
	
		List<Data> dataList = new ArrayList<Data>();
	
		DOMParser parser = new DOMParser();
		
		XMLDataHelper dataHelp = new XMLDataHelper();
		dataHelp.setDateFormat(dateFormat);
		
		try {
	
			/* get XML source and parse instead of using URL direct, Xerces gets upset when it finds ampersands, and for some reason
			 * the WWF API allows ampersands in attributes to XML nodes!!! yikes 
			 */
			String xml = this.parseDodgyCharacters(this.getXMLSource(query));
			BufferedReader reader = new  BufferedReader(new StringReader(xml));
			InputSource source = new InputSource(reader);
	
			/* parse the data! after all that formatting */
			parser.parse(source);
	
			/* get DOM doc from parsed remote URL */
			Element root = parser.getDocument().getDocumentElement();
	
			NodeList results = root.getElementsByTagName("result");
	
			// If result from query is >0 create Data Object(s)
			if (results!=null && results.getLength() > 0){
	
				// Cycle through every result and create
				// a PeekData Object containing the values
				// returned in the XML file
				for (int i = 0; i < results.getLength(); i++){
	
					Data data = new PeekData();
	
					//get the employee element
					Element result =  (Element) results.item(i);
					
					// Extract fields from XML
					String headline, body, link, date;
					
					headline = getNodeText(result,"title",1);
					body = getNodeText(result,"abstract",1);
					link = getNodeText(result,"url",1);
					date = getNodeText(result,"date",1);
	
					dataHelp.setHeadline(data, headline);
					dataHelp.setBody(data, body);
					dataHelp.setLink(data, link);
					dataHelp.setDate(data, date);
					
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
			textVal = el.getFirstChild().getNodeValue();
		}
	
		return textVal;
	}

	public void checkLimit(int limit) throws KeywordLimitException{
		if (limit>20)
			throw new KeywordLimitException();
	}

}
