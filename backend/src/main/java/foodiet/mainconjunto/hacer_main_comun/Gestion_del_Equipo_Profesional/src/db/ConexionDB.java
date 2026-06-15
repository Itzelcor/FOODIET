package hacer_main_comun.Gestion_del_Equipo_Profesional.src.db;
// db/ConexionDB.java
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConexionDB {

    private static final String URL = "jdbc:mysql://localhost:33306/FooDiet";
    private static final String USER = "superuser";
    private static final String PWD = "alumnoalumno";

    // Constructor privado — no se instancia, solo se usa el método estático
    private ConexionDB() {}

    public static Connection getConexion() throws SQLException {
        return DriverManager.getConnection(URL, USER, PWD);
    }
}