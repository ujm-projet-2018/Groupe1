package projetTutore;

import java.awt.List;
import java.lang.reflect.Array;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.JOptionPane;

import com.mysql.jdbc.SQLError;
import com.mysql.jdbc.exceptions.jdbc4.MySQLSyntaxErrorException;

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
		String agregats = "[,| |, ]COUNT[(](.+)[)]"
				+ "|[,| |, ]AVG[ ]*[(](.+)[)]"//il peut y avoir des espaces entre le AVG et sa parenthese, pas pour les autres agregats
				+ "|[,| |, ]MIN[(](.+)[)]"
				+ "|[,| |, ]MAX[(](.+)[)]"
				+ "|[,| |, ]SUM[(](.+)[)]";
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
		//requete = "T_eleve,T_prof,T_emploi";
		/*
		 * on cherche a savoir si il y a un WHERE
		 */
		pattern = Pattern.compile(" WHERE ");
		matcher = pattern.matcher(requete);
		if(matcher.find()) {
			System.out.println("Il y a un WHERE");
			pattern = Pattern.compile("(.+) WHERE(.+)");
			matcher = pattern.matcher(requete);
			if(matcher.find()) {
				String avantWhere,apresWhere;
				avantWhere=matcher.group(1);
				apresWhere=matcher.group(2);
				System.out.println("avantWhere : "+avantWhere+
						"\n"
						+ "apresWhere : "+apresWhere);


				pattern = Pattern.compile(",");
				attribut = pattern.split(avantWhere);
				for(int i=0;i<attribut.length;i++) {
					System.out.println(attribut[i]);
				}

				ArrayList<String> attributs = new ArrayList<>();
				for(int i=0;i<attribut.length;i++) {
					attributs.add(attribut[i]);
				}
				Collections.sort(attributs);
				arbre.ajouter(attributs.toString());
				System.out.println("Voila donc les elements du FROM : "+attributs);
				System.out.println("Nous allons maintenant appeler analyseWhere...");
				analyseWhere(apresWhere, arbre);
			}
		}else {
			System.out.println("Il n'y a pas de WHERE");

			String avantWhere = requete;
			pattern = Pattern.compile(",");
			attribut = pattern.split(avantWhere);
			for(int i=0;i<attribut.length;i++) {
				System.out.println(attribut[i]);
			}

			ArrayList<String> attributs = new ArrayList<>();
			for(int i=0;i<attribut.length;i++) {
				attributs.add(attribut[i]);
			}
			Collections.sort(attributs);
			arbre.ajouter(attributs.toString());
			System.out.println("Voila donc les elements du FROM : "+attributs);
			System.out.println("Il n'y a pas de where");

			//prendre en charge les GROUP BY, HAVING, ORDER BY
		}
	}



	public boolean opComparaison(String requete) {
		String agregats = "[ ]*=[ ]*"
				+ "|[ ]*<>[ ]*"
				+ "|[ ]*!=[ ]*"
				+ "|[ ]*<[ ]*"
				+ "|[ ]*>[ ]*"
				+ "|[ ]*>=[ ]*"
				+ "|[ ]*<=[ ]*"
				+ "|[ ]*IN[ ]*"
				+ "|[ ]*BETWEEN[ ]*"
				+ "|[ ]*LIKE[ ]*"
				+ "|[ ]*IS NULL[ ]*"
				+ "|[ ]*IS NOT NULL[ ]*"
				+ "|[ ]*AND[ ]*"
				+ "|[ ]*OR[ ]*"
				+ "|[,| |, ]MAX[(](.+)[)]";
		pattern = Pattern.compile(agregats);
		matcher = pattern.matcher(requete);
		if(matcher.find()) {
			return true;
		}
		return false;
	}



	public void analyseWhere(String requete, Arbre arbre) {

		//requete=" t_zzzlev.elev=t_prof.prof AND t_en.pro=tgh.tf AND id<2";
		System.out.println("(where) La requete analysée est : "+requete);
		arbre.ajouter(SQL.WHERE);
		ArrayList<String> jointure = new ArrayList<>();

		/*
		 * on va d'abord chercher si il existe des jointures
		 * donc de la forme XXX.XXX = XXX.XXX
		 * 
		 */
		pattern = Pattern.compile("([a-zA-Z0-9_-]+)[.]([a-zA-Z0-9_-]+)=([a-zA-Z0-9_-]+)[.]([a-zA-Z0-9_-]+)");
		/*
		 * regex dessus permet de reconnaitre tout mot SANS ESPACE compose de lettre 
		 * de chiffre ou de - et _
		 */
		matcher = pattern.matcher(requete);
		while(matcher.find()) {
			String test = matcher.group();
			jointure.add(test);
			System.out.println("La jointures detectee est : "+test);
			System.out.println("La requete est : "+requete);
			requete = requete.replace(test, "");
			System.out.println("La requete nettoye est : "+requete);
		}
		System.out.println("La liste des jointures est : "+jointure.toString());
		Collections.sort(jointure);
		System.out.println("La liste des jointures est : "+jointure.toString());
		/*
		 * on a maintenant toutes les jointures de la requete dans une liste
		 * on les ajoute a l'arbre
		 */
		arbre.ajouter(SQL.JOINTURE);
		arbre.ajouter(jointure.toString());
		/*
		 * on ajoute directement la liste
		 * on pourra tres facilement comparer leurs taille et savoir si il manque des jointures
		 * ou pas 
		 */


		/*
		 * on teste si il reste des choses dans requete
		 */
		//requete = filtre(requete);
		System.out.println("etat de la requete traitee : "+requete);
		//	pattern = Pattern.compile("[ ]*AND[ ]*");

		pattern = Pattern.compile("([a-zA-Z0-9_+-]+)"
				+ "[ ]*"
				+ "([=|<>|!=|<|>|<=|>=])"
				+ "[ ]*"
				+ "([a-zA-Z0-9_+-]+)");
		matcher = pattern.matcher(requete);
		while(matcher.find()) {
			System.out.println("Operateur de comparaison trouvee !"
					+ "\n >>> "+matcher.group());
			System.out.println("Le group 1 est : "+matcher.group(1)+" l'operateur est "+ matcher.group(2)+",et le groupe 3 est "+matcher.group(3));
			String operateur = matcher.group(2);
			String p1 = matcher.group(1);
			String p2 = matcher.group(3);
			String p = matcher.group();
			/*
			 * on a trouve une operation de comparaison
			 * on ajoute dans l'arbre et on supprime
			 */
			arbre.ajouter(SQL.COMPARAISON);
			ArrayList<String> temp = new ArrayList<>();
			switch(operateur) {
			case(SQL.EGAL):
				arbre.ajouter(SQL.EGAL);
			temp.add(p1);
			temp.add(p2);
			arbre.ajouter(temp.toString());
			break;
			case(SQL.DIFF1):
				temp.add(p1);
			temp.add(p2);
			Collections.sort(temp);
			arbre.ajouter(SQL.DIFF1);
			arbre.ajouter(temp.toString());
			break;
			case(SQL.DIFF2):
				temp.add(p1);
			temp.add(p2);
			Collections.sort(temp);
			arbre.ajouter(SQL.DIFF1);
			arbre.ajouter(temp.toString());
			break;
			case(SQL.SUP):
				temp.add(p1);
			temp.add(p2);
			arbre.ajouter(SQL.SUP);
			arbre.ajouter(temp.toString());
			break;
			case(SQL.INF):
				temp.add(p1);
			temp.add(p2);
			arbre.ajouter(SQL.INF);
			arbre.ajouter(temp.toString());
			break;
			case(SQL.SUP_EGAL):
				temp.add(p1);
			temp.add(p2);
			arbre.ajouter(SQL.SUP_EGAL);
			arbre.ajouter(temp.toString());
			break;
			case(SQL.INF_EGAL):
				temp.add(p1);
			temp.add(p2);
			arbre.ajouter(SQL.INF_EGAL);
			arbre.ajouter(temp.toString());
			break;
			}
			requete = requete.replace(p,"");
			System.out.println("etat de la requete : "+ requete);

			/*
			 * 
			 * si il ne reste que des AND, c'est fini
			 * 
			 * a faire : traitement des OR et LIKE et IN
			 * 
			 */
		}
	}


	/**
	 * methode a applle si on rencontre des agregats
	 * @param requete
	 * @return l'arbre qui represente l'agragats
	 */
	public Arbre analyseAgregat(String requete) {
		Arbre arbre = new Arbre();
		String agregats = "[,| |, ]COUNT[(](.+)[)]"
				+ "|[,| |, ]AVG[ ]*[(](.+)[)]"//il peut y avoir des espaces entre le AVG et sa parenthese, pas pour les autres agregats
				+ "|[,| |, ]MIN[(](.+)[)]"
				+ "|[,| |, ]MAX[(](.+)[)]"
				+ "|[,| |, ]SUM[(](.+)[)]";
		requete = "SELECT *,MIN(t_eleve),hju";
		pattern = Pattern.compile(agregats);
		matcher = pattern.matcher(requete);
		if(matcher.find()) {
			//System.out.println("Il y a "+matcher.groupCount());
			System.out.println("match");
			System.out.println("'test)"+matcher.group());
			System.out.println(">> "+matcher.group(1));//mettre dans une variable
			System.out.println("le contenue est : "+matcher.group(2));//dans une autre variables
		}

		//chercher regex dans variable 1
		//creer arbre avec variable 1 et 2
		//System.out.println();
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
	public Arbre decomposeArbre(String requete) {
		Arbre decomposition = new Arbre();
		System.out.println("La requete analysee par moi est "+requete);
		//requete = "SELECT t-eleve,alpha.t-prof,COUNT(rotie)      \n FROM TABLE,eleve WHERE eleve = 2 AND prof=7 AND prof.nom=eleve.nom";// WHERE eleve = 2
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
				System.out.println("()tadAtt "+tabAttribut[i]);
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
				System.out.println("Il y a un agregat");
				//il y a des agregat/distinct, on separe la liste en 2 :
				//une liste de mot cle type SUM, COUNT ... et une liste d'attibut
				tabMotCleExiste = true;
				for(int i=0;i<arrayAttributs.size();i++) {
					System.out.println(arrayAttributs.get(i)+" est analysée");
					if(distinct(arrayAttributs.get(i)) || agregat(" "+arrayAttributs.get(i))){//on ajoute un espace 
						System.out.println("c'est un agregats");
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
				//tabMotCle.addAll(arrayAttributs);
			}


			System.out.println("Les tables sont :");
			System.out.println("tabMotCle : "+tabMotCle);
			System.out.println("arrayAttribut : "+arrayAttributs);

			System.out.println("Et donc :");
			if(tabMotCleExiste) {
				System.out.println(tabMotCle);
				decomposition.ajouter(SQL.AGREGAT);
				decomposition.ajouter(tabMotCle.toString());
			}
			System.out.println(arrayAttributs);
			decomposition.ajouter(SQL.ATTRIBUT);
			decomposition.ajouter(arrayAttributs.toString());


			System.out.println(decomposition);
			System.out.println("apres analyse du from :");
			analyseFrom(apresFrom, decomposition);
			System.out.println(decomposition);

			/*
			 * il reste a associer les attributs a leurs table
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

	public boolean compareReponse(String requeteEleve, String requeteProf) {


		ResultSet rs1 = null,rs2 =null;
		/*
		 * ICI on va tester si MySQL "accepte" la reque
		 * c'est a dire si la requete est correcte d'un point de vue syntaxique 
		 * et si les attributs et les noms des champs existent...
		 * Sinon on sort et on explicite l'erreur
		 */

		
		try {
			rs2=Test.bdd.st.executeQuery(requeteProf);
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		try {
			rs1 = Test.bdd.st.executeQuery(requeteEleve);
			Test.bdd.afficherRes(rs1);
		}catch (SQLException sqle) {
			System.out.println(rs1);
			String message = sqle.getMessage(); 
		   String sqlState = sqle.getSQLState(); 
		      int errorCode = sqle.getErrorCode(); 
		      System.out.println("Message = "+message); 
		      System.out.println("SQLState = "+sqlState); 
		      System.out.println("ErrorCode = "+errorCode); 
		      if(errorCode==1064) {
		    	  Test.ie.ecrireErreur("Erreur syntaxique, vérifier votre requête...\n");
		    	  return false;
		      }

			System.out.println("Message = "+message); 

			System.out.println(" (CompareReponse) La requete est incorrect !\n"+sqle);
			Test.ie.ecrireErreur("Erreur SQL, vérifier votre requête...\n>>>"+message+"\n "+errorCode);

			return false;
		}


		System.out.println(rs1);
		System.out.println(rs2);
		/*
		 * 
		 * comparaison des resultats
		 * 
		 */
		boolean resultatIdentique;
		try {
		resultatIdentique=comparer(rs1, rs2);
		}catch (SQLException e) {
			// TODO: handle exception
			resultatIdentique=false;
		}
		System.out.println("La requete du prof est :: "+requeteProf+" et celle de l'eleve est "+requeteEleve);
		Arbre arbreProf = decomposeArbre(requeteProf);
		Arbre arbreEleve = decomposeArbre(requeteEleve);
		System.out.println("<<<<<<<<<>>>>>>>>>>");
		ArrayList<String> arrayProf = arbre2array(arbreProf);
		ArrayList<String> arrayEleve = arbre2array(arbreEleve);

		if(arrayEleve.equals(arrayProf)) {
			System.out.println("Les deux sont strictement equivalent !");
			System.out.println(arrayEleve);
			System.out.println(arrayProf);
			return true;
		}
		System.out.println(arbreProf);
		int curseurProf,curseurEleve;
		String temp1,temp2;
		switch(arrayProf.get(0)) {
		case(SQL.SELECT):
			if(arrayEleve.get(0)!=SQL.SELECT) {
				Test.ie.ecrireErreur("Mauvais type de requete !");
				return false;
			}
		/*
		 * si on est la c'est que les 2 requete sont des select
		 */
		//Test.ie.ecrireErreur("ok type de requete !");
		curseurProf=1;
		curseurEleve=1;
		boolean terminer = false;
		while(!terminer || curseurProf<arrayProf.size() || curseurEleve<arrayEleve.size()) {
			System.out.println("La taille de arrayProf est "+arrayProf.size()+" la taille de eleve est "+arrayEleve.size()+"et curseur sont"
					+ " pcurseur prof : "+curseurProf+" curseur eleve"+curseurEleve);
			System.out.println("salut toi "+arrayProf.get(curseurProf));

			if(curseurEleve >= arrayEleve.size() && curseurProf>= arrayProf.size()) {
				return true;
			}
			switch (arrayProf.get(curseurProf)) {
			case SQL.AGREGAT:
				curseurProf++;
				if(arrayEleve.get(curseurEleve)!=SQL.AGREGAT) {
					Test.ie.ecrireErreur("Erreur dans le SELECT \n il manque des AGRAGATS");
					return false;
				}
				curseurEleve++;
				/*
				 * il y a un agregats dans les 2 requetes
				 */
				/*
				 * on regarde si ce sont les meme agregats
				 * on enleve tout les espaces, les crochet et on les compare
				 */

				temp1=filtreAgregat(arrayProf.get(curseurProf));
				temp2=filtreAgregat(arrayEleve.get(curseurEleve));
				if(!temp1.equals(temp2)) {
					Test.ie.ecrireErreur("Erreur dans le SELECT \n\t agragats incorrect");
					return false;
				}
				curseurEleve++;
				curseurProf++;
				break;

			case SQL.ATTRIBUT:

				if(!arrayEleve.get(curseurEleve).equals(SQL.ATTRIBUT)) {
					Test.ie.ecrireErreur("Erreur dans le SELECT \n\t il manque un(des) attribut(s) !");
					return false;
				}
				curseurEleve++;
				curseurProf++;
				temp1=filtreAgregat(arrayProf.get(curseurProf));
				temp2=filtreAgregat(arrayEleve.get(curseurEleve));
				System.out.println(temp2+"\n\n\n\n"+temp1);
				if(!temp1.equals(temp2)) {
					Test.ie.ecrireErreur("Erreur dans le SELECT \n\t mauvais attributs !");

					return false;
					/*
					 * 
					 * on peut comparer la taille pour etre plus precis
					 */
				}
				System.out.println("L'element prof traite est : "+arrayProf.get(curseurProf));
				curseurEleve++;
				curseurProf++;

				/*
				 * 
				 * il faut maintenant traiter les attributs
				 */
				System.out.println("L'element prof traite est : "+arrayProf.get(curseurProf));
				break;

			case SQL.FROM:
				if(!arrayEleve.get(curseurEleve).equals(SQL.FROM)) {
					Test.ie.ecrireErreur("Erreur dans le FROM \n\t il manque le FROM");
					return false;
				}
				curseurEleve++;
				curseurProf++;
				temp1=filtreAgregat(arrayProf.get(curseurProf));
				temp2=filtreAgregat(arrayEleve.get(curseurEleve));
				if(!temp1.equals(temp2)) {
					Test.ie.ecrireErreur("Erreur dans le FROM \n\t table erronne");
					return false;
					/*
					 * 
					 * on peut comparer la taille pour savoir si il en manque ...
					 */
				}
				JOptionPane jop1 = new JOptionPane();
				jop1.showMessageDialog(null, "c la merde", "Information", JOptionPane.INFORMATION_MESSAGE);
				/*
				 * ON COMPARe LES TABLES
				 */

				temp1=filtreAgregat(arrayEleve.get(curseurEleve));
				temp2=filtreAgregat(arrayProf.get(curseurProf));
				if(!temp1.equals(temp2)) {
					Test.ie.ecrireErreur("Erreur FROM \n\t ce ne sont pas les bonnes tables");
					return false;
				}
				curseurEleve++;
				curseurProf++;
				break;

			case(SQL.WHERE):
				if(!arrayEleve.get(curseurEleve).equals(SQL.WHERE)) {
					//teste l'equivalence
					if(resultatIdentique) {
						Test.ie.ecrireErreur("Les clauses du WHERE ne sont pas nécessaires");
						return true;
					}
					Test.ie.ecrireErreur("Erreur dans le WHERE \n\t il manque les clauses dans le WHERE");
					return false;
				}
			curseurEleve++;
			curseurProf++;
			temp1=filtreAgregat(arrayProf.get(curseurProf));
			temp2=filtreAgregat(arrayEleve.get(curseurEleve));
			if(!temp1.equals(temp2)) {
				/*
				 * on test si les resultats sont les memes
				 */
				Test.ie.ecrireErreur("Erreur dans le WHERE \n\t table erronne");
				return false;
				/*
				 * 
				 * on peut comparer la taille pour savoir si il en manque ...
				 */
			}
			/*
			 * ON COMPARe LES TABLES
			 */

			temp1=filtreAgregat(arrayEleve.get(curseurEleve));
			temp2=filtreAgregat(arrayProf.get(curseurProf));
			if(!temp1.equals(temp2)) {
				Test.ie.ecrireErreur("Erreur FROM \n\t ce ne sont pas les bonnes tables");
				return false;
			}
			curseurEleve++;
			curseurProf++;
			break;


			default:
				//Test.ie.ecrireErreur("On est dans la place");

				/*	if(resultatIdentique) {
					System.out.println("Resultats identiques");
					return true;
				}*/

			}
			/*
			 * 
			 * nous ne somme pas parvenue a trouver de différences notables, c'est donc considéré comme juste...
			 */
			return true;
		}
		}
		Test.ie.ecrireErreur("OK");
		return true;
	}
	
	
	
	/**
     * retourne true si les RS correspondent, false sinon
     * @param rs1
     * @param rs2
     * @return 
     */
    public static boolean comparer(ResultSet rs1, ResultSet rs2) throws SQLException
    {
        /* recup rsmd */
        ResultSetMetaData rsmd1, rsmd2;
        rsmd1 = rs1.getMetaData();
        rsmd2 = rs2.getMetaData();
        
        /* comparer nb colonnes */
        int nbCol1 = rsmd1.getColumnCount();
        int nbCol2 = rsmd2.getColumnCount();
        if (nbCol1 != nbCol2) 
            return false;
        
        /* comparer nom colonne */
        String[] nomsCol1 = new String[nbCol1];
        String[] nomsCol2 = new String[nbCol1];
        for (int i=0; i<nbCol1; i++)
        {
            nomsCol1[i] = rsmd1.getColumnName(i+1);
            nomsCol2[i] = rsmd2.getColumnName(i+1);
        }
        for (String w:nomsCol1)
        {
            if (!estDedans(w, nomsCol2))
                return false;
        }
        
        /* comparer type colonne */
        String[] typeCol1 = new String[nbCol1];
        String[] typeCol2 = new String[nbCol1];
        for (int i=0; i<nbCol1; i++)
        {
            typeCol1[i] = rsmd1.getColumnTypeName(i+1);
            typeCol2[i] = rsmd2.getColumnTypeName(i+1);
        }
        for (String w:typeCol1)
        {
            if (!estDedans(w, typeCol2))
                return false;
        }
        
        /* tous les tests n'ont donnÃ© de diffÃ©rence */
        return true;
    }
    
    /**
     * retourne true si w est dans a, sinon retourne false
     * @param w
     * @param a
     * @return 
     */
    public static boolean estDedans(String w, String[] a)
    {
        for (String s:a)
        {
            if (w.equalsIgnoreCase(s))
                return true;
        }
        return false;
    }
	public boolean comparateur(String requeteEleve, String requeteProf) {
		ResultSet rs1 = null,rs2 =null;
		/*
		 * ICI on va tester si MySQL "accepte" la reque
		 * c'est a dire si la requete est correcte d'un point de vue syntaxique 
		 * et si les attributs et les noms des champs existent...
		 * Sinon on sort et on explicite l'erreur
		 */

		try {
			rs1 = Test.bdd.st.executeQuery(requeteEleve);
			Test.bdd.afficherRes(rs1);
		}catch (SQLException sqle) {
			System.out.println(rs1);
			String message = sqle.getMessage(); 
			String sqlState = sqle.getSQLState(); 
			int errorCode = sqle.getErrorCode(); 
			System.out.println("Message = "+message); 
			System.out.println("SQLState = "+sqlState); 
			System.out.println("ErrorCode = "+errorCode); 
			if(errorCode==1064) {
				Test.ie.ecrireErreur("Erreur syntaxique, vérifier votre requête...\n");
				return false;
			}

			System.out.println("Message = "+message); 

			System.out.println(" (CompareReponse) La requete est incorrect !\n"+sqle);
			Test.ie.ecrireErreur("Erreur SQL, vérifier votre requête...\n>>>"+message+"\n "+errorCode);

			return false;
		}
		/*
		 * le 2 requetes sont ok
		 */
		/*
		 * 
		 * comparaison des resultats
		 * 
		 */
		boolean resultatIdentique=true;
		System.out.println("La requete du prof est :: "+requeteProf+" et celle de l'eleve est "+requeteEleve);
		Arbre arbreProf = decomposeArbre(requeteProf);
		Arbre arbreEleve = decomposeArbre(requeteEleve);
		System.out.println("<<<<<<<<<>>>>>>>>>>");
		ArrayList<String> arrayProf = arbre2array(arbreProf);
		ArrayList<String> arrayEleve = arbre2array(arbreEleve);
		String temp;
		ArrayList<String> arrayProf2 = new ArrayList<>();
		ArrayList<String> arrayEleve2 = new ArrayList<>();
		for(int i=0;i<arrayEleve.size();i++) {
			temp=arrayEleve.get(i);
			temp.replaceAll(" ", "");
			arrayProf2.add(temp);
		}
		for(int i=0;i<arrayProf.size();i++) {
			temp=arrayProf.get(i);
			temp.replaceAll(" ", "");
			arrayEleve2.add(temp);
		}
		int tailleEleve = arrayEleve2.size(), tailleProf = arrayProf2.size();
		int fin = Integer.min(tailleEleve, tailleProf);
		String zoneCourante = "REQUETE";
		for (int i=0;i<fin;i++) {
			System.out.println("--------------------------PROF : "+arrayProf2.get(i)+" ----------ELEVE "+arrayEleve2.get(i));
			if(arrayProf2.get(i)==SQL.SELECT) {
				zoneCourante=SQL.SELECT;
			}else {
				if(arrayProf2.get(i)==SQL.FROM) {
					zoneCourante=SQL.FROM;
					
				}
			}
			if(arrayEleve2.get(i)==SQL.WHERE ) {
			
				/*
				 * traitement du where
				 */
				
			}else {
				if(arrayEleve2.get(i).equals(arrayProf2.get(i))) {
					/*
					 * c'est ok on continue
					 */
				}else {
					System.out.println("\n\n\n\n\n\n Erreur dans la zone : "+zoneCourante);
					Test.ie.ecrireErreur("Erreur dans "+zoneCourante);
					return false;
				}
			}
		}
		return true;
	}

	
	public String filtreAgregat(String filte) {
		String beta;
		beta = filte.replaceAll("[|]| ", "");
		return beta;
	}
	public ArrayList<String> arbre2array(Arbre arbre){
		ArrayList<String> array = new ArrayList<>();
		Arbre courant;
		String element = arbre.getElement().replaceAll(" ","");
		array.add(element);
		courant=arbre;
		while(courant.getDroite()!=null && courant.getGauche() != null) {
			array.add(courant.getGauche().getElement());
			array.add(courant.getDroite().getElement());
			courant=courant.getDroite();
		}

		return array;
	}

}
