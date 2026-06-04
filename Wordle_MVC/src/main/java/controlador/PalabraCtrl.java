package controlador;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.LinkedList;
import javax.swing.JOptionPane;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import modelo.Categoria;
import modelo.ConsultasBD;
import modelo.LogicaWordle;
import modelo.Palabra;
import vista.GestionPalabras;

public class PalabraCtrl implements ActionListener {

    private ConsultasBD modelo_consultas;
    private GestionPalabras vista;
    private int longitud;

    public PalabraCtrl(int longitud) {
        this.longitud = longitud;
        this.modelo_consultas = new ConsultasBD();
        this.vista = new GestionPalabras(longitud);
        this.vista.jButtonAñadir.addActionListener(this);
        this.vista.jButtonModificar.addActionListener(this);
        this.vista.jButtonEliminar.addActionListener(this);
        this.vista.jButtonLimpiar.addActionListener(this);
        this.vista.jButtonVolver.addActionListener(this);

        this.vista.jTablePalabras.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                int fila = vista.jTablePalabras.getSelectedRow();
                if (fila >= 0) {
                    String texto = vista.modeloTabla.getValueAt(fila, 1).toString();
                    String catNombre = vista.modeloTabla.getValueAt(fila, 2).toString();
                    int dif = Integer.parseInt(vista.modeloTabla.getValueAt(fila, 3).toString());
                    vista.jTextFieldPalabra.setText(texto);
                    seleccionarCategoriaPorNombre(catNombre);
                    vista.jComboBoxDificultad.setSelectedItem(dif);
                }
            }
        });
    }

    public void iniciar() {
        cargarCategorias();
        cargarTabla();
        vista.setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {

        if (e.getSource() == vista.jButtonAñadir) {
            añadir();
        }
        if (e.getSource() == vista.jButtonModificar) {
            modificar();
        }
        if (e.getSource() == vista.jButtonEliminar) {
            eliminar();
        }
        if (e.getSource() == vista.jButtonLimpiar) {
            limpiar();
        }
        if (e.getSource() == vista.jButtonVolver) {
            vista.dispose();
            new SeleccionGestionPalabrasCtrl().iniciar();
        }
    }

    private void cargarCategorias() {
        vista.jComboBoxCategoria.removeAllItems();
        LinkedList<Categoria> lista = modelo_consultas.listarCategorias();
        for (int i = 0; i < lista.size(); i++) {
            vista.jComboBoxCategoria.addItem(lista.get(i));
        }
    }

    private void seleccionarCategoriaPorNombre(String nombre) {
        for (int i = 0; i < vista.jComboBoxCategoria.getItemCount(); i++) {
            Categoria c = vista.jComboBoxCategoria.getItemAt(i);
            if (c.getNombre().equals(nombre)) {
                vista.jComboBoxCategoria.setSelectedIndex(i);
                return;
            }
        }
    }

    private void cargarTabla() {
        vista.modeloTabla.setRowCount(0);
        LinkedList<Palabra> lista = modelo_consultas.listarPalabras(longitud);
        for (int i = 0; i < lista.size(); i++) {
            Palabra p = lista.get(i);
            Object[] fila = {p.getId(), p.getPalabra(), p.getCategoriaNombre(), p.getDificultad()};
            vista.modeloTabla.addRow(fila);
        }
    }

    private void añadir() {
        String texto = vista.jTextFieldPalabra.getText().trim().toUpperCase();
        if (!validar(texto)) return;
        Categoria cat = (Categoria) vista.jComboBoxCategoria.getSelectedItem();
        int dif = (Integer) vista.jComboBoxDificultad.getSelectedItem();
        if (cat == null) {
            JOptionPane.showMessageDialog(null, "Selecciona una categoría", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }
        Palabra p = new Palabra(0, texto, cat.getId(), cat.getNombre(), dif, longitud);
        if (modelo_consultas.añadirPalabra(p)) {
            LogicaWordle.agregarPalabra(texto);
            JOptionPane.showMessageDialog(null, "Palabra añadida", "Información", JOptionPane.INFORMATION_MESSAGE);
            cargarTabla();
            limpiar();
        } else {
            JOptionPane.showMessageDialog(null, "Error al añadir (puede que ya exista)", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void modificar() {
        int fila = vista.jTablePalabras.getSelectedRow();
        if (fila < 0) {
            JOptionPane.showMessageDialog(null, "Selecciona una palabra de la tabla", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }
        String texto = vista.jTextFieldPalabra.getText().trim().toUpperCase();
        if (!validar(texto)) return;
        Categoria cat = (Categoria) vista.jComboBoxCategoria.getSelectedItem();
        int dif = (Integer) vista.jComboBoxDificultad.getSelectedItem();
        int id = Integer.parseInt(vista.modeloTabla.getValueAt(fila, 0).toString());
        Palabra p = new Palabra(id, texto, cat.getId(), cat.getNombre(), dif, longitud);
        if (modelo_consultas.modificarPalabra(p)) {
            LogicaWordle.agregarPalabra(texto);
            JOptionPane.showMessageDialog(null, "Palabra modificada", "Información", JOptionPane.INFORMATION_MESSAGE);
            cargarTabla();
            limpiar();
        } else {
            JOptionPane.showMessageDialog(null, "Error al modificar", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void eliminar() {
        int id = -1;
        String texto = vista.jTextFieldPalabra.getText().trim().toUpperCase();

        if (!texto.equals("")) {
            for (int i = 0; i < vista.modeloTabla.getRowCount(); i++) {
                String palabraFila = vista.modeloTabla.getValueAt(i, 1).toString();
                if (palabraFila.equalsIgnoreCase(texto)) {
                    id = Integer.parseInt(vista.modeloTabla.getValueAt(i, 0).toString());
                }
            }
            if (id == -1) {
                JOptionPane.showMessageDialog(null, "La palabra '" + texto + "' no está en la tabla", "Aviso", JOptionPane.WARNING_MESSAGE);
                return;
            }
        } else {
            int fila = vista.jTablePalabras.getSelectedRow();
            if (fila < 0) {
                JOptionPane.showMessageDialog(null, "Escribe una palabra o selecciona una fila", "Aviso", JOptionPane.WARNING_MESSAGE);
                return;
            }
            id = Integer.parseInt(vista.modeloTabla.getValueAt(fila, 0).toString());
        }

        int respuesta = JOptionPane.showConfirmDialog(null, "¿Eliminar la palabra?", "Confirmar", JOptionPane.YES_NO_OPTION);
        if (respuesta != JOptionPane.YES_OPTION) return;

        if (modelo_consultas.eliminarPalabra(id, longitud)) {
            cargarTabla();
            limpiar();
        } else {
            JOptionPane.showMessageDialog(null, "Error al eliminar", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void limpiar() {
        vista.jTextFieldPalabra.setText("");
        vista.jComboBoxDificultad.setSelectedItem(3);
        vista.jTablePalabras.clearSelection();
    }

    private boolean validar(String texto) {
        if (texto.length() != longitud) {
            JOptionPane.showMessageDialog(null, "La palabra debe tener " + longitud + " letras", "Aviso", JOptionPane.WARNING_MESSAGE);
            return false;
        }
        for (int i = 0; i < texto.length(); i++) {
            if (!Character.isLetter(texto.charAt(i))) {
                JOptionPane.showMessageDialog(null, "Solo se permiten letras", "Aviso", JOptionPane.WARNING_MESSAGE);
                return false;
            }
        }
        return true;
    }
}
