package modelo;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConexionBD {

    private final String bd = "wordle";
    private final String user = "root";
    private final String password = "";
    private final String url = "jdbc:mysql://localhost:3306/" + bd
            + "?useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true";
    private Connection con = null;

    public Connection getConexion() {
        try {
            con = DriverManager.getConnection(this.url, this.user, this.password);
        } catch (SQLException e) {
            System.err.println("Error de conexión: " + e);
        }
        return con;
    }
}
