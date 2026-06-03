import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConexionBD {

    private static final String URL =
            "jdbc:mysql://localhost:3306/FOODIET";

    private static final String USUARIO =
            "root";

    private static final String PASSWORD =
            "";

    public static Connection conectar() {

        Connection conexion = null;

        try {

            Class.forName("com.mysql.cj.jdbc.Driver");

            conexion = DriverManager.getConnection(
                    URL,
                    USUARIO,
                    PASSWORD
            );

            System.out.println(
                    "Conexión establecida correctamente."
            );

        } catch (ClassNotFoundException e) {

            System.out.println(
                    "No se encontró el driver MySQL."
            );

        } catch (SQLException e) {

            System.out.println(
                    "Error de conexión: " +
                    e.getMessage()
            );
        }

        return conexion;
    }

    public static void cerrar(Connection conexion) {

        try {

            if (conexion != null) {
                conexion.close();
            }

        } catch (SQLException e) {

            System.out.println(
                    "Error al cerrar conexión."
            );
        }
    }
}