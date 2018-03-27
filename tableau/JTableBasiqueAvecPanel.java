/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tableau;

import java.awt.BorderLayout;
import java.awt.Color;
import javax.swing.JFrame;
import javax.swing.JTable;

/**
 *
 * @author anis
 */
public class JTableBasiqueAvecPanel extends JFrame {
    
    public enum Sport {
    TENNIS,
    FOOTBALL,
    NATATION,
    RIEN;
    }
    
    public JTableBasiqueAvecPanel() {
        super();
 
        setTitle("JTable basique dans un JPanel");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
 
        Object[][] donnees = {
                {"Johnathan", "Sykes", Color.red, true, Sport.TENNIS},
                {"Nicolas", "Van de Kampf", Color.black, true, Sport.FOOTBALL},
                {"Damien", "Cuthbert", Color.cyan, true, Sport.RIEN},
                {"Corinne", "Valance", Color.blue, false, Sport.NATATION},
                {"Emilie", "Schrödinger", Color.magenta, false, Sport.FOOTBALL},
                {"Delphine", "Duke", Color.yellow, false, Sport.TENNIS},
                {"Eric", "Trump", Color.pink, true, Sport.FOOTBALL},
        };
 
        String[] entetes = {"Prénom", "Nom", "Couleur favorite", "Homme", "Sport"};
 
        JTable tableau = new JTable(donnees, entetes);
 
        getContentPane().add(tableau.getTableHeader(), BorderLayout.NORTH);
        getContentPane().add(tableau, BorderLayout.CENTER);
 
        pack();
    }
 
    public static void main(String[] args) {
        new JTableBasiqueAvecPanel().setVisible(true);
    }
}