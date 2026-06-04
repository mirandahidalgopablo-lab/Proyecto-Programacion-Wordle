package controlador;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.LinkedList;
import javax.swing.JOptionPane;
import modelo.ConsultasBD;
import modelo.Partida;
import vista.Historial;

public class HistorialCtrl implements ActionListener {

    private ConsultasBD modelo_consultas;
    private Historial vista;

    public HistorialCtrl() {
        this.modelo_consultas = new ConsultasBD();
        this.vista = new Historial();
        this.vista.jButtonRefrescar.addActionListener(this);
        this.vista.jButtonEliminar.addActionListener(this);
        this.vista.jButtonVolver.addActionListener(this);
    }

    public void iniciar() {
        cargarTabla();
        vista.setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {

        if (e.getSource() == vista.jButtonRefrescar) cargarTabla();
        if (e.getSource() == vista.jButtonEliminar) eliminar();
        if (e.getSource() == vista.jButtonVolver) {
            vista.dispose();
            new MenuCtrl().iniciar();
        }
    }

    private void cargarTabla() {
        vista.modeloTabla.setRowCount(0);
        LinkedList<Partida> lista = modelo_consultas.listarPartidas();
        for (int i = 0; i < lista.size(); i++) {
            Partida p = lista.get(i);
            String ganada;
            if (p.isVictoria()) {
                ganada = "Sí";
            } else {
                ganada = "No";
            }
            Object[] fila = {
                p.getId(),
                p.getNombreUsuario(),
                p.getPalabraTexto(),
                p.getLongitud(),
                p.getIntentos(),
                ganada,
                p.getTiempoSegundos(),
                p.getFecha()
            };
            vista.modeloTabla.addRow(fila);
        }
    }

    private void eliminar() {
        int fila = vista.jTablePartidas.getSelectedRow();
        if (fila < 0) {
            JOptionPane.showMessageDialog(null, "Selecciona una partida", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }
        int op = JOptionPane.showConfirmDialog(null, "¿Eliminar la partida?", "Confirmar", JOptionPane.YES_NO_OPTION);
        if (op != JOptionPane.YES_OPTION) return;
        int id = Integer.parseInt(vista.modeloTabla.getValueAt(fila, 0).toString());
        if (modelo_consultas.eliminarPartida(id)) {
            cargarTabla();
        }
    }
}
