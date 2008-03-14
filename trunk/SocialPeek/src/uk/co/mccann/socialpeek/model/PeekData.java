package uk.co.mccann.socialpeek.model;

import java.io.StringWriter;
import java.util.Calendar;

import uk.co.mccann.socialpeek.interfaces.Data;

public class PeekData implements Data {
	
	private String headline,body,link,user,userProfilePhoto;
	private Calendar date;
	
	public String getBody() {
		return this.body;
	}

	public Calendar getDate() {
		return this.date;
	}

	public String getHeadline() {
		return this.headline;
	}

	public String getLink() {
		return this.link;
	}

	public String getUser() {
		return this.user;
	}

	public String getUserProfilePhoto() {
		return this.userProfilePhoto;
	}

	public void setBody(String body) {
		this.body = body;
	}

	public void setDate(Calendar date) {
		this.date = date;
	}

	public void setHeadline(String headline) {
		this.headline = headline;
	}

	public void setLink(String link) {
		this.link = link;

	}

	public void setUser(String user) {
		this.user = user;

	}

	public void setUserProfilePhoto(String photo) {
		this.userProfilePhoto = photo;

	}
	
	public String toString() {
		
		StringWriter writer = new StringWriter();
		writer.append("_headline : " + this.getHeadline());
		writer.append("'n_link : " + this.getLink());
		writer.append("\n_user : " + this.getUser());
		writer.append("\n_date : " + this.getDate().getTime());
		writer.append("\n_userPhoto : " + this.getUserProfilePhoto());
		writer.append("\n_body : " + this.getBody());
		
		return writer.toString();
		
	}

}
