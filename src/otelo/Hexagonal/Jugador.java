/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package otelo.Hexagonal;

/**
 *
 * @author cliente
 */
public class Jugador {
    private String Nombre;
    private int tipo;   //maquina o humano
    public static int MAQUINA = 100;
    public static int HUMANO = 200;
    public static String FIC_BLA = "Fichas Blancas";
    public static String FIC_NEG = "Fichas Negras";

    public Jugador(String Nombre, int tipo) {
        this.Nombre = Nombre;
        this.tipo = tipo;
    }

    public String getNombre() {
        return Nombre;
    }

    public void setNombre(String Nombre) {
        this.Nombre = Nombre;
    }

    public int getTipo() {
        return tipo;
    }

    public void setTipo(int tipo) {
        this.tipo = tipo;
    }
}
