package projetTutore;


import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;

import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import javax.swing.JFrame;


public class Test {
	

	
	
	
	public static void main(String[] args) throws SQLException, InterruptedException, ScriptException {
		// TODO Auto-generated method stub
		
		/*
		Arbre a = new Arbre();
		Arbre droit = new Arbre("fils droit", null, null);
		Arbre gauche = new Arbre("fils gauche", null, null);
		a.setDroite(droit);
		a.setGauche(gauche);
		a.setElement("test");
		System.out.println(a.toString());
		*/
		
		AnalyseReponse analyseR = new AnalyseReponse();
		analyseR.decomposeArbre("");
		//System.out.println(analyseR.agregat("SELECT AVG     (televe, jena"));
		//analyseR.analyseFrom("t_eleve , t,prof");
		
		
		analyseR.analyseAgregat("");
		
		
		Arbre arbre = new Arbre("ts");
		//System.out.println(arbre);
		arbre.ajouter("1");
		arbre.ajouter("2");
		arbre.ajouter("3");
		arbre.ajouter("4");
		arbre.ajouter("5");
		arbre.ajouter("6");
		//System.out.println("<<<<< \n" +arbre);
		/*
	    Pattern pattern;
	    Matcher matcher;
		String agregats = "(.)+";
		pattern = Pattern.compile(agregats);
        matcher = pattern.matcher("salut");
        if(matcher.find()) {
        	System.out.println("trouve !!!");
        
        }
        */
        /**
         * ALGO D'ANALYSE
         * 
         * Passer la requete dans le filtre
         * Verifier si elle match avec les agregats
         * Si oui -> envoyer vers une methode de traitement specifique
         * Si non -> analyser ce qu'il y a avant le FROM
         */

	}

}
