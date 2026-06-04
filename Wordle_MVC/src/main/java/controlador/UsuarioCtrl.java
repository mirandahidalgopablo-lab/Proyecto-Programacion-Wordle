package controlador;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.LinkedList;
import javax.swing.JOptionPane;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import modelo.ConsultasBD;
import modelo.Usuario;
import vista.GestionUsuarios;

public class UsuarioCtrl implements ActionListener {

    private ConsultasBD modelo_consultas;
    private GestionUsuarios vista;

    public UsuarioCtrl() {
        this.modelo_consultas = new ConsultasBD();
        this.vista = new GestionUsuarios();
        this.vista.jButtonAñadir.addActionListener(this);
        this.vista.jButtonModificar.addActionListener(this);
        this.vista.jButtonEliminar.addActionListener(this);
        this.vista.jButtonLimpiar.addActionListener(this);
        this.vista.jButtonVolver.addActionListener(this);

        this.vista.jTableUsuarios.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                int fila = vista.jTableUsuarios.getSelectedRow();
                if (fila >= 0) {
                    vista.jTextFieldNombre.setText(vista.modeloTabla.getValueAt(fila, 1).toString());
                    vista.jTextFieldVictorias.setText(vista.modeloTabla.getValueAt(fila, 2).toString());
                    vista.jTextFieldPartidas.setText(vista.modeloTabla.getValueAt(fila, 3).toString());
                    vista.jTextFieldRachaActual.setText(vista.modeloTabla.getValueAt(fila, 4).toString());
                    vista.jTextFieldMejorRacha.setText(vista.modeloTabla.getValueAt(fila, 5).toString());
                }
            }
        });
    }

    public void iniciar() {
        cargarTabla();
        vista.setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {

        if (e.getSource() == vista.jButtonAñadir) añadir();
        if (e.getSource() == vista.jButtonModificar) modificar();
        if (e.getSource() == vista.jButtonEliminar) eliminar();
        if (e.getSource() == vista.jButtonLimpiar) limpiar();
        if (e.getSource() == vista.jButtonVolver) {
            vista.dispose();
            new MenuCtrl().iniciar();
        }
    }

    private void cargarTabla() {
        vista.modeloTabla.setRowCount(0);
        LinkedList<Usuario> lista = modelo_consultas.listarUsuarios();
        for (int i = 0; i < lista.size(); i++) {
            Usuario u = lista.get(i);
            Object[] fila = {u.getId(), u.getNombre(), u.getVictorias(), u.getPartidasJugadas(), u.getRachaActual(), u.getMejorRacha(), u.getFechaRegistro()};
            vista.modeloTabla.addRow(fila);
        }
    }

    private void añadir() {
        String nombre = vista.jTextFieldNombre.getText().trim();
        int victorias = parsearInt(vista.jTextFieldVictorias.getText());
        int partidas = parsearInt(vista.jTextFieldPartidas.getText());
        int racha = parsearInt(vista.jTextFieldRachaActual.getText());
        int mejor = parsearInt(vista.jTextFieldMejorRacha.getText());
        if (nombre.equals("") || victorias < 0 || partidas < 0 || racha < 0 || mejor < 0) {
            JOptionPane.showMessageDialog(null, "Datos no válidos", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }
        Usuario u = new Usuario(0, nombre, victorias, partidas, racha, mejor, "");
        if (modelo_consultas.añadirUsuario(u)) {
            JOptionPane.showMessageDialog(null, "Usuario añadido", "Información", JOptionPane.INFORMATION_MESSAGE);
            cargarTabla();
            limpiar();
        } else {
            JOptionPane.showMessageDialog(null, "Error al añadir (nombre repetido)", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void modificar() {
        int fila = vista.jTableUsuarios.getSelectedRow();
        if (fila < 0) {
            JOptionPane.showMessageDialog(null, "Selecciona un usuario", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }
        String nombre = vista.jTextFieldNombre.getText().trim();
        int victorias = parsearInt(vista.jTextFieldVictorias.getText());
        int partidas = parsearInt(vista.jTextFieldPartidas.getText());
        int racha = parsearInt(vista.jTextFieldRachaActual.getText());
        int mejor = parsearInt(vista.jTextFieldMejorRacha.getText());
        if (nombre.equals("") || victorias < 0 || partidas < 0 || racha < 0 || mejor < 0) {
            JOptionPane.showMessageDialog(null, "Datos no válidos", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }
        int id = Integer.parseInt(vista.modeloTabla.getValueAt(fila, 0).toString());
        Usuario u = new Usuario(id, nombre, victorias, partidas, racha, mejor, "");
        if (modelo_consultas.modificarUsuario(u)) {
            JOptionPane.showMessageDialog(null, "Usuario modificado", "Información", JOptionPane.INFORMATION_MESSAGE);
            cargarTabla();
            limpiar();
        } else {
            JOptionPane.showMessageDialog(null, "Error al modificar", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void eliminar() {
        int fila = vista.jTableUsuarios.getSelectedRow();
        if (fila < 0) {
            JOptionPane.showMessageDialog(null, "Selecciona un usuario", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }
        int op = JOptionPane.showConfirmDialog(null, "¿Eliminar el usuario y sus partidas?", "Confirmar", JOptionPane.YES_NO_OPTION);
        if (op != JOptionPane.YES_OPTION) return;
        int id = Integer.parseInt(vista.modeloTabla.getValueAt(fila, 0).toString());
        if (modelo_consultas.eliminarUsuario(id)) {
            cargarTabla();
            limpiar();
        } else {
            JOptionPane.showMessageDialog(null, "Error al eliminar", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private int parsearInt(String t) {
        t = t.trim();
        if (t.equals("")) return 0;
        try {
            return Integer.parseInt(t);
        } catch (NumberFormatException ex) {
            return -1;
        }
    }

    private void limpiar() {
        vista.jTextFieldNombre.setText("");
        vista.jTextFieldVictorias.setText("");
        vista.jTextFieldPartidas.setText("");
        vista.jTextFieldRachaActual.setText("");
        vista.jTextFieldMejorRacha.setText("");
        vista.jTableUsuarios.clearSelection();
    }
}
