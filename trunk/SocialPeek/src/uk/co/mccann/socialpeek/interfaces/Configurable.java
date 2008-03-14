package uk.co.mccann.socialpeek.interfaces;

import java.util.Calendar;
import java.util.List;
import uk.co.mccann.socialpeek.model.SocialService;

public interface Configurable {

	public void setFeedType(int returnType);
	
	public int getFeedType();
	
	public void registerService(SocialService service);
	
	public void setHistoricalStartPoint(Calendar cal);
	
	public Calendar getHistoricalStartPoint();
	
	public void setHistoricalEndPoint(Calendar cal);
	
	public Calendar getHistoricalEndPoint();

	public List<SocialService> getRegisteredServices();
	
}
