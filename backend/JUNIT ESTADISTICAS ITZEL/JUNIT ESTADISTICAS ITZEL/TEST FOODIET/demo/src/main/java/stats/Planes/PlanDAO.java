package stats.Planes;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class PlanDAO {

    private Connection conexion;

    public PlanDAO(Connection conexion) {
        this.conexion = conexion;
    }

    public ArrayList<PlanDietetico> listarTodos() {
        ArrayList<PlanDietetico> lista = new ArrayList<>();
        String sql = "SELECT * FROM plan_dietetico ORDER BY fecha_inicio DESC";
        try {
            PreparedStatement ps = conexion.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                lista.add(new PlanDietetico(
                    rs.getInt("id_plan"),
                    rs.getInt("id_paciente"),
                    rs.getInt("id_nutricionista"),
                    rs.getString("objetivo"),
                    rs.getInt("calorias_diarias"),
                    rs.getString("fecha_inicio"),
                    rs.getString("fecha_fin"),
                    rs.getString("estado")
                ));
            }
        } catch (SQLException e) {
            System.out.println("Error al listar planes: " + e.getMessage());
        }
        return lista;
    }

    public ArrayList<PlanDietetico> listarPorPaciente(int idPaciente) {
        ArrayList<PlanDietetico> lista = new ArrayList<>();
        String sql = "SELECT * FROM plan_dietetico WHERE id_paciente = ?";
        try {
            PreparedStatement ps = conexion.prepareStatement(sql);
            ps.setInt(1, idPaciente);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                lista.add(new PlanDietetico(
                    rs.getInt("id_plan"),
                    rs.getInt("id_paciente"),
                    rs.getInt("id_nutricionista"),
                    rs.getString("objetivo"),
                    rs.getInt("calorias_diarias"),
                    rs.getString("fecha_inicio"),
                    rs.getString("fecha_fin"),
                    rs.getString("estado")
                ));
            }
        } catch (SQLException e) {
            System.out.println("Error al listar planes por paciente: " + e.getMessage());
        }
        return lista;
    }

    public PlanDietetico buscarPorId(int idPlan) {
        PlanDietetico plan = null;
        String sql = "SELECT * FROM plan_dietetico WHERE id_plan = ?";
        try {
            PreparedStatement ps = conexion.prepareStatement(sql);
            ps.setInt(1, idPlan);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                plan = new PlanDietetico(
                    rs.getInt("id_plan"),
                    rs.getInt("id_paciente"),
                    rs.getInt("id_nutricionista"),
                    rs.getString("objetivo"),
                    rs.getInt("calorias_diarias"),
                    rs.getString("fecha_inicio"),
                    rs.getString("fecha_fin"),
                    rs.getString("estado")
                );
            }
        } catch (SQLException e) {
            System.out.println("Error al buscar plan: " + e.getMessage());
        }
        return plan;
    }

    public boolean insertar(PlanDietetico p) {
        boolean resultado = false;
        String sql = "INSERT INTO plan_dietetico (id_paciente, id_nutricionista, objetivo, "
                   + "calorias_diarias, fecha_inicio, fecha_fin, estado) "
                   + "VALUES (?, ?, ?, ?, ?, ?, ?)";
        try {
            PreparedStatement ps = conexion.prepareStatement(sql);
            ps.setInt(1, p.getIdPaciente());
            ps.setInt(2, p.getIdNutricionista());
            ps.setString(3, p.getObjetivo());
            ps.setInt(4, p.getCaloriasDiarias());
            ps.setString(5, p.getFechaInicio());
            ps.setString(6, p.getFechaFin());
            ps.setString(7, p.getEstado());
            ps.executeUpdate();
            System.out.println("Plan insertado correctamente.");
            resultado = true;
        } catch (SQLException e) {
            System.out.println("Error al insertar plan: " + e.getMessage());
        }
        return resultado;
    }

    public boolean actualizar(int idPlan, String nuevoObjetivo, int nuevasCalorias) {
        boolean resultado = false;
        String sql = "UPDATE plan_dietetico SET objetivo = ?, calorias_diarias = ? WHERE id_plan = ?";
        try {
            PreparedStatement ps = conexion.prepareStatement(sql);
            ps.setString(1, nuevoObjetivo);
            ps.setInt(2, nuevasCalorias);
            ps.setInt(3, idPlan);
            int filas = ps.executeUpdate();
            if (filas > 0) {
                System.out.println("Plan actualizado correctamente.");
                resultado = true;
            }
        } catch (SQLException e) {
            System.out.println("Error al actualizar plan: " + e.getMessage());
        }
        return resultado;
    }

    public boolean desactivar(int idPlan) {
        boolean resultado = false;
        String sql = "UPDATE plan_dietetico SET estado = 'inactivo' WHERE id_plan = ?";
        try {
            PreparedStatement ps = conexion.prepareStatement(sql);
            ps.setInt(1, idPlan);
            int filas = ps.executeUpdate();
            if (filas > 0) {
                System.out.println("Plan desactivado correctamente.");
                resultado = true;
            }
        } catch (SQLException e) {
            System.out.println("Error al desactivar plan: " + e.getMessage());
        }
        return resultado;
    }

    public boolean eliminar(int idPlan) {
        boolean resultado = false;
        String sql = "DELETE FROM plan_dietetico WHERE id_plan = ?";
        try {
            PreparedStatement ps = conexion.prepareStatement(sql);
            ps.setInt(1, idPlan);
            int filas = ps.executeUpdate();
            if (filas > 0) {
                System.out.println("Plan eliminado correctamente.");
                resultado = true;
            }
        } catch (SQLException e) {
            System.out.println("Error al eliminar plan: " + e.getMessage());
        }
        return resultado;
    }

    public ArrayList<Comida> listarComidasPorMenu(int idMenu) {
        ArrayList<Comida> lista = new ArrayList<>();
        String sql = "SELECT * FROM comida WHERE id_menu = ?";
        try {
            PreparedStatement ps = conexion.prepareStatement(sql);
            ps.setInt(1, idMenu);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                lista.add(new Comida(
                    rs.getInt("id_comida"),
                    rs.getInt("id_menu"),
                    rs.getString("nombre"),
                    rs.getString("tipo"),
                    rs.getInt("calorias")
                ));
            }
        } catch (SQLException e) {
            System.out.println("Error al listar comidas: " + e.getMessage());
        }
        return lista;
    }
}
