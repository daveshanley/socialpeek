package uk.co.mccann.socialpeek.generator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import uk.co.mccann.socialpeek.exceptions.ParseException;
import uk.co.mccann.socialpeek.interfaces.Data;


public class JSONGenerator extends AbstractGenerator {
	
	public JSONGenerator() {
		super();
	}
	
	public String generate(Data dataIn) throws ParseException {
		
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
			throw new ParseException("unable to build JSON Object : " + e.getMessage());
		}
		
	}

	public String generate(List<Data> dataIn) throws ParseException {
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
			throw new ParseException("unable to build JSON Object : " + e.getMessage());
		}
	}

}
