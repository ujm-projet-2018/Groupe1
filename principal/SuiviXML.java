package projetTutore;

import java.io.File;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
 

/**
 * Gestion du suivi de l'eleve
 * @author madjid
 *
 */
public class SuiviXML {{
	try {
		 DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
	     DocumentBuilder docBuilder = dbFactory.newDocumentBuilder();
		}catch (ParserConfigurationException pce) {
	        pce.printStackTrace();
	    }
}}
