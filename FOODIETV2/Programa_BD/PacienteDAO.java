import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class PacienteDAO {

    public void insertarPaciente(
            Paciente paciente) {

        String sql =
        "INSERT INTO Paciente(nombre) VALUES(?)";

        try {

            Connection con =
                    ConexionBD.conectar();

            PreparedStatement ps =
                    con.prepareStatement(sql);

            ps.setString(
                    1,
                    paciente.getNombre()
            );

            ps.executeUpdate();

            System.out.println(
                    "Paciente guardado."
            );

            ps.close();
            con.close();

        } catch (SQLException e) {

            System.out.println(
                    e.getMessage()
            );
        }
    }
}