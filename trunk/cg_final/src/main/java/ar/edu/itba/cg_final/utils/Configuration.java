package ar.edu.itba.cg_final.utils;

import java.io.IOException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;

public class Configuration {

	// Vehicle Handling
	private int KEY_FORWARD, KEY_BACKWARD, KEY_LEFT, KEY_RIGHT, KEY_HORN;
	// Menu Handling
	private int KEY_MENU_UP, KEY_MENU_DOWN, KEY_MENU_LEFT, KEY_MENU_RIGHT, KEY_MENU_SELECT, KEY_MENU_BACK;
	// Game Handling
	private int KEY_PAUSE, KEY_AXIS, KEY_SCREENSHOT;

	public Configuration() {
		super();
		
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		
		try {
			DocumentBuilder db = dbf.newDocumentBuilder();
			
			Document dom = db.parse("conf.xml");
			
			//We obtain the base element.
			Element baseElem = dom.getDocumentElement();
			
		} catch (ParserConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SAXException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	
	
	
}
