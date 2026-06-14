package dao;

import model.Sustitucion;
import db.ConexionDB;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Acceso a datos para la entidad Sustitucion.
 *
 * Gestiona los períodos en los que un profesional cubre a otro.
 */
public class SustitucionDAO {

    // =========================================================================
    // CREATE
    // =========================================================================

    /**
     * Registra una nueva sustitución.
     *
     * @param sustitucion Objeto con los datos de la sustitución
     * @return true si se creó correctamente
     */
    public boolean crear(Sustitucion sustitucion) {
        String sql = "INSERT INTO sustituciones (id_profesional_ausente, id_profesional_sustituto, " +
                     "fecha_inicio, fecha_fin, motivo) VALUES (?, ?, ?, ?, ?)";

        try (Connection con = ConexionDB.getConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, sustitucion.getIdProfesionalAusente());
            ps.setInt(2, sustitucion.getIdProfesionalSustituto());
            ps.setString(3, sustitucion.getFechaInicio());
            ps.setString(4, sustitucion.getFechaFin());
            ps.setString(5, sustitucion.getMotivo());

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            System.out.println("Error al crear sustitución: " + e.getMessage());
            return false;
        }
    }

    // =========================================================================
    // READ
    // =========================================================================

    /**
     * Devuelve todas las sustituciones registradas.
     *
     * @return Lista completa de sustituciones
     */
    public List<Sustitucion> listarTodas() {
        List<Sustitucion> lista = new ArrayList<>();
        String sql = "SELECT * FROM sustituciones ORDER BY fecha_inicio";

        try (Connection con = ConexionDB.getConexion();
             Statement st = con.createStatement();
             ResultSet rs = st.executeQuery(sql)) {

            while (rs.next()) {
                lista.add(mapearSustitucion(rs));
            }

        } catch (SQLException e) {
            System.out.println("Error al listar sustituciones: " + e.getMessage());
        }

        return lista;
    }

    /**
     * Devuelve las sustituciones donde un profesional actúa como sustituto.
     *
     * @param idSustituto ID del profesional sustituto
     * @return Lista de sustituciones en las que participa como sustituto
     */
    public List<Sustitucion> listarPorSustituto(int idSustituto) {
        List<Sustitucion> lista = new ArrayList<>();
        String sql = "SELECT * FROM sustituciones WHERE id_profesional_sustituto = ?";

        try (Connection con = ConexionDB.getConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, idSustituto);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                lista.add(mapearSustitucion(rs));
            }

        } catch (SQLException e) {
            System.out.println("Error al listar sustituciones por sustituto: " + e.getMessage());
        }

        return lista;
    }

    // =========================================================================
    // UPDATE
    // =========================================================================

    /**
     * Actualiza una sustitución existente.
     *
     * @param sustitucion Objeto con los nuevos datos (debe incluir el id)
     * @return true si se actualizó correctamente
     */
    public boolean actualizar(Sustitucion sustitucion) {
        String sql = "UPDATE sustituciones SET id_profesional_ausente = ?, " +
                     "id_profesional_sustituto = ?, fecha_inicio = ?, " +
                     "fecha_fin = ?, motivo = ? WHERE id = ?";

        try (Connection con = ConexionDB.getConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, sustitucion.getIdProfesionalAusente());
            ps.setInt(2, sustitucion.getIdProfesionalSustituto());
            ps.setString(3, sustitucion.getFechaInicio());
            ps.setString(4, sustitucion.getFechaFin());
            ps.setString(5, sustitucion.getMotivo());
            ps.setInt(6, sustitucion.getId());

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            System.out.println("Error al actualizar sustitución: " + e.getMessage());
            return false;
        }
    }

    // =========================================================================
    // DELETE
    // =========================================================================

    /**
     * Elimina una sustitución por su ID.
     *
     * @param id ID de la sustitución a eliminar
     * @return true si se eliminó correctamente
     */
    public boolean eliminar(int id) {
        String sql = "DELETE FROM sustituciones WHERE id = ?";

        try (Connection con = ConexionDB.getConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, id);
            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            System.out.println("Error al eliminar sustitución: " + e.getMessage());
            return false;
        }
    }

    // =========================================================================
    // Auxiliar
    // =========================================================================
    private Sustitucion mapearSustitucion(ResultSet rs) throws SQLException {
        Sustitucion s = new Sustitucion();
        s.setId(rs.getInt("id"));
        s.setIdProfesionalAusente(rs.getInt("id_profesional_ausente"));
        s.setIdProfesionalSustituto(rs.getInt("id_profesional_sustituto"));
        s.setFechaInicio(rs.getString("fecha_inicio"));
        s.setFechaFin(rs.getString("fecha_fin"));
        s.setMotivo(rs.getString("motivo"));
        return s;
    }
}
