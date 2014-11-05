/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package securitySystem;

//import com.sun.org.apache.xml.internal.serialize.OutputFormat;
//import com.sun.org.apache.xml.internal.serialize.XMLSerializer;
//import com.sun.org.apache.xml.internal.serialize.*;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.xml.serialize.OutputFormat;
import org.apache.xml.serialize.XMLSerializer;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Text;


public class ToXML {
    List user;
    Document dom;
    
    public ToXML(UserInformation ui){ 
        
        user = new ArrayList();
        loadData(ui);
        createDocument();
        createDOMTree();
        printToFile();
    }
    
    private void loadData(UserInformation ui){
        user.add(ui);
        
    }
    
    private void createDocument(){
              //get an instance of factory
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		try {
		//get an instance of builder
		DocumentBuilder db = dbf.newDocumentBuilder();

		//create an instance of DOM
		dom = db.newDocument();

		}catch(ParserConfigurationException pce) {
			//dump it
			System.out.println("Error while trying to instantiate DocumentBuilder " + pce);
			System.exit(1);
		}
    }
    private void createDOMTree(){

		//create the root element 
		Element rootEle = dom.createElement("Personnel");
		dom.appendChild(rootEle);

		//No enhanced for
		Iterator it  = user.iterator();
		while(it.hasNext()) {
			UserInformation b = (UserInformation)it.next();
			//For each Book object  create  element and attach it to root
			Element bookEle = createUserElement(b);
			rootEle.appendChild(bookEle);
		}
	}
    
         private Element createUserElement(UserInformation b){

		Element userEle = dom.createElement("User");
		//bookEle.setAttribute("", b.getSubject());

		//create author element and author text node and attach it to bookElement
		Element nameEle = dom.createElement("name");
		Text nameText = dom.createTextNode(b.getname());
		nameEle.appendChild(nameText);
		userEle.appendChild(nameEle);

		//create title element and title text node and attach it to bookElement
		Element emailEle = dom.createElement("email");
		Text emailText = dom.createTextNode(b.getemail());
		emailEle.appendChild(emailText);
		userEle.appendChild(emailEle);

		Element phoneEle = dom.createElement("phoneNumber");
		Text phoneText = dom.createTextNode(b.getphoneNumber());
		phoneEle.appendChild(phoneText);
		userEle.appendChild(phoneEle);

		Element passwordEle = dom.createElement("password");
		Text passwordText = dom.createTextNode(b.getpassword());
		passwordEle.appendChild(passwordText);
		userEle.appendChild(passwordEle);
                
                
		return userEle;
	}
         
        private void printToFile(){

		try
		{
			//print
			OutputFormat format = new OutputFormat(dom);
			format.setIndenting(true);

			//to generate output to console use this serializer
			//XMLSerializer serializer = new XMLSerializer(System.out, format);


			//to generate a file output use fileoutputstream instead of system.out
			XMLSerializer serializer = new XMLSerializer(
			new FileOutputStream(new File("settings.xml")), format);

			serializer.serialize(dom);

		} catch(IOException ie) {
		    ie.printStackTrace();
		}
	}         
}
