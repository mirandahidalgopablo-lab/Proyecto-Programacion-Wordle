package controlador;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JFrame;
import vista.MenuPrincipal;

public class MenuCtrl implements ActionListener {

    private MenuPrincipal vista;
    private JFrame ventana;

    public MenuCtrl() {
        this.vista = new MenuPrincipal();

        this.ventana = new JFrame("WORDLE - Menú Principal");
        this.ventana.add(this.vista);

        this.ventana.pack();
        this.ventana.setLocationRelativeTo(null);
        this.ventana.setResizable(false);
        this.ventana.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        this.vista.jButtonJugar.setRolloverEnabled(false);
        this.vista.jButtonPalabras.setRolloverEnabled(false);
        this.vista.jButtonUsuarios.setRolloverEnabled(false);
        this.vista.jButtonHistorial.setRolloverEnabled(false);
        this.vista.jButtonSalir.setRolloverEnabled(false);

        this.vista.jButtonJugar.setFocusPainted(false);
        this.vista.jButtonPalabras.setFocusPainted(false);
        this.vista.jButtonUsuarios.setFocusPainted(false);
        this.vista.jButtonHistorial.setFocusPainted(false);
        this.vista.jButtonSalir.setFocusPainted(false);

        this.vista.jButtonJugar.setContentAreaFilled(false);
        this.vista.jButtonPalabras.setContentAreaFilled(false);
        this.vista.jButtonUsuarios.setContentAreaFilled(false);
        this.vista.jButtonHistorial.setContentAreaFilled(false);
        this.vista.jButtonSalir.setContentAreaFilled(false);

        this.vista.jButtonJugar.addActionListener(this);
        this.vista.jButtonPalabras.addActionListener(this);
        this.vista.jButtonUsuarios.addActionListener(this);
        this.vista.jButtonHistorial.addActionListener(this);
        this.vista.jButtonSalir.addActionListener(this);
    }

    public void iniciar() {
        this.ventana.setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {

        if (e.getSource() == vista.jButtonJugar) {
            SeleccionModoCtrl ctrl = new SeleccionModoCtrl();
            ctrl.iniciar();
            this.ventana.dispose();
        }

        if (e.getSource() == vista.jButtonPalabras) {
            SeleccionGestionPalabrasCtrl ctrl = new SeleccionGestionPalabrasCtrl();
            ctrl.iniciar();
            this.ventana.dispose();
        }

        if (e.getSource() == vista.jButtonUsuarios) {
            UsuarioCtrl ctrl = new UsuarioCtrl();
            ctrl.iniciar();
            this.ventana.dispose();
        }

        if (e.getSource() == vista.jButtonHistorial) {
            HistorialCtrl ctrl = new HistorialCtrl();
            ctrl.iniciar();
            this.ventana.dispose();
        }

        if (e.getSource() == vista.jButtonSalir) {
            System.exit(0);
        }
    }
}
