package projetTutore;


import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import javax.swing.JFrame;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

public class Test {
	public static InterfaceEtudiant ie ;
	public static GestionBDD bdd;
	public static AnalyseReponse analyseR;
	public static Exercice exercice;
	public static DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
	public static DocumentBuilder docBuilder ;
	public static Document doc;
	public static Element racine;
	
	public static void xmlQuestionSuivante() {
		Element contact = doc.createElement("Question");
        racine.appendChild(contact);
 
        // attributs de l'élément contact
        Attr attr = doc.createAttribute("id");
        attr.setValue(String.valueOf(exercice.numQuestion+1));
        contact.setAttributeNode(attr);
	}
	
	public static void xmlFin() throws TransformerConfigurationException {
        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        Transformer transformer = transformerFactory.newTransformer();
        DOMSource source = new DOMSource(doc);
        StreamResult resultat = new StreamResult(new File("resultat.xml"));
 
        try {
			transformer.transform(source, resultat);
		} catch (TransformerException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}
	
	public static void main(String[] args) throws SQLException, InterruptedException, ScriptException, TransformerConfigurationException, ParserConfigurationException {
		// TODO Auto-generated method stub
		exercice = new Exercice("Test");
		Question q1 = new Question("Donnez le nom des animaux", "SELECT nom FROM animal");
		exercice.ajouterQuestion(q1);
		Question q2 = new Question("Donnez le nom, la date de naissance et le sexe des animaux", "SELECT nom,date_naissance,sexe FROM animal");
		exercice.ajouterQuestion(q2);
		
		
		
		
		docBuilder = dbFactory.newDocumentBuilder();
		doc = docBuilder.newDocument();
		racine = doc.createElement("Exercice");
		doc.appendChild(racine);
		
        Element contact = doc.createElement("Question");
        racine.appendChild(contact);
 
        // attributs de l'élément contact
        Attr attr = doc.createAttribute("id");
        attr.setValue(String.valueOf(exercice.numQuestion+1));
        contact.setAttributeNode(attr);
        
        

		
		
		
		///////////////////////////////////////

		ie = new InterfaceEtudiant();
		ie.setVisible(true);
		bdd = new GestionBDD();
	    analyseR = new AnalyseReponse();
	    
		String enonce = Test.exercice.exercice.get(Test.exercice.numQuestion).enonce;
		ie.ecrireEnonce(enonce);
		
		try {
		//boolean essai = analyseR.compareReponse("SELECT eleve,prof,classe FROM prof  ", "SELECT eleve FROM eleve");
		//System.out.println(essai);
		}catch(java.lang.IndexOutOfBoundsException e) {
			System.out.println("Une erreur s'est produite...");
			/*
			 * 
			 * erreur IndexOutOfBound
			 */
		}
	}

}
