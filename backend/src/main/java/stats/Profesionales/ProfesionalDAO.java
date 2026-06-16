package stats.Profesionales;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import stats.Usuarios.Usuario;
import stats.Usuarios.UsuarioDAO;

public class ProfesionalDAO {
 
    private Connection conexion;
 
    public ProfesionalDAO(Connection conexion) {
        this.conexion = conexion;
    }
 
    public ArrayList<ProfesionalClinica> listarTodos() {
        ArrayList<ProfesionalClinica> lista = new ArrayList<>();
        String sql = "SELECT * FROM profesional ORDER BY nom_completo";
        try {
            PreparedStatement ps = conexion.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                lista.add(crearProfesional(rs));
            }
        } catch (SQLException e) {
            System.out.println("Error al listar profesionales: " + e.getMessage());
        }
        return lista;
    }
 
    public ArrayList<ProfesionalClinica> listarActivos() {
        ArrayList<ProfesionalClinica> lista = new ArrayList<>();
        String sql = "SELECT * FROM profesional WHERE activo = 1 ORDER BY nom_completo";
        try {
            PreparedStatement ps = conexion.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                lista.add(crearProfesional(rs));
            }
        } catch (SQLException e) {
            System.out.println("Error al listar profesionales activos: " + e.getMessage());
        }
        return lista;
    }
 
    public boolean insertar(ProfesionalClinica p, int idUsuario) {
        boolean resultado = false;
        String fechaHoy = LocalDate.now().toString();
        String sql = "INSERT INTO profesional (id_usuario, nom_completo, email, "
                   + "telefono, anos_exp, activo, fecha_alta) "
                   + "VALUES (?, ?, ?, ?, ?, ?, ?)";
        try {
            PreparedStatement ps = conexion.prepareStatement(sql);
            ps.setInt(1, idUsuario);
            ps.setString(2, p.nombreCompleto());
            ps.setString(3, p.getEmail());
            ps.setString(4, p.getTelefono());
            ps.setInt(5, p.getAnosExp());
            ps.setBoolean(6, p.isActivo());
            ps.setString(7, fechaHoy);
            ps.executeUpdate();
            System.out.println("Profesional insertado: " + p.nombreCompleto());
            resultado = true;
        } catch (SQLException e) {
            System.out.println("Error al insertar profesional: " + e.getMessage());
        }
        return resultado;
    }
 
    public boolean darDeBaja(int idProfesional) {
        boolean resultado = false;
        String sql = "UPDATE profesional SET activo = 0 WHERE id_profesional = ?";
        try {
            PreparedStatement ps = conexion.prepareStatement(sql);
            ps.setInt(1, idProfesional);
            int filas = ps.executeUpdate();
            if (filas > 0) {
                System.out.println("Profesional dado de baja.");
                resultado = true;
            }
        } catch (SQLException e) {
            System.out.println("Error al dar de baja: " + e.getMessage());
        }
        return resultado;
    }
 
    public boolean eliminar(int idProfesional) {
        boolean resultado = false;
        String sql = "DELETE FROM profesional WHERE id_profesional = ?";
        try {
            PreparedStatement ps = conexion.prepareStatement(sql);
            ps.setInt(1, idProfesional);
            int filas = ps.executeUpdate();
            if (filas > 0) {
                System.out.println("Profesional eliminado.");
                resultado = true;
            }
        } catch (SQLException e) {
            System.out.println("Error al eliminar profesional: " + e.getMessage());
        }
        return resultado;
    }
 
    private ProfesionalClinica crearProfesional(ResultSet rs) throws SQLException {
        int     id     = rs.getInt("id_profesional");
        String  nombre = rs.getString("nom_completo");
        String  email  = rs.getString("email");
        String  tel    = rs.getString("telefono") != null ? rs.getString("telefono") : "";
        int     exp    = rs.getInt("anos_exp");
        boolean activo = rs.getBoolean("activo");
        return new Nutricionista(id, nombre, "", email, tel, exp, activo);
    }
}
 