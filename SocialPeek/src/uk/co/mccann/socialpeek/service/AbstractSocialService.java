package uk.co.mccann.socialpeek.service;

import java.util.Calendar;
import java.util.List;

import uk.co.mccann.socialpeek.exceptions.ParseException;
import uk.co.mccann.socialpeek.exceptions.SocialPeekException;
import uk.co.mccann.socialpeek.generator.GeneratorFactory;
import uk.co.mccann.socialpeek.interfaces.Configurable;
import uk.co.mccann.socialpeek.interfaces.Data;
import uk.co.mccann.socialpeek.interfaces.Generator;
import uk.co.mccann.socialpeek.interfaces.Parser;
import uk.co.mccann.socialpeek.model.SocialService;

/**
 * <b>AbstractSocialService</b><br/>
 * All services should subclass this class.
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
public abstract class AbstractSocialService implements SocialService {
	
	protected Parser parser;
	protected GeneratorFactory genFactory;
	protected Configurable configuration;
	protected String username, password, apiKey;
	protected Data randomPeek, userPeek;
	protected List<Data> randomPeekList, userPeekList;
	
	protected Parser getParser() {
		return this.parser;
	}

	protected void setParser(Parser parser) {
		this.parser = parser;
	}
	
	protected GeneratorFactory getGeneratorFactory() {
		return this.genFactory;
	}

	public void registerGeneratorFactory(GeneratorFactory factory) {
		this.genFactory = factory;
	}
	
	public void registerConfiguration(Configurable config) {
		this.configuration = config;
	}
	
	protected Configurable getConfiguration() {
		return this.configuration;
	}
	
	public Calendar getHistoricalEndPoint() {
		return this.getConfiguration().getHistoricalEndPoint();
	}

	public Calendar getHistoricalStartPoint() {
		return this.getConfiguration().getHistoricalStartPoint();
	}
	
	public String getAPIKey() {
		return this.apiKey;
	}

	public String getPassword() {
		return this.password;
	}

	public String getUsername() {
		return this.username;
	}

	public void setAPIKey(String key) {
		this.apiKey = key;
		
	}

	public void setPassword(String password) {
		this.password = password;
		
	}

	public void setUsername(String username) {
		this.username = username;
		
	}
	
	public AbstractSocialService() { }
	
	public AbstractSocialService(Parser parser) {
		
		/* set our parser up */
		this.parser = parser;
	
	}
	
	public String getRandomPeek() throws SocialPeekException {
		
		/* check user has added account details */
		if(this.getUsername() != null && this.getPassword() != null) {
		
			try {
		
				this.randomPeek = this.parser.getSingleItem();
				Generator generator = this.genFactory.getGenerator();
				return generator.generate(this.randomPeek);
			
			} catch (ParseException exp) {
			
				throw new SocialPeekException("parsing exception occured : " + exp.getMessage());
			}
		
		} else {
			
			throw new SocialPeekException("unable to use service without authentication credentials");
		}
		
	}

	public String getRandomPeek(int limit) throws SocialPeekException {
		
		/* check user has added account details */
		if(this.getUsername() != null && this.getPassword() != null) {
			
		
			try {
		
				this.randomPeekList = this.parser.getMultipleItems(limit);
				Generator generator = this.genFactory.getGenerator();
				return generator.generate(this.randomPeekList);
			
			} catch (ParseException exp) {
				
				throw new SocialPeekException("parsing exception occured : " + exp.getMessage());
			}
		
		} else {
			
			throw new SocialPeekException("unable to use service without authentication credentials");
			
		}
	}

	public String getUserPeek(int userId) throws SocialPeekException {
		
		/* check user has added account details */
		if(this.getUsername() != null && this.getPassword() != null) {
			
			try {
			
				this.userPeek = this.parser.getSingleUserItem(userId);
				Generator generator = this.genFactory.getGenerator();
				return generator.generate(this.userPeek);
			
			} catch (ParseException exp) {
				
				throw new SocialPeekException("parsing exception occured : " + exp.getMessage());
			
			}
		
		} else {
			
			throw new SocialPeekException("unable to use service without authentication credentials");
			
		}
		
	}

	public String getUserPeek(String userId) throws SocialPeekException {
		
		/* check user has added account details */
		if(this.getUsername() != null && this.getPassword() != null) {
			
			try {
			
				this.userPeek = this.parser.getSingleUserItem(userId);
				Generator generator = this.genFactory.getGenerator();
				return generator.generate(this.userPeek);
			
			} catch (ParseException exp) {
				
				throw new SocialPeekException("parsing exception occured : " + exp.getMessage());
			
			}
		
		} else {
			
			throw new SocialPeekException("unable to use service without authentication credentials");
			
		}
		
		
	}

	public String getUserPeek(int userId, int limit) throws SocialPeekException {
		
		/* check user has added account details */
		if(this.getUsername() != null && this.getPassword() != null) {
			
			try {
			
				this.userPeekList = this.parser.getMultipleUserItems(userId, limit);
				Generator generator = this.genFactory.getGenerator();
				return generator.generate(this.userPeekList);
			
			} catch (ParseException exp) {
				
				throw new SocialPeekException("parsing exception occured : " + exp.getMessage());
			
			}
		
		} else {
			
			throw new SocialPeekException("unable to use service without authentication credentials");
			
		}
	}

	public String getUserPeek(String userId, int limit) throws SocialPeekException {
		
		
		/* check user has added account details */
		if(this.getUsername() != null && this.getPassword() != null) {
			
			try {
			
				this.userPeekList = this.parser.getMultipleUserItems(userId, limit);
				Generator generator = this.genFactory.getGenerator();
				return generator.generate(this.userPeekList);
			
			} catch (ParseException exp) {
				
				throw new SocialPeekException("parsing exception occured : " + exp.getMessage());
			
			}
		
		} else {
			
			throw new SocialPeekException("unable to use service without authentication credentials");
			
		}
		
		
	}

	public Class getServiceClass() {
		return this.getClass();
	}

	public Data getRawDataRandomPeek() throws SocialPeekException {
		
		/* check user has added account details */
		if(this.getUsername() != null && this.getPassword() != null) {
			
			try {
			
				return this.parser.getSingleItem();
				 
			} catch (ParseException exp) {
				
				throw new SocialPeekException("parsing exception occured : " + exp.getMessage());
			
			}
		
		} else {
			
			throw new SocialPeekException("unable to use service without authentication credentials");
			
		}
		
	}

	public List<Data> getRawDataRandomPeek(int limit) throws SocialPeekException {
		
		/* check user has added account details */
		if(this.getUsername() != null && this.getPassword() != null) {
			
			try {
			
				return this.parser.getMultipleItems(limit);
				 
			} catch (ParseException exp) {
				
				throw new SocialPeekException("parsing exception occured : " + exp.getMessage());
			
			}
		
		} else {
			
			throw new SocialPeekException("unable to use service without authentication credentials");
			
		}
		
	}

	public Data getRawDataUserPeek(int userId) throws SocialPeekException {
		
		/* check user has added account details */
		if(this.getUsername() != null && this.getPassword() != null) {
			
			try {
			
				return this.parser.getSingleUserItem(userId);
				 
			} catch (ParseException exp) {
				
				throw new SocialPeekException("parsing exception occured : " + exp.getMessage());
			
			}
		
		} else {
			
			throw new SocialPeekException("unable to use service without authentication credentials");
			
		}
		
		
	}

	public Data getRawDataUserPeek(String userId) throws SocialPeekException {
		
		/* check user has added account details */
		if(this.getUsername() != null && this.getPassword() != null) {
			
			try {
			
				return this.parser.getSingleUserItem(userId);
				 
			} catch (ParseException exp) {
				
				throw new SocialPeekException("parsing exception occured : " + exp.getMessage());
			
			}
		
		} else {
			
			throw new SocialPeekException("unable to use service without authentication credentials");
			
		}
	}

	public List<Data> getRawDataUserPeek(int userId, int limit) throws SocialPeekException {
		
		/* check user has added account details */
		if(this.getUsername() != null && this.getPassword() != null) {
			
			try {
			
				return this.parser.getMultipleUserItems(userId, limit);
				 
			} catch (ParseException exp) {
				
				throw new SocialPeekException("parsing exception occured : " + exp.getMessage());
			
			}
		
		} else {
			
			throw new SocialPeekException("unable to use service without authentication credentials");
			
		}
	}

	public List<Data> getRawDataUserPeek(String userId, int limit) throws SocialPeekException {
		
		/* check user has added account details */
		if(this.getUsername() != null && this.getPassword() != null) {
			
			try {
			
				return this.parser.getMultipleUserItems(userId, limit);
				 
			} catch (ParseException exp) {
				
				throw new SocialPeekException("parsing exception occured : " + exp.getMessage());
			
			}
		
		} else {
			
			throw new SocialPeekException("unable to use service without authentication credentials");
			
		}
	}

	public String getLatestUserPeek(String userId) throws SocialPeekException {
		
		/* check user has added account details */
		if(this.getUsername() != null && this.getPassword() != null) {
			
			try {
			
				this.userPeek = this.parser.getLatestSingleUserItem(userId);
				Generator generator = this.genFactory.getGenerator();
				return generator.generate(this.randomPeek);
				 
			} catch (ParseException exp) {
				
				throw new SocialPeekException("parsing exception occured : " + exp.getMessage());
			
			}
		
		} else {
			
			throw new SocialPeekException("unable to use service without authentication credentials");
			
		}
		
		
	}

	public String getLatestUserPeek(int userId, int limit) throws SocialPeekException {
		
		/* check user has added account details */
		if(this.getUsername() != null && this.getPassword() != null) {
			
			try {
			
				this.userPeekList = this.parser.getLatestMultipleUserItems(userId, limit);
				Generator generator = this.genFactory.getGenerator();
				return generator.generate(this.randomPeek);
				 
			} catch (ParseException exp) {
				
				throw new SocialPeekException("parsing exception occured : " + exp.getMessage());
			
			}
		
		} else {
			
			throw new SocialPeekException("unable to use service without authentication credentials");
			
		}
		
	}

	public String getLatestUserPeek(String userId, int limit) throws SocialPeekException {
		
		/* check user has added account details */
		if(this.getUsername() != null && this.getPassword() != null) {
			
			try {
			
				this.userPeekList = this.parser.getLatestMultipleUserItems(userId, limit);
				Generator generator = this.genFactory.getGenerator();
				return generator.generate(this.randomPeek);
				 
			} catch (ParseException exp) {
				
				throw new SocialPeekException("parsing exception occured : " + exp.getMessage());
			
			}
		
		} else {
			
			throw new SocialPeekException("unable to use service without authentication credentials");
			
		}
	}
	
	public String getMultiplePeekUsingTag(String tag, int limit) throws SocialPeekException {
		
		/* check user has added account details */
		if(this.getUsername() != null && this.getPassword() != null) {
			
			try {
			
				this.userPeekList = this.parser.getMultipleKeywordItems(tag, limit);
				Generator generator = this.genFactory.getGenerator();
				return generator.generate(this.userPeekList);
				 
			} catch (ParseException exp) {
				
				throw new SocialPeekException("parsing exception occured : " + exp.getMessage());
			
			}
		
		} else {
			
			throw new SocialPeekException("unable to use service without authentication credentials");
			
		}
	}

	public String getMultiplePeekUsingTags(String[] tags, int limit) throws SocialPeekException {
		
		/* check user has added account details */
		if(this.getUsername() != null && this.getPassword() != null) {
			
			try {
			
				this.userPeekList = this.parser.getMultipleKeywordItems(tags, limit);
				Generator generator = this.genFactory.getGenerator();
				return generator.generate(this.userPeekList);
				 
			} catch (ParseException exp) {
				
				throw new SocialPeekException("parsing exception occured : " + exp.getMessage());
			
			}
		
		} else {
			
			throw new SocialPeekException("unable to use service without authentication credentials");
			
		}
		
		
	}

	public String getRandomPeekUsingTag(String tag) throws SocialPeekException {
		
		/* check user has added account details */
		if(this.getUsername() != null && this.getPassword() != null) {
			
			try {
			
				this.userPeek = this.parser.getKeywordItem(tag);
				Generator generator = this.genFactory.getGenerator();
				return generator.generate(this.userPeek);
				 
			} catch (ParseException exp) {
				
				throw new SocialPeekException("parsing exception occured : " + exp.getMessage());
			
			}
		
		} else {
			
			throw new SocialPeekException("unable to use service without authentication credentials");
			
		}
		
	}

	public String getRandomPeekUsingTags(String[] tags) throws SocialPeekException {
		
		/* check user has added account details */
		if(this.getUsername() != null && this.getPassword() != null) {
			
			try {
			
				this.userPeek = this.parser.getKeywordItem(tags);
				Generator generator = this.genFactory.getGenerator();
				return generator.generate(this.userPeek);
				 
			} catch (ParseException exp) {
				
				throw new SocialPeekException("parsing exception occured : " + exp.getMessage());
			
			}
		
		} else {
			
			throw new SocialPeekException("unable to use service without authentication credentials");
			
		}
	
	}

	public List<Data> getRawMultiplePeekUsingTag(String tag, int limit) throws SocialPeekException {
		
		/* check user has added account details */
		if(this.getUsername() != null && this.getPassword() != null) {
			
			try {
			
				this.userPeekList = this.parser.getMultipleKeywordItems(tag, limit);
				return userPeekList;
				 
			} catch (ParseException exp) {
				
				throw new SocialPeekException("parsing exception occured : " + exp.getMessage());
			
			}
		
		} else {
			
			throw new SocialPeekException("unable to use service without authentication credentials");
			
		}
		
	}

	public List<Data> getRawMultiplePeekUsingTags(String[] tags, int limit) throws SocialPeekException {
		
		/* check user has added account details */
		if(this.getUsername() != null && this.getPassword() != null) {
			
			try {
			
				this.userPeekList = this.parser.getMultipleKeywordItems(tags, limit);
				return this.userPeekList;
				 
			} catch (ParseException exp) {
				
				throw new SocialPeekException("parsing exception occured : " + exp.getMessage());
			
			}
		
		} else {
			
			throw new SocialPeekException("unable to use service without authentication credentials");
			
		}
	}

	public Data getRawRandomPeekUsingTag(String tag) throws SocialPeekException {
		
		/* check user has added account details */
		if(this.getUsername() != null && this.getPassword() != null) {
			
			try {
			
				this.userPeek= this.parser.getKeywordItem(tag);
				return this.userPeek;
				 
			} catch (ParseException exp) {
				
				throw new SocialPeekException("parsing exception occured : " + exp.getMessage());
			
			}
		
		} else {
			
			throw new SocialPeekException("unable to use service without authentication credentials");
			
		}
		
	}

	public Data getRawRandomPeekUsingTags(String[] tags) throws SocialPeekException {
		
		/* check user has added account details */
		if(this.getUsername() != null && this.getPassword() != null) {
			
			try {
			
				this.userPeek= this.parser.getKeywordItem(tags);
				return this.userPeek;
				 
			} catch (ParseException exp) {
				
				throw new SocialPeekException("parsing exception occured : " + exp.getMessage());
			
			}
		
		} else {
			
			throw new SocialPeekException("unable to use service without authentication credentials");
			
		}
	}
	
}
