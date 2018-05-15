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
	
	
	public ArrayList<Question> exercice;
    public int nbQuestions;
	public String titre;
	public String initialisation;
	public String preambule;
	public int numQuestion;
	
	/**
     * Constructeur d'un exo ou on demande un titre et une initialisation
     */
    public Exercice() {
        exercice = new ArrayList<Question>();
    }
    
    
    /**
	 * Constructeur d'un exo ou on demande un titre et une initialisation
	 * @param titre
	 * @param init ==> c'est l'ensemble des requetes SQL pour "creer" l'environnement de travail de l'eleve
	 */
	public Exercice(String titre,String init) {
        this();
        this.titre = titre;
		this.initialisation = init;
	}
	
	
	/**
	 * Surcharge du constructeur de Exercie, avec un preambule en plus
	 * @param titre
	 * @param init
	 * @param preambule ===> le "contexte" de l'exercie
	 * @exemple Pour preambule : "Nous souhaitons traiter une base de donnee des regions des pays..."
	 */
	public Exercice(String titre, String init, String preambule) {
		this(titre, init);
        this.preambule = preambule;
	}
	
	/**
	 * Surcharge de constructeur : ici on prendre seulement le titre, on completeras a l'aide des setX par la suite,
	 * utilise si l'on souhaite avoir un code clair et plus lisible...
	 * @param titre ===> titre de l'Exercie
	 */
	public Exercice(String titre) {
		this();
        this.titre = titre;
	}
	
    /* Getter & Setter */
    public ArrayList<Question> getQuestions() {
        return exercice;
    }
    public String getTitre() {
        return titre;
    }
    public void setTitre(String t) {
        titre = t;
    }
    public String getInitialisation() {
        return initialisation;
    }
    public void setInitialisation(String init) {
        initialisation = init;
    }
    public String getPreambule() {
        return preambule;
    }
    public void setPreambule(String prea) {
        preambule = prea;
    }
    public int getNbQuestions() {
        return nbQuestions;
    }
    
    /**
     * Méthode ajoutant une question à la liste attribut d'un exercices
     * @param question 
     */
	public void ajouterQuestion(Question question) {
        nbQuestions++;
        this.exercice.add(question);
	}
    
    /**
     * Méthode ajoutant une question à la liste attribut d'un exercices
     * @param question 
     */
	public void supprimerQuestion(int indice) {
        nbQuestions--;
        this.exercice.remove(indice);
	}
    /**
     * Méthode ajoutant une question à la liste attribut d'un exercices à la position index
     * @param index
     * @param question 
     */
	public void ajouterQuestion(int index, Question question) {
		nbQuestions++;
        this.exercice.add(index, question);
	}
	/**
	 * Methode pour rentrer le fichier source (ensemble de requete SQL) dans la variable this.initialisation
	 * !!! la methode a l'air efficace : fichier de plus de 1000 ligne charge instantanement
	 * Effectuer des tests de performances plus pouss� !
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
	
	public String toString() {
		int tailleExo = this.exercice.size();
		String chaine = ">>> Péambule : \n\n" + this.preambule + "\n";
		//chaine = chaine + this.initialisation+"\n";
		for(int i = 0; i < tailleExo; i++) {
			chaine = chaine+"\n>>> Question n°" + (i + 1) + " :\n";
			chaine = chaine+this.exercice.get(i).toString();
		}
		return chaine;
	}
}
















