package securitySystem;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class ParseXML {
    
	Document dom;
        UserInformation user;

	public ParseXML(){
            run();
        }
	public void run() {
		
		//parse the xml file and get the dom object
		parseXmlFile();		
		//get each employee element and create a Employee object
		parseDocument();	
	}
	
	
	private void parseXmlFile(){
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		try {
			DocumentBuilder db = dbf.newDocumentBuilder();
			dom = db.parse("settings.xml");
                        System.out.println(dom);
		}catch(ParserConfigurationException pce) {
			pce.printStackTrace();
		}catch(SAXException se) {
			se.printStackTrace();
		}catch(IOException ioe) {
			ioe.printStackTrace();
		}
	}

	
	private void parseDocument(){
		//get the root elememt
		Element docEle = dom.getDocumentElement();
                System.out.println(docEle);
		//get a nodelist of <employee> elements
		NodeList nl = docEle.getElementsByTagName("User");
                //Element el = (Element)nl.item(0);
                //System.out.println(el);
                if(nl != null && nl.getLength() > 0) {
			for(int i = 0 ; i < nl.getLength();i++) {

				//get the employee element
				Element el = (Element)nl.item(i);

				//get the UserInformation object
				 user = getUser(el);

			}
		}
	}


	/**
	 * I take an employee element and read the values in, create
	 * an Employee object and return it
	 * @param empEl
	 * @return
	 */
	
        private UserInformation getUser(Element empEl) {
		
		//for each <employee> element get text or int values of 
		//name ,id, age and name
		String name = getTextValue(empEl,"name");
                System.out.println(name);
		String email = getTextValue(empEl,"email");
		String phoneNumber = getTextValue(empEl,"phoneNumber");
                String password = getTextValue(empEl,"password");
		
                String type = empEl.getAttribute("type");
		//Create a new Employee with the value read from the xml nodes
		UserInformation ui = new UserInformation(name, phoneNumber, email, password);
                
		return ui;
	}


	/**
	 * I take a xml element and the tag name, look for the tag and get
	 * the text content 
	 * i.e for <employee><name>John</name></employee> xml snippet if
	 * the Element points to employee node and tagName is name I will return John  
	 * @param ele
	 * @param tagName
	 * @return
	 */
	private String getTextValue(Element ele, String tagName) {
		String textVal = null;
		NodeList nl = ele.getElementsByTagName(tagName);
		if(nl != null && nl.getLength() > 0) {
			Element el = (Element)nl.item(0);
			textVal = el.getFirstChild().getNodeValue();
		}

		return textVal;
	}
        
        public UserInformation getXMLUser(){return user;}
}