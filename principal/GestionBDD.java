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
			// TODO Bloc catch généré automatiquement
			e.printStackTrace();
		}
		//connexion
		//String url = "jdbc:mysql://mira2.univ-st-etienne.fr/ba02996q";
		String url = "jdbc:mysql://localhost/projet";
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
	public ResultSet reqSQL(String query, char type) throws SQLException{
		//c.setAutoCommit(false);
		st=c.createStatement();
		
		System.out.println("Requete : "+query);
		switch(type){
		case('s'):
			rs = st.executeQuery(query);
		afficherRes(rs);
		return rs;
		
		case('m'):
			int res = st.executeUpdate(query);
		System.out.println(res + " tuple(s) affecté(s)\n");
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


		// --- LISTING DATABASE TABLE NAMES ---
		String[] types = { "TABLE" };
		resultSet = c.getMetaData().getTables("projettut", null, "%", types);
		String tableName = "";
		while (resultSet.next()) {
			tableName = resultSet.getString(3);
			System.out.println("Table Name = " + tableName);
			DatabaseMetaData meta = c.getMetaData();
			resultSet2 = meta.getColumns("projettut", null, tableName, "%");
			while (resultSet2.next()) {
				System.out.println("Column Name of table " + tableName + " = "+ resultSet2.getString(4));
			}
		}
	}

}
