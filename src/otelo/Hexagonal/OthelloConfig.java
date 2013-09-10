/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package otelo.Hexagonal;

/**
 *
 * @author cliente
 */
public class OthelloConfig {
    private int tamTab;
    private int jug1;
    private int jug2;

    public OthelloConfig(int tamTab, int jug1, int jug2) {
        this.tamTab = tamTab;
        this.jug1 = jug1;
        this.jug2 = jug2;
    }

    public int getTamTab() {
        return tamTab;
    }

    public void setTamTab(int tamTab) {
        this.tamTab = tamTab;
    }

    public int getJug1() {
        return jug1;
    }

    public void setJug1(int jug1) {
        this.jug1 = jug1;
    }

    public int getJug2() {
        return jug2;
    }

    public void setJug2(int jug2) {
        this.jug2 = jug2;
    }

}
