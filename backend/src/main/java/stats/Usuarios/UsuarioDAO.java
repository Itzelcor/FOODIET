package stats.Usuarios;



import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.time.LocalDate;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;


public class UsuarioDAO {
 
    private Connection conexion;
 
    public UsuarioDAO(Connection conexion) {
        this.conexion = conexion;
    }
 
    public ArrayList<Usuario> listarTodos() {
        ArrayList<Usuario> lista = new ArrayList<>();
        String sql = "SELECT * FROM usuario ORDER BY rol";
        try {
            PreparedStatement ps = conexion.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                lista.add(new Usuario(
                    rs.getInt("id_usuario"),
                    rs.getString("email"),
                    rs.getString("password_hash"),
                    rs.getString("rol"),
                    rs.getBoolean("activo"),
                    rs.getString("fecha_alta")
                ));
            }
        } catch (SQLException e) {
            System.out.println("Error al listar usuarios: " + e.getMessage());
        }
        return lista;
    }
 
    public Usuario buscarPorEmail(String email) {
        Usuario usuario = null;
        String sql = "SELECT * FROM usuario WHERE email = ?";
        try {
            PreparedStatement ps = conexion.prepareStatement(sql);
            ps.setString(1, email);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                usuario = new Usuario(
                    rs.getInt("id_usuario"),
                    rs.getString("email"),
                    rs.getString("password_hash"),
                    rs.getString("rol"),
                    rs.getBoolean("activo"),
                    rs.getString("fecha_alta")
                );
            }
        } catch (SQLException e) {
            System.out.println("Error al buscar usuario: " + e.getMessage());
        }
        return usuario;
    }
 
    // Devuelve el id generado o -1 si falla
    public int insertarYObtenerID(String email, String password, String rol, String fechaAlta) {
        int idGenerado = -1;
        String sql = "INSERT INTO usuario (email, password_hash, rol, activo, fecha_alta) "
                   + "VALUES (?, ?, ?, 1, ?)";
        try {
            PreparedStatement ps = conexion.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, email);
            ps.setString(2, password);
            ps.setString(3, rol);
            ps.setString(4, fechaAlta);
            ps.executeUpdate();
 
            ResultSet keys = ps.getGeneratedKeys();
            if (keys.next()) {
                idGenerado = keys.getInt(1);
                System.out.println("Usuario creado con ID: " + idGenerado);
            }
        } catch (SQLException e) {
            System.out.println("Error al insertar usuario: " + e.getMessage());
        }
        return idGenerado;
    }
 
    public boolean desactivar(int idUsuario) {
        boolean resultado = false;
        String sql = "UPDATE usuario SET activo = 0 WHERE id_usuario = ?";
        try {
            PreparedStatement ps = conexion.prepareStatement(sql);
            ps.setInt(1, idUsuario);
            int filas = ps.executeUpdate();
            if (filas > 0) {
                System.out.println("Usuario desactivado correctamente.");
                resultado = true;
            }
        } catch (SQLException e) {
            System.out.println("Error al desactivar usuario: " + e.getMessage());
        }
        return resultado;
    }
 
    public boolean eliminar(int idUsuario) {
        boolean resultado = false;
        String sql = "DELETE FROM usuario WHERE id_usuario = ?";
        try {
            PreparedStatement ps = conexion.prepareStatement(sql);
            ps.setInt(1, idUsuario);
            int filas = ps.executeUpdate();
            if (filas > 0) {
                System.out.println("Usuario eliminado correctamente.");
                resultado = true;
            }
        } catch (SQLException e) {
            System.out.println("Error al eliminar usuario: " + e.getMessage());
        }
        return resultado;
    }
}
 