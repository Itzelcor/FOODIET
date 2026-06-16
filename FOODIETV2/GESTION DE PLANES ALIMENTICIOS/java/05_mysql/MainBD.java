import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class MainBD {
    public static void main(String[] args) {
        String url = "jdbc:mysql://localhost:3306/foodiet_planes";
        String usuario = "root";
        String clave = "";

        try {
            Connection conexion = DriverManager.getConnection(url, usuario, clave);

            String insertar = "INSERT INTO plan_alimentacion "
                    + "(id_paciente, id_dietista, objetivo, fecha_inicio, fecha_fin, calorias_dia, observaciones) "
                    + "VALUES (?, ?, ?, ?, ?, ?, ?)";

            PreparedStatement ps = conexion.prepareStatement(insertar);
            ps.setInt(1, 1);
            ps.setInt(2, 1);
            ps.setString(3, "Plan sencillo");
            ps.setString(4, "2026-06-01");
            ps.setString(5, "2026-07-01");
            ps.setInt(6, 1900);
            ps.setString(7, "Ejemplo desde Java");
            ps.executeUpdate();
            ps.close();

            Statement st = conexion.createStatement();
            ResultSet rs = st.executeQuery(
                    "SELECT pl.id_plan, p.nombre, pl.objetivo, pl.calorias_dia "
                    + "FROM plan_alimentacion pl "
                    + "JOIN paciente p ON pl.id_paciente = p.id_paciente");

            while (rs.next()) {
                Plan plan = new Plan(
                        rs.getInt("id_plan"),
                        rs.getString("nombre"),
                        rs.getString("objetivo"),
                        rs.getInt("calorias_dia"));
                plan.mostrar();
            }

            rs.close();
            st.close();
            conexion.close();
        } catch (SQLException e) {
            System.out.println("Error conectando con MySQL");
            System.out.println(e.getMessage());
        }
    }
}
