package modelo;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.text.Normalizer;
import java.util.HashSet;

public class LogicaWordle {

    public static final int GRIS = 0;
    public static final int AMARILLO = 1;
    public static final int VERDE = 2;

    private static HashSet<String> diccionario = null;

    public static int[] evaluar(String intento, String solucion) {
        int n = intento.length();
        int[] resultado = new int[n];
        intento = intento.toUpperCase();
        solucion = solucion.toUpperCase();

        boolean[] usadas = new boolean[n];

        for (int i = 0; i < n; i++) {
            if (intento.charAt(i) == solucion.charAt(i)) {
                resultado[i] = VERDE;
                usadas[i] = true;
            }
        }

        for (int i = 0; i < n; i++) {
            if (resultado[i] == VERDE) {
                continue;
            }
            boolean encontrada = false;
            for (int j = 0; j < n; j++) {
                if (!usadas[j] && intento.charAt(i) == solucion.charAt(j) && !encontrada) {
                    resultado[i] = AMARILLO;
                    usadas[j] = true;
                    encontrada = true;
                }
            }
            if (!encontrada) {
                resultado[i] = GRIS;
            }
        }

        return resultado;
    }

    public static boolean esVictoria(int[] resultado) {
        for (int i = 0; i < resultado.length; i++) {
            if (resultado[i] != VERDE) {
                return false;
            }
        }
        return true;
    }

    public static void cargarDiccionario() {
        if (diccionario != null) {
            return;
        }
        diccionario = new HashSet<>();
        try {
            FileReader fr = new FileReader("diccionario.txt");
            BufferedReader br = new BufferedReader(fr);
            String linea = br.readLine();
            while (linea != null) {
                String palabra = limpiarLinea(linea);
                if (esPalabraAceptable(palabra)) {
                    diccionario.add(palabra);
                }
                linea = br.readLine();
            }
            br.close();
            fr.close();
            System.out.println("Diccionario cargado: " + diccionario.size() + " palabras");
        } catch (IOException ex) {
            System.err.println("No se pudo cargar diccionario.txt: " + ex);
        }
    }
    
    public static void agregarPalabra(String palabra) {
        if (diccionario == null) {
            diccionario = new HashSet<>();
        }
        String norm = normalizar(palabra);
        if (esPalabraAceptable(norm)) {
            diccionario.add(norm);
        }
    }

    private static String limpiarLinea(String linea) {
        linea = linea.trim();
        int coma = linea.indexOf(',');
        if (coma >= 0) {
            linea = linea.substring(0, coma);
        }
        return normalizar(linea);
    }

    private static String normalizar(String s) {
        if (s == null) {
            return "";
        }
        s = s.trim().toUpperCase();
        StringBuilder sb = new StringBuilder(s.length());
        char enie = 'Ñ';
        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);
            if (c == enie) {
                sb.append(enie);
                continue;
            }
            String desc = Normalizer.normalize(String.valueOf(c), Normalizer.Form.NFD);
            for (int j = 0; j < desc.length(); j++) {
                char d = desc.charAt(j);
                if (Character.getType(d) != Character.NON_SPACING_MARK) {
                    sb.append(d);
                }
            }
        }
        return sb.toString();
    }

    private static boolean esPalabraAceptable(String p) {
        if (p.length() < 3) {
            return false;
        }
        char enie = 'Ñ';
        for (int i = 0; i < p.length(); i++) {
            char c = p.charAt(i);
            boolean letra = (c >= 'A' && c <= 'Z') || c == enie;
            if (!letra) {
                return false;
            }
        }
        return true;
    }

    public static boolean esPalabraValida(String palabra) {
        if (diccionario == null || diccionario.isEmpty()) {
            return true;
        }
        return diccionario.contains(normalizar(palabra));
    }
}
