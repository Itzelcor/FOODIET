package hacer_main_comun.Gestion_del_Equipo_Profesional.src.dao;

import hacer_main_comun.Gestion_del_Equipo_Profesional.src.model.Profesional;
import hacer_main_comun.Gestion_del_Equipo_Profesional.src.model.Rol;
import hacer_main_comun.Gestion_del_Equipo_Profesional.src.db.ConexionDB;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Acceso a datos para la entidad Profesional.
 *
 * Implementa las cuatro operaciones CRUD:
 *  - crear    → INSERT
 *  - buscarPorId / listarTodos → SELECT
 *  - actualizar → UPDATE
 *  - eliminar   → DELETE
 */
public class ProfesionalDAO {

    // =========================================================================
    // CREATE — Insertar un nuevo profesional
    // =========================================================================

    /**
     * Inserta un profesional en la base de datos.
     *
     * @param profesional Objeto con los datos a guardar (sin id)
     * @return true si se insertó correctamente, false en caso de error
     */
    public boolean crear(Profesional profesional) {
        String sql = "INSERT INTO profesionales (nombre, apellidos, especialidad, " +
                     "formacion, email, telefono, rol) VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (Connection con = ConexionDB.getConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, profesional.getNombre());
            ps.setString(2, profesional.getApellidos());
            ps.setString(3, profesional.getEspecialidad());
            ps.setString(4, profesional.getFormacion());
            ps.setString(5, profesional.getEmail());
            ps.setString(6, profesional.getTelefono());
            ps.setString(7, profesional.getRol().name()); // guarda el nombre del enum

            int filasAfectadas = ps.executeUpdate();
            return filasAfectadas > 0;

        } catch (SQLException e) {
            System.out.println("Error al crear profesional: " + e.getMessage());
            return false;
        }
    }

    // =========================================================================
    // READ — Consultar profesionales
    // =========================================================================

    /**
     * Busca un profesional por su ID.
     *
     * @param id Identificador del profesional
     * @return El profesional encontrado, o null si no existe
     */
    public Profesional buscarPorId(int id) {
        String sql = "SELECT * FROM profesionales WHERE id = ?";

        try (Connection con = ConexionDB.getConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return mapearProfesional(rs);
            }

        } catch (SQLException e) {
            System.out.println("Error al buscar profesional: " + e.getMessage());
        }

        return null;
    }

    /**
     * Devuelve todos los profesionales registrados.
     *
     * @return Lista de profesionales (vacía si no hay ninguno)
     */
    public List<Profesional> listarTodos() {
        List<Profesional> lista = new ArrayList<>();
        String sql = "SELECT * FROM profesionales";

        try (Connection con = ConexionDB.getConexion();
             Statement st = con.createStatement();
             ResultSet rs = st.executeQuery(sql)) {

            while (rs.next()) {
                lista.add(mapearProfesional(rs));
            }

        } catch (SQLException e) {
            System.out.println("Error al listar profesionales: " + e.getMessage());
        }

        return lista;
    }

    // =========================================================================
    // UPDATE — Actualizar datos de un profesional
    // =========================================================================

    /**
     * Actualiza todos los datos de un profesional existente.
     *
     * @param profesional Objeto con los nuevos datos (debe incluir el id)
     * @return true si se actualizó correctamente, false en caso de error
     */
    public boolean actualizar(Profesional profesional) {
        String sql = "UPDATE profesionales SET nombre = ?, apellidos = ?, especialidad = ?, " +
                     "formacion = ?, email = ?, telefono = ?, rol = ? WHERE id = ?";

        try (Connection con = ConexionDB.getConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, profesional.getNombre());
            ps.setString(2, profesional.getApellidos());
            ps.setString(3, profesional.getEspecialidad());
            ps.setString(4, profesional.getFormacion());
            ps.setString(5, profesional.getEmail());
            ps.setString(6, profesional.getTelefono());
            ps.setString(7, profesional.getRol().name());
            ps.setInt(8, profesional.getId());

            int filasAfectadas = ps.executeUpdate();
            return filasAfectadas > 0;

        } catch (SQLException e) {
            System.out.println("Error al actualizar profesional: " + e.getMessage());
            return false;
        }
    }

    // =========================================================================
    // DELETE — Eliminar un profesional
    // =========================================================================

    /**
     * Elimina un profesional de la base de datos por su ID.
     *
     * @param id Identificador del profesional a eliminar
     * @return true si se eliminó correctamente, false en caso de error
     */
    public boolean eliminar(int id) {
        String sql = "DELETE FROM profesionales WHERE id = ?";

        try (Connection con = ConexionDB.getConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, id);
            int filasAfectadas = ps.executeUpdate();
            return filasAfectadas > 0;

        } catch (SQLException e) {
            System.out.println("Error al eliminar profesional: " + e.getMessage());
            return false;
        }
    }

    // =========================================================================
    // Método auxiliar — convierte una fila del ResultSet en un objeto Profesional
    // =========================================================================
    private Profesional mapearProfesional(ResultSet rs) throws SQLException {
        Profesional p = new Profesional();
        p.setId(rs.getInt("id"));
        p.setNombre(rs.getString("nombre"));
        p.setApellidos(rs.getString("apellidos"));
        p.setEspecialidad(rs.getString("especialidad"));
        p.setFormacion(rs.getString("formacion"));
        p.setEmail(rs.getString("email"));
        p.setTelefono(rs.getString("telefono"));
        p.setRol(Rol.valueOf(rs.getString("rol"))); // convierte el String al enum
        return p;
    }
}
