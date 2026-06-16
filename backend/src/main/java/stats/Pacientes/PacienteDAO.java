package stats.Pacientes;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import stats.Usuarios.Usuario;
import stats.Usuarios.UsuarioDAO;

public class PacienteDAO {
 
    private Connection conexion;
 
    public PacienteDAO(Connection conexion) {
        this.conexion = conexion;
    }
 
    public ArrayList<Paciente> listarTodos() {
        ArrayList<Paciente> lista = new ArrayList<>();
        String sql = "SELECT * FROM paciente ORDER BY apellidos";
        try {
            PreparedStatement ps = conexion.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                lista.add(crearPaciente(rs));
            }
        } catch (SQLException e) {
            System.out.println("Error al listar pacientes: " + e.getMessage());
        }
        return lista;
    }
 
    public Paciente buscarPorId(int id) {
        Paciente paciente = null;
        String sql = "SELECT * FROM paciente WHERE id_paciente = ?";
        try {
            PreparedStatement ps = conexion.prepareStatement(sql);
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                paciente = crearPaciente(rs);
            }
        } catch (SQLException e) {
            System.out.println("Error al buscar paciente: " + e.getMessage());
        }
        return paciente;
    }
 
    public boolean insertar(Paciente p, int idUsuario) {
        boolean resultado = false;
        String fechaHoy = LocalDate.now().toString();
        String sql = "INSERT INTO paciente (id_usuario, dni, nombre, apellidos, telefono, "
                   + "email, direccion, fecha_nac, sexo, altura, tipo_paciente, fecha_registro) "
                   + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try {
            PreparedStatement ps = conexion.prepareStatement(sql);
            ps.setInt(1, idUsuario);
            ps.setString(2, p.getDni());
            ps.setString(3, p.getNombre());
            ps.setString(4, p.getApellidos());
            ps.setString(5, p.getTelefono());
            ps.setString(6, p.getEmail());
            ps.setString(7, p.getDireccion());
            ps.setString(8, p.getFechaNac());
            ps.setString(9, p.getSexo());
            ps.setDouble(10, p.getAltura());
            ps.setString(11, p.tipoPaciente().toLowerCase());
            ps.setString(12, fechaHoy);
            ps.executeUpdate();
            System.out.println("Paciente insertado: " + p.nombreCompleto());
            resultado = true;
        } catch (SQLException e) {
            System.out.println("Error al insertar paciente: " + e.getMessage());
        }
        return resultado;
    }
 
    public boolean actualizar(int id, String telefono, String email) {
        boolean resultado = false;
        String sql = "UPDATE paciente SET telefono = ?, email = ? WHERE id_paciente = ?";
        try {
            PreparedStatement ps = conexion.prepareStatement(sql);
            ps.setString(1, telefono);
            ps.setString(2, email);
            ps.setInt(3, id);
            int filas = ps.executeUpdate();
            if (filas > 0) {
                System.out.println("Paciente actualizado correctamente.");
                resultado = true;
            }
        } catch (SQLException e) {
            System.out.println("Error al actualizar paciente: " + e.getMessage());
        }
        return resultado;
    }
 
    public boolean eliminar(int id) {
        boolean resultado = false;
        String sql = "DELETE FROM paciente WHERE id_paciente = ?";
        try {
            PreparedStatement ps = conexion.prepareStatement(sql);
            ps.setInt(1, id);
            int filas = ps.executeUpdate();
            if (filas > 0) {
                System.out.println("Paciente eliminado correctamente.");
                resultado = true;
            }
        } catch (SQLException e) {
            System.out.println("Error al eliminar paciente: " + e.getMessage());
        }
        return resultado;
    }
 
    private Paciente crearPaciente(ResultSet rs) throws SQLException {
        String tipo      = rs.getString("tipo_paciente");
        int    id        = rs.getInt("id_paciente");
        String dni       = rs.getString("dni");
        String nombre    = rs.getString("nombre");
        String apellidos = rs.getString("apellidos");
        String telefono  = rs.getString("telefono");
        String email     = rs.getString("email");
        String direccion = rs.getString("direccion");
        String fechaNac  = rs.getString("fecha_nac");
        String sexo      = rs.getString("sexo");
        double altura    = rs.getDouble("altura");
 
        Paciente paciente = new PacienteAdulto(id, dni, nombre, apellidos,
                            telefono, email, direccion, fechaNac, sexo, altura);
 
        if (tipo != null && tipo.equals("joven")) {
            paciente = new PacienteJoven(id, dni, nombre, apellidos,
                       telefono, email, direccion, fechaNac, sexo, altura, false);
        } else if (tipo != null && tipo.equals("jubilado")) {
            paciente = new PacienteJubilado(id, dni, nombre, apellidos,
                       telefono, email, direccion, fechaNac, sexo, altura, true);
        }
        return paciente;
    }
}
