package uk.co.mccann.socialpeek.interfaces;

import uk.co.mccann.socialpeek.exceptions.SocialPeekException;
import uk.co.mccann.socialpeek.model.SocialService;

public interface PeekFactory {
	
	public SocialService getPeeker(Class<SocialService> service) throws SocialPeekException;
	
}
