package uk.co.mccann.socialpeek.interfaces;

import java.util.Calendar;

public interface Data {
	
	public void setHeadline(String headline);
	
	public void setBody(String body);
	
	public void setDate(Calendar date);
	
	public void setLink(String link);
	
	public void setUser(String user);
	
	public void setUserProfilePhoto(String photo);
	
	public String getHeadline();
	
	public String getBody();
	
	public Calendar getDate();
	
	public String getLink();
	
	public String getUser();
	
	public String getUserProfilePhoto();
	

}
