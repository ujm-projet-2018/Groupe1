package projettut;

import java.awt.BorderLayout;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.swing.JFrame;

public class test
{
  public static void main(String[] args)
  {
	  String query = "SELECT * FROM Communaute, Personne";
    Connection connection = getConnection();
    try
    {
        Statement st = connection.createStatement( ResultSet.TYPE_SCROLL_INSENSITIVE,
                								   ResultSet.CONCUR_READ_ONLY );
        
        ResultSet rs = st.executeQuery( query );
        Resultat rtm = new Resultat( rs );
        
        TablePanel tablePanel = new TablePanel( rtm );
        
        JFrame mainFrame = new JFrame( "Affiche table " );
        mainFrame.add( tablePanel, BorderLayout.CENTER );
        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE );
        mainFrame.setSize( 640, 480 );
        mainFrame.setVisible( true );
    } 
    catch ( SQLException e )
    {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
  }
  
  public static Connection getConnection()
    {
      Connection connection = null;
      boolean ok = false;
    
      //--- chargement en mémoire du pilote
      try
     {
       Class.forName( PILOTE );
       ok = true;
     } 
     catch ( ClassNotFoundException e )
     {
       System.out.println( "ERREUR chargement du pilote: pilote non trouvé" );
       System.out.println("Driver ok");
       e.printStackTrace();
     }
    
     //--- connexion à la base de données
      String url = "jdbc:mysql://localhost:3306/devweb";
		String user = "root";
		String password = "sinama";
     if ( ok )
     {
       try
       {
         connection = DriverManager.getConnection( url, user, password);
       } 
       catch ( SQLException e )
       {
         System.out.println( "ERREUR de connexion à la base de données: ");
         e.printStackTrace();
       }
     }
    
      return connection;
    }
    
    //--- attributs
    private static final String PILOTE = "com.mysql.jdbc.Driver";
}
