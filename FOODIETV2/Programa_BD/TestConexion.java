import java.sql.Connection;

public class TestConexion {

    public static void main(String[] args) {

        Connection conexion =
                ConexionBD.conectar();

        if (conexion != null) {

            System.out.println(
                    "FOODIET conectado a MySQL."
            );

            ConexionBD.cerrar(conexion);
        }
    }
}