package uk.co.mccann.socialpeek.interfaces;

import java.util.Calendar;

import uk.co.mccann.socialpeek.SocialPeekConfiguration;
import uk.co.mccann.socialpeek.generator.GeneratorFactory;

public interface Service {
	
	public void registerGeneratorFactory(GeneratorFactory factory);
	
	public void registerConfiguration(Configurable configuration);
	
	public String getAPIKey();

	public String getPassword();

	public String getUsername();
	
	public void setAPIKey(String key);

	public void setPassword(String password);

	public void setUsername(String username);
	
	public Calendar getHistoricalEndPoint();

	public Calendar getHistoricalStartPoint();
	
}
