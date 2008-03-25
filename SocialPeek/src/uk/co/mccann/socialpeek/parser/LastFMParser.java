package uk.co.mccann.socialpeek.parser;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import org.apache.xerces.parsers.DOMParser;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import uk.co.mccann.socialpeek.exceptions.ParseException;
import uk.co.mccann.socialpeek.interfaces.Data;
import uk.co.mccann.socialpeek.interfaces.Parser;
import uk.co.mccann.socialpeek.model.LastFMRecentTrack;
import uk.co.mccann.socialpeek.model.LastFMTrack;
import uk.co.mccann.socialpeek.model.LastFMUser;
import uk.co.mccann.socialpeek.model.PeekData;
import uk.co.mccann.socialpeek.service.LastFMService;
import uk.co.mccann.socialpeek.service.WeFeelFineService;

/**
* <b>LastFMParser</b><br/>
* extract and process data from audio scrobbler web services
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
public class LastFMParser extends AbstractParser implements Parser {
	
	private SimpleDateFormat lastfmDateFormat;
	
	private LastFMTrack extractTrackFromChart() throws ParseException {
	
		List<LastFMTrack> trackList = extractTrackFromChart(50);
		if(trackList.size()>0) {
			/* return random track */
			return trackList.get(this.random.nextInt(trackList.size()-1));
		} else {
			return null;
		}
		
	}
	
	private List<LastFMTrack> extractTrackFromChart(int limit) throws ParseException {
		
		try {
			DOMParser parser = new DOMParser();
			/* parse the data! after all that formatting */
			parser.parse(LastFMService.CURRENT_CHART);
			
			List<LastFMTrack> trackArray = new ArrayList<LastFMTrack>();
			
			/* get DOM doc from parsed remote URL */
			Document node = parser.getDocument();
			
			/* first node should always be 'toptracks' */
			if (node.hasChildNodes()) {
				
				/* get feelings node, and all feeling children */
				if (node.getFirstChild().getFirstChild() != null) {
					
					/* all children */
					NodeList children = node.getFirstChild().getChildNodes();
					
					for (int x = 0; x < children.getLength(); x++) {
						
						LastFMTrack trackObject = new LastFMTrack();
						
						/* track */
						Node track = children.item(x);
						
						/* attributes */
						NamedNodeMap map = track.getAttributes();
						if(map !=null) {
							//System.out.println("Track Name: " + map.getNamedItem("name").getTextContent());
							trackObject.setName(map.getNamedItem("name").getTextContent());
						}
						
						/* look at the track */
						if(track.hasChildNodes()) {
							
							NodeList trackNodes = track.getChildNodes();
							
							for (int y = 0; y < trackNodes.getLength(); y++) {
								
								Node trackItem = trackNodes.item(y);
								if(!trackItem.getNodeName().equals("#text")) {
									
									if(trackItem.getNodeName().equals("artist")) {
										NamedNodeMap artistmap = trackItem.getAttributes();
										//System.out.println(" - Artist: " + artistmap.getNamedItem("name").getTextContent());
										trackObject.setArtist(artistmap.getNamedItem("name").getTextContent());
									} else {
										if(trackItem.getNodeName().equals("url")) trackObject.setUrl(trackItem.getChildNodes().item(0).getNodeValue());
										if(trackItem.getNodeName().equals("thumbnail")) trackObject.setThumbnail(trackItem.getChildNodes().item(0).getNodeValue());
										if(trackItem.getNodeName().equals("image")) trackObject.setImage(trackItem.getChildNodes().item(0).getNodeValue());
										//System.out.println(" - " + trackItem.getNodeName() + " = " + trackItem.getChildNodes().item(0).getNodeValue());
									}
									
									trackArray.add(trackObject);
								}
							}
						}
						
					}

				}
			
			}
			
			/* shuffle charts */
			Collections.shuffle(trackArray);
			
			/* truncate list to limit size */
			List<LastFMTrack> compactedTrackList = new ArrayList<LastFMTrack>();
			if(limit > trackArray.size()) limit = trackArray.size();
			int counter = 0;
			for(LastFMTrack track : trackArray) {
				if(counter < limit) {
					compactedTrackList.add(track);
				} else {
					break;
				}
			}
			return compactedTrackList;
			
		
		} catch (Exception exp) {
			exp.printStackTrace();
			throw new ParseException("unable to parse LastFM XML: " + exp.getMessage());
		}
		
		
	}
	
	private LastFMUser extractFanFromArtist(LastFMTrack track) throws ParseException {
		
		List<LastFMUser> userList = this.extractFanFromArtist(track, 50);
		if(userList.size()>0) {
			/* return random user */
			return userList.get(this.random.nextInt(userList.size()-1));
		} else {
			return null;
		}
	}
		
	
	private List<LastFMUser> extractFanFromArtist(LastFMTrack track, int limit) throws ParseException {
		
		try {
			
			DOMParser parser = new DOMParser();
			/* parse the data! after all that formatting */
			parser.parse(LastFMService.ARTIST_API + track.getArtist().replace(" ", "+") + "/fans.xml");
			
			List<LastFMUser> userArray = new ArrayList<LastFMUser>();
			
			/* get DOM doc from parsed remote URL */
			Document node = parser.getDocument();
			
			/* first node should always be 'fans' */
			if (node.hasChildNodes()) {
				
				/* get feelings node, and all feeling children */
				if (node.getFirstChild().getFirstChild() != null) {
					
					/* all children */
					NodeList children = node.getFirstChild().getChildNodes();
					
					for (int x = 0; x < children.getLength(); x++) {
						
						LastFMUser userObject = new LastFMUser();
						
						/* fans */
						Node fans = children.item(x);
						
						/* attributes */
						NamedNodeMap map = fans.getAttributes();
						if(map !=null) {
							//System.out.println("User Name: " + map.getNamedItem("username").getTextContent());
							userObject.setUsername(map.getNamedItem("username").getTextContent());
						}
						
						/* look at the user */
						if(fans.hasChildNodes()) {
							
							NodeList userNodes = fans.getChildNodes();
							
							for (int y = 0; y < userNodes.getLength(); y++) {
								
								Node userItem = userNodes.item(y);
								if(!userItem.getNodeName().equals("#text")) {
									if(userItem.getNodeName().equals("image")) userObject.setImage(userItem.getChildNodes().item(0).getNodeValue());
									//System.out.println(" - " + userItem.getNodeName() + " = " + userItem.getChildNodes().item(0).getNodeValue());
									userArray.add(userObject);
								}
							}
						}
						
					}

				}
			
			}
			
			/* shuffle charts */
			Collections.shuffle(userArray);
			
			/* truncate list to limit size */
			List<LastFMUser> compactedUserList = new ArrayList<LastFMUser>();
			if(limit > userArray.size()) limit = userArray.size();
			int counter = 0;
			for(LastFMUser user : userArray) {
				if(counter < limit) {
					compactedUserList.add(user);
				} else {
					break;
				}
			}
			return compactedUserList;
			
			
		
		} catch (Exception exp) {
			exp.printStackTrace();
			throw new ParseException("unable to parse LastFM XML: " + exp.getMessage());
		}
		
		
	}
	
	private LastFMRecentTrack extractRecentPlayFromFan(LastFMUser user) throws ParseException {
		List<LastFMRecentTrack> tracks = this.extractRecentPlayFromFan(user, 10);
		if(tracks.size() > 0){ 
			return tracks.get(0); // returns most recent play
		} else {
			return null;
		}
		
	}
	
	
	private List<LastFMRecentTrack> extractRecentPlayFromFan(LastFMUser user, int limit) throws ParseException {
		
		try {
			
			DOMParser parser = new DOMParser();
			/* parse the data! after all that formatting */
			parser.parse(LastFMService.USER_API + user.getUsername().replace(" ", "+") + "/recenttracks.xml");
			
			List<LastFMRecentTrack> trackArray = new ArrayList<LastFMRecentTrack>();
			
			/* get DOM doc from parsed remote URL */
			Document node = parser.getDocument();
			
			LastFMRecentTrack trackObject = new LastFMRecentTrack();
			
			/* attributes */
			NamedNodeMap mainmap = node.getFirstChild().getAttributes();
			if(mainmap !=null) {
				//System.out.println("User Name: " + mainmap.getNamedItem("user").getTextContent());
				trackObject.setUsername(mainmap.getNamedItem("user").getTextContent());
			}
			
			/* first node should always be 'recenttracks' */
			if (node.hasChildNodes()) {
				
				/* get feelings node, and all feeling children */
				if (node.getFirstChild().getFirstChild() != null) {
					
					/* all children */
					NodeList children = node.getFirstChild().getChildNodes();
					
					for (int x = 0; x < children.getLength(); x++) {
						
						/* track */
						Node track = children.item(x);
						
						/* look at the track */
						if(track.hasChildNodes()) {
							
							NodeList trackNodes = track.getChildNodes();
							
							for (int y = 0; y < trackNodes.getLength(); y++) {
								
								Node trackItem = trackNodes.item(y);
								if(!trackItem.getNodeName().equals("#text")) {
									if(trackItem.hasChildNodes()) {
									if(trackItem.getNodeName().equals("artist")) trackObject.setArtist(trackItem.getChildNodes().item(0).getNodeValue());
									if(trackItem.getNodeName().equals("name")) trackObject.setName(trackItem.getChildNodes().item(0).getNodeValue());
									if(trackItem.getNodeName().equals("url")) trackObject.setUrl(trackItem.getChildNodes().item(0).getNodeValue());
									if(trackItem.getNodeName().equals("album")) trackObject.setAlbum(trackItem.getChildNodes().item(0).getNodeValue());
									if(trackItem.getNodeName().equals("date")) trackObject.setPlayed(this.lastfmDateFormat.parse(trackItem.getChildNodes().item(0).getNodeValue()));
									//System.out.println(" - " + trackItem.getNodeName() + " = " + trackItem.getChildNodes().item(0).getNodeValue());
									
									
									trackArray.add(trackObject);
									}
								}
							}
						}
						
					}

				}
			
			}
			
			/* truncate list to limit size */
			List<LastFMRecentTrack> compactedTrackList = new ArrayList<LastFMRecentTrack>();
			if(limit > trackArray.size()) limit = trackArray.size();
			int counter = 0;
			for(LastFMRecentTrack track : trackArray) {
				if(counter < limit) {
					compactedTrackList.add(track);
				} else {
					break;
				}
			}
			return compactedTrackList;
			
		
		} catch (Exception exp) {
			exp.printStackTrace();
			throw new ParseException("unable to parse LastFM XML: " + exp.getMessage());
		}
		
		
	}
	
	private LastFMUser getFanProfile(String fan) throws ParseException {
		
		try {
			
			DOMParser parser = new DOMParser();
			/* parse the data! after all that formatting */
			parser.parse(LastFMService.USER_API + fan.replace(" ", "+") + "/profile.xml");
			
			/* get DOM doc from parsed remote URL */
			Document node = parser.getDocument();
			
			LastFMUser userObject = new LastFMUser();
			userObject.setUsername(fan);
			
			/* first node should always be 'fans' */
			if (node.hasChildNodes()) {
				
				/* get feelings node, and all feeling children */
				if (node.getFirstChild().getFirstChild() != null) {
					
					/* all children */
					NodeList children = node.getFirstChild().getChildNodes();
					
					for (int x = 0; x < children.getLength(); x++) {
						
						Node userItem = children.item(x);
						if(!userItem.getNodeName().equals("#text")) {
							if(userItem.getNodeName().equals("url")) userObject.setUrl(userItem.getChildNodes().item(0).getNodeValue());
							if(userItem.getNodeName().equals("realname")) userObject.setRealname(userItem.getChildNodes().item(0).getNodeValue());
							if(userItem.getNodeName().equals("age")) userObject.setAge(new Integer(userItem.getChildNodes().item(0).getNodeValue()));
							if(userItem.getNodeName().equals("gender")) userObject.setGender(userItem.getChildNodes().item(0).getNodeValue());
							if(userItem.getNodeName().equals("country")) userObject.setCountry(userItem.getChildNodes().item(0).getNodeValue());
							if(userItem.getNodeName().equals("avatar")) userObject.setImage(userItem.getChildNodes().item(0).getNodeValue());
							
						}
					}
				}
			}
			
			return userObject;
			
		} catch (Exception exp) {
			exp.printStackTrace();
			throw new ParseException("unable to parse LastFM XML: " + exp.getMessage());
		}
	}
	
	public Data getKeywordItem(String keyword) throws ParseException {
		// TODO Auto-generated method stub
		return null;
	}

	public Data getKeywordItem(String[] keywords) throws ParseException {
		// TODO Auto-generated method stub
		return null;
	}

	public List<Data> getLatestMultipleUserItems(int userId, int limit)
			throws ParseException {
		// TODO Auto-generated method stub
		return null;
	}

	public List<Data> getLatestMultipleUserItems(String userId, int limit)
			throws ParseException {
		// TODO Auto-generated method stub
		return null;
	}

	public Data getLatestSingleUserItem(int userId) throws ParseException {
		// TODO Auto-generated method stub
		return null;
	}

	public Data getLatestSingleUserItem(String userId) throws ParseException {
		// TODO Auto-generated method stub
		return null;
	}

	public List<Data> getMultipleItems(int limit) throws ParseException {
		// TODO Auto-generated method stub
		return null;
	}

	public List<Data> getMultipleKeywordItems(String keyword, int limit)
			throws ParseException {
		// TODO Auto-generated method stub
		return null;
	}

	public List<Data> getMultipleKeywordItems(String[] keywords, int limit)
			throws ParseException {
		// TODO Auto-generated method stub
		return null;
	}

	public List<Data> getMultipleUserItems(int userId, int limit)
			throws ParseException {
		// TODO Auto-generated method stub
		return null;
	}

	public List<Data> getMultipleUserItems(String userId, int limit) throws ParseException {
		
		/* get user profile */
		LastFMUser userProfile = this.getFanProfile(userId);
		List<LastFMRecentTrack> trackList = this.extractRecentPlayFromFan(userProfile, limit);
		
		List<Data> dataList = new ArrayList<Data>();
		for(LastFMRecentTrack track : trackList) {
			
			Data data = new PeekData();
			data.setHeadline(track.getName());
			String body = "<a href=\"http://www.last.fm/user/" + userProfile.getUsername().replaceAll(" ","+") + "\">" + userProfile.getUsername() + "</a> was listening to '<a href=\"" + userProfile.getUrl() + "\">" + track.getName() + "</a>', by <a href=\"http://www.last.fm/music/" + track.getArtist().replaceAll(" ","+") + "\">" + track.getArtist() + "</a>";
				   body += " on " + this.lastfmDateFormat.format(track.getPlayed());
			data.setBody(body);
			Calendar trackCal = Calendar.getInstance();
			trackCal.setTime(track.getPlayed());
			data.setDate(trackCal);
			data.setLink(track.getUrl());
			data.setUser(userProfile.getUsername());
			data.setUserProfilePhoto(userProfile.getImage());
			data.setLocation(userProfile.getImage());
			dataList.add(data);
		}
		
		return dataList;
	}

	public Data getSingleItem() throws ParseException {
		
		/* get a single track from the charts */
		LastFMTrack track = extractTrackFromChart();
		LastFMUser fan = extractFanFromArtist(track);
		LastFMRecentTrack recentTrack = extractRecentPlayFromFan(fan);
		
		Data data = new PeekData();
		data.setHeadline(recentTrack.getName());
		String body = "<a href=\"http://www.last.fm/user/" + fan.getUsername().replaceAll(" ","+") + "\">" + fan.getUsername() + "</a> was listening to '<a href=\"" + recentTrack.getUrl() + "\">" + recentTrack.getName() + "</a>', by <a href=\"http://www.last.fm/music/" + recentTrack.getArtist().replaceAll(" ","+") + "\">" + recentTrack.getArtist() + "</a>";
			   body += " on " + this.lastfmDateFormat.format(recentTrack.getPlayed());
		data.setBody(body);
		Calendar trackCal = Calendar.getInstance();
		trackCal.setTime(recentTrack.getPlayed());
		data.setDate(trackCal);
		data.setLink(recentTrack.getUrl());
		data.setUser(fan.getUsername());
		data.setUserProfilePhoto(fan.getImage());
		
		System.out.println(recentTrack);
		LastFMUser profile = this.getFanProfile(fan.getUsername());
		System.out.println(profile);
		
		return data;
	}

	public Data getSingleUserItem(int userId) throws ParseException {
		// TODO Auto-generated method stub
		return null;
	}

	public Data getSingleUserItem(String userId) throws ParseException {
		// TODO Auto-generated method stub
		return null;
	}

	public void setUpParser() {
		
		this.random = new Random();
		this.lastfmDateFormat = new SimpleDateFormat("dd MMM yyyy',' kk:mm");
	
	}

}
