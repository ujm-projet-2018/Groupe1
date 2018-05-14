package projetTutore;

import java.sql.*;

public class GestionBDD implements General {
	public Statement st;
	public Connection c;
	public ResultSet rs ;
	public GestionBDD() throws SQLException{
		//on charge le driver
		try {
			Class.forName("com.mysql.jdbc.Driver");
		} catch (ClassNotFoundException e) {
			// TODO Bloc catch gÃ©nÃ©rÃ© automatiquement
			e.printStackTrace();
		}
		//connexion
		//String url = "jdbc:mysql://mira2.univ-st-etienne.fr/ba02996q";
		String url = "jdbc:mysql://localhost/projettut";
		String identifiant = General.identifiant;
		String mdp = General.mdp;
		c =DriverManager.getConnection(url,identifiant,mdp);
		c.setAutoCommit(false);
		st=c.createStatement();
		
	}

	/**
	 * 
	 * @param query requete SQL a executer
	 * @param type m si sa modifie la base s sinon 
	 * @throws SQLException
	 */
	public ResultSet reqSQL(String query, char type) {
		//c.setAutoCommit(false);
		try {
			st=c.createStatement();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		System.out.println("Requete : "+query);
		switch(type){
		case('s'):
			try {
				rs = st.executeQuery(query);
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			try {
				afficherRes(rs);
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		return rs;
		
		case('m'):
			int res = -1;
			try {
				res = st.executeUpdate(query);
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		System.out.println(res + " tuple(s) affecté©(s)\n");
		return rs;
		
		default:
			System.out.println("Erreur dans reqSQL, le type est inconnu.");
			break;	
		}
		return null;

	}
	
	
	/**
	 * Affichage console des ResultSet
	 * @param rs
	 * @throws SQLException
	 */
	public void afficherRes(ResultSet rs) throws SQLException {
		int i;
		ResultSetMetaData meta = (ResultSetMetaData) rs.getMetaData();
		int nbColonne = meta.getColumnCount();
		System.out.println("Il y a "+nbColonne+" colonnes dans ce ResultSet");
		/*
		for(i=1;i<=nbColonne;i++) {
			System.out.println("Colonne "+i+" ---> "+ meta.getColumnClassName(i) );
		}
		*/
		for(i=1;i<=nbColonne;i++) {
			Object alpha = meta.getColumnName(i);
			System.out.print(" | "+alpha+" | ");
		}
		System.out.println();
		
		while(rs.next()) {
			for(i=1;i<=nbColonne;i++) {
				Object alpha = rs.getObject(i);
				System.out.print(" | "+alpha+" | ");
			}
			System.out.println();
			
		}
		
	}
	
	/**
	 * Cette methode affiche toutes les tables et leur attributs ==> il faudrait les mettre dans une liste
	 * @throws SQLException
	 */
	public void infoBase() throws SQLException {

		ResultSet resultSet = c.getMetaData().getCatalogs();
		ResultSet resultSet2;

		System.out.println("Titre : "+Test.exercice.titre);
		System.out.println("Préambule : \n" + Test.exercice.preambule);

		// --- LISTING DATABASE TABLE NAMES ---
		String[] types = { "TABLE" };
		resultSet = c.getMetaData().getTables("projettut", null, "%", types);
		String tableName = "";
		
		while (resultSet.next()) {
			tableName = resultSet.getString(3);
			System.out.println("Nom de la table : " + tableName);
			DatabaseMetaData meta = c.getMetaData();
			resultSet2 = meta.getColumns("projettut", null, tableName, "%");
			while (resultSet2.next()) {
				System.out.println("   Colonne de la table " + tableName + " = "+ resultSet2.getString(4));
			}
		}
	}
	
	public String info() throws SQLException{
		
		String retour = "";
		ResultSet resultSet = c.getMetaData().getCatalogs();
		ResultSet resultSet2;

		retour=retour+"Titre : "+Test.exercice.titre+"\n";
		System.out.println("Titre : "+Test.exercice.titre);
		retour=retour+"Préambule : \n" + Test.exercice.preambule+"\n";
		System.out.println("Préambule : \n" + Test.exercice.preambule+"\nVoici les différents tables : \n");

		// --- LISTING DATABASE TABLE NAMES ---
		String[] types = { "TABLE" };
		resultSet = c.getMetaData().getTables("projettut", null, "%", types);
		String tableName = "";
		
		while (resultSet.next()) {
			tableName = resultSet.getString(3);
			retour=retour+"Nom de la table : " + tableName;
			System.out.println("Nom de la table : " + tableName);
			DatabaseMetaData meta = c.getMetaData();
			resultSet2 = meta.getColumns("projettut", null, tableName, "%");
			retour=retour+"\t\n(";
			while (resultSet2.next()) {
				retour=retour+resultSet2.getString(4)+",";
				System.out.println("   Colonne de la table " + tableName + " = "+ resultSet2.getString(4));
			}
			retour=retour+")\n";
		}
		return retour;
	}
	
	

}
