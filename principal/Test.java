package projetTutore;


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


public class Test {
	public static InterfaceEtudiant ie ;
	public static GestionBDD bdd;
	
	
	
	public static void main(String[] args) throws SQLException, InterruptedException, ScriptException {
		// TODO Auto-generated method stub
		ie = new InterfaceEtudiant();
        ie.setVisible(true);
        bdd = new GestionBDD();
        AnalyseReponse analyseR = new AnalyseReponse();
        analyseR.compareReponse("SELECT g", "SELECT gr");
		/*
		Arbre a = new Arbre();
		Arbre droit = new Arbre("fils droit", null, null);
		Arbre gauche = new Arbre("fils gauche", null, null);
		a.setDroite(droit);
		a.setGauche(gauche);
		a.setElement("test");
		System.out.println(a.toString());
		*/
		
		
		analyseR.decomposeArbre("");
		//System.out.println(analyseR.agregat("SELECT AVG     (televe, jena"));
		//analyseR.analyseFrom("t_eleve , t,prof");
		boolean b = analyseR.opComparaison("SELECT nom_colonnes FROM nom_table WHERE condition1 A ND (condition2 O R condition3)");
		System.out.println(b);
		//analyseR.analyseAgregat("");
		
		
		Pattern pattern;
		Matcher matcher;
		String requete = "ele<15 AND eleve.ter = 52 AND elve_y.nom=prof.nom AND ele.ef=fg.gt";
		//pattern = Pattern.compile("((.+)[.](.+)[ ]*=[ ]*(.+¨ )[.](.+))");
		pattern = Pattern.compile("([a-zA-Z0-9_-]+)[.]([a-zA-Z0-9_-]+)=([a-zA-Z0-9_-]+)[.]([a-zA-Z0-9_-]+)");
		matcher = pattern.matcher(requete);
		if(matcher.find()) {
			String g1=matcher.group(1);
			String g2=matcher.group(2);
			String g3=matcher.group(3);
			String g4=matcher.group(4);
			String test = matcher.group();
			System.out.println(requete);
			requete = requete.replace(test, "");
			System.out.println(requete);
		}
		
		
		analyseR.analyseWhere("", new Arbre());
		
		Exercice exercice = new Exercice("test");
		exercice.getTitre();
		Question actuelle;
		
       // java.awt.EventQueue.invokeLater(new Runnable() {
          //  public void run() {
                
           // }
       // });
              

	}

}
