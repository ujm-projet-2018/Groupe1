package projetTutore;

import java.awt.BorderLayout;
import java.awt.Button;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.LayoutManager;

import javax.swing.JButton;
import javax.swing.JEditorPane;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.WindowConstants;

import jsyntaxpane.DefaultSyntaxKit;
import jsyntaxpane.actions.CompleteWordAction;
 
public class SyntaxTester {

    public static void main(String[] args) {
        java.awt.EventQueue.invokeLater(new Runnable() {

            @Override
            public void run() {
               SyntaxTester st = new SyntaxTester();
            }
        });
    }

    public SyntaxTester() {
        JFrame f = new JFrame("editeur");
        final Container c = f.getContentPane();
        c.setLayout(new BorderLayout());

        DefaultSyntaxKit.initKit();

        final JEditorPane codeEditor = new JEditorPane();
        JScrollPane scrPane = new JScrollPane(codeEditor);
        c.add(scrPane, BorderLayout.CENTER);
        c.doLayout();
        codeEditor.setContentType("text/sql");
        codeEditor.setText("SELECT * FROM T_eleve;");
        

        String test = codeEditor.getText();
        System.out.println(test);
        f.setSize(800, 600);
        f.setVisible(true);
       // f.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
 
       
        
    }
}