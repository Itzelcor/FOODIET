package foodiet.datos;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConexionBD {

    private static final String URL = "jdbc:mysql://localhost:33306/FooDiet";
    private static final String USUARIO = "superuser";
    private static final String CONTRASEÑA = "alumnoalumno";
    private static Connection conexion = null;

    private ConexionBD() {
    }

    public static Connection getConexion() {
        if (conexion == null) {
            try {
                Class.forName("com.mysql.cj.jdbc.Driver");
                conexion = DriverManager.getConnection(URL, USUARIO, CONTRASEÑA);
            } catch (ClassNotFoundException e) {
                System.out.println("Error: Driver JDBC no encontrado - " + e.getMessage());
            } catch (SQLException e) {
                System.out.println("Error al conectar a la base de datos - " + e.getMessage());
            }
        }
        return conexion;
    }

    public static void cerrarConexion() {
        if (conexion != null) {
            try {
                conexion.close();
                conexion = null;
            } catch (SQLException e) {
                System.out.println("Error al cerrar la conexi\u00f3n - " + e.getMessage());
            }
        }
    }
}
