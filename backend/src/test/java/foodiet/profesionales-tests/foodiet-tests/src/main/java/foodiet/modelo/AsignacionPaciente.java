package foodiet.modelo;

import java.time.LocalDate;

/**
 * Representa la asignación de un paciente a un profesional.
 * NOTA: id_paciente es FK pendiente de confirmar con el subsistema de Dani.
 */
public class AsignacionPaciente {

    private int id;
    private int idProfesional;
    private int idPaciente;         // TODO: FK a tabla pacientes (subsistema Dani)
    private LocalDate fechaAsignacion;
    private String observaciones;

    public AsignacionPaciente() {}

    public AsignacionPaciente(int id, int idProfesional, int idPaciente,
                               LocalDate fechaAsignacion, String observaciones) {
        this.id               = id;
        this.idProfesional    = idProfesional;
        this.idPaciente       = idPaciente;
        this.fechaAsignacion  = fechaAsignacion;
        this.observaciones    = observaciones;
    }

    public int getId()                                     { return id; }
    public void setId(int id)                              { this.id = id; }

    public int getIdProfesional()                          { return idProfesional; }
    public void setIdProfesional(int idProfesional)        { this.idProfesional = idProfesional; }

    public int getIdPaciente()                             { return idPaciente; }
    public void setIdPaciente(int idPaciente)              { this.idPaciente = idPaciente; }

    public LocalDate getFechaAsignacion()                          { return fechaAsignacion; }
    public void setFechaAsignacion(LocalDate fechaAsignacion)      { this.fechaAsignacion = fechaAsignacion; }

    public String getObservaciones()                               { return observaciones; }
    public void setObservaciones(String observaciones)             { this.observaciones = observaciones; }

    @Override
    public String toString() {
        return "AsignacionPaciente{id=" + id + ", idProfesional=" + idProfesional +
               ", idPaciente=" + idPaciente + "}";
    }
}
