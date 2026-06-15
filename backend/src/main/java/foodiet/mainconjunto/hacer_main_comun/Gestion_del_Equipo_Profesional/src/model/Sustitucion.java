package hacer_main_comun.Gestion_del_Equipo_Profesional.src.model;
/**
 * Representa una sustitución entre profesionales.
 *
 * Cuando un profesional está ausente (por vacaciones u otro motivo),
 * otro profesional cubre sus responsabilidades durante ese período.
 *
 * Campos:
 * - id : clave primaria
 * - idProfesionalAusente : FK → profesional que se ausenta
 * - idProfesionalSustituto: FK → profesional que cubre la ausencia
 * - fechaInicio : primer día de la sustitución (formato: "YYYY-MM-DD")
 * - fechaFin : último día de la sustitución (formato: "YYYY-MM-DD")
 * - motivo : razón de la ausencia (ej. "Vacaciones", "Baja médica")
 */
public class Sustitucion {

    private int id;
    private int idProfesionalAusente;
    private int idProfesionalSustituto;
    private String fechaInicio;
    private String fechaFin;
    private String motivo;

    // -------------------------------------------------------------------------
    // Constructor vacío
    // -------------------------------------------------------------------------
    public Sustitucion() {
    }

    // -------------------------------------------------------------------------
    // Constructor completo (sin id)
    // -------------------------------------------------------------------------
    public Sustitucion(int idProfesionalAusente, int idProfesionalSustituto,
            String fechaInicio, String fechaFin, String motivo) {
        this.idProfesionalAusente = idProfesionalAusente;
        this.idProfesionalSustituto = idProfesionalSustituto;
        this.fechaInicio = fechaInicio;
        this.fechaFin = fechaFin;
        this.motivo = motivo;
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

    public int getIdProfesionalAusente() {
        return idProfesionalAusente;
    }

    public void setIdProfesionalAusente(int idProfesionalAus) {
        this.idProfesionalAusente = idProfesionalAus;
    }

    public int getIdProfesionalSustituto() {
        return idProfesionalSustituto;
    }

    public void setIdProfesionalSustituto(int idProfesionalSust) {
        this.idProfesionalSustituto = idProfesionalSust;
    }

    public String getFechaInicio() {
        return fechaInicio;
    }

    public void setFechaInicio(String fechaInicio) {
        this.fechaInicio = fechaInicio;
    }

    public String getFechaFin() {
        return fechaFin;
    }

    public void setFechaFin(String fechaFin) {
        this.fechaFin = fechaFin;
    }

    public String getMotivo() {
        return motivo;
    }

    public void setMotivo(String motivo) {
        this.motivo = motivo;
    }

    @Override
    public String toString() {
        return "Sustitucion {" +
                "\n  ID                    : " + id +
                "\n  Profesional ausente   : " + idProfesionalAusente +
                "\n  Profesional sustituto : " + idProfesionalSustituto +
                "\n  Desde                 : " + fechaInicio +
                "\n  Hasta                 : " + fechaFin +
                "\n  Motivo                : " + motivo +
                "\n}";
    }
}
