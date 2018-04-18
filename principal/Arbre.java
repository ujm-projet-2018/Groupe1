package projetTutore;

public class Arbre {
	private String element;
	Arbre gauche, droite;

	public Arbre() {
		Arbre curseur;
		String noeudActuel;
		String element;
		Arbre gauche;
		Arbre droite;
	}

	public Arbre(String element) {
		super();
		this.element = element;
	}

	@Override
	public String toString() {
		return "Arbre [element=" + element + ", gauche=" + gauche + ", droite=" + droite + "]";
	}

	public Arbre(String element, Arbre gauche, Arbre droite) {
		super();
		this.element = element;
		this.gauche = gauche;
		this.droite = droite;
	}

	public Arbre getGauche() {
		return gauche;
	}
	public void setGauche(Arbre gauche) {
		this.gauche = gauche;
	}
	public Arbre getDroite() {
		return droite;
	}
	public void setDroite(Arbre droite) {
		this.droite = droite;
	}
	public String getElement() {
		return element;
	}
	public void setElement(String element) {
		this.element = element;
	}


	/**
	 * 
	 * @return le dernier noeud de l'arbre
	 */
	public Arbre courant() {
		Arbre courant;
		courant = this;
		while(courant.getDroite() != null && courant.getGauche()!=null) {
			courant=courant.getDroite();
		}
		return courant;
	}
	
	
	/**
	 * Ajoute l'argument dans l'arbre
	 * @param ajout
	 */
	public void ajouter(String ajout) {
		
		Arbre courant;
		courant=this;
		while(courant.getDroite()!=null && courant.getGauche() != null) {
			courant=courant.getDroite();
		}
		if(courant.getGauche()==null) {
			courant.setGauche(new Arbre(ajout));
		}else {
			courant.setDroite(new Arbre(ajout));
		}
	}
}



