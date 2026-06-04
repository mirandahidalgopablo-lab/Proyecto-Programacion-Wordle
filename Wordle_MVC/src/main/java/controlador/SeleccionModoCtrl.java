package controlador;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JFrame;
import vista.SeleccionModo;

public class SeleccionModoCtrl implements ActionListener {

    private SeleccionModo vista;

    private int modoSeleccionado = 1;
    private int longitudSeleccionada = 5;

    public SeleccionModoCtrl() {
        this.vista = new SeleccionModo();

        this.vista.pack();
        this.vista.setLocationRelativeTo(null);
        this.vista.setResizable(false);
        this.vista.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        this.vista.jButtonFacil.addActionListener(this);
        this.vista.jButtonDificil.addActionListener(this);
        this.vista.jButton4Letras.addActionListener(this);
        this.vista.jButton5Letras.addActionListener(this);
        this.vista.jButton6Letras.addActionListener(this);
        this.vista.jButtonEmpezar.addActionListener(this);
    }

    public void iniciar() {
        this.vista.setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {

        if (e.getSource() == vista.jButtonFacil) {
            modoSeleccionado = 1;
            System.out.println("Has seleccionado: Fácil");
        }

        if (e.getSource() == vista.jButtonDificil) {
            modoSeleccionado = 2;
            System.out.println("Has seleccionado: Difícil");
        }

        if (e.getSource() == vista.jButton4Letras) {
            longitudSeleccionada = 4;
            System.out.println("Has seleccionado: 4 Letras");
        }

        if (e.getSource() == vista.jButton5Letras) {
            longitudSeleccionada = 5;
            System.out.println("Has seleccionado: 5 Letras");
        }

        if (e.getSource() == vista.jButton6Letras) {
            longitudSeleccionada = 6;
            System.out.println("Has seleccionado: 6 Letras");
        }

        if (e.getSource() == vista.jButtonEmpezar) {
            PartidaCtrl ctrlPartida = new PartidaCtrl(longitudSeleccionada, modoSeleccionado);
            ctrlPartida.iniciar();

            this.vista.dispose();
        }
    }
}