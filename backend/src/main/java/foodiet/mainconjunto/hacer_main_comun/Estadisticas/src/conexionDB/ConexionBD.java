package hacer_main_comun.Estadisticas.src.conexionDB;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Gestiona la conexión con la base de datos FooDiet.
 * Solo crea una conexión y la reutiliza siempre.
 */
public class ConexionBD {

    private static Connection conexion = null;

    private static final String URL      = "jdbc:mysql://localhost:33306/FooDiet";
    private static final String USUARIO  = "superuser";
    private static final String PASSWORD = "alumnoalumno";

    public static Connection getConexion() {
        if (conexion == null) {
            try {
                conexion = DriverManager.getConnection(URL, USUARIO, PASSWORD);
                System.out.println("Conexión con FooDiet establecida.");
            } catch (SQLException e) {
                System.out.println("Error al conectar con la BD: " + e.getMessage());
            }
        }
        return conexion;
    }

    public static void cerrar() {
        try {
            if (conexion != null && !conexion.isClosed()) {
                conexion.close();
                System.out.println("Conexión cerrada.");
            }
        } catch (SQLException e) {
            System.out.println("Error al cerrar la conexión: " + e.getMessage());
        }
    }
}