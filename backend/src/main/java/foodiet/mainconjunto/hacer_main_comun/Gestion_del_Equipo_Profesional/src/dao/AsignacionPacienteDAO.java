package hacer_main_comun.Gestion_del_Equipo_Profesional.src.dao;

import hacer_main_comun.Gestion_del_Equipo_Profesional.src.model.AsignacionPaciente;
import hacer_main_comun.Gestion_del_Equipo_Profesional.src.db.ConexionDB;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Acceso a datos para la entidad AsignacionPaciente.
 *
 * Gestiona qué pacientes tiene asignados cada profesional.
 *
 * NOTA:
 * El campo idPaciente hace referencia a la tabla del subsistema de Pacientes
 * (desarrollado por otro compañero). Ver comentarios en AsignacionPaciente.java.
 */
public class AsignacionPacienteDAO {

    // =========================================================================
    // CREATE
    // =========================================================================

    /**
     * Registra la asignación de un paciente a un profesional.
     *
     * @param asignacion Objeto con los datos de la asignación
     * @return true si se creó correctamente
     */
    public boolean crear(AsignacionPaciente asignacion) {
        String sql = "INSERT INTO asignaciones_paciente (id_profesional, id_paciente, " +
                     "fecha_asignacion) VALUES (?, ?, ?)";

        try (Connection con = ConexionDB.getConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, asignacion.getIdProfesional());
            ps.setInt(2, asignacion.getIdPaciente());
            ps.setString(3, asignacion.getFechaAsignacion());

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            System.out.println("Error al crear asignación: " + e.getMessage());
            return false;
        }
    }

    // =========================================================================
    // READ
    // =========================================================================

    /**
     * Devuelve todas las asignaciones de un profesional concreto.
     *
     * @param idProfesional ID del profesional
     * @return Lista de asignaciones del profesional
     */
    public List<AsignacionPaciente> listarPorProfesional(int idProfesional) {
        List<AsignacionPaciente> lista = new ArrayList<>();
        String sql = "SELECT * FROM asignaciones_paciente WHERE id_profesional = ?";

        try (Connection con = ConexionDB.getConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, idProfesional);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                lista.add(mapearAsignacion(rs));
            }

        } catch (SQLException e) {
            System.out.println("Error al listar asignaciones: " + e.getMessage());
        }

        return lista;
    }

    /**
     * Devuelve todas las asignaciones registradas.
     *
     * @return Lista completa de asignaciones
     */
    public List<AsignacionPaciente> listarTodas() {
        List<AsignacionPaciente> lista = new ArrayList<>();
        String sql = "SELECT * FROM asignaciones_paciente";

        try (Connection con = ConexionDB.getConexion();
             Statement st = con.createStatement();
             ResultSet rs = st.executeQuery(sql)) {

            while (rs.next()) {
                lista.add(mapearAsignacion(rs));
            }

        } catch (SQLException e) {
            System.out.println("Error al listar todas las asignaciones: " + e.getMessage());
        }

        return lista;
    }

    // =========================================================================
    // UPDATE
    // =========================================================================

    /**
     * Actualiza los datos de una asignación existente.
     *
     * @param asignacion Objeto con los nuevos datos (debe incluir el id)
     * @return true si se actualizó correctamente
     */
    public boolean actualizar(AsignacionPaciente asignacion) {
        String sql = "UPDATE asignaciones_paciente SET id_profesional = ?, id_paciente = ?, " +
                     "fecha_asignacion = ? WHERE id = ?";

        try (Connection con = ConexionDB.getConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, asignacion.getIdProfesional());
            ps.setInt(2, asignacion.getIdPaciente());
            ps.setString(3, asignacion.getFechaAsignacion());
            ps.setInt(4, asignacion.getId());

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            System.out.println("Error al actualizar asignación: " + e.getMessage());
            return false;
        }
    }

    // =========================================================================
    // DELETE
    // =========================================================================

    /**
     * Elimina una asignación por su ID.
     *
     * @param id ID de la asignación a eliminar
     * @return true si se eliminó correctamente
     */
    public boolean eliminar(int id) {
        String sql = "DELETE FROM asignaciones_paciente WHERE id = ?";

        try (Connection con = ConexionDB.getConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, id);
            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            System.out.println("Error al eliminar asignación: " + e.getMessage());
            return false;
        }
    }

    // =========================================================================
    // Auxiliar
    // =========================================================================
    private AsignacionPaciente mapearAsignacion(ResultSet rs) throws SQLException {
        AsignacionPaciente a = new AsignacionPaciente();
        a.setId(rs.getInt("id"));
        a.setIdProfesional(rs.getInt("id_profesional"));
        a.setIdPaciente(rs.getInt("id_paciente"));
        a.setFechaAsignacion(rs.getString("fecha_asignacion"));
        return a;
    }
}
