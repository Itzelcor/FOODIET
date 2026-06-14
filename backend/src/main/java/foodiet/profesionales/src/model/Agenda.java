package model;
/**
 * Representa una entrada en la agenda de un profesional.
 *
 * Cada entrada indica qué hará el profesional en una fecha concreta:
 * una consulta, un período de vacaciones o una sustitución.
 *
 * Campos:
 * - id : clave primaria
 * - idProfesional : FK → tabla profesionales
 * - fecha : fecha del evento (formato: "YYYY-MM-DD")
 * - tipoEvento : tipo de actividad (CONSULTA, VACACIONES, SUSTITUCION)
 * - descripcion : notas adicionales sobre el evento
 */
public class Agenda {

    private int id;
    private int idProfesional;
    private String fecha; // formato: "YYYY-MM-DD"
    private TipoEvento tipoEvento;
    private String descripcion;

    // -------------------------------------------------------------------------
    // Constructor vacío
    // -------------------------------------------------------------------------
    public Agenda() {
    }

    // -------------------------------------------------------------------------
    // Constructor completo (sin id)
    // -------------------------------------------------------------------------
    public Agenda(int idProfesional, String fecha, TipoEvento tipoEvento, String descripcion) {
        this.idProfesional = idProfesional;
        this.fecha = fecha;
        this.tipoEvento = tipoEvento;
        this.descripcion = descripcion;
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

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public TipoEvento getTipoEvento() {
        return tipoEvento;
    }

    public void setTipoEvento(TipoEvento tipoEvento) {
        this.tipoEvento = tipoEvento;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    @Override
    public String toString() {
        return "Agenda {" +
                "\n  ID             : " + id +
                "\n  ID Profesional : " + idProfesional +
                "\n  Fecha          : " + fecha +
                "\n  Tipo de evento : " + tipoEvento +
                "\n  Descripción    : " + descripcion +
                "\n}";
    }
}
