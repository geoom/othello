/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package otelo;

/**
 *
 * @author cliente
 */
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class Main implements ActionListener {
    public static int CELDAS=8; // Número de celdas por fila y por columna

    private JButton b1, b2;

    Juego juegothread;

    MiCanvas canvas;

    public static void main(String args[]){
        Main m = new Main();
    }

    public Main() {
        b1 = new JButton("Empezar");
        b2 = new JButton("Terminar");

        Panel botones = new Panel();
        botones.setLayout(new FlowLayout());
        botones.add(b1);
        botones.add(b2);
        b2.setEnabled(false);
        b1.addActionListener(this);
        b2.addActionListener(this);

        canvas = new MiCanvas(CELDAS);
        canvas.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent e) {
                if (canvas.estaEsperandoClick()) {
                    canvas.calcularMovimiento(e.getX(), e.getY());
                }
            }
        });


        JFrame ventana = new JFrame();
        ventana.setVisible(true);
        ventana.setTitle("Otelo IA");
        ventana.setLayout(new BorderLayout());
        ventana.add(botones,BorderLayout.NORTH);
        ventana.add(canvas,BorderLayout.CENTER);
        ventana.setSize(new Dimension(408,465));
        ventana.setResizable(false);
        ventana.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        canvas.setTablero(new Tablero(CELDAS));
        canvas.repaint();
    }

    public void actionPerformed(ActionEvent e){
        if (e.getActionCommand() == "Empezar"){
            b1.setEnabled(false);
            b2.setEnabled(true);
            juegothread = new Juego(canvas, CELDAS, b1, b2);
            juegothread.start();
            System.out.println("Empezando el juego");
        }
        else if (e.getActionCommand() == "Terminar"){
            b1.setEnabled(true);
            b2.setEnabled(false);
            juegothread.stop();
            System.out.println("Juego terminado");
        }
    }
}

