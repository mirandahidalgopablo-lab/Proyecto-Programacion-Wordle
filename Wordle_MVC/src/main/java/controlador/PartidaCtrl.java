package controlador;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.LinkedList;
import javax.swing.BorderFactory;
import javax.swing.JOptionPane;
import modelo.ConsultasBD;
import modelo.LogicaWordle;
import modelo.Palabra;
import modelo.Partida;
import modelo.Usuario;
import vista.PartidaWordle;

public class PartidaCtrl implements ActionListener {

    private ConsultasBD modelo_consultas;
    private PartidaWordle vista;
    private int longitud;
    private int modo;

    private Usuario jugador;
    private Palabra solucion;
    private int intentoActual;
    private boolean partidaActiva;
    private long tiempoInicio;
    private int pistasUsadas;
    private static final int MAX_PISTAS = 2;

    private int[][] historialResultados;

    public PartidaCtrl(int longitud, int modo) {
        this.longitud = longitud;
        this.modo = modo;
        this.modelo_consultas = new ConsultasBD();
        this.vista = new PartidaWordle(longitud);
        this.intentoActual = 0;
        this.partidaActiva = false;
        this.pistasUsadas = 0;
        this.historialResultados = new int[6][longitud];
        this.vista.jButtonEnviar.addActionListener(this);
        this.vista.jButtonNuevaPartida.addActionListener(this);
        this.vista.jButtonVolver.addActionListener(this);
        this.vista.jButtonPista.addActionListener(this);
        this.vista.jButtonRendirse.addActionListener(this);

        LogicaWordle.cargarDiccionario();
        sincronizarPalabrasBD();
    }

    private void sincronizarPalabrasBD() {
        LinkedList<Palabra> lista = modelo_consultas.listarPalabras(longitud);
        for (int i = 0; i < lista.size(); i++) {
            LogicaWordle.agregarPalabra(lista.get(i).getPalabra());
        }
    }

