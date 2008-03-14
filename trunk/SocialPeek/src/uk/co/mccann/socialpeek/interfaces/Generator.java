package uk.co.mccann.socialpeek.interfaces;

import java.util.List;

import uk.co.mccann.socialpeek.exceptions.SocialPeekException;


public interface Generator {
	
	public String generate(Data dataIn) throws SocialPeekException;
	
	public String generate(List<Data> dataIn) throws SocialPeekException;
	
	
}

