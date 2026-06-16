package stats;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import stats.Planes.PlanDAO;

public class ProgresoDAO {

    private Connection conexion;

    public ProgresoDAO() {
        this.conexion = ConexionBD.getConexion();
    }

    public ArrayList<Progreso> listarTodos() {
        ArrayList<Progreso> lista = new ArrayList<>();
        String sql = "SELECT * FROM progreso ORDER BY fecha DESC";
        try {
            PreparedStatement ps = conexion.prepareStatement(sql);
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
            System.out.println("Error al listar: " + e.getMessage());
        }
        return lista;
    }

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

    public boolean insertar(Progreso p) {
        boolean resultado = false;
        String sql = "INSERT INTO progreso (id_paciente, id_plan, fecha, peso, imc, observaciones) "
                   + "VALUES (?, ?, ?, ?, ?, ?)";
        try {
            // Check patient exists to avoid FK error
            stats.Pacientes.PacienteDAO pacienteDAO = new stats.Pacientes.PacienteDAO(conexion);
            if (pacienteDAO.buscarPorId(p.getIdPaciente()) == null) {
                System.out.println("Error al insertar: paciente con ID " + p.getIdPaciente() + " no existe.");
                return false;
            }
            PlanDAO planDAO = new PlanDAO(conexion);
            if (planDAO.buscarPorId(p.getIdPlan()) == null) {
                System.out.println("Error al insertar: plan con ID " + p.getIdPlan() + " no existe.");
                return false;
            }
            PreparedStatement ps = conexion.prepareStatement(sql);
            ps.setInt(1, p.getIdPaciente());
            ps.setInt(2, p.getIdPlan());
            ps.setString(3, p.getFecha());
            ps.setDouble(4, p.getPeso());
            ps.setDouble(5, p.getImc());
            ps.setString(6, p.getObservaciones());
            ps.executeUpdate();
            System.out.println("Progreso insertado correctamente.");
            resultado = true;
        } catch (SQLException e) {
            System.out.println("Error al insertar: " + e.getMessage());
        }
        return resultado;
    }

    public boolean actualizar(int idProgreso, double nuevoPeso, double nuevoImc) {
        boolean resultado = false;
        String sql = "UPDATE progreso SET peso = ?, imc = ? WHERE id_progreso = ?";
        try {
            PreparedStatement ps = conexion.prepareStatement(sql);
            ps.setDouble(1, nuevoPeso);
            ps.setDouble(2, nuevoImc);
            ps.setInt(3, idProgreso);
            int filas = ps.executeUpdate();
            if (filas > 0) {
                System.out.println("Registro actualizado correctamente.");
                resultado = true;
            } else {
                System.out.println("No se encontró el registro con ID " + idProgreso);
            }
        } catch (SQLException e) {
            System.out.println("Error al actualizar: " + e.getMessage());
        }
        return resultado;
    }

    public boolean eliminar(int idProgreso) {
        boolean resultado = false;
        String sql = "DELETE FROM progreso WHERE id_progreso = ?";
        try {
            PreparedStatement ps = conexion.prepareStatement(sql);
            ps.setInt(1, idProgreso);
            int filas = ps.executeUpdate();
            if (filas > 0) {
                System.out.println("Registro eliminado correctamente.");
                resultado = true;
            } else {
                System.out.println("No se encontró el registro con ID " + idProgreso);
            }
        } catch (SQLException e) {
            System.out.println("Error al eliminar: " + e.getMessage());
        }
        return resultado;
    }
}
