package stats.Administracion;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class FacturaDAO {

    private Connection conexion;

    public FacturaDAO(Connection conexion) {
        this.conexion = conexion;
    }

    public ArrayList<Factura> listarTodas() {
        ArrayList<Factura> lista = new ArrayList<>();
        String sql = "SELECT * FROM factura ORDER BY fecha_emision DESC";
        try {
            PreparedStatement ps = conexion.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                lista.add(new Factura(
                    rs.getInt("id_factura"),
                    rs.getInt("id_cita"),
                    rs.getInt("id_paciente"),
                    rs.getDouble("importe"),
                    rs.getString("estado_pago"),
                    rs.getString("metodo_pago"),
                    rs.getString("fecha_emision"),
                    rs.getString("fecha_pago")
                ));
            }
        } catch (SQLException e) {
            System.out.println("Error al listar facturas: " + e.getMessage());
        }
        return lista;
    }

    public ArrayList<Factura> listarPorPaciente(int idPaciente) {
        ArrayList<Factura> lista = new ArrayList<>();
        String sql = "SELECT * FROM factura WHERE id_paciente = ? ORDER BY fecha_emision DESC";
        try {
            PreparedStatement ps = conexion.prepareStatement(sql);
            ps.setInt(1, idPaciente);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                lista.add(new Factura(
                    rs.getInt("id_factura"),
                    rs.getInt("id_cita"),
                    rs.getInt("id_paciente"),
                    rs.getDouble("importe"),
                    rs.getString("estado_pago"),
                    rs.getString("metodo_pago"),
                    rs.getString("fecha_emision"),
                    rs.getString("fecha_pago")
                ));
            }
        } catch (SQLException e) {
            System.out.println("Error al listar facturas por paciente: " + e.getMessage());
        }
        return lista;
    }

    public ArrayList<Factura> listarPendientes() {
        ArrayList<Factura> lista = new ArrayList<>();
        String sql = "SELECT * FROM factura WHERE estado_pago = 'pendiente'";
        try {
            PreparedStatement ps = conexion.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                lista.add(new Factura(
                    rs.getInt("id_factura"),
                    rs.getInt("id_cita"),
                    rs.getInt("id_paciente"),
                    rs.getDouble("importe"),
                    rs.getString("estado_pago"),
                    rs.getString("metodo_pago"),
                    rs.getString("fecha_emision"),
                    rs.getString("fecha_pago")
                ));
            }
        } catch (SQLException e) {
            System.out.println("Error al listar facturas pendientes: " + e.getMessage());
        }
        return lista;
    }

    public boolean insertar(Factura f) {
        boolean resultado = false;
        String sql = "INSERT INTO factura (id_cita, id_paciente, importe, "
                   + "estado_pago, metodo_pago, fecha_emision) "
                   + "VALUES (?, ?, ?, ?, ?, ?)";
        try {
            PreparedStatement ps = conexion.prepareStatement(sql);
            ps.setInt(1, f.getIdCita());
            ps.setInt(2, f.getIdPaciente());
            ps.setDouble(3, f.getImporte());
            ps.setString(4, f.getEstadoPago());
            ps.setString(5, f.getMetodoPago());
            ps.setString(6, f.getFechaEmision());
            ps.executeUpdate();
            System.out.println("Factura insertada correctamente.");
            resultado = true;
        } catch (SQLException e) {
            System.out.println("Error al insertar factura: " + e.getMessage());
        }
        return resultado;
    }

    public boolean marcarPagada(int idFactura, String metodoPago, String fechaPago) {
        boolean resultado = false;
        String sql = "UPDATE factura SET estado_pago = 'pagada', "
                   + "metodo_pago = ?, fecha_pago = ? WHERE id_factura = ?";
        try {
            PreparedStatement ps = conexion.prepareStatement(sql);
            ps.setString(1, metodoPago);
            ps.setString(2, fechaPago);
            ps.setInt(3, idFactura);
            int filas = ps.executeUpdate();
            if (filas > 0) {
                System.out.println("Factura marcada como pagada.");
                resultado = true;
            }
        } catch (SQLException e) {
            System.out.println("Error al actualizar factura: " + e.getMessage());
        }
        return resultado;
    }

    public boolean eliminar(int idFactura) {
        boolean resultado = false;
        String sql = "DELETE FROM factura WHERE id_factura = ?";
        try {
            PreparedStatement ps = conexion.prepareStatement(sql);
            ps.setInt(1, idFactura);
            int filas = ps.executeUpdate();
            if (filas > 0) {
                System.out.println("Factura eliminada correctamente.");
                resultado = true;
            }
        } catch (SQLException e) {
            System.out.println("Error al eliminar factura: " + e.getMessage());
        }
        return resultado;
    }

    public double calcularTotalIngresos() {
        double total = 0;
        String sql = "SELECT SUM(importe) AS total FROM factura WHERE estado_pago = 'pagada'";
        try {
            PreparedStatement ps = conexion.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                total = rs.getDouble("total");
            }
        } catch (SQLException e) {
            System.out.println("Error al calcular ingresos: " + e.getMessage());
        }
        return total;
    }
}