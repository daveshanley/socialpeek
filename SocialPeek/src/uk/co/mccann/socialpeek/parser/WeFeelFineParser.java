package uk.co.mccann.socialpeek.parser;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringWriter;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.xerces.parsers.DOMParser;
import org.w3c.dom.DOMException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import uk.co.mccann.socialpeek.exceptions.ParseException;
import uk.co.mccann.socialpeek.interfaces.Data;
import uk.co.mccann.socialpeek.interfaces.Parser;
import uk.co.mccann.socialpeek.model.PeekData;
import uk.co.mccann.socialpeek.service.WeFeelFineService;

public class WeFeelFineParser extends AbstractParser implements Parser {

	private URL wwfURL;
	private String apiURL = WeFeelFineService.API_URL + "ShowFeelings?";
	private String returnFields = "display=xml&returnfields=feeling,gender,sentence,imageid,posturl,posttime,postdate,country,state,city,born";
	private String limitField ="limit=";
	private String feelingField ="feeling=";

	/* XML variables */
	private DOMParser parser;
	private Document document;
	private Element rootElement;
	
	private SimpleDateFormat wwfDateFormat;

	/**
	 * Get XML Source, no longer required as Xerces will grab the XML remote
	 * automatically
	 * 
	 * @throws ParseException
	 */
	private String getXMLSource(String url) throws ParseException {

		try {

			/* open stream */
			InputStream in = wwfURL.openStream();

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

			throw new ParseException("unable to retrive from WWF API: "
					+ e.getMessage());
		}

	}
	
	
	
	private String getImgPath(String postdate, String imageid, String imagesize){
	    String imgpath = "http://images.wefeelfine.org/data/images/ ";
	    imgpath += postdate.replace('-','/');
	    imgpath += "/"+imageid;
	    if(imagesize == "thumb") imgpath += "_thumb.jpg";
	    else imgpath += "_full.jpg";
	    return imgpath;
	}
	
	private List<Data> generateFeelings() throws ParseException {
		
		return this.parseXML(this.apiURL + this.limitField + 50 + "&" +  this.returnFields);
		
	}
	
	private List<Data> generateFeelings(int limit, String emotion) throws ParseException {
	
		return this.parseXML(this.apiURL + this.limitField + limit + "&" + this.feelingField + emotion + "&" +  this.returnFields);
		
	}
	
	private List<Data> generateFeelings(int limit) throws ParseException {
		
		return this.parseXML(this.apiURL + this.limitField + limit + "&" + this.returnFields);
	}
	
	private List<Data> generateFeelings(String emotion) throws ParseException {
		
		return this.parseXML(this.apiURL  + this.feelingField + emotion + "&" +  this.returnFields + "&" + this.limitField + 50);
		
	}
	
	
	private List<Data> parseXML(String url) throws ParseException {

		try {
			
			DOMParser parser = new DOMParser();
			
			parser.parse(url);
			
			List<Data> dataArray = new ArrayList<Data>();
			
			/* get DOM doc from parsed remote URL */
			Document node = parser.getDocument();
			
			/* first node should always be 'feelings' */
			if (node.hasChildNodes()) {
				
				/* get feelings node, and all feeling children */
				if (node.getFirstChild().getFirstChild() != null) {
					
					/* all children */
					NodeList children = node.getFirstChild().getChildNodes();

					for (int x = 0; x < children.getLength(); x++) {
						
						NamedNodeMap map = children.item(x).getAttributes();
						
						/* make sure that there is content in the node */
						if (map != null) {
							
							Data data = new PeekData(); 
							if(map.getNamedItem("feeling")!=null) data.setHeadline(map.getNamedItem("feeling").getTextContent());
							if(map.getNamedItem("sentence")!=null)data.setBody(map.getNamedItem("sentence").getTextContent());
							if(map.getNamedItem("posturl")!=null)data.setLink(map.getNamedItem("posturl").getTextContent());
							
							/* set image */
							String postDate = map.getNamedItem("postdate").getTextContent();
							if(map.getNamedItem("imageid")!=null) {
								String imageId = map.getNamedItem("imageid").getTextContent();
								data.setUserProfilePhoto(this.getImgPath(postDate, imageId, "thumb"));
								
							}
							
							/* set date */
							Calendar date = Calendar.getInstance();
							date.setTime(this.wwfDateFormat.parse(postDate));
							data.setDate(date);
							
							String location, city = null, state = null, country = null;
							if(map.getNamedItem("city")!=null) city = map.getNamedItem("city").getTextContent();
							if(map.getNamedItem("state")!=null) state = map.getNamedItem("state").getTextContent();
							if(map.getNamedItem("country")!=null) country = map.getNamedItem("country").getTextContent();
							
							/* parse location */
							location = "";
							if(city!=null) location += city;
							if(state!=null) state += ", " + state;
							if(country!=null) country += ", " + country;
							data.setLocation(location);
							
							
							/* parse user */
							String age = null, gender = null;
							if(map.getNamedItem("gender")!=null) gender = map.getNamedItem("gender").getTextContent();
							if(map.getNamedItem("age")!=null) age = map.getNamedItem("born").getTextContent();
							
							String user = null;
							if(gender!=null) {
								Integer genderValue = new Integer(gender);
								if(genderValue.equals(1)) gender= "Male";
								if(genderValue.equals(0)) gender= "Female";
								user += gender;
								if(age!=null) {
									Calendar cal = Calendar.getInstance();
									Integer thisYear = cal.get(Calendar.YEAR);
									Integer ageValue = thisYear - Integer.valueOf(age);
									user += ", " + ageValue + " years old";
								}
							}
							
							data.setUser(user);
							dataArray.add(data);
						}

					}
				} else {
					throw new ParseException("no data recovered from url: " + url);
				}
				
			} else {
				throw new ParseException("no data recovered from url: " + url);
			}
			
			return dataArray;

		} catch (Exception exp) {
			exp.printStackTrace();
			throw new ParseException("unable to parse WeFeelFine XML: " + exp.getMessage());
		}

	}

