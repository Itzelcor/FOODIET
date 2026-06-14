import java.util.ArrayList;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.io.FileWriter;
import java.io.BufferedWriter;
import java.io.IOException;

/*
 * Subsistema de Administrador - FooDiet
 * Andrei Anton Veres
 * Curso 2025/2026 - DAW
 */

// Clase base de la jerarquia de usuarios de la aplicacion
class Usuario {
    protected int idUsuario;
    protected String nombre;
    protected String email;

    public Usuario(int idUsuario, String nombre, String email) {
        this.idUsuario = idUsuario;
        this.nombre = nombre;
        this.email = email;
    }

    public int getIdUsuario() {
        return idUsuario;
    }

    public String getNombre() {
        return nombre;
    }

    public String getEmail() {
        return email;
    }

    public String toString() {
        return "Usuario [" + idUsuario + "] " + nombre + " - " + email;
    }
}

// El administrador hereda de Usuario y añade el nivel de acceso al panel de seguridad
class Administrador extends Usuario {

    private String nivelSeguridad; // ALTO / MEDIO / BAJO segun permisos del panel

    public Administrador(int idUsuario, String nombre, String email, String nivelSeguridad) {
        super(idUsuario, nombre, email);
        this.nivelSeguridad = nivelSeguridad;
    }

    public String getNivelSeguridad() {
        return nivelSeguridad;
    }

    // Revisa los logs que hay cargados en el gestor (paso del diagrama de actividades)
    public void revisarRegistros(GestorAuditoria gestor) {
        System.out.println("\nEl admin " + nombre + " (" + nivelSeguridad + ") entra a revisar el registro de actividad...");
        gestor.mostrarLogsActivos();
    }

    // Aviso rapido cuando el admin detecta algo raro durante la revision
    public void notificarIncidencia(String mensaje) {
        System.out.println(">> Aviso de " + nombre + ": " + mensaje);
    }
}


// Almacen en memoria de las acciones que va registrando el sistema
class GestorAuditoria {

    private ArrayList<String> logs;

    public GestorAuditoria() {
        logs = new ArrayList<String>();
    }

    public void registrarAccion(String accion) {
        logs.add(accion);
        System.out.println("[LOG] " + accion);
    }

    public void mostrarLogsActivos() {
        System.out.println("---- Registro de actividad (" + logs.size() + " entradas) ----");
        if (logs.size() == 0) {
            System.out.println("No hay nada registrado todavia.");
        } else {
            int i = 1;
            for (String l : logs) {
                System.out.println(i + ") " + l);
                i++;
            }
        }
        System.out.println("------------------------------------------------");
    }

    public ArrayList<String> getLogs() {
        return logs;
    }
}


// Contrato comun para los distintos chequeos de seguridad que puede lanzar el admin
abstract class ComponenteSeguridad {

    protected String descripcion;

    public ComponenteSeguridad(String descripcion) {
        this.descripcion = descripcion;
    }

    public abstract String ejecutarControl();

    public String getDescripcion() {
        return descripcion;
    }
}

// Control de accesos: mira si el numero de intentos fallidos es sospechoso
class ControlAccesos extends ComponenteSeguridad {

    private int intentosFallidos;
    private int umbralMaximo; // a partir de aqui se considera sospechoso

    public ControlAccesos(String descripcion, int intentosFallidos, int umbralMaximo) {
        super(descripcion);
        this.intentosFallidos = intentosFallidos;
        this.umbralMaximo = umbralMaximo;
    }

    public String ejecutarControl() {
        System.out.println("Comprobando accesos -> " + descripcion + " (intentos: " + intentosFallidos + ")");
        if (intentosFallidos > umbralMaximo) {
            return "uso incorrecto";
        }
        return "uso correcto";
    }
}

// Control de modificaciones sobre datos de pacientes/planes nutricionales
class ControlModificaciones extends ComponenteSeguridad {

    private boolean autorizada;
    private String tablaAfectada; // ej: PACIENTES, PLAN_NUTRICIONAL, HISTORIAL_MEDICO

