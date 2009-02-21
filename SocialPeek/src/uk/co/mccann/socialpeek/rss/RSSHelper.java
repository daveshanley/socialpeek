package uk.co.mccann.socialpeek.rss;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import uk.co.mccann.socialpeek.interfaces.Data;
import uk.co.mccann.socialpeek.model.PeekData;

import com.sun.cnpi.rss.elements.Item;


/**
 * @author Lewis Taylor - lewis.taylor@europe.mccann.com
 *
 */
public class RSSHelper {

	// The data format of the service used.
	private SimpleDateFormat dateFormat;


	public RSSHelper(){
		dateFormat = null;
	}

	public RSSHelper(String dateFormat){
		setDateFormat(dateFormat);
	}

	public RSSHelper(SimpleDateFormat dateFormat){
		setDateFormat(dateFormat);
	}

	/**
	 * Sets the format of the date to allow
	 * parsing of the date element in the 
	 * RSS item.
	 * 
	 * @param dateFormat
	 */
	public void setDateFormat(String dateFormat){

		this.dateFormat = dateFormat==null ? null : new SimpleDateFormat(dateFormat);
	}

	public void setDateFormat(SimpleDateFormat dateFormat){

		this.dateFormat = dateFormat;
	}

	public Data convertToData(Item item){

		List<Item> items = new ArrayList<Item>();
		items.add(item);

		// Return the first item in the returned list
		return convertToData(items).get(0);
	}

	public List<Data> convertToData(List<Item> items){

		List<Data> dataItems = new ArrayList<Data>();


		// Map each rss item to a data object
		for (Item rssItem : items){

			Data data = new PeekData();


			// Set Title
			if(rssItem.getTitle()!=null) 
				data.setHeadline(rssItem.getTitle().getText());
			else 
				data.setHeadline("not available");


			// Set Description
			if(rssItem.getDescription()!=null && !rssItem.getDescription().equals("null")) 
				data.setBody(rssItem.getDescription().getText());
			else 
				data.setBody("");


			// Set Link
			if(rssItem.getLink()!= null) 
				data.setLink(rssItem.getLink().getText()); 
			else 
				data.setLink("http://socialpeek.com");
			
			// Set User
			if(rssItem.getAuthor()!=null) 
				data.setUser(rssItem.getAuthor().getText()); // only take the first creator
			else 
				data.setUser("not available");


			// Set Date
			Calendar cal = Calendar.getInstance();

			try {
				if (dateFormat!=null && rssItem.getPubDate()!=null)
					cal.setTime(dateFormat.parse(rssItem.getPubDate().getText()));
			} catch (ParseException e) {
				cal.setTime(Calendar.getInstance().getTime());
			}

			data.setDate(cal);


			dataItems.add(data);

		}

		return dataItems;

	}

	public Data covertFlickrToData(Item item){

		List<Item> items = new ArrayList<Item>();
		items.add(item);

		// Return the first item in the returned list
		return convertFlickrToData(items).get(0);
	}

	public List<Data> convertFlickrToData(List<Item> items){

		List<Data> dataItems = new ArrayList<Data>();


		// Map each rss item to a data object
		for (Item rssItem : items){

			Data data = convertToData(rssItem);

			// Set photo
			if(rssItem.getEnclosure()!=null)
				data.setThumbnail(rssItem.getEnclosure().getAttribute("url"));
			else
				data.setThumbnail("not available");

			dataItems.add(data);
		}

		return dataItems;
	}

	public Data covertYouTubeToData(Item item){

		List<Item> items = new ArrayList<Item>();
		items.add(item);

		// Return the first item in the returned list
		return convertYouTubeToData(items).get(0);
	}

	public List<Data> convertYouTubeToData(List<Item> items){

		List<Data> dataItems = new ArrayList<Data>();


		// Map each rss item to a data object
		for (Item rssItem : items){

			Data data = convertToData(rssItem);

			
			// Extract thumbnail data from description as YouTube is shit!
			String description = data.getBody();
		
			
			String[] result = description.split("<img alt=\"\" src=\"");

			String thumbnail = null;
			if (result[1]!=null){
				int end = 0;
				end = result[1].indexOf("\">");
				thumbnail = result[1].substring(0, end);
			}
			
			data.setThumbnail(thumbnail);
			
			// Also do the same with the user
			String[] userResult = description.split("user=");

			String user = null;
			if (userResult[1]!=null){
				int end = 0;
				end = userResult[1].indexOf("\">");
				user = userResult[1].substring(0, end);
			}
			
			data.setUser(user);
			
			dataItems.add(data);
		}

		return dataItems;
	}

	public Data covertTruveoToData(Item item){
	
		List<Item> items = new ArrayList<Item>();
		items.add(item);
	
		// Return the first item in the returned list
		return convertTruveoToData(items).get(0);
	}

	public List<Data> convertTruveoToData(List<Item> items){
	
		List<Data> dataItems = new ArrayList<Data>();
	
	
		// Map each rss item to a data object
		for (Item rssItem : items){
	
			Data data = convertToData(rssItem);
	
			
			// Extract thumbnail data from description as YouTube is shit!
			String description = data.getBody();
		
			
			String[] result = description.split("<img src=\"");
	
			String thumbnail = null;
			if (result[1]!=null){
				int end = 0;
				end = result[1].indexOf("\">");
				thumbnail = result[1].substring(0, end);
			}
			
			data.setThumbnail(thumbnail);
			
			dataItems.add(data);
		}
	
		return dataItems;
	}


}