	public Data getKeywordItem(String keyword) throws ParseException {
		
		List<Data> extractedData = this.generateFeelings(keyword);
		return extractedData.get(this.random.nextInt(extractedData.size()-1));
	}

	public Data getKeywordItem(String[] keywords) throws ParseException {
		
		List<Data> dataCollection = new ArrayList<Data>();
		List<Data> compactedData = new ArrayList<Data>();
		
		for(String keyword : keywords) {
			
			List<Data> extractedData = this.generateFeelings(keyword);
			Collections.shuffle(extractedData);
			/* nested loop */
			for(Data dataItem : extractedData) {
				dataCollection.add(dataItem);
			}
		
		}
		
		/* shuffle it up */
		Collections.shuffle(dataCollection);
		
		return compactedData.get(this.random.nextInt(compactedData.size()-1));
		
		
	}

	public List<Data> getLatestMultipleUserItems(int userId, int limit) throws ParseException {
		
		/* not implemented */
		return null;
	}

	public List<Data> getLatestMultipleUserItems(String userId, int limit) throws ParseException {
		
		/* not implemented */
		return null;
	}

	public Data getLatestSingleUserItem(int userId) throws ParseException {
		
		/* not implemented */
		return null;
	}

	public Data getLatestSingleUserItem(String userId) throws ParseException {
		
		/* not implemented */
		return null;
	}

	public List<Data> getMultipleItems(int limit) throws ParseException {
		
		List<Data> extractedData = this.generateFeelings(limit);
		Collections.shuffle(extractedData);
		
		return extractedData;
		
	}

	public List<Data> getMultipleKeywordItems(String keyword, int limit) throws ParseException {
		
		List<Data> extractedData = this.generateFeelings(limit, keyword);
		Collections.shuffle(extractedData);
		
		return extractedData;
		
	}

	public List<Data> getMultipleKeywordItems(String[] keywords, int limit) throws ParseException {
		
		List<Data> dataCollection = new ArrayList<Data>();
		List<Data> compactedData = new ArrayList<Data>();
		
		for(String keyword : keywords) {
			
			List<Data> extractedData = this.generateFeelings(limit, keyword);
			Collections.shuffle(extractedData);
			/* nested loop */
			for(Data dataItem : extractedData) {
				dataCollection.add(dataItem);
			}
		
		}
		
		/* shuffle it up */
		Collections.shuffle(dataCollection);
		
		/* now trim it up */
		if(limit > dataCollection.size()) limit = dataCollection.size(); // make sure we don't go out of bounds!
		for(int x = 0; x < limit; x++) {
			compactedData.add(dataCollection.get(x));
		}
		
		return compactedData;
		
	}

	public List<Data> getMultipleUserItems(int userId, int limit) throws ParseException {
		
		/* not implemented */
		return null;
		
	}

	public List<Data> getMultipleUserItems(String userId, int limit) throws ParseException {
		
		/* not implemented */
		return null;
		
	}

	public Data getSingleItem() throws ParseException {
	
		List<Data> extractedData = this.generateFeelings();
		return extractedData.get(this.random.nextInt(extractedData.size()-1));
	
	}

	public Data getSingleUserItem(int userId) throws ParseException {
		
		/* not implemented */
		return null;
	}

	public Data getSingleUserItem(String userId) throws ParseException {
		
		/* not implemented */
		return null;
	}

	public void setUpParser() {
		
		this.random = new Random();
		this.wwfDateFormat = new SimpleDateFormat("yyyy-mm-dd");
	}

}