    public ControlModificaciones(String descripcion, boolean autorizada, String tablaAfectada) {
        super(descripcion);
        this.autorizada = autorizada;
        this.tablaAfectada = tablaAfectada;
    }

    public String ejecutarControl() {
        System.out.println("Comprobando modificacion en " + tablaAfectada + " -> " + descripcion);
        if (!autorizada) {
            return "uso incorrecto";
        }
        return "uso correcto";
    }
}


// Coordina los controles, su volcado a BD y la exportacion del informe final
class SubsistemaAdministrador {

    // Datos de conexion del entorno de pruebas del proyecto
    private static final String URL_BD = "jdbc:mysql://localhost:3306/bd_foodiet";
    private static final String USUARIO_BD = "admin_foodiet";
    private static final String PASS_BD = "foodiet2026";

    private GestorAuditoria gestorAuditoria;

    public SubsistemaAdministrador(GestorAuditoria gestorAuditoria) {
        this.gestorAuditoria = gestorAuditoria;
    }

    // Ejecuta el control que le pasen (polimorfismo) y guarda el resultado en LOGS_AUDITORIA
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
            // En el entorno local no hay servidor MySQL levantado, asi que esto entra siempre
            // y dejamos constancia por consola de que el registro quedo solo en memoria
            System.out.println("(sin conexion a BD, el registro queda solo en el ArrayList: " + resultado + ")");

        } finally {
            try {
                if (ps != null) ps.close();
                if (con != null) con.close();
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
            // No hay BD disponible -> generamos el informe a partir de lo que tenemos en memoria
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
                if (rs != null) rs.close();
                if (st != null) st.close();
                if (con != null) con.close();
            } catch (SQLException e) {
                System.out.println("Error cerrando recursos de la consulta: " + e.getMessage());
            }
            try {
                if (bw != null) bw.close();
            } catch (IOException e) {
                System.out.println("Error cerrando el fichero: " + e.getMessage());
            }
        }
    }
}


public class PrincipalAdmin {

    public static void main(String[] args) {

        System.out.println("=== Subsistema de Administrador - FooDiet ===");

        Administrador admin = new Administrador(1, "Marina Soler", "marina.soler@foodiet.com", "ALTO");

        GestorAuditoria gestor = new GestorAuditoria();

        // Cargamos un par de eventos iniciales como si vinieran de otros modulos
        gestor.registrarAccion("Login correcto del usuario nutricionista_jgomez");
        gestor.registrarAccion("Acceso al modulo de pacientes fuera del horario habitual (23:47h)");

        admin.revisarRegistros(gestor);

        SubsistemaAdministrador subsistema = new SubsistemaAdministrador(gestor);

        // Control de accesos: este usuario ha fallado el login 5 veces, umbral son 3
        ComponenteSeguridad cAcceso = new ControlAccesos("Accesos del usuario nutricionista_jgomez", 5, 3);

        // Modificacion no autorizada del historial medico del paciente 204
        ComponenteSeguridad cModSinAutorizar = new ControlModificaciones(
                "Cambio en alergias del paciente PAC-204", false, "HISTORIAL_MEDICO");

        // Modificacion normal y autorizada de un plan nutricional
        ComponenteSeguridad cModOk = new ControlModificaciones(
                "Actualizacion de calorias del plan PAC-310", true, "PLAN_NUTRICIONAL");

        System.out.println("\n-- Lanzando controles de seguridad --");
        subsistema.procesarControlYPersistir(cAcceso);
        subsistema.procesarControlYPersistir(cModSinAutorizar);
        subsistema.procesarControlYPersistir(cModOk);

        admin.revisarRegistros(gestor);

        admin.notificarIncidencia("Hay accesos y modificaciones marcadas como uso incorrecto, revisar antes de cerrar el turno.");

        System.out.println("\n-- Exportando informe de alertas --");
        subsistema.exportarAlertasUsoIncorrecto();

        System.out.println("\n=== Fin de la ejecucion ===");
    }
}