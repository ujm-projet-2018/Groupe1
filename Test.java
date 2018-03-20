package creation;

import java.io.File;

public class Test {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Exercice tourDeFrance = new Exercice("Tour de France");
		tourDeFrance.setPreambule("Soit le modèle relationnel suivant relatif à la gestion simplifiée des étapes du \r\n" + 
				"Tour de France 97, dont une des étapes de type \"contre la montre individuel\" se \r\n" + 
				"déroula à Saint-Etienne : \r\n" + 
				"EQUIPE(CodeEquipe, NomEquipe, DirecteurSportif) \r\n" + 
				"COUREUR(NuméroCoureur, NomCoureur, CodeEquipe*, CodePays*) \r\n" + 
				"PAYS(CodePays, NomPays) \r\n" + 
				"TYPE_ETAPE(CodeType, LibelléType) \r\n" + 
				"ETAPE(NuméroEtape, DateEtape, VilleDép, VilleArr, NbKm, CodeType*) \r\n" + 
				"PARTICIPER(NuméroCoureur*, NuméroEtape*, TempsRéalisé) \r\n" + 
				"ATTRIBUER_BONIFICATION(NuméroEtape*, km, Rang, NbSecondes, \r\n" + 
				"NuméroCoureur*) \r\n" + 
				"Remarque : les clés primaires sont soulignées et les clés étrangères sont \r\n" + 
				"marquées par * ");
		tourDeFrance.setInit(new File("source.sql"));
		Question question1 = new Question("Quelle est la composition de l'équipe Festina (Numéro, nom et pays des \r\n" + 
				"coureurs) ? ", "SELECT NuméroCoureur, NomCoureur, NomPays \r\n" + 
						"FROM EQUIPE A, COUREUR B, PAYS C \r\n" + 
						"WHERE A.CodeEquipe=B.CodeEquipe And B.CodePays=C.CodePays \r\n" + 
						"And NomEquipe=\"FESTINA\" ; ");
		Question question2 = new Question("Quel est le nombre de kilomètres total du Tour de France 97 ?", 
				"SELECT SUM(Nbkm) FROM ETAPE ;" );
		
		Question question3 = new Question("Quel est le nombre de kilomètres total des étapes de type HAUTE MONTAGNE ?",""
				+ "SELECT SUM(Nbkm) FROM ETAPE A, TYPE_ETAPE B "
				+ "WHERE A.CodeType=B.CodeType And LibelléType=\"HAUTE MONTAGNE\" ;" );
		Question question4 = new Question(" Quels sont les noms des coureurs qui n'ont pas obtenu de bonifications ? \r\n",
				"SELECT NomCoureur FROM COUREUR \r\n" + 
				"WHERE NuméroCoureur NOT IN (SELECT NuméroCoureur FROM \r\n" + 
				"ATTRIBUER_BONIFICATION) ; ");
		
		Question question5 = new Question("Quels sont les noms des coureurs qui ont participé à toutes les étapes ? \r\n" , 
				"SELECT NomCoureur FROM PARTICIPER A, COUREUR B \r\n" + 
				"WHERE A.NuméroCoureur=B.NuméroCoureur \r\n" + 
				"GROUP BY NuméroCoureur, NomCoureur \r\n" + 
				"HAVING COUNT(*)=(SELECT COUNT(*) FROM ETAPE) ; ");
		
		Question question6 = new Question("Quel est le classement général des coureurs (nom, code équipe, code pays \r\n" +
				"et temps des coureurs) à l'issue des 13 premières étapes sachant que les \r\n" + 
				"bonifications ont été intégrées dans les temps réalisés à chaque étape ? \r\n" ,
				"SELECT NomCoureur, CodeEquipe, CodePays, SUM(TempsRéalisé) AS Total \r\n" + 
				"FROM PARTICIPER A, COUREUR B \r\n" + 
				"WHERE A.NuméroCoureur=B.NuméroCoureur and NuméroEtape<=13 \r\n" + 
				"GROUP BY A.NuméroCoureur, NomCoureur, CodeEquipe, CodePays \r\n" + 
				"ORDER BY Total; ");
		
		Question question7 = new Question("Quel est le classement par équipe à l'issue des 13 premières étapes (nom et \r\n" +
				"temps des équipes) ? \r\n", 
				"SELECT NomEquipe, SUM(TempsRéalisé) AS Total \r\n" + 
				"FROM PARTICIPER A, COUREUR B, EQUIPE C \r\n" + 
				"WHERE A.NuméroCoureur=B.NuméroCoureur And \r\n" + 
				"B.CodeEquipe=C.CodeEquipe \r\n" + 
				"And NuméroEtape<=13 \r\n" + 
				"GROUP BY B.CodeEquipe, NomEquipe \r\n" + 
				"ORDER BY Total;");
		
		tourDeFrance.exercice.add(question1);
		tourDeFrance.exercice.add(question2);
		tourDeFrance.exercice.add(question3);
		tourDeFrance.exercice.add(question4);
		tourDeFrance.exercice.add(question5);
		tourDeFrance.exercice.add(question6);
		tourDeFrance.exercice.add(question7);
		System.out.println(tourDeFrance);
		
		/*
		 * SOURCE DE L'EXO :
		 * http://www.commentcamarche.net/forum/affich-2000578-exercice-sql-server
		 * 
		 */
		}

}
