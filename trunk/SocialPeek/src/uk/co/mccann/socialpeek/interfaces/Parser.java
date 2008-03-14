package uk.co.mccann.socialpeek.interfaces;

import java.util.List;

import uk.co.mccann.socialpeek.exceptions.ParseException;
import uk.co.mccann.socialpeek.model.SocialService;

public interface Parser {
	
	public void setSocialService(SocialService service);
	
	public void setUpParser();
	
	public Data getSingleItem() throws ParseException;
	
	public List<Data> getMultipleItems(int limit) throws ParseException;
	
	public Data getSingleUserItem(int userId) throws ParseException;
	
	public Data getSingleUserItem(String userId) throws ParseException;
	
	public Data getLatestSingleUserItem(int userId) throws ParseException;
	
	public Data getLatestSingleUserItem(String userId) throws ParseException;	
	
	public List<Data> getMultipleUserItems(int userId, int limit) throws ParseException;
	
	public List<Data> getMultipleUserItems(String userId, int limit) throws ParseException;
	
	public List<Data> getLatestMultipleUserItems(int userId, int limit) throws ParseException;
	
	public List<Data> getLatestMultipleUserItems(String userId, int limit) throws ParseException;
	
	public Data getKeywordItem(String keyword) throws ParseException;
	
	public List<Data> getMultipleKeywordItems(String keyword, int limit) throws ParseException;
	
	public List<Data> getMultipleKeywordItems(String[] keywords, int limit) throws ParseException;
		
}
