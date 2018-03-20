package creation;

import java.io.File;

public class Test {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Exercice tourDeFrance = new Exercice("Tour de France");
		tourDeFrance.setPreambule("Soit le mod�le relationnel suivant relatif � la gestion simplifi�e des �tapes du \r\n" + 
				"Tour de France 97, dont une des �tapes de type \"contre la montre individuel\" se \r\n" + 
				"d�roula � Saint-Etienne : \r\n" + 
				"EQUIPE(CodeEquipe, NomEquipe, DirecteurSportif) \r\n" + 
				"COUREUR(Num�roCoureur, NomCoureur, CodeEquipe*, CodePays*) \r\n" + 
				"PAYS(CodePays, NomPays) \r\n" + 
				"TYPE_ETAPE(CodeType, Libell�Type) \r\n" + 
				"ETAPE(Num�roEtape, DateEtape, VilleD�p, VilleArr, NbKm, CodeType*) \r\n" + 
				"PARTICIPER(Num�roCoureur*, Num�roEtape*, TempsR�alis�) \r\n" + 
				"ATTRIBUER_BONIFICATION(Num�roEtape*, km, Rang, NbSecondes, \r\n" + 
				"Num�roCoureur*) \r\n" + 
				"Remarque : les cl�s primaires sont soulign�es et les cl�s �trang�res sont \r\n" + 
				"marqu�es par * ");
		tourDeFrance.setInit(new File("source.sql"));
		Question question1 = new Question("Quelle est la composition de l'�quipe Festina (Num�ro, nom et pays des \r\n" + 
				"coureurs) ? ", "SELECT Num�roCoureur, NomCoureur, NomPays \r\n" + 
						"FROM EQUIPE A, COUREUR B, PAYS C \r\n" + 
						"WHERE A.CodeEquipe=B.CodeEquipe And B.CodePays=C.CodePays \r\n" + 
						"And NomEquipe=\"FESTINA\" ; ");
		Question question2 = new Question("Quel est le nombre de kilom�tres total du Tour de France 97 ?", 
				"SELECT SUM(Nbkm) FROM ETAPE ;" );
		
		Question question3 = new Question("Quel est le nombre de kilom�tres total des �tapes de type HAUTE MONTAGNE ?",""
				+ "SELECT SUM(Nbkm) FROM ETAPE A, TYPE_ETAPE B "
				+ "WHERE A.CodeType=B.CodeType And Libell�Type=\"HAUTE MONTAGNE\" ;" );
		Question question4 = new Question(" Quels sont les noms des coureurs qui n'ont pas obtenu de bonifications ? \r\n",
				"SELECT NomCoureur FROM COUREUR \r\n" + 
				"WHERE Num�roCoureur NOT IN (SELECT Num�roCoureur FROM \r\n" + 
				"ATTRIBUER_BONIFICATION) ; ");
		
		Question question5 = new Question("Quels sont les noms des coureurs qui ont particip� � toutes les �tapes ? \r\n" , 
				"SELECT NomCoureur FROM PARTICIPER A, COUREUR B \r\n" + 
				"WHERE A.Num�roCoureur=B.Num�roCoureur \r\n" + 
				"GROUP BY Num�roCoureur, NomCoureur \r\n" + 
				"HAVING COUNT(*)=(SELECT COUNT(*) FROM ETAPE) ; ");
		
		Question question6 = new Question("Quel est le classement g�n�ral des coureurs (nom, code �quipe, code pays \r\n" +
				"et temps des coureurs) � l'issue des 13 premi�res �tapes sachant que les \r\n" + 
				"bonifications ont �t� int�gr�es dans les temps r�alis�s � chaque �tape ? \r\n" ,
				"SELECT NomCoureur, CodeEquipe, CodePays, SUM(TempsR�alis�) AS Total \r\n" + 
				"FROM PARTICIPER A, COUREUR B \r\n" + 
				"WHERE A.Num�roCoureur=B.Num�roCoureur and Num�roEtape<=13 \r\n" + 
				"GROUP BY A.Num�roCoureur, NomCoureur, CodeEquipe, CodePays \r\n" + 
				"ORDER BY Total; ");
		
		Question question7 = new Question("Quel est le classement par �quipe � l'issue des 13 premi�res �tapes (nom et \r\n" +
				"temps des �quipes) ? \r\n", 
				"SELECT NomEquipe, SUM(TempsR�alis�) AS Total \r\n" + 
				"FROM PARTICIPER A, COUREUR B, EQUIPE C \r\n" + 
				"WHERE A.Num�roCoureur=B.Num�roCoureur And \r\n" + 
				"B.CodeEquipe=C.CodeEquipe \r\n" + 
				"And Num�roEtape<=13 \r\n" + 
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
