package exercice;    

import java.io.Serializable;

public class Question implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String enonce;
	private String reponse;
	/**
	 * Constructeur de Question, une Question est une partie d'un Exercice
	 * @param enonce, il s'agit de l'enonce de la question
	 * @param reponse, il s'agit de la requete-reponse
	 */
	public Question(String enonce, String reponse) {
		this.enonce=enonce;
		this.reponse=reponse;
	}
	/**
	 * 
	 * @return l'enonce de la question
	 */
	public String getEnonce() {
		return this.enonce;
	}
	
	/**
	 * 
	 * @return la requete-reponse de la Question
	 * ATTENTION A NE JAMAIS MONTRER LA REPONSE A L'ELEVE
	 */
	public String getReponse() {
		return this.reponse;
	}
	
	/**
	 * Utiliser par l'outils professeur
	 * @param enonce
	 */
	public void setEnonce(String enonce) {
		this.enonce = enonce;
	}
	
	/**
	 * Utiliser par l'outils professeur
	 * @param reponse
	 */
	public void setReponse(String reponse) {
		this.reponse = reponse;
	}
	
	public String toString() {
		String chaine;
		chaine="> Question :"+this.enonce+"\n> Reponse : "+this.reponse;
		return chaine;
	}
}
