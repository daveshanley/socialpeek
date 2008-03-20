package uk.co.mccann.socialpeek.test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringWriter;
import java.net.MalformedURLException;
import java.net.URL;

import static org.junit.Assert.fail;

import org.apache.xalan.xsltc.runtime.Node;
import org.apache.xerces.parsers.DOMParser;
import org.junit.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import uk.co.mccann.socialpeek.service.WeFeelFineService;

public class URLTest {
	
	
	
	@Test public void urlTestConnect() {
		
		//URL wwfURL;
		
		try {
			
		
        DOMParser parser = new DOMParser();
		parser.parse(WeFeelFineService.API_URL + "ShowFeelings?limit=10&feeling=panic&display=xml&returnfields=feeling,gender,sentence,imageid,posturl,posttime,postdate,country,state,city");
			
		Document node = parser.getDocument();
			
		System.out.println ("Node Type:" + node.getNodeType()  + "\nNode Name:" + node.getNodeName());
			
		if(node.hasChildNodes()) {
			
			if(node.getFirstChild().getFirstChild() != null) { 
				NodeList children = node.getFirstChild().getChildNodes();
				
				for(int x = 0; x < children.getLength(); x++) {
					NamedNodeMap map = children.item(x).getAttributes();
					if(map!=null) {
					
					System.out.print("name: " + children.item(x).getNodeName());
					
						System.out.println(" -- sentence: " + map.getNamedItem("sentence").getTextContent());
					}
					
					// + " -- sentence: " + map.getNamedItem("sentence").getTextContent()
				}
				
			}
			
				
		}
		
        
		
        
        
        
		} catch (Exception e) { 
			e.printStackTrace();
		}
		
	}
	
}
