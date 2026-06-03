import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class PlanAlimenticioDAO {

    public void mostrarPlanes() {

        String sql =
                "SELECT * FROM PlanAlimenticio";

        try {

            Connection conexion =
                    ConexionBD.conectar();

            PreparedStatement ps =
                    conexion.prepareStatement(sql);

            ResultSet rs =
                    ps.executeQuery();

            while (rs.next()) {

                System.out.println(
                        rs.getInt("id_plan")
                        + " - "
                        + rs.getString("nombre_plan")
                        + " - "
                        + rs.getInt("calorias_totales")
                );
            }

            rs.close();
            ps.close();
            conexion.close();

        } catch (SQLException e) {

            System.out.println(
                    e.getMessage()
            );
        }
    }
}