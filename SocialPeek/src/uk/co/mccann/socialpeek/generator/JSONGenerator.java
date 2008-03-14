package uk.co.mccann.socialpeek.generator;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import uk.co.mccann.socialpeek.exceptions.ParseException;
import uk.co.mccann.socialpeek.exceptions.SocialPeekException;
import uk.co.mccann.socialpeek.interfaces.Data;

/**
 * JSONGenerator
 * Generate valid JSON data from PeekData object(s) ready to be parsed by JSON friendly apps.
 *
 * <h4>Copyright and License</h4>
 * This code is copyright (c) McCann Erickson Advertising Ltd, 2008 except where
 * otherwise stated. It is released as
 * open-source under the LGPL license. See
 * <a href="http://www.gnu.org/licenses/lgpl.html">http://www.gnu.org/licenses/lgpl.html</a>
 * for license details. This code comes with no warranty or support.
 *
 * @author Dave Shanley <david.shanley@europe.mccann.com>
 */

public class JSONGenerator extends AbstractGenerator {
	
	/**
     * Default constructor 
   	 * 
   	 * call's super from AbstractGenerator to set up SimpleDateFormat property.
   	 * 
     * @see AbstractGenerator
     */
	public JSONGenerator() {
		super();
	}
	
	
	/**
     * Generate JSON data from single PeekData Object
   	 * 
   	 * Will read, parse and build JSON string using single item, JSON based on SocialPeek XML XSD.
   	 * 
     * @param dataIn the PeekData object you want to build into JSON data,
     * @return the valid JSON String
     * @see Data
     * @see PeekData
     * @throws SocialPeekException
     * 
     */
	public String generate(Data dataIn) throws SocialPeekException {
		
		try {
			/* construct a JSON Object */
			JSONObject jsonObject = new JSONObject();
			
			/* create a good old fashioned collection */
			Map<String,Object> dataCollection = new HashMap<String,Object>();
			dataCollection.put("headline",dataIn.getHeadline());
			dataCollection.put("body",dataIn.getBody());
			dataCollection.put("link",dataIn.getLink());
			dataCollection.put("date",this.sdf.format(dataIn.getDate().getTime()));
			
			/* check for a photo, if so, add it as an attribute*/
			if(dataIn.getUserProfilePhoto()!=null) {
				dataCollection.put("user_photo",dataIn.getUserProfilePhoto());
			}
			
			/* add to our collection */
			jsonObject.put("post",dataCollection);
			return jsonObject.toString();
			
			
		} catch (JSONException e) {
			
			/* problem building JSONObject */
			throw new SocialPeekException("unable to build JSON Object : " + e.getMessage());
		}
		
	}
	
	/**
     * Generate JSON from multiple PeekData Objects.
   	 * 
   	 * Will read, parse and build JSON string using multiple items, JSON based on SocialPeek XML XSD.
   	 * 
     * @param dataIn the List of Data objects you want to build into JSON data,
     * @return the valid JSON String
     * @see Data
     * @see PeekData
     * @throws ParseException
     * 
     */
	public String generate(List<Data> dataIn) throws SocialPeekException {
		try {
			
			/* iterate through posts */
			List<JSONObject> dataArray = new ArrayList<JSONObject>();
			
			for(Data data : dataIn) {
			
				/* construct a JSON Object */
				JSONObject jsonObject = new JSONObject();
			
				/* create a good old fashioned collection */
				Map<String,String> dataCollection = new HashMap<String,String>();
				dataCollection.put("headline",data.getHeadline());
				dataCollection.put("body",data.getBody());
				dataCollection.put("link",data.getLink());
				dataCollection.put("date",this.sdf.format(data.getDate().getTime()));
			
				/* check for a photo, if so, add it as an attribute*/
				if(data.getUserProfilePhoto()!=null) {
					dataCollection.put("user_photo",data.getUserProfilePhoto());
				}
			
				/* add to our collection */
				jsonObject.put("post",dataCollection);
				dataArray.add(jsonObject);
			
			}
			
			/* construct one big jucy array */
			JSONArray array = new JSONArray(dataArray);
			return array.toString();
			
			
		} catch (JSONException e) {
			
			/* problem building JSONObject */
			throw new SocialPeekException("unable to build JSON Object : " + e.getMessage());
		}
	}

}
