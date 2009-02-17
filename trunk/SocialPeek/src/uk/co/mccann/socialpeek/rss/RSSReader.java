package uk.co.mccann.socialpeek.rss;

import java.net.MalformedURLException;
import java.net.URL;

import com.sun.cnpi.rss.elements.Channel;
import com.sun.cnpi.rss.elements.Rss;
import com.sun.cnpi.rss.parser.RssParser;
import com.sun.cnpi.rss.parser.RssParserFactory;

/**
 * <b>RSSReader</b><br/>
 * Parses an RSS Feed and writes to file
 *
 * <h4>Copyright and License</h4>
 * This code is copyright (c) McCann Erickson Advertising Ltd, 2008 except where
* otherwise stated. It is released as
* open-source under the Creative Commons NC-SA license. See
* <a href="http://creativecommons.org/licenses/by-nc-sa/2.5/">http://creativecommons.org/licenses/by-nc-sa/2.5/</a>
* for license details. This code comes with no warranty or support.
 *
 * @author Lewis Taylor <lewis.taylor@europe.mccann.com>
 */
public class RSSReader {
	
	private URL url;
	
	public RSSReader(){
		super();
		url = null;
	}
	
	public void setURL(String url){
		try {
			setURL(new URL(url));
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
	}
	
	public void setURL(URL url){
		this.url = url;
	}
	
	public Channel parseFeed() throws Exception {
	    		
	    RssParser parser = RssParserFactory.createDefault();
	    Rss rss = parser.parse(url);
	 
	    return rss.getChannel();
	}

}