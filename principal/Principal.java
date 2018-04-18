package projetTutore;


import java.sql.SQLException;

public class Principal {
	public GestionBDD connect;
	public Exercice exo;
	public Principal() throws SQLException {
		connect= new GestionBDD();
		
	}
	
	public void chargeExo(String nom) {
		Exercice ex = new Exercice("Temp");
		ex.importation(nom);
		this.exo=ex;
	}
	
}
