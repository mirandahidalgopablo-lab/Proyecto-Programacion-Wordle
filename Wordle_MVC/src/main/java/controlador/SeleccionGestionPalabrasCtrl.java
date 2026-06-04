package controlador;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import vista.SeleccionGestionPalabras;

public class SeleccionGestionPalabrasCtrl implements ActionListener {

    private SeleccionGestionPalabras vista;

    public SeleccionGestionPalabrasCtrl() {
        this.vista = new SeleccionGestionPalabras();
        this.vista.jButton4.addActionListener(this);
        this.vista.jButton5.addActionListener(this);
        this.vista.jButton6.addActionListener(this);
        this.vista.jButtonVolver.addActionListener(this);
    }

    public void iniciar() {
        vista.setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {

        if (e.getSource() == vista.jButton4) {
            abrirGestion(4);
        }
        if (e.getSource() == vista.jButton5) {
            abrirGestion(5);
        }
        if (e.getSource() == vista.jButton6) {
            abrirGestion(6);
        }
        if (e.getSource() == vista.jButtonVolver) {
            vista.dispose();
            new MenuCtrl().iniciar();
        }
    }

    private void abrirGestion(int longitud) {
        vista.dispose();
        PalabraCtrl ctrl = new PalabraCtrl(longitud);
        ctrl.iniciar();
    }
}
