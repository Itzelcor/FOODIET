package hacer_main_comun.Gestion_del_Equipo_Profesional.src.dao;

import hacer_main_comun.Gestion_del_Equipo_Profesional.src.model.Agenda;
import hacer_main_comun.Gestion_del_Equipo_Profesional.src.model.TipoEvento;
import hacer_main_comun.Gestion_del_Equipo_Profesional.src.db.ConexionDB;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Acceso a datos para la entidad Agenda.
 *
 * Permite gestionar los eventos de la agenda de cada profesional:
 * consultas, vacaciones y sustituciones.
 */
public class AgendaDAO {

    // =========================================================================
    // CREATE
    // =========================================================================

    /**
     * Añade un evento a la agenda.
     *
     * @param agenda Objeto con los datos del evento
     * @return true si se insertó correctamente
     */
    public boolean crear(Agenda agenda) {
        String sql = "INSERT INTO agenda (id_profesional, fecha, tipo_evento, descripcion) " +
                     "VALUES (?, ?, ?, ?)";

        try (Connection con = ConexionDB.getConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, agenda.getIdProfesional());
            ps.setString(2, agenda.getFecha());
            ps.setString(3, agenda.getTipoEvento().name());
            ps.setString(4, agenda.getDescripcion());

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            System.out.println("Error al crear evento de agenda: " + e.getMessage());
            return false;
        }
    }

    // =========================================================================
    // READ
    // =========================================================================

    /**
     * Devuelve todos los eventos de agenda de un profesional.
     *
     * @param idProfesional ID del profesional
     * @return Lista de eventos de su agenda
     */
    public List<Agenda> listarPorProfesional(int idProfesional) {
        List<Agenda> lista = new ArrayList<>();
        String sql = "SELECT * FROM agenda WHERE id_profesional = ? ORDER BY fecha";

        try (Connection con = ConexionDB.getConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, idProfesional);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                lista.add(mapearAgenda(rs));
            }

        } catch (SQLException e) {
            System.out.println("Error al listar agenda: " + e.getMessage());
        }

        return lista;
    }

    /**
     * Devuelve todos los eventos de agenda de todos los profesionales.
     *
     * @return Lista completa de eventos
     */
    public List<Agenda> listarTodos() {
        List<Agenda> lista = new ArrayList<>();
        String sql = "SELECT * FROM agenda ORDER BY fecha";

        try (Connection con = ConexionDB.getConexion();
             Statement st = con.createStatement();
             ResultSet rs = st.executeQuery(sql)) {

            while (rs.next()) {
                lista.add(mapearAgenda(rs));
            }

        } catch (SQLException e) {
            System.out.println("Error al listar toda la agenda: " + e.getMessage());
        }

        return lista;
    }

    // =========================================================================
    // UPDATE
    // =========================================================================

    /**
     * Actualiza un evento de agenda existente.
     *
     * @param agenda Objeto con los nuevos datos (debe incluir el id)
     * @return true si se actualizó correctamente
     */
    public boolean actualizar(Agenda agenda) {
        String sql = "UPDATE agenda SET id_profesional = ?, fecha = ?, " +
                     "tipo_evento = ?, descripcion = ? WHERE id = ?";

        try (Connection con = ConexionDB.getConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, agenda.getIdProfesional());
            ps.setString(2, agenda.getFecha());
            ps.setString(3, agenda.getTipoEvento().name());
            ps.setString(4, agenda.getDescripcion());
            ps.setInt(5, agenda.getId());

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            System.out.println("Error al actualizar evento de agenda: " + e.getMessage());
            return false;
        }
    }

    // =========================================================================
    // DELETE
    // =========================================================================

    /**
     * Elimina un evento de agenda por su ID.
     *
     * @param id ID del evento a eliminar
     * @return true si se eliminó correctamente
     */
    public boolean eliminar(int id) {
        String sql = "DELETE FROM agenda WHERE id = ?";

        try (Connection con = ConexionDB.getConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, id);
            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            System.out.println("Error al eliminar evento de agenda: " + e.getMessage());
            return false;
        }
    }

    // =========================================================================
    // Auxiliar
    // =========================================================================
    private Agenda mapearAgenda(ResultSet rs) throws SQLException {
        Agenda a = new Agenda();
        a.setId(rs.getInt("id"));
        a.setIdProfesional(rs.getInt("id_profesional"));
        a.setFecha(rs.getString("fecha"));
        a.setTipoEvento(TipoEvento.valueOf(rs.getString("tipo_evento")));
        a.setDescripcion(rs.getString("descripcion"));
        return a;
    }
}
