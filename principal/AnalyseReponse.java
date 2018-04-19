package projetTutore;

import java.awt.List;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AnalyseReponse extends Arbre{


	Pattern pattern;
	Matcher matcher;
	private Arbre reponse;
	private String zone;
	private Arbre decomposition;


	/**
	 * 
	 */
	public AnalyseReponse() {
		Arbre reponse;
		String zone;
	}

	public AnalyseReponse(Arbre reponse) {
		super();
		this.reponse = reponse;
	}


	public AnalyseReponse(Arbre reponse, String zone) {
		super();
		this.reponse = reponse;
		this.zone = zone;
	}

	public Arbre getReponse() {
		return reponse;
	}

	public void setReponse(Arbre reponse) {
		this.reponse = reponse;
	}

	public String getZone() {
		return zone;
	}

	public void setZone(String zone) {
		this.zone = zone;
	}


	/**
	 * 
	 * @param requete
	 * @return la requete "nettoyer", sans double espace, sans \n
	 */
	public static String filtre(String requete) {
		System.out.println("Requete avant filtrage : "+requete);
		requete = requete.toUpperCase();
		requete = requete.replaceAll("[\r\n]+", "");
		requete = requete.replaceAll(" ( )+", " ");
		return requete;
	}


	public Arbre decomposeSelect(String partie,Arbre enCour) {
		Arbre arbre = new Arbre(SQL.SELECT);

		return arbre;
	}


	/**
	 * 
	 * @param requete
	 * @return vrai si la requete contient des agregats COUNT/AVG/MIN/MAX/SUM
	 */
	public boolean agregat(String requete) {
		String agregats = "( COUNT[(]| AVG( )*[(]| MIN[(]| MAX[(]| SUM[(])";
		/*
		*attention, regex a retravailler , truc du genre [,| ]COUNT([(.+)])[,| ]
		*/
		pattern = Pattern.compile(agregats);
		matcher = pattern.matcher(requete.toUpperCase());
		if(matcher.find()) {
			return true;
		}
		return false;
	}

	public boolean distinct(String requete) {
		pattern = Pattern.compile("DISTINCT");
		matcher = pattern.matcher(requete);
		if(matcher.find()) {
			return true;
		}
		return false;
	}

	public boolean estSelect(String requete) {
		pattern = Pattern.compile("SELECT (.)+");
		matcher = pattern.matcher(requete);
		if(matcher.find()) {
			return true;
		}
		return false;
	}


	/**
	 * 
	 * @param requete,arbre
	 * @return modifie l'arbre en parametre pour le completer
	 */
	public void analyseFrom(String requete, Arbre arbre) {
		String[] attribut;
		arbre.ajouter(SQL.FROM);
		requete = "T_eleve,T_prof,T_emploi";
		pattern = Pattern.compile(",");
		attribut = pattern.split(requete);
		for(int i=0;i<attribut.length;i++) {
			System.out.println(attribut[i]);
		}
		
		ArrayList<String> attributs = new ArrayList<>();
		for(int i=0;i<attribut.length;i++) {
			attributs.add(attribut[i]);
		}
		Collections.sort(attributs);
		arbre.ajouter(attributs.toString());
		System.out.println("Test tefr"+attributs);
		// reste a trier la liste, puis la mettre dans l'arbre
	}
	/**
	 * methode a applle si on rencontre des agregats
	 * @param requete
	 * @return l'arbre qui represente l'agragats
	 */
	public Arbre analyseAgregat(String requete) {
		Arbre arbre = new Arbre();
		String agregats = "(,COUNT[(](.+)[)]| COUNT[(.+)]| AVG( )*[(]| MIN[(]| MAX[(]| SUM[(])";
		requete = " eleve,COUNT(t_eleve),hju";
		pattern = Pattern.compile(agregats);
		matcher = pattern.matcher(requete);
		if(matcher.find()) {
			System.out.println("Il y a "+matcher.groupCount());
			System.out.println("match");
			System.out.println(matcher.group());//mettre dans une variable
			System.out.println("le contenue est : "+matcher.group(2));//dans une autre variables
		}
		
		//chercher regex dans variable 1
		//creer arbre avec variable 1 et 2
		System.out.println();
		return arbre;
	}

	/**
	 * si on recontre un dictinct
	 * @param requete
	 * @return
	 */
	public Arbre analyseDistinct(String requete) {
		/*
		 * le fils gauche du parent est SQL.DISTINCT
		 * on traite le fils droit
		 */
		Arbre arbre = new Arbre();
		return arbre;
	}
	
	/*
	*split le while, detection AS, jointure / operateur / comparateur / mot cle SQL
	*
	*/
	public Arbre decomposeArbre(String requete) {
		Arbre decomposition = new Arbre();
		requete = "SELECT t-eleve,al^pha.t-prof, COUNT(rotie)      \n FROM TABLE WHILE eleve = 2";
		requete = filtre(requete);
		System.out.println("La requete analysée est : "+requete);

		pattern = Pattern.compile("SELECT (.+)FROM (.+)");
		matcher = pattern.matcher(requete);
		if(matcher.find()) {
			System.out.println("SELECT détecter");
			/**
			 * La requete commence donc par un SELECT
			 * On verifie si il existe une fonction d'agregats
			 */
			decomposition=new Arbre(SQL.SELECT);
			String selectFrom=matcher.group(1);
			String apresFrom=matcher.group(2);
			System.out.println("SELECT ... FROM : "+selectFrom);
			System.out.println("FROM ... = "+apresFrom);
			String tabAttribut[];
			pattern = Pattern.compile(",");
			tabAttribut = pattern.split(selectFrom);
			ArrayList<String> arrayAttributs = new ArrayList<>();
			System.out.println("Les differents attibuts :");
			for(int i=0;i<tabAttribut.length;i++) {
				System.out.println(tabAttribut[i]);
			}
			for(int i=0;i<tabAttribut.length;i++) {
				arrayAttributs.add(tabAttribut[i]);
			}
			/*
			 * ici on a mis tout les attributs dans un tableau, maintenant on va separer ce tableau si il contient des agregats ou distinct
			 */
			ArrayList<String> tabMotCle = new ArrayList<>();
			Boolean tabMotCleExiste = false;
			if(agregat(selectFrom) || distinct(selectFrom)) {
				//il y a des agregat/distinct, on separe la liste en 2 :
				//une liste de mot cle type SUM, COUNT ... et une liste d'attibut
				tabMotCleExiste = true;
				for(int i=0;i<arrayAttributs.size();i++) {
					if(distinct(arrayAttributs.get(i)) || agregat(arrayAttributs.get(i))){
						tabMotCle.add(arrayAttributs.get(i));
						arrayAttributs.remove(i);
					}
				}
			}
			
			/*
			 * on a maintenant deux liste :
			 * (tabMotCle si il y a des agregats) et arrayAttribut
			 * on va les trier et les ajouter a l'arbre
			 */
			Collections.sort(arrayAttributs);
			if(tabMotCleExiste) {
				Collections.sort(tabMotCle);
				tabMotCle.addAll(arrayAttributs);
			}


			System.out.println("Les tables sont :");
			System.out.println(tabMotCle);
			System.out.println(arrayAttributs);
			
			System.out.println("Et donc :");
			if(tabMotCleExiste) {
				System.out.println(tabMotCle);
				decomposition.ajouter(tabMotCle.toString());
			}else {
				System.out.println(arrayAttributs);
				decomposition.ajouter(arrayAttributs.toString());
			}
			
			System.out.println(decomposition);
			System.out.println("apres analyse du from :");
			analyseFrom(apresFrom, decomposition);
			System.out.println(decomposition);
			/*
			 * il reste a associer les attributs a leurs table
			 */
			
			
			
			
			/*
			 * 
			 * while, on split, puis on recupere les operations, les comparaison, et les jointure
			 */
		}else {
			/**
			 * La requete n'est pas un SELECT, testons les autres possibilité
			 */
			pattern = Pattern.compile("INSERT INTO (.+)");
			matcher = pattern.matcher(requete);
			if(matcher.find()) {
				System.out.println("INSERT INTO");
				/*
				 * la requete commence par INSERT INTO
				 */

			}else {
				pattern = Pattern.compile("DELETE FROM (.+)");
				matcher = pattern.matcher(requete);
				if(matcher.find()) {
					System.out.println("DELETE FROM ...");
					/*
					 * DELETE FROM ... http://sql.sh/cours/delete
					 */

				}else {
					pattern = Pattern.compile("UPDATE(.+)");
					matcher = pattern.matcher(requete);
					if(matcher.find()) {
						System.out.println("UPDATE");
						/*
						 * http://sql.sh/cours/update
						 */

					}
				}
			}
		}
		return decomposition;
	}
}
