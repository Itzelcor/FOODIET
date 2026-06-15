package dao;

import model.*;
import java.sql.*;

class SubsistemaAdministrador {

    // Datos de conexion del entorno de pruebas del proyecto
    private static final String URL_BD = "jdbc:mysql://localhost:33306/FooDiet";
    private static final String USUARIO_BD = "superuser";
    private static final String PASS_BD = "alumnoalumno";

    private GestorAuditoria gestorAuditoria;

    public SubsistemaAdministrador(GestorAuditoria gestorAuditoria) {
        this.gestorAuditoria = gestorAuditoria;
    }

    // Ejecuta el control que le pasen (polimorfismo) y guarda el resultado en
    // LOGS_AUDITORIA
    public void procesarControlYPersistir(ComponenteSeguridad componente) {

        String resultado = componente.ejecutarControl();

        gestorAuditoria.registrarAccion(componente.getDescripcion() + " => " + resultado);

        Connection con = null;
        PreparedStatement ps = null;

        try {
            con = DriverManager.getConnection(URL_BD, USUARIO_BD, PASS_BD);

            String sql = "INSERT INTO LOGS_AUDITORIA (descripcion, estado) VALUES (?, ?)";
            ps = con.prepareStatement(sql);
            ps.setString(1, componente.getDescripcion());
            ps.setString(2, resultado);

            int filas = ps.executeUpdate();
            System.out.println("Insertado en LOGS_AUDITORIA (" + filas + " fila) -> " + resultado);

        } catch (SQLException e) {
            // En el entorno local no hay servidor MySQL levantado, asi que esto entra
            // siempre
            // y dejamos constancia por consola de que el registro quedo solo en memoria
            System.out.println("(sin conexion a BD, el registro queda solo en el ArrayList: " + resultado + ")");

        } finally {
            try {
                if (ps != null)
                    ps.close();
                if (con != null)
                    con.close();
            } catch (SQLException e) {
                System.out.println("Error cerrando conexion: " + e.getMessage());
            }
        }
    }

    // Saca a un .txt todos los logs marcados como "uso incorrecto"
    public void exportarAlertasUsoIncorrecto() {

        Connection con = null;
        Statement st = null;
        ResultSet rs = null;
        BufferedWriter bw = null;

        try {
            con = DriverManager.getConnection(URL_BD, USUARIO_BD, PASS_BD);
            st = con.createStatement();

            String sql = "SELECT * FROM LOGS_AUDITORIA WHERE estado = 'uso incorrecto'";
            rs = st.executeQuery(sql);

            bw = new BufferedWriter(new FileWriter("informe_errores.txt"));
            bw.write("Informe de incidencias - FooDiet");
            bw.newLine();
            bw.write("================================");
            bw.newLine();

            while (rs.next()) {
                String desc = rs.getString("descripcion");
                String est = rs.getString("estado");
                bw.write("- " + desc + " | " + est);
                bw.newLine();
            }

            System.out.println("Informe generado desde la BD correctamente.");

        } catch (SQLException e) {
            // No hay BD disponible -> generamos el informe a partir de lo que tenemos en
            // memoria
            System.out.println("No se pudo consultar LOGS_AUDITORIA, se exporta desde el ArrayList...");

            try {
                bw = new BufferedWriter(new FileWriter("informe_errores.txt"));
                bw.write("Informe de incidencias - FooDiet (datos en memoria)");
                bw.newLine();
                bw.write("================================");
                bw.newLine();

                for (String log : gestorAuditoria.getLogs()) {
                    if (log.contains("uso incorrecto")) {
                        bw.write("- " + log);
                        bw.newLine();
                    }
                }

                System.out.println("Fichero informe_errores.txt generado correctamente.");

            } catch (IOException ioEx) {
                System.out.println("Fallo al escribir el fichero: " + ioEx.getMessage());
            }

        } catch (IOException e) {
            System.out.println("Error de I/O al generar informe_errores.txt: " + e.getMessage());

        } finally {
            try {
                if (rs != null)
                    rs.close();
                if (st != null)
                    st.close();
                if (con != null)
                    con.close();
            } catch (SQLException e) {
                System.out.println("Error cerrando recursos de la consulta: " + e.getMessage());
            }
            try {
                if (bw != null)
                    bw.close();
            } catch (IOException e) {
                System.out.println("Error cerrando el fichero: " + e.getMessage());
            }
        }
    }
}