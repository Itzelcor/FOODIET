package stats.Pacientes;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class CitaDAO {

    private Connection conexion;

    public CitaDAO(Connection conexion) {
        this.conexion = conexion;
    }

    public ArrayList<Cita> listarTodas() {
        ArrayList<Cita> lista = new ArrayList<>();
        String sql = "SELECT * FROM cita ORDER BY fecha_cita DESC";
        try {
            PreparedStatement ps = conexion.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                lista.add(new Cita(
                        rs.getInt("id_cita"),
                        rs.getInt("id_paciente"),
                        rs.getInt("id_profesional"),
                        rs.getString("fecha_cita"),
                        rs.getString("hora_cita"),
                        rs.getString("estado_cita"),
                        rs.getString("modalidad"),
                        rs.getString("motivo_consulta"),
                        rs.getString("observacion")));
            }
        } catch (SQLException e) {
            System.out.println("Error al listar citas: " + e.getMessage());
        }
        return lista;
    }

    public ArrayList<Cita> listarPorPaciente(int idPaciente) {
        ArrayList<Cita> lista = new ArrayList<>();
        String sql = "SELECT * FROM cita WHERE id_paciente = ? ORDER BY fecha_cita DESC";
        try {
            PreparedStatement ps = conexion.prepareStatement(sql);
            ps.setInt(1, idPaciente);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                lista.add(new Cita(
                        rs.getInt("id_cita"),
                        rs.getInt("id_paciente"),
                        rs.getInt("id_profesional"),
                        rs.getString("fecha_cita"),
                        rs.getString("hora_cita"),
                        rs.getString("estado_cita"),
                        rs.getString("modalidad"),
                        rs.getString("motivo_consulta"),
                        rs.getString("observacion")));
            }
        } catch (SQLException e) {
            System.out.println("Error al listar citas por paciente: " + e.getMessage());
        }
        return lista;
    }

    public boolean insertar(Cita c) {
        boolean resultado = false;
        String sql = "INSERT INTO cita (id_paciente, id_profesional, fecha_cita, hora_cita, "
                + "estado_cita, modalidad, motivo_consulta) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try {
            PreparedStatement ps = conexion.prepareStatement(sql);
            ps.setInt(1, c.getIdPaciente());
            ps.setInt(2, c.getIdProfesional());
            ps.setString(3, c.getFecha());
            ps.setString(4, c.getHora());
            ps.setString(5, c.getEstado());
            ps.setString(6, c.getModalidad());
            ps.setString(7, c.getMotivoConsulta());
            ps.executeUpdate();
            System.out.println("Cita insertada correctamente.");
            resultado = true;
        } catch (SQLException e) {
            System.out.println("Error al insertar cita: " + e.getMessage());
        }
        return resultado;
    }

    public boolean cancelar(int idCita) {
        boolean resultado = false;
        String sql = "UPDATE cita SET estado_cita = 'cancelada' WHERE id_cita = ?";
        try {
            PreparedStatement ps = conexion.prepareStatement(sql);
            ps.setInt(1, idCita);
            int filas = ps.executeUpdate();
            if (filas > 0) {
                System.out.println("Cita cancelada.");
                resultado = true;
            }
        } catch (SQLException e) {
            System.out.println("Error al cancelar cita: " + e.getMessage());
        }
        return resultado;
    }

    public boolean eliminar(int idCita) {
        boolean resultado = false;
        String sql = "DELETE FROM cita WHERE id_cita = ?";
        try {
            PreparedStatement ps = conexion.prepareStatement(sql);
            ps.setInt(1, idCita);
            int filas = ps.executeUpdate();
            if (filas > 0) {
                System.out.println("Cita eliminada.");
                resultado = true;
            }
        } catch (SQLException e) {
            System.out.println("Error al eliminar cita: " + e.getMessage());
        }
        return resultado;
    }
}
