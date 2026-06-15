package foodiet.gestion;

import foodiet.datos.ConexionBD;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class Estadisticas {

    private Connection conexion;

    public Estadisticas() {
        this.conexion = ConexionBD.getConexion();
    }

    public void mostrarResumen() {
        String sqlP = "SELECT COUNT(*) AS total FROM pacientes";
        String sqlC = "SELECT COUNT(*) AS total FROM citas";
        String sqlPl = "SELECT COUNT(*) AS total FROM planes_alimentacion";
        String sqlI = "SELECT COALESCE(SUM(monto), 0) AS total FROM pagos WHERE estado_pago = 'pagado'";

        try {
            Statement st = conexion.createStatement();

            ResultSet rs = st.executeQuery(sqlP);
            int totalP = 0;
            if (rs.next()) { totalP = rs.getInt("total"); }
            rs.close();

            rs = st.executeQuery(sqlC);
            int totalC = 0;
            if (rs.next()) { totalC = rs.getInt("total"); }
            rs.close();

            rs = st.executeQuery(sqlPl);
            int totalPl = 0;
            if (rs.next()) { totalPl = rs.getInt("total"); }
            rs.close();

            rs = st.executeQuery(sqlI);
            double ingresos = 0;
            if (rs.next()) { ingresos = rs.getDouble("total"); }
            rs.close();

            st.close();

            System.out.println("\n--- RESUMEN GENERAL ---");
            System.out.println("Pacientes: " + totalP + " | Citas: " + totalC +
                               " | Planes: " + totalPl + " | Ingresos: " + String.format("%.2f", ingresos) + "\u20ac");

        } catch (SQLException e) {
            System.out.println("Error en resumen - " + e.getMessage());
        }
    }

    public void mostrarIngresosPorNutricionista() {
        String sql = "SELECT CONCAT(pr.nombre, ' ', pr.apellido) AS nutricionista, " +
                     "COUNT(c.id_cita) AS citas, COALESCE(SUM(p.monto), 0) AS ingresos " +
                     "FROM profesionales pr " +
                     "LEFT JOIN citas c ON pr.id_profesional = c.id_profesional AND c.estado = 'completada' " +
                     "LEFT JOIN pagos p ON c.id_cita = p.id_cita AND p.estado_pago = 'pagado' " +
                     "GROUP BY pr.id_profesional ORDER BY ingresos DESC";

        try {
            Statement st = conexion.createStatement();
            ResultSet rs = st.executeQuery(sql);
            System.out.println("\n--- INGRESOS POR NUTRICIONISTA ---");
            boolean hay = false;
            while (rs.next()) {
                hay = true;
                System.out.println(rs.getString("nutricionista") +
                    " | Citas: " + rs.getInt("citas") +
                    " | Ingresos: " + String.format("%.2f", rs.getDouble("ingresos")) + "\u20ac");
            }
            if (!hay) { System.out.println("Sin datos"); }
            rs.close();
            st.close();
        } catch (SQLException e) {
            System.out.println("Error en ingresos - " + e.getMessage());
        }
    }

    public void mostrarCitasPorModalidad() {
        String sql = "SELECT modalidad, COUNT(*) AS total, " +
                     "ROUND(COUNT(*) * 100.0 / (SELECT COUNT(*) FROM citas), 1) AS porcentaje " +
                     "FROM citas GROUP BY modalidad";
        try {
            Statement st = conexion.createStatement();
            ResultSet rs = st.executeQuery(sql);
            System.out.println("\n--- CITAS POR MODALIDAD ---");
            boolean hay = false;
            while (rs.next()) {
                hay = true;
                System.out.println(rs.getString("modalidad") + ": " +
                    rs.getInt("total") + " (" + rs.getDouble("porcentaje") + "%)");
            }
            if (!hay) { System.out.println("Sin citas registradas"); }
            rs.close();
            st.close();
        } catch (SQLException e) {
            System.out.println("Error en modalidad - " + e.getMessage());
        }
    }

    public void mostrarProximasCitas() {
        String sql = "SELECT CONCAT(p.nombre, ' ', p.apellido) AS paciente, " +
                     "CONCAT(pr.nombre, ' ', pr.apellido) AS nutri, " +
                     "c.fecha_cita, c.hora_cita, c.modalidad " +
                     "FROM citas c JOIN pacientes p ON c.id_paciente = p.id_paciente " +
                     "JOIN profesionales pr ON c.id_profesional = pr.id_profesional " +
                     "WHERE c.estado IN ('pendiente', 'confirmada') " +
                     "AND c.fecha_cita BETWEEN CURDATE() AND DATE_ADD(CURDATE(), INTERVAL 7 DAY) " +
                     "ORDER BY c.fecha_cita";
        try {
            Statement st = conexion.createStatement();
            ResultSet rs = st.executeQuery(sql);
            System.out.println("\n--- PR\u00d3XIMAS CITAS (7 D\u00cdAS) ---");
            boolean hay = false;
            while (rs.next()) {
                hay = true;
                System.out.println(rs.getDate("fecha_cita") + " " + rs.getTime("hora_cita") +
                    " | " + rs.getString("paciente") + " con " + rs.getString("nutri") +
                    " (" + rs.getString("modalidad") + ")");
            }
            if (!hay) { System.out.println("No hay citas pr\u00f3ximas"); }
            rs.close();
            st.close();
        } catch (SQLException e) {
            System.out.println("Error en pr\u00f3ximas citas - " + e.getMessage());
        }
    }
}
