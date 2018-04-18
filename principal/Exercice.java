package projetTutore;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.io.StringWriter;
import java.util.ArrayList;


public class Exercice implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	
	public ArrayList<Question> exercice = new ArrayList<Question>();
	public String titre;
	public String initialisation;
	public String preambule;
	public int numQuestion;
	
	/**
	 * Constructeur d'un exo ou on demande un titre et une initialisation
	 * @param titre
	 * @param init ==> c'est l'ensemble des requetes SQL pour "creer" l'environnement de travail de l'eleve
	 */
	public Exercice(String titre,String init) {
		this.titre = titre;
		this.initialisation = init;
		this.numQuestion=0;
	}
	
	
	/**
	 * Surcharge du constructeur de Exercie, avec un preambule en plus
	 * @param titre
	 * @param init
	 * @param preambule ===> le "contexte" de l'exercie
	 * @exemple Pour preambule : "Nous souhaitons traiter une base de donnee des regions des pays..."
	 */
	public Exercice(String titre, String init, String preambule) {
		this.titre = titre;
		this.initialisation = init;
		this.preambule = preambule;
		this.numQuestion=0;
	}
	
	/**
	 * Surcharge de constructeur : ici on prendre seulement le titre, on completeras a l'aide des setX par la suite,
	 * utilise si l'on souhaite avoir un code clair et plus lisible...
	 * @param titre ===> titre de l'Exercie
	 */
	public Exercice(String titre) {
		this.titre = titre;
	}
	
	public void ajouterQuestion(Question question) {
		this.exercice.add(question);
	}
	
	public void setTitre(String titre) {
		this.titre = titre;
	}
	
	public void setPreambule(String preambule) {
		this.preambule=preambule;
	}
	
	
	/**
	 * Methode pour rentrer le fichier source (ensemble de requete SQL) dans la variable this.initialisation
	 * !!! la methode a l'air efficace : fichier de plus de 1000 ligne charge instantanement
	 * Effectuer des tests de performances plus poussé !
	 * @param source ===> fichier SQL
	 */
	public void setInit(File source) {
	    try {
	        BufferedInputStream in = new BufferedInputStream(new FileInputStream(source));
	        StringWriter out = new StringWriter();
	        int b;
	        while ((b=in.read()) != -1)
	            out.write(b);
	        out.flush();
	        out.close();
	        in.close();
	        this.initialisation = out.toString();
	     }
	     catch (IOException ie)
	     {
	          ie.printStackTrace(); 
	     }
		
	}
	
	public String getTitre() {
		return this.titre;
	}
	
	/**
	 * Methode d'export de l'exo, pour pouvoir etre distribue aux eleves
	 * @param nom ===> nom de l'export
	 */
	public void exportation(String nom) {
		try {
			FileOutputStream fs = new FileOutputStream(nom);
			ObjectOutputStream os = new ObjectOutputStream(fs);
			os.writeObject(this);
			os.close();
		} catch (Exception e) { 
			e.printStackTrace(); 
		}
	}
	
	public Exercice importation(String nom) {
		Exercice result = new Exercice("ERREUR");
	      try {
	          FileInputStream fis = new FileInputStream(nom);
	          ObjectInputStream ois = new ObjectInputStream(fis);
	          result = (Exercice) ois.readObject(); 
	          ois.close();
	          return result;
	       } catch (Exception e) { 
	          e.printStackTrace(); 
	       }
	      return result;	
	}
	
	/**
	 * 
	 * @return true sil'exo est fini
	 */
	public boolean finExo() {
		if(this.numQuestion==this.exercice.size()) {
			return true;
		}else {
			return false;
		}
	}
	
	
	/**
	 * Attention a d'abord appeler fin exo !
	 * @return la question suivante de l'exo
	 */
	public Question questionSuivante() {
		this.numQuestion++;
		return this.exercice.get(this.numQuestion);
	}
	
	/**
	 * Attention a ne pas appeler cette methode a la premiere question
	 * @return la question precedente
	 */
	public Question questionPrecedente() {
		this.numQuestion--;
		return this.exercice.get(numQuestion);
	}
	
	
	public String toString() {
		int tailleExo = this.exercice.size();
		String chaine = "Titre de l'exercice : "+this.titre+"\n";
		chaine = chaine + this.preambule+"\n";
		//chaine = chaine + this.initialisation+"\n";
		for(int i=0;i<tailleExo;i++) {
			chaine = chaine+"\n>>>Question "+i+" :\n";
			chaine = chaine+">>>"+this.exercice.get(i).toString();
		}
		return chaine;
	}
	
	
	
	
}
