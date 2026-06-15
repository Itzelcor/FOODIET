package hacer_main_comun.Estadisticas.src.dao;

import hacer_main_comun.Estadisticas.src.conexionDB.ConexionBD;
import hacer_main_comun.Estadisticas.src.modelo.Progreso;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 * Clase DAO (Data Access Object) del subsistema de Estadísticas.
 * Gestiona todas las operaciones CRUD sobre la tabla 'progreso'.
 */
public class ProgresoDAO {

    private Connection conexion;

    public ProgresoDAO() {
        this.conexion = ConexionBD.getConexion();
    }

    // ── LISTAR TODOS ──────────────────────────────────────────────
    /**
     * Devuelve todos los registros de progreso como ArrayList.
     */
    public ArrayList<Progreso> listarTodos() {
        ArrayList<Progreso> lista = new ArrayList<>();
        String sql = "SELECT * FROM progreso ORDER BY fecha DESC";

        try {
            PreparedStatement ps = conexion.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Progreso p = new Progreso(
                    rs.getInt("id_progreso"),
                    rs.getInt("id_paciente"),
                    rs.getInt("id_plan"),
                    rs.getString("fecha"),
                    rs.getDouble("peso"),
                    rs.getDouble("imc"),
                    rs.getString("observaciones")
                );
                lista.add(p);
            }
        } catch (SQLException e) {
            System.out.println("Error al listar: " + e.getMessage());
        }

        return lista;
    }

    // ── LISTAR POR PACIENTE ───────────────────────────────────────
    /**
     * Devuelve los registros de un paciente concreto.
     */
    public ArrayList<Progreso> listarPorPaciente(int idPaciente) {
        ArrayList<Progreso> lista = new ArrayList<>();
        String sql = "SELECT * FROM progreso WHERE id_paciente = ? ORDER BY fecha";

        try {
            PreparedStatement ps = conexion.prepareStatement(sql);
            ps.setInt(1, idPaciente);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                lista.add(new Progreso(
                    rs.getInt("id_progreso"),
                    rs.getInt("id_paciente"),
                    rs.getInt("id_plan"),
                    rs.getString("fecha"),
                    rs.getDouble("peso"),
                    rs.getDouble("imc"),
                    rs.getString("observaciones")
                ));
            }
        } catch (SQLException e) {
            System.out.println("Error al buscar por paciente: " + e.getMessage());
        }

        return lista;
    }

    // ── INSERTAR ──────────────────────────────────────────────────
    /**
     * Inserta un nuevo registro de progreso en la BD.
     */
    public boolean insertar(Progreso p) {
        String sql = "INSERT INTO progreso (id_paciente, id_plan, fecha, peso, imc, observaciones) "
                   + "VALUES (?, ?, ?, ?, ?, ?)";
        try {
            PreparedStatement ps = conexion.prepareStatement(sql);
            ps.setInt(1, p.getIdPaciente());
            ps.setInt(2, p.getIdPlan());
            ps.setString(3, p.getFecha());
            ps.setDouble(4, p.getPeso());
            ps.setDouble(5, p.getImc());
            ps.setString(6, p.getObservaciones());
            ps.executeUpdate();
            System.out.println("Progreso insertado correctamente.");
            return true;
        } catch (SQLException e) {
            System.out.println("Error al insertar: " + e.getMessage());
            return false;
        }
    }

    // ── ACTUALIZAR ────────────────────────────────────────────────
    /**
     * Actualiza el peso y el IMC de un registro existente.
     */
    public boolean actualizar(int idProgreso, double nuevoPeso, double nuevoImc) {
        String sql = "UPDATE progreso SET peso = ?, imc = ? WHERE id_progreso = ?";
        try {
            PreparedStatement ps = conexion.prepareStatement(sql);
            ps.setDouble(1, nuevoPeso);
            ps.setDouble(2, nuevoImc);
            ps.setInt(3, idProgreso);
            int filas = ps.executeUpdate();
            if (filas > 0) {
                System.out.println("Registro actualizado correctamente.");
                return true;
            } else {
                System.out.println("No se encontró el registro con ID " + idProgreso);
                return false;
            }
        } catch (SQLException e) {
            System.out.println("Error al actualizar: " + e.getMessage());
            return false;
        }
    }

    // ── ELIMINAR ──────────────────────────────────────────────────
    /**
     * Elimina un registro de progreso por su ID.
     */
    public boolean eliminar(int idProgreso) {
        String sql = "DELETE FROM progreso WHERE id_progreso = ?";
        try {
            PreparedStatement ps = conexion.prepareStatement(sql);
            ps.setInt(1, idProgreso);
            int filas = ps.executeUpdate();
            if (filas > 0) {
                System.out.println("Registro eliminado correctamente.");
                return true;
            } else {
                System.out.println("No se encontró el registro con ID " + idProgreso);
                return false;
            }
        } catch (SQLException e) {
            System.out.println("Error al eliminar: " + e.getMessage());
            return false;
        }
    }
}