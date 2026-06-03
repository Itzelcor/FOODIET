import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class PacienteDAO {

    public void insertarPaciente(
            String nombre,
            String apellidos,
            double peso,
            double altura) {

        String sql =
                "INSERT INTO Paciente " +
                "(nombre, apellidos, peso, altura) " +
                "VALUES (?, ?, ?, ?)";

        try {

            Connection conexion =
                    ConexionBD.conectar();

            PreparedStatement ps =
                    conexion.prepareStatement(sql);

            ps.setString(1, nombre);
            ps.setString(2, apellidos);
            ps.setDouble(3, peso);
            ps.setDouble(4, altura);

            ps.executeUpdate();

            System.out.println(
                    "Paciente insertado."
            );

            ps.close();
            conexion.close();

        } catch (SQLException e) {

            System.out.println(
                    e.getMessage()
            );
        }
    }
}