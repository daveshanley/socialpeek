package uk.co.mccann.socialpeek.parser;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.io.StringWriter;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import org.apache.xerces.parsers.DOMParser;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;


import uk.co.mccann.socialpeek.exceptions.ParseException;
import uk.co.mccann.socialpeek.interfaces.Data;
import uk.co.mccann.socialpeek.model.PeekData;
import uk.co.mccann.socialpeek.service.BloglinesService;
import uk.co.mccann.socialpeek.service.WeFeelFineService;
import uk.co.mccann.socialpeek.xml.DataHelper;

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
 * @author Dave Shanley <david.shanley@europe.mccann.com>
 */
public class BloglinesParser extends AbstractParser {

	public static final String BASE_URL ="http://www.bloglines.com/search?format=publicapi&";
	public static final String USER = "apiuser=david.shanley@europe.mccann.com&";
	public static final String API_KEY = "apikey=4008F3701868836DAAF79A47E5C8FA7B&";
	public static final String BLOGLINES_URL = BASE_URL + USER + API_KEY + "q=";

	// Create a DataHelper object to allow
	// easier PeekData manipulation
	private DataHelper dataHelp = new DataHelper();

	private SimpleDateFormat dateFormat;

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


	private List<Data> parseXML(String url) throws ParseException {
		
		List<Data> dataList = new ArrayList<Data>();

		DOMParser parser = new DOMParser();
		
		try {

			/* get XML source and parse instead of using URL direct, Xerces gets upset when it finds ampersands, and for some reason
			 * the WWF API allows ampersands in attributes to XML nodes!!! yikes 
			 */
			String xml = this.parseDodgyCharacters(this.getXMLSource(url));
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
					String headline, body, link, user, date;
					
					headline = getNodeText(result,"title",1);
					body = getNodeText(result,"abstract",1);
					link = getNodeText(result,"url",2);
					user = getNodeText(result,"url",1);
					date = result.getAttribute("date");

					dataHelp.setHeadline(data, headline);
					dataHelp.setBody(data, body);
					dataHelp.setLink(data, link);
					dataHelp.setUser(data, user);
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


	private List<Data> getBlogs(String query) throws ParseException {

		return this.parseXML(BLOGLINES_URL + query);

	}


	public Data getSingleItem() throws ParseException {

		/* not implemented */
		return null;

	}


	public List<Data> getMultipleItems(int limit) throws ParseException {

		/* not implemented */
		return null;
	}


	public Data getKeywordItem(String keyword) throws ParseException {

		List<Data> extractedData = this.getBlogs(keyword);
		int randomNumber = this.random.nextInt(extractedData.size());
		if(randomNumber > 0) {
			return extractedData.get(randomNumber);
		} else {
			return extractedData.get(0);
		}
	}

	public Data getKeywordItem(String[] keywords) throws ParseException {

		// Construct query in form: term1+term2+term3
		String query = keywords[0];

		for (int i = 1; i < keywords.length; i++)
			query.concat("+" + keywords[i]);

		return getKeywordItem(query);
	}

	public List<Data> getMultipleKeywordItems(String keyword, int limit) throws ParseException {

		List<Data> extractedData = this.getBlogs(keyword);

		if (extractedData.size() > limit)
			return extractedData.subList(0,limit);
		else
			return extractedData;
	}


	public List<Data> getMultipleKeywordItems(String[] keywords, int limit) throws ParseException {

		// Construct query in form: term1+term2+term3
		String query = keywords[0];

		for (int i = 1; i < keywords.length; i++)
			query.concat("+" + keywords[i]);

		return getMultipleKeywordItems(query, limit);

	}


	public Data getLatestSingleUserItem(int userId) throws ParseException {

		/* not implemented */
		return null;
	}


	public Data getLatestSingleUserItem(String userId) throws ParseException {

		/* not implemented */
		return null;
	}


	public List<Data> getLatestMultipleUserItems(int userId, int limit) throws ParseException {

		/* not implemented */
		return null;
	}

	public List<Data> getLatestMultipleUserItems(String userId, int limit) throws ParseException {

		/* not implemented */
		return null;
	}

	public Data getSingleUserItem(int userId) throws ParseException {

		/* not implemented */
		return null;
	}

	public Data getSingleUserItem(String userId) throws ParseException {

		/* not implemented */
		return null;
	}

	public List<Data> getMultipleUserItems(int userId, int limit) throws ParseException {

		/* not implemented */
		return null;

	}


	public List<Data> getMultipleUserItems(String userId, int limit) throws ParseException {

		/* not implemented */
		return null;

	}


	public void setUpParser() {

		this.random = new Random();
		// Fri, 20 Oct 2006 20:46:00 GMT
		this.dateFormat = new SimpleDateFormat("EEE, d MMM yyyy H:mm:ss z");
		
		this.dataHelp.setDateFormat(dateFormat);
	}

}

