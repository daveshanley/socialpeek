package uk.co.mccann.socialpeek.model;

import java.text.SimpleDateFormat;
import java.util.Random;

public abstract class AbstractParser {
	
	protected SocialService service;
	protected Random random;
	
	
	public AbstractParser() { }
	
	public AbstractParser(SocialService service) {
	
		this.service = service;
	}
	
	public void setSocialService(SocialService service) {
		
		this.service = service;
	}
	
	protected SocialService getSocialService() {
		
		return this.service;
	}
	

}
