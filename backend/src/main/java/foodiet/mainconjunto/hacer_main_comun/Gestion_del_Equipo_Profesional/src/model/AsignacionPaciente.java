package hacer_main_comun.Gestion_del_Equipo_Profesional.src.model;
/**
 * Representa la asignación de un paciente a un profesional.
 *
 * NOTA PARA INTEGRACIÓN:
 * El campo 'idPaciente' hace referencia a la tabla 'pacientes' del subsistema
 * de Gestión de Pacientes (definido por otro compañero del equipo).
 * Cuando recibas esa tabla, asegúrate de que:
 * - El tipo de dato de 'idPaciente' aquí (int) coincide con la PK de su tabla.
 * - La FK en el script SQL apunta correctamente a esa tabla.
 *
 * Campos:
 * - id : clave primaria
 * - idProfesional : FK → tabla profesionales
 * - idPaciente : FK → tabla pacientes (subsistema externo) ← REVISAR con
 * compañero
 * - fechaAsignacion : fecha en que se realizó la asignación (formato:
 * YYYY-MM-DD)
 */
public class AsignacionPaciente {

    private int id;
    private int idProfesional;

    // ==========================================================================
    // TODO: Confirmar con el compañero del subsistema de Pacientes:
    // - Nombre exacto de la tabla de pacientes
    // - Tipo y nombre de su clave primaria
    // - Sustituir el tipo 'int' si fuera necesario
    // ==========================================================================
    private int idPaciente;

    private String fechaAsignacion; // formato esperado: "YYYY-MM-DD"

    // -------------------------------------------------------------------------
    // Constructor vacío
    // -------------------------------------------------------------------------
    public AsignacionPaciente() {
    }

    // -------------------------------------------------------------------------
    // Constructor completo (sin id)
    // -------------------------------------------------------------------------
    public AsignacionPaciente(int idProfesional, int idPaciente, String fechaAsignacion) {
        this.idProfesional = idProfesional;
        this.idPaciente = idPaciente;
        this.fechaAsignacion = fechaAsignacion;
    }

    // -------------------------------------------------------------------------
    // Getters y Setters
    // -------------------------------------------------------------------------
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getIdProfesional() {
        return idProfesional;
    }

    public void setIdProfesional(int idProfesional) {
        this.idProfesional = idProfesional;
    }

    public int getIdPaciente() {
        return idPaciente;
    }

    public void setIdPaciente(int idPaciente) {
        this.idPaciente = idPaciente;
    }

    public String getFechaAsignacion() {
        return fechaAsignacion;
    }

    public void setFechaAsignacion(String fechaAsignacion) {
        this.fechaAsignacion = fechaAsignacion;
    }

    @Override
    public String toString() {
        return "Asignacion de Paciente {" +
                "\n  ID             : " + id +
                "\n  ID Profesional : " + idProfesional +
                "\n  ID Paciente    : " + idPaciente +
                "\n  Fecha          : " + fechaAsignacion +
                "\n}";
    }
}
