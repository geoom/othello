/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package otelo.Hexagonal;

import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.UnsupportedLookAndFeelException;

/**
 *
 * @author cliente
 */
public class TestLisp {

    public static void main(String []args){
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                }
            }
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(TestLisp.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            Logger.getLogger(TestLisp.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            Logger.getLogger(TestLisp.class.getName()).log(Level.SEVERE, null, ex);
        } catch (UnsupportedLookAndFeelException ex) {
            Logger.getLogger(TestLisp.class.getName()).log(Level.SEVERE, null, ex);
        }
        OthelloHexagonal oth = new OthelloHexagonal(6, 0, 0);
        oth.mp();
        /*Jatha MyLisp = new Jatha(false, false);
        MyLisp.init();
        MyLisp.start();*/
    }
}
