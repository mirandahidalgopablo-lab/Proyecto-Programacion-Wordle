package modelo;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.LinkedList;

public class ConsultasBD extends ConexionBD {

    public LinkedList<Categoria> listarCategorias() {
        LinkedList<Categoria> lista = new LinkedList<>();
        Connection con = getConexion();
        String sql = "SELECT id, nombre, descripcion FROM categorias ORDER BY nombre";
        try {
            Statement s = con.createStatement();
            ResultSet rs = s.executeQuery(sql);
            while (rs.next()) {
                lista.add(new Categoria(rs.getInt(1), rs.getString(2), rs.getString(3)));
            }
            rs.close();
            s.close();
            con.close();
        } catch (SQLException e) {
            System.err.println(e);
        }
        return lista;
    }

    public boolean añadirUsuario(Usuario u) {
        Connection con = getConexion();
        String sql = "INSERT INTO usuarios (nombre, victorias, partidas_jugadas, racha_actual, mejor_racha) VALUES (?, ?, ?, ?, ?)";
        try {
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, u.getNombre());
            ps.setInt(2, u.getVictorias());
            ps.setInt(3, u.getPartidasJugadas());
            ps.setInt(4, u.getRachaActual());
            ps.setInt(5, u.getMejorRacha());
            ps.executeUpdate();
            ps.close();
            con.close();
            return true;
        } catch (SQLException e) {
            System.err.println(e);
            return false;
        }
    }

    public LinkedList<Usuario> listarUsuarios() {
        LinkedList<Usuario> lista = new LinkedList<>();
        Connection con = getConexion();
        String sql = "SELECT id, nombre, victorias, partidas_jugadas, racha_actual, mejor_racha, fecha_registro FROM usuarios ORDER BY nombre";
        try {
            Statement s = con.createStatement();
            ResultSet rs = s.executeQuery(sql);
            while (rs.next()) {
                lista.add(new Usuario(rs.getInt(1), rs.getString(2), rs.getInt(3), rs.getInt(4), rs.getInt(5), rs.getInt(6), rs.getString(7)));
            }
            rs.close();
            s.close();
            con.close();
        } catch (SQLException e) {
            System.err.println(e);
        }
        return lista;
    }

    public Usuario buscarUsuario(String nombre) {
        Connection con = getConexion();
        String sql = "SELECT id, nombre, victorias, partidas_jugadas, racha_actual, mejor_racha, fecha_registro FROM usuarios WHERE nombre = ?";
        Usuario u = null;
        try {
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, nombre);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                u = new Usuario(rs.getInt(1), rs.getString(2), rs.getInt(3), rs.getInt(4), rs.getInt(5), rs.getInt(6), rs.getString(7));
            }
            rs.close();
            ps.close();
            con.close();
        } catch (SQLException e) {
            System.err.println(e);
        }
        return u;
    }

    public boolean modificarUsuario(Usuario u) {
        Connection con = getConexion();
        String sql = "UPDATE usuarios SET nombre = ?, victorias = ?, partidas_jugadas = ?, racha_actual = ?, mejor_racha = ? WHERE id = ?";
        try {
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, u.getNombre());
            ps.setInt(2, u.getVictorias());
            ps.setInt(3, u.getPartidasJugadas());
            ps.setInt(4, u.getRachaActual());
            ps.setInt(5, u.getMejorRacha());
            ps.setInt(6, u.getId());
            ps.executeUpdate();
            ps.close();
            con.close();
            return true;
        } catch (SQLException e) {
            System.err.println(e);
            return false;
        }
    }

    public boolean eliminarUsuario(int id) {
        Connection con = getConexion();
        try {
            PreparedStatement ps1 = con.prepareStatement("DELETE FROM partidas WHERE usuario_id = ?");
            ps1.setInt(1, id);
            ps1.executeUpdate();
            ps1.close();

            PreparedStatement ps2 = con.prepareStatement("DELETE FROM usuarios WHERE id = ?");
            ps2.setInt(1, id);
            ps2.executeUpdate();
            ps2.close();
            con.close();
            return true;
        } catch (SQLException e) {
            System.err.println(e);
            return false;
        }
    }

    public void registrarVictoria(int idUsuario) {
        Connection con = getConexion();
        String sql = "UPDATE usuarios SET victorias = victorias + 1, racha_actual = racha_actual + 1, "
                + "mejor_racha = GREATEST(mejor_racha, racha_actual + 1) WHERE id = ?";
        try {
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setInt(1, idUsuario);
            ps.executeUpdate();
            ps.close();
            con.close();
        } catch (SQLException e) {
            System.err.println(e);
        }
    }

    public void registrarDerrota(int idUsuario) {
        Connection con = getConexion();
        String sql = "UPDATE usuarios SET racha_actual = 0 WHERE id = ?";
        try {
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setInt(1, idUsuario);
            ps.executeUpdate();
            ps.close();
            con.close();
        } catch (SQLException e) {
            System.err.println(e);
        }
    }

    public void sumarPartidaJugada(int idUsuario) {
        Connection con = getConexion();
        String sql = "UPDATE usuarios SET partidas_jugadas = partidas_jugadas + 1 WHERE id = ?";
        try {
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setInt(1, idUsuario);
            ps.executeUpdate();
            ps.close();
            con.close();
        } catch (SQLException e) {
            System.err.println(e);
        }
    }

    private String tablaPalabras(int longitud) {
        return "palabras_" + longitud;
    }

    public boolean añadirPalabra(Palabra p) {
        Connection con = getConexion();
        String sql = "INSERT INTO " + tablaPalabras(p.getLongitud()) + " (palabra, categoria_id, dificultad) VALUES (?, ?, ?)";
        try {
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, p.getPalabra().toUpperCase());
            ps.setInt(2, p.getCategoriaId());
            ps.setInt(3, p.getDificultad());
            ps.executeUpdate();
            ps.close();
            con.close();
            return true;
        } catch (SQLException e) {
            System.err.println(e);
            return false;
        }
    }

    public LinkedList<Palabra> listarPalabras(int longitud) {
        LinkedList<Palabra> lista = new LinkedList<>();
        Connection con = getConexion();
        String sql = "SELECT p.id, p.palabra, p.categoria_id, c.nombre, p.dificultad "
                + "FROM " + tablaPalabras(longitud) + " p, categorias c "
                + "WHERE p.categoria_id = c.id "
                + "ORDER BY p.palabra";
        try {
            Statement s = con.createStatement();
            ResultSet rs = s.executeQuery(sql);
            while (rs.next()) {
                lista.add(new Palabra(rs.getInt(1), rs.getString(2), rs.getInt(3), rs.getString(4), rs.getInt(5), longitud));
            }
            rs.close();
            s.close();
            con.close();
        } catch (SQLException e) {
            System.err.println(e);
        }
        return lista;
    }

    public boolean modificarPalabra(Palabra p) {
        Connection con = getConexion();
        String sql = "UPDATE " + tablaPalabras(p.getLongitud()) + " SET palabra = ?, categoria_id = ?, dificultad = ? WHERE id = ?";
        try {
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, p.getPalabra().toUpperCase());
            ps.setInt(2, p.getCategoriaId());
            ps.setInt(3, p.getDificultad());
            ps.setInt(4, p.getId());
            ps.executeUpdate();
            ps.close();
            con.close();
            return true;
        } catch (SQLException e) {
            System.err.println(e);
            return false;
        }
    }

    public boolean eliminarPalabra(int id, int longitud) {
        Connection con = getConexion();
        try {
            PreparedStatement ps = con.prepareStatement("DELETE FROM " + tablaPalabras(longitud) + " WHERE id = ?");
            ps.setInt(1, id);
            ps.executeUpdate();
            ps.close();
            con.close();
            return true;
        } catch (SQLException e) {
            System.err.println(e);
            return false;
        }
    }

    public Palabra palabraAleatoria(int longitud, int modo) {
        LinkedList<Palabra> todas = listarPalabras(longitud);
        LinkedList<Palabra> filtradas = new LinkedList<>();
        for (int i = 0; i < todas.size(); i++) {
            Palabra p = todas.get(i);
            int d = p.getDificultad();
            if (modo == 0) {
                filtradas.add(p);
            } else if (modo == 1 && (d == 1 || d == 2)) {
                filtradas.add(p);
            } else if (modo == 2 && (d == 3 || d == 4)) {
                filtradas.add(p);
            }
        }
        if (filtradas.isEmpty()) {
            return null;
        }
        int pos = (int) (Math.random() * filtradas.size());
        return filtradas.get(pos);
    }

    public boolean añadirPartida(Partida p) {
        Connection con = getConexion();
        String sql = "INSERT INTO partidas (usuario_id, palabra_texto, longitud, intentos, victoria, tiempo_segundos) VALUES (?, ?, ?, ?, ?, ?)";
        try {
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setInt(1, p.getUsuarioId());
            ps.setString(2, p.getPalabraTexto());
            ps.setInt(3, p.getLongitud());
            ps.setInt(4, p.getIntentos());
            ps.setBoolean(5, p.isVictoria());
            ps.setInt(6, p.getTiempoSegundos());
            ps.executeUpdate();
            ps.close();
            con.close();
            return true;
        } catch (SQLException e) {
            System.err.println(e);
            return false;
        }
    }

    public LinkedList<Partida> listarPartidas() {
        LinkedList<Partida> lista = new LinkedList<>();
        Connection con = getConexion();
        String sql = "SELECT p.id, u.nombre, p.palabra_texto, p.longitud, p.intentos, p.victoria, p.tiempo_segundos, p.fecha "
                + "FROM partidas p, usuarios u "
                + "WHERE p.usuario_id = u.id "
                + "ORDER BY p.fecha DESC";
        try {
            Statement s = con.createStatement();
            ResultSet rs = s.executeQuery(sql);
            while (rs.next()) {
                Partida pa = new Partida();
                pa.setId(rs.getInt(1));
                pa.setNombreUsuario(rs.getString(2));
                pa.setPalabraTexto(rs.getString(3));
                pa.setLongitud(rs.getInt(4));
                pa.setIntentos(rs.getInt(5));
                pa.setVictoria(rs.getBoolean(6));
                pa.setTiempoSegundos(rs.getInt(7));
                pa.setFecha(rs.getString(8));
                lista.add(pa);
            }
            rs.close();
            s.close();
            con.close();
        } catch (SQLException e) {
            System.err.println(e);
        }
        return lista;
    }

    public boolean eliminarPartida(int id) {
        Connection con = getConexion();
        try {
            PreparedStatement ps = con.prepareStatement("DELETE FROM partidas WHERE id = ?");
            ps.setInt(1, id);
            ps.executeUpdate();
            ps.close();
            con.close();
            return true;
        } catch (SQLException e) {
            System.err.println(e);
            return false;
        }
    }
}
