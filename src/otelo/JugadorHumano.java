/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package otelo;

/**
 *
 * @author cliente
 */
// Clase que representa al jugador humano. Cuando le toca tirar al humano la clase Juego
// llama a su método run
public class JugadorHumano extends Jugador {
    Tablero tablero;
    MiCanvas canvas;
    public JugadorHumano() {

    }

    public void setTablero(Tablero t) {
        tablero = t;
    }

    public void setCanvas(MiCanvas c) {
        canvas = c;
    }

    @SuppressWarnings("empty-statement")
    public Movimiento run() {
        // Obtenemos el movimiento a partir de la posición sobre la que el jugador hizo click
        // en el canvas que representa el tablero
        canvas.esperandoClick();
        while (canvas.obtenerMovimiento() == null ||!tablero.movimientoCorrecto(canvas.obtenerMovimiento(),1) ){
                while (canvas.estaEsperandoClick());
                if (!tablero.movimientoCorrecto(canvas.obtenerMovimiento(),1))
                    canvas.esperandoClick();
        }
        return canvas.obtenerMovimiento();
    }
}