    public void iniciar() {
        String nombre = JOptionPane.showInputDialog(null, "Introduce tu nombre:", "Jugador", JOptionPane.QUESTION_MESSAGE);
        if (nombre == null) return;
        nombre = nombre.trim();
        if (nombre.equals("")) return;

        jugador = modelo_consultas.buscarUsuario(nombre);
        if (jugador == null) {
            Usuario nuevo = new Usuario(0, nombre, 0, 0, 0, 0, "");
            modelo_consultas.añadirUsuario(nuevo);
            jugador = modelo_consultas.buscarUsuario(nombre);
        }

        String nombreModo = "Normal";
        if (modo == 1) nombreModo = "Fácil";
        if (modo == 2) nombreModo = "Difícil";
        vista.jLabelModo.setText("Modo: " + longitud + " letras - " + nombreModo);

        nuevaPartida();
        vista.setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {

        if (e.getSource() == vista.jButtonVolver) {
            vista.dispose();
            new MenuCtrl().iniciar();
        }
        if (e.getSource() == vista.jButtonEnviar) {
            if (partidaActiva) procesarIntento();
        }
        if (e.getSource() == vista.jButtonNuevaPartida) {
            nuevaPartida();
        }
        if (e.getSource() == vista.jButtonPista) {
            if (partidaActiva) usarPista();
        }
        if (e.getSource() == vista.jButtonRendirse) {
            if (partidaActiva) rendirse();
        }
    }

    private void nuevaPartida() {
        solucion = modelo_consultas.palabraAleatoria(longitud, modo);
        if (solucion == null) {
            JOptionPane.showMessageDialog(null, "No hay palabras para este modo. Añade alguna primero.", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }

        jugador = modelo_consultas.buscarUsuario(jugador.getNombre());

        for (int i = 0; i < 6; i++) {
            for (int j = 0; j < longitud; j++) {
                vista.casillas[i][j].setText("");
                vista.casillas[i][j].setBackground(Color.WHITE);
                vista.casillas[i][j].setForeground(Color.BLACK);
                vista.casillas[i][j].setBorder(BorderFactory.createLineBorder(new Color(180, 180, 180), 2));
                historialResultados[i][j] = -1;
            }
        }

        for (int i = 0; i < 26; i++) {
            vista.teclado[i].setBackground(Color.WHITE);
            vista.teclado[i].setForeground(Color.BLACK);
        }

        vista.jTextFieldIntento.setText("");
        vista.jLabelInfo.setText("Escribe una palabra de " + longitud + " letras");
        vista.jLabelUsuario.setText("Jugador: " + jugador.getNombre());
        actualizarStats();
        vista.jButtonNuevaPartida.setEnabled(false);

        intentoActual = 0;
        partidaActiva = true;
        pistasUsadas = 0;
        tiempoInicio = System.currentTimeMillis();
    }

    private void actualizarStats() {
        String txt = "<html>Partidas jugadas: " + jugador.getPartidasJugadas()
                + " &nbsp;&nbsp;|&nbsp;&nbsp; Victorias: " + jugador.getVictorias()
                + " &nbsp;&nbsp;|&nbsp;&nbsp; Racha actual: " + jugador.getRachaActual()
                + " &nbsp;&nbsp;|&nbsp;&nbsp; Mejor racha: " + jugador.getMejorRacha() + "</html>";
        vista.jLabelStats.setText(txt);
    }

    private void procesarIntento() {
        String intento = vista.jTextFieldIntento.getText().trim().toUpperCase();

        if (intento.length() != longitud) {
            JOptionPane.showMessageDialog(null, "La palabra debe tener " + longitud + " letras.", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }

        boolean soloLetras = true;
        for (int i = 0; i < intento.length(); i++) {
            if (!Character.isLetter(intento.charAt(i))) soloLetras = false;
        }
        if (!soloLetras) {
            JOptionPane.showMessageDialog(null, "Solo se permiten letras.", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }

        if (!LogicaWordle.esPalabraValida(intento)) {
            JOptionPane.showMessageDialog(null, "Esa palabra no existe en el diccionario.", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int[] resultado = LogicaWordle.evaluar(intento, solucion.getPalabra());
        pintarFila(intentoActual, intento, resultado);
        actualizarTeclado(intento, resultado);
        guardarResultado(intentoActual, resultado);
        vista.jTextFieldIntento.setText("");

        if (LogicaWordle.esVictoria(resultado)) {
            finalizarVictoria();
            return;
        }

        intentoActual++;

        if (intentoActual == 6) {
            finalizarDerrota();
        }
    }

    private void finalizarVictoria() {
        partidaActiva = false;
        int tiempo = calcularTiempo();
        guardarPartida(intentoActual + 1, true, tiempo);
        modelo_consultas.registrarVictoria(jugador.getId());
        modelo_consultas.sumarPartidaJugada(jugador.getId());
        jugador = modelo_consultas.buscarUsuario(jugador.getNombre());
        actualizarStats();
        vista.jButtonNuevaPartida.setEnabled(true);
        JOptionPane.showMessageDialog(null,
                "¡Has ganado en " + (intentoActual + 1) + " intentos! Tiempo: " + tiempo + " s.",
                "Victoria", JOptionPane.INFORMATION_MESSAGE);
    }

    private void finalizarDerrota() {
        partidaActiva = false;
        int tiempo = calcularTiempo();
        guardarPartida(6, false, tiempo);
        modelo_consultas.registrarDerrota(jugador.getId());
        modelo_consultas.sumarPartidaJugada(jugador.getId());
        jugador = modelo_consultas.buscarUsuario(jugador.getNombre());
        actualizarStats();
        vista.jButtonNuevaPartida.setEnabled(true);
        JOptionPane.showMessageDialog(null,
                "Se acabaron los intentos. La palabra era: " + solucion.getPalabra(),
                "Has perdido", JOptionPane.INFORMATION_MESSAGE);
    }

    private void pintarFila(int fila, String intento, int[] resultado) {
        for (int i = 0; i < longitud; i++) {
            vista.casillas[fila][i].setText(String.valueOf(intento.charAt(i)));
            vista.casillas[fila][i].setForeground(Color.WHITE);

            if (resultado[i] == LogicaWordle.VERDE) {
                vista.casillas[fila][i].setBackground(new Color(80, 170, 80));
            }
            if (resultado[i] == LogicaWordle.AMARILLO) {
                vista.casillas[fila][i].setBackground(new Color(220, 180, 60));
            }
            if (resultado[i] == LogicaWordle.GRIS) {
                vista.casillas[fila][i].setBackground(new Color(140, 140, 140));
            }
        }
    }

    private void actualizarTeclado(String intento, int[] resultado) {
        for (int i = 0; i < longitud; i++) {
            char c = intento.charAt(i);
            int idx = c - 'A';
            if (idx < 0 || idx > 25) continue;
            int estadoNuevo = resultado[i];
            Color actual = vista.teclado[idx].getBackground();
            boolean yaVerde = actual.equals(new Color(80, 170, 80));
            boolean yaAmarillo = actual.equals(new Color(220, 180, 60));
            if (yaVerde) continue;
            if (estadoNuevo == LogicaWordle.VERDE) {
                vista.teclado[idx].setBackground(new Color(80, 170, 80));
                vista.teclado[idx].setForeground(Color.WHITE);
            } else if (estadoNuevo == LogicaWordle.AMARILLO && !yaAmarillo) {
                vista.teclado[idx].setBackground(new Color(220, 180, 60));
                vista.teclado[idx].setForeground(Color.WHITE);
            } else if (estadoNuevo == LogicaWordle.GRIS && actual.equals(Color.WHITE)) {
                vista.teclado[idx].setBackground(new Color(140, 140, 140));
                vista.teclado[idx].setForeground(Color.WHITE);
            }
        }
    }

    private void guardarResultado(int fila, int[] resultado) {
        for (int i = 0; i < longitud; i++) {
            historialResultados[fila][i] = resultado[i];
        }
    }

    private void usarPista() {
        if (pistasUsadas >= MAX_PISTAS) {
            JOptionPane.showMessageDialog(null, "Ya has usado todas las pistas (" + MAX_PISTAS + ")", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }

        LinkedListInt posLibres = new LinkedListInt();
        for (int i = 0; i < longitud; i++) {
            boolean yaVerde = false;
            for (int f = 0; f < intentoActual; f++) {
                if (historialResultados[f][i] == LogicaWordle.VERDE) {
                    yaVerde = true;
                }
            }
            if (!yaVerde) {
                posLibres.add(i);
            }
        }
        if (posLibres.size() == 0) {
            JOptionPane.showMessageDialog(null, "Ya tienes todas las letras descubiertas", "Aviso", JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        int idx = posLibres.get((int) (Math.random() * posLibres.size()));
        char letra = solucion.getPalabra().toUpperCase().charAt(idx);
        pistasUsadas++;
        JOptionPane.showMessageDialog(null,
                "Pista: la letra en la posición " + (idx + 1) + " es '" + letra + "'\nPistas usadas: " + pistasUsadas + "/" + MAX_PISTAS,
                "Pista", JOptionPane.INFORMATION_MESSAGE);
    }

    private void rendirse() {
        int op = JOptionPane.showConfirmDialog(null, "¿Seguro que quieres rendirte?", "Confirmar", JOptionPane.YES_NO_OPTION);
        if (op != JOptionPane.YES_OPTION) return;
        finalizarDerrota();
    }

    private int calcularTiempo() {
        long ms = System.currentTimeMillis() - tiempoInicio;
        return (int) (ms / 1000);
    }

    private void guardarPartida(int intentos, boolean victoria, int tiempo) {
        Partida p = new Partida();
        p.setUsuarioId(jugador.getId());
        p.setPalabraTexto(solucion.getPalabra());
        p.setLongitud(longitud);
        p.setIntentos(intentos);
        p.setVictoria(victoria);
        p.setTiempoSegundos(tiempo);
        modelo_consultas.añadirPartida(p);
    }

    private static class LinkedListInt {
        private int[] datos = new int[10];
        private int n = 0;
        public void add(int v) { datos[n++] = v; }
        public int get(int i) { return datos[i]; }
        public int size() { return n; }
    }
}
