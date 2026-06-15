package com.foodiet.datos;

import com.foodiet.modelo.Cita;
import com.foodiet.modelo.Paciente;
import com.foodiet.modelo.PacienteAdulto;
import com.foodiet.modelo.PacienteJoven;
import com.foodiet.modelo.PacienteJubilado;
import com.foodiet.modelo.PlanAlimenticio;
import com.foodiet.modelo.ProfesionalClinica;
import com.foodiet.modelo.Usuario;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class GestorBD {

    private Connection conexion;

    public GestorBD() {
        this.conexion = ConexionBD.getConexion();
    }

    public int insertarPaciente(Paciente p) {
        int idGenerado = -1;
        String sql = "INSERT INTO pacientes (id_usuario, nombre, apellido, edad, peso, altura, telefono, " +
                     "historial_medico, objetivos_nutricionales, tipo_paciente) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try {
            PreparedStatement ps = conexion.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setInt(1, p.getIdUsuario());
            ps.setString(2, p.getNombre());
            ps.setString(3, p.getApellido());
            ps.setInt(4, p.getEdad());
            ps.setDouble(5, p.getPeso());
            ps.setDouble(6, p.getAltura());
            ps.setString(7, p.getTelefono());
            ps.setString(8, p.getHistorialMedico());
            ps.setString(9, p.getObjetivosNutricionales());
            ps.setString(10, p.tipoPaciente());
            ps.executeUpdate();
            ResultSet rs = ps.getGeneratedKeys();
            if (rs.next()) {
                idGenerado = rs.getInt(1);
                p.setId(idGenerado);
            }
            rs.close();
            ps.close();
        } catch (SQLException e) {
            System.out.println("Error al insertar paciente - " + e.getMessage());
        }
        return idGenerado;
    }

    public Paciente obtenerPaciente(int id) {
        Paciente resultado = null;
        String sql = "SELECT * FROM pacientes WHERE id_paciente = ?";
        try {
            PreparedStatement ps = conexion.prepareStatement(sql);
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                resultado = crearPaciente(rs);
            }
            rs.close();
            ps.close();
        } catch (SQLException e) {
            System.out.println("Error al obtener paciente - " + e.getMessage());
        }
        return resultado;
    }

    public ArrayList<Paciente> obtenerTodosPacientes() {
        ArrayList<Paciente> lista = new ArrayList<>();
        String sql = "SELECT * FROM pacientes ORDER BY apellido, nombre";
        try {
            Statement st = conexion.createStatement();
            ResultSet rs = st.executeQuery(sql);
            while (rs.next()) {
                lista.add(crearPaciente(rs));
            }
            rs.close();
            st.close();
        } catch (SQLException e) {
            System.out.println("Error al listar pacientes - " + e.getMessage());
        }
        return lista;
    }

    public boolean eliminarPaciente(int idPaciente) {
        boolean exito = false;
        String sql = "DELETE FROM pacientes WHERE id_paciente = ?";
        try {
            PreparedStatement ps = conexion.prepareStatement(sql);
            ps.setInt(1, idPaciente);
            int filas = ps.executeUpdate();
            exito = filas > 0;
            ps.close();
        } catch (SQLException e) {
            System.out.println("Error al eliminar paciente - " + e.getMessage());
        }
        return exito;
    }

    public int insertarCita(Cita c) {
        int idGenerado = -1;
        String sql = "INSERT INTO citas (id_paciente, id_profesional, fecha_cita, hora_cita, " +
                     "modalidad, estado, motivo, enlace_online) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        try {
            PreparedStatement ps = conexion.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setInt(1, c.getIdPaciente());
            ps.setInt(2, c.getIdProfesional());
            ps.setString(3, c.getAnyo() + "-" + String.format("%02d", c.getMes()) + "-" + String.format("%02d", c.getDia()));
            ps.setString(4, "10:00:00");
            ps.setString(5, c.getModalidad());
            ps.setString(6, c.getEstado());
            ps.setString(7, c.getMotivo());
            ps.setString(8, c.getEnlaceOnline());
            ps.executeUpdate();
            ResultSet rs = ps.getGeneratedKeys();
            if (rs.next()) {
                idGenerado = rs.getInt(1);
                c.setId(idGenerado);
                c.setCodigoCita(idGenerado);
            }
            rs.close();
            ps.close();
        } catch (SQLException e) {
            System.out.println("Error al insertar cita - " + e.getMessage());
        }
        return idGenerado;
    }

    public ArrayList<Cita> obtenerTodasCitas() {
        ArrayList<Cita> lista = new ArrayList<>();
        String sql = "SELECT * FROM citas ORDER BY fecha_cita DESC";
        try {
            Statement st = conexion.createStatement();
            ResultSet rs = st.executeQuery(sql);
            while (rs.next()) {
                int idCita = rs.getInt("id_cita");
                int idPaciente = rs.getInt("id_paciente");
                String modalidad = rs.getString("modalidad");
                String estado = rs.getString("estado");
                String motivo = rs.getString("motivo") != null ? rs.getString("motivo") : "";
                String enlaceOnline = rs.getString("enlace_online") != null ? rs.getString("enlace_online") : "";
                java.sql.Date fechaSQL = rs.getDate("fecha_cita");
                String[] partes = fechaSQL.toString().split("-");
                int anyo = Integer.parseInt(partes[0]);
                int mes = Integer.parseInt(partes[1]);
                int dia = Integer.parseInt(partes[2]);

                Paciente paciente = obtenerPaciente(idPaciente);
                ProfesionalClinica profesional = new ProfesionalClinica(0, "", "", "", 0, "", "");

                Cita cita = new Cita(idCita, paciente, profesional, dia, mes, anyo,
                                     modalidad, estado, motivo, enlaceOnline);
                cita.setId(idCita);
                cita.setCodigoCita(idCita);
                lista.add(cita);
            }
            rs.close();
            st.close();
        } catch (SQLException e) {
            System.out.println("Error al listar citas - " + e.getMessage());
        }
        return lista;
    }

    public boolean actualizarEstadoCita(int idCita, String nuevoEstado) {
        boolean exito = false;
        String sql = "UPDATE citas SET estado = ? WHERE id_cita = ?";
        try {
            PreparedStatement ps = conexion.prepareStatement(sql);
            ps.setString(1, nuevoEstado);
            ps.setInt(2, idCita);
            int filas = ps.executeUpdate();
            exito = filas > 0;
            ps.close();
        } catch (SQLException e) {
            System.out.println("Error al actualizar estado de cita - " + e.getMessage());
        }
        return exito;
    }

    public boolean eliminarCita(int idCita) {
        boolean exito = false;
        String sql = "DELETE FROM citas WHERE id_cita = ?";
        try {
            PreparedStatement ps = conexion.prepareStatement(sql);
            ps.setInt(1, idCita);
            int filas = ps.executeUpdate();
            exito = filas > 0;
            ps.close();
        } catch (SQLException e) {
            System.out.println("Error al eliminar cita - " + e.getMessage());
        }
        return exito;
    }

    public int insertarPlan(PlanAlimenticio plan) {
        int idGenerado = -1;
        String sql = "INSERT INTO planes_alimentacion (id_paciente, id_profesional, fecha_inicio, fecha_fin, " +
                     "objetivo, descripcion, calorias_diarias) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try {
            PreparedStatement ps = conexion.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setInt(1, plan.getIdPaciente());
            ps.setInt(2, plan.getIdProfesional());
            ps.setString(3, plan.getFechaInicio());
            ps.setString(4, plan.getFechaFin());
            ps.setString(5, plan.getObjetivo());
            ps.setString(6, plan.getDescripcion());
            ps.setInt(7, plan.getCaloriasDiarias());
            ps.executeUpdate();
            ResultSet rs = ps.getGeneratedKeys();
            if (rs.next()) {
                idGenerado = rs.getInt(1);
                plan.setId(idGenerado);
            }
            rs.close();
            ps.close();
        } catch (SQLException e) {
            System.out.println("Error al insertar plan alimenticio - " + e.getMessage());
        }
        return idGenerado;
    }

    public ArrayList<PlanAlimenticio> obtenerTodosPlanes() {
        ArrayList<PlanAlimenticio> lista = new ArrayList<>();
        String sql = "SELECT * FROM planes_alimentacion ORDER BY fecha_inicio DESC";
        try {
            Statement st = conexion.createStatement();
            ResultSet rs = st.executeQuery(sql);
            while (rs.next()) {
                PlanAlimenticio plan = new PlanAlimenticio(
                    rs.getInt("id_plan"),
                    rs.getInt("id_paciente"),
                    rs.getInt("id_profesional"),
                    rs.getDate("fecha_inicio") != null ? rs.getDate("fecha_inicio").toString() : "",
                    rs.getDate("fecha_fin") != null ? rs.getDate("fecha_fin").toString() : null,
                    rs.getString("objetivo") != null ? rs.getString("objetivo") : "",
                    rs.getString("descripcion") != null ? rs.getString("descripcion") : "",
                    rs.getInt("calorias_diarias")
                );
                lista.add(plan);
            }
            rs.close();
            st.close();
        } catch (SQLException e) {
            System.out.println("Error al listar planes alimenticios - " + e.getMessage());
        }
        return lista;
    }

    public boolean eliminarPlan(int idPlan) {
        boolean exito = false;
        String sql = "DELETE FROM planes_alimentacion WHERE id_plan = ?";
        try {
            PreparedStatement ps = conexion.prepareStatement(sql);
            ps.setInt(1, idPlan);
            int filas = ps.executeUpdate();
            exito = filas > 0;
            ps.close();
        } catch (SQLException e) {
            System.out.println("Error al eliminar plan alimenticio - " + e.getMessage());
        }
        return exito;
    }

    public Usuario autenticarUsuario(String nombreUsuario, String contraseña) {
        Usuario resultado = null;
        String sql = "SELECT * FROM usuarios WHERE nombre_usuario = ? AND contraseña_hash = SHA2(?, 256) AND activo = TRUE";
        try {
            PreparedStatement ps = conexion.prepareStatement(sql);
            ps.setString(1, nombreUsuario);
            ps.setString(2, contraseña);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                resultado = new Usuario(
                    rs.getInt("id_usuario"),
                    rs.getString("nombre_usuario"),
                    rs.getString("email"),
                    rs.getString("contraseña_hash"),
                    rs.getString("rol")
                );
            }
            rs.close();
            ps.close();
        } catch (SQLException e) {
            System.out.println("Error en autenticaci\u00f3n - " + e.getMessage());
        }
        return resultado;
    }

    public boolean registrarUsuario(String nombreUsuario, String email, String contraseña, String rol) {
        boolean exito = false;
        String sql = "INSERT INTO usuarios (nombre_usuario, email, contraseña_hash, rol) VALUES (?, ?, SHA2(?, 256), ?)";
        try {
            PreparedStatement ps = conexion.prepareStatement(sql);
            ps.setString(1, nombreUsuario);
            ps.setString(2, email);
            ps.setString(3, contraseña);
            ps.setString(4, rol);
            int filas = ps.executeUpdate();
            exito = filas > 0;
            ps.close();
        } catch (SQLException e) {
            System.out.println("Error al registrar usuario - " + e.getMessage());
        }
        return exito;
    }

    private Paciente crearPaciente(ResultSet rs) throws SQLException {
        Paciente resultado = null;
        int id = rs.getInt("id_paciente");
        int idUsuario = rs.getInt("id_usuario");
        String nombre = rs.getString("nombre");
        String apellido = rs.getString("apellido");
        int edad = rs.getInt("edad");
        double peso = rs.getDouble("peso");
        double altura = rs.getDouble("altura");
        String telefono = rs.getString("telefono") != null ? rs.getString("telefono") : "";
        String email = rs.getString("email") != null ? rs.getString("email") : "";
        String historial = rs.getString("historial_medico") != null ? rs.getString("historial_medico") : "";
        String objetivos = rs.getString("objetivos_nutricionales") != null ? rs.getString("objetivos_nutricionales") : "";
        String tipo = rs.getString("tipo_paciente");

        switch (tipo) {
            case "Joven":
                resultado = new PacienteJoven(id, nombre, apellido, edad, peso, altura,
                                              telefono, email, historial, objetivos);
                break;
            case "Jubilado":
                resultado = new PacienteJubilado(id, nombre, apellido, edad, peso, altura,
                                                 telefono, email, historial, objetivos);
                break;
            default:
                resultado = new PacienteAdulto(id, nombre, apellido, edad, peso, altura,
                                               telefono, email, historial, objetivos);
                break;
        }
        if (resultado != null) {
            resultado.setIdUsuario(idUsuario);
        }
        return resultado;
    }
}
