package projetTutore;


import java.awt.List;
import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
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
	public static Element questionCourante;
	public static int nbEssais = 0;
	
	public static void xmlQuestionSuivante() {
		questionCourante = doc.createElement("Question");
        racine.appendChild(questionCourante);
 
        // attributs de l'�l�ment contact
        Attr attr = doc.createAttribute("id");
        attr.setValue(String.valueOf(exercice.numQuestion+1));
        questionCourante.setAttributeNode(attr);

	}
	/*
	 * 
	 * A faire :
	 * 

	 * 
	 * 
	 * 
	 * Gestion du WHERE
	 * 
	 * Faire la diapo
	 * 
	 * dans reponse xml --> mettre les question + reponse attendue
	 * 
	 * reponse equivalente = meme nombre de ligne et de colonne
	 * 
	 * 
	 * 
	 * 
	 */
	
	public static void xmlProposition(String proposition) {
        Element nom = doc.createElement("Proposition");
        nom.appendChild(doc.createTextNode(proposition));
        System.out.println(proposition);
        questionCourante.appendChild(nom);
	}
	
	
	public static void xmlFin() throws TransformerConfigurationException {
		TransformerFactory transformerFactory = TransformerFactory.newInstance();
        Transformer transformer = transformerFactory.newTransformer();
        DOMSource source = new DOMSource(doc);
        StreamResult resultat = new StreamResult(new File("resultat.xml"));
 
        try {
			transformer.transform(source, resultat);
			System.out.println("XML VALIDER----------------------------------------------------");
		} catch (TransformerException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}
	
	public static void main(String[] args) throws SQLException, InterruptedException, ScriptException, TransformerConfigurationException, ParserConfigurationException {
		// TODO Auto-generated method stub
		ie = new InterfaceEtudiant();
		ie.setVisible(true);
		bdd = new GestionBDD();
	    analyseR = new AnalyseReponse();
		
		
		exercice = new Exercice("Test");
		Question q1 = new Question("Donnez le nom des animaux", "SELECT nom FROM animal");
		exercice.ajouterQuestion(q1);
		Question q2 = new Question("Donnez le nom, la date de naissance et le sexe des animaux", "SELECT nom,date_naissance,sexe FROM animal");
		exercice.ajouterQuestion(q2);
		exercice.setPreambule("Ceci est un exercice de test");
		exercice.initialisation ="SET NAMES utf8;\r\n" + 
				"\r\n" + 
				"\r\n" + 
				"DROP TABLE IF EXISTS Animal;\r\n" + 
				"DROP TABLE IF EXISTS Race;\r\n" + 
				"DROP TABLE IF EXISTS Espece;\r\n" + 
				"\r\n" + 
				"\r\n" + 
				"CREATE TABLE Espece (\r\n" + 
				"  id smallint(6) unsigned NOT NULL AUTO_INCREMENT,\r\n" + 
				"  nom_courant varchar(40) NOT NULL,\r\n" + 
				"  nom_latin varchar(40) NOT NULL,\r\n" + 
				"  description text,\r\n" + 
				"  prix decimal(7,2) unsigned DEFAULT NULL,\r\n" + 
				"  PRIMARY KEY (id),\r\n" + 
				"  UNIQUE KEY nom_latin (nom_latin)\r\n" + 
				") ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=latin1;\r\n" + 
				"\r\n" + 
				"LOCK TABLES Espece WRITE;\r\n" + 
				"INSERT INTO Espece VALUES (1,'Chien','Canis canis','Bestiole � quatre pattes qui aime les caresses et tire souvent la langue',200.00),(2,'Chat','Felis silvestris','Bestiole � quatre pattes qui saute tr�s haut et grimpe aux arbres',150.00),(3,'Tortue d''Hermann','Testudo hermanni','Bestiole avec une carapace tr�s dure',140.00),\r\n" + 
				"(4,'Perroquet amazone','Alipiopsitta xanthops','Joli oiseau parleur vert et jaune',700.00),(5,'Rat brun','Rattus norvegicus','Petite bestiole avec de longues moustaches et une longue queue sans poils',10.00);\r\n" + 
				"UNLOCK TABLES;\r\n" + 
				"\r\n" + 
				"\r\n" + 
				"CREATE TABLE Race (\r\n" + 
				"  id smallint(6) unsigned NOT NULL AUTO_INCREMENT,\r\n" + 
				"  nom varchar(40) NOT NULL,\r\n" + 
				"  espece_id smallint(6) unsigned NOT NULL,\r\n" + 
				"  description text,\r\n" + 
				"  prix decimal(7,2) unsigned DEFAULT NULL,\r\n" + 
				"  PRIMARY KEY (id)\r\n" + 
				") ENGINE=InnoDB AUTO_INCREMENT=10 DEFAULT CHARSET=latin1;\r\n" + 
				"\r\n" + 
				"LOCK TABLES Race WRITE;\r\n" + 
				"INSERT INTO Race VALUES (1,'Berger allemand',1,'Chien sportif et �l�gant au pelage dense, noir-marron-fauve, noir ou gris.',485.00),(2,'Berger blanc suisse',1,'Petit chien au corps compact, avec des pattes courtes mais bien proportionn�es et au pelage tricolore ou bicolore.',935.00),(3,'Singapura',2,'Chat de petite taille aux grands yeux en amandes.',985.00),\r\n" + 
				"(4,'Bleu russe',2,'Chat aux yeux verts et � la robe �paisse et argent�e.',835.00),(5,'Maine coon',2,'Chat de grande taille, � poils mi-longs.',735.00),(7,'Sphynx',2,'Chat sans poils.',1235.00),\r\n" + 
				"(8,'Nebelung',2,'Chat bleu russe, mais avec des poils longs...',985.00),(9,'Rottweiller',1,'Chien d''apparence solide, bien muscl�, � la robe noire avec des taches feu bien d�limit�es.',600.00);\r\n" + 
				"UNLOCK TABLES;\r\n" + 
				"\r\n" + 
				"\r\n" + 
				"CREATE TABLE Animal (\r\n" + 
				"  id smallint(6) unsigned NOT NULL AUTO_INCREMENT,\r\n" + 
				"  sexe char(1) DEFAULT NULL,\r\n" + 
				"  date_naissance datetime NOT NULL,\r\n" + 
				"  nom varchar(30) DEFAULT NULL,\r\n" + 
				"  commentaires text,\r\n" + 
				"  espece_id smallint(6) unsigned NOT NULL,\r\n" + 
				"  race_id smallint(6) unsigned DEFAULT NULL,\r\n" + 
				"  mere_id smallint(6) unsigned DEFAULT NULL,\r\n" + 
				"  pere_id smallint(6) unsigned DEFAULT NULL,\r\n" + 
				"  PRIMARY KEY (id),\r\n" + 
				"  UNIQUE KEY ind_uni_nom_espece_id (nom,espece_id)\r\n" + 
				") ENGINE=InnoDB AUTO_INCREMENT=63 DEFAULT CHARSET=utf8;\r\n" + 
				"\r\n" + 
				"LOCK TABLES Animal WRITE;\r\n" + 
				"INSERT INTO Animal VALUES (1,'M','2010-04-05 13:43:00','Rox','Mordille beaucoup',1,1,18,22),(2,NULL,'2010-03-24 02:23:00','Roucky',NULL,2,NULL,40,30),(3,'F','2010-09-13 15:02:00','Schtroumpfette',NULL,2,4,41,31),\r\n" + 
				"(4,'F','2009-08-03 05:12:00',NULL,'Bestiole avec une carapace tr�s dure',3,NULL,NULL,NULL),(5,NULL,'2010-10-03 16:44:00','Choupi','N� sans oreille gauche',2,NULL,NULL,NULL),(6,'F','2009-06-13 08:17:00','Bobosse','Carapace bizarre',3,NULL,NULL,NULL),\r\n" + 
				"(7,'F','2008-12-06 05:18:00','Caroline',NULL,1,2,NULL,NULL),(8,'M','2008-09-11 15:38:00','Bagherra',NULL,2,5,NULL,NULL),(9,NULL,'2010-08-23 05:18:00',NULL,'Bestiole avec une carapace tr�s dure',3,NULL,NULL,NULL),\r\n" + 
				"(10,'M','2010-07-21 15:41:00','Bobo',NULL,1,NULL,7,21),(11,'F','2008-02-20 15:45:00','Canaille',NULL,1,NULL,NULL,NULL),(12,'F','2009-05-26 08:54:00','Cali',NULL,1,2,NULL,NULL),\r\n" + 
				"(13,'F','2007-04-24 12:54:00','Rouquine',NULL,1,1,NULL,NULL),(14,'F','2009-05-26 08:56:00','Fila',NULL,1,2,NULL,NULL),(15,'F','2008-02-20 15:47:00','Anya',NULL,1,NULL,NULL,NULL),\r\n" + 
				"(16,'F','2009-05-26 08:50:00','Louya',NULL,1,NULL,NULL,NULL),(17,'F','2008-03-10 13:45:00','Welva',NULL,1,NULL,NULL,NULL),(18,'F','2007-04-24 12:59:00','Zira',NULL,1,1,NULL,NULL),\r\n" + 
				"(19,'F','2009-05-26 09:02:00','Java',NULL,1,2,NULL,NULL),(20,'M','2007-04-24 12:45:00','Balou',NULL,1,1,NULL,NULL),(21,'F','2008-03-10 13:43:00','Pataude',NULL,1,NULL,NULL,NULL),\r\n" + 
				"(22,'M','2007-04-24 12:42:00','Bouli',NULL,1,1,NULL,NULL),(24,'M','2007-04-12 05:23:00','Cartouche',NULL,1,NULL,NULL,NULL),(25,'M','2006-05-14 15:50:00','Zambo',NULL,1,1,NULL,NULL),\r\n" + 
				"(26,'M','2006-05-14 15:48:00','Samba',NULL,1,1,NULL,NULL),(27,'M','2008-03-10 13:40:00','Moka',NULL,1,NULL,NULL,NULL),(28,'M','2006-05-14 15:40:00','Pilou',NULL,1,1,NULL,NULL),\r\n" + 
				"(29,'M','2009-05-14 06:30:00','Fiero',NULL,2,3,NULL,NULL),(30,'M','2007-03-12 12:05:00','Zonko',NULL,2,5,NULL,NULL),(31,'M','2008-02-20 15:45:00','Filou',NULL,2,4,NULL,NULL),\r\n" + 
				"(32,'M','2009-07-26 11:52:00','Spoutnik',NULL,3,NULL,52,NULL),(33,'M','2006-05-19 16:17:00','Caribou',NULL,2,4,NULL,NULL),(34,'M','2008-04-20 03:22:00','Capou',NULL,2,5,NULL,NULL),\r\n" + 
				"(35,'M','2006-05-19 16:56:00','Raccou','Pas de queue depuis la naissance',2,4,NULL,NULL),(36,'M','2009-05-14 06:42:00','Boucan',NULL,2,3,NULL,NULL),(37,'F','2006-05-19 16:06:00','Callune',NULL,2,8,NULL,NULL),\r\n" + 
				"(38,'F','2009-05-14 06:45:00','Boule',NULL,2,3,NULL,NULL),(39,'F','2008-04-20 03:26:00','Zara',NULL,2,5,NULL,NULL),(40,'F','2007-03-12 12:00:00','Milla',NULL,2,5,NULL,NULL),\r\n" + 
				"(41,'F','2006-05-19 15:59:00','Feta',NULL,2,4,NULL,NULL),(42,'F','2008-04-20 03:20:00','Bilba','Sourde de l''oreille droite � 80%',2,5,NULL,NULL),(43,'F','2007-03-12 11:54:00','Cracotte',NULL,2,5,NULL,NULL),\r\n" + 
				"(44,'F','2006-05-19 16:16:00','Cawette',NULL,2,8,NULL,NULL),(45,'F','2007-04-01 18:17:00','Nikki','Bestiole avec une carapace tr�s dure',3,NULL,NULL,NULL),(46,'F','2009-03-24 08:23:00','Tortilla','Bestiole avec une carapace tr�s dure',3,NULL,NULL,NULL),\r\n" + 
				"(47,'F','2009-03-26 01:24:00','Scroupy','Bestiole avec une carapace tr�s dure',3,NULL,NULL,NULL),(48,'F','2006-03-15 14:56:00','Lulla','Bestiole avec une carapace tr�s dure',3,NULL,NULL,NULL),(49,'F','2008-03-15 12:02:00','Dana','Bestiole avec une carapace tr�s dure',3,NULL,NULL,NULL),\r\n" + 
				"(50,'F','2009-05-25 19:57:00','Cheli','Bestiole avec une carapace tr�s dure',3,NULL,NULL,NULL),(51,'F','2007-04-01 03:54:00','Chicaca','Bestiole avec une carapace tr�s dure',3,NULL,NULL,NULL),(52,'F','2006-03-15 14:26:00','Redbul','Insomniaque',3,NULL,NULL,NULL),\r\n" + 
				"(54,'M','2008-03-16 08:20:00','Bubulle','Bestiole avec une carapace tr�s dure',3,NULL,NULL,NULL),(55,'M','2008-03-15 18:45:00','Relou','Surpoids',3,NULL,NULL,NULL),(56,'M','2009-05-25 18:54:00','Bulbizard','Bestiole avec une carapace tr�s dure',3,NULL,NULL,NULL),\r\n" + 
				"(57,'M','2007-03-04 19:36:00','Safran','Coco veut un g�teau !',4,NULL,NULL,NULL),(58,'M','2008-02-20 02:50:00','Gingko','Coco veut un g�teau !',4,NULL,NULL,NULL),(59,'M','2009-03-26 08:28:00','Bavard','Coco veut un g�teau !',4,NULL,NULL,NULL),\r\n" + 
				"(60,'F','2009-03-26 07:55:00','Parlotte','Coco veut un g�teau !',4,NULL,NULL,NULL),(61,'M','2010-11-09 00:00:00','Yoda',NULL,2,5,NULL,NULL),(62,'M','2010-11-05 00:00:00','Pipo',NULL,1,9,NULL,NULL);\r\n" + 
				"UNLOCK TABLES;\r\n" + 
				"\r\n" + 
				"\r\n" + 
				"ALTER TABLE Race ADD CONSTRAINT fk_race_espece_id FOREIGN KEY (espece_id) REFERENCES Espece (id) ON DELETE CASCADE;\r\n" + 
				"\r\n" + 
				"ALTER TABLE Animal ADD CONSTRAINT fk_race_id FOREIGN KEY (race_id) REFERENCES Race (id) ON DELETE SET NULL;\r\n" + 
				"ALTER TABLE Animal ADD CONSTRAINT fk_espece_id FOREIGN KEY (espece_id) REFERENCES Espece (id);\r\n" + 
				"ALTER TABLE Animal ADD CONSTRAINT fk_mere_id FOREIGN KEY (mere_id) REFERENCES Animal (id) ON DELETE SET NULL;\r\n" + 
				"ALTER TABLE Animal ADD CONSTRAINT fk_pere_id FOREIGN KEY (pere_id) REFERENCES Animal (id) ON DELETE SET NULL;";
		/*
		 * 
		 * 
		 * 
		 * 
		 * 
		 * DANS INITIALISATION IL FAUT METTRE CE QUE L'ON A POUR INIT LA TABLE
		 * 
		 */
		String query = exercice.initialisation;
		ArrayList<String> arrayQuery = new ArrayList<>();
		query.replaceAll("\n|\r", "");
		ArrayList<String> list = new ArrayList<String>(Arrays.asList(query.split(";")));
		for(int i=0;i<list.size();i++) {
			bdd.reqSQL(list.get(i), 'm');
		}
		//ResultSet initRS = bdd.reqSQL(query, 'm');
		
		docBuilder = dbFactory.newDocumentBuilder();
		doc = docBuilder.newDocument();
		racine = doc.createElement("Exercice");
		doc.appendChild(racine);
		
        questionCourante = doc.createElement("Question");
        racine.appendChild(questionCourante);
 
        // attributs de l'�l�ment contact
        Attr attr = doc.createAttribute("id");
        attr.setValue(String.valueOf(exercice.numQuestion+1));
        questionCourante.setAttributeNode(attr);
        
        

		
		
		
		///////////////////////////////////////

		
	
		
		try {
			System.out.println("test");
			bdd.infoBase();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
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
