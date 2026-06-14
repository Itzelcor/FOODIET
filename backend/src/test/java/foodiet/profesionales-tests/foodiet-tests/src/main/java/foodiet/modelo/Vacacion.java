package foodiet.modelo;

import java.time.LocalDate;

/**
 * Representa un período de vacaciones o sustitución de un profesional.
 */
public class Vacacion {

    private int id;
    private int idProfesional;
    private LocalDate fechaInicio;
    private LocalDate fechaFin;
    private String motivo;          // VACACIONES | BAJA | SUSTITUCIÓN
    private Integer idSustituto;    // Puede ser null si no hay sustituto asignado

    public Vacacion() {}

    public Vacacion(int id, int idProfesional,
                    LocalDate fechaInicio, LocalDate fechaFin,
                    String motivo, Integer idSustituto) {
        this.id             = id;
        this.idProfesional  = idProfesional;
        this.fechaInicio    = fechaInicio;
        this.fechaFin       = fechaFin;
        this.motivo         = motivo;
        this.idSustituto    = idSustituto;
    }

    public int getId()                                     { return id; }
    public void setId(int id)                              { this.id = id; }

    public int getIdProfesional()                          { return idProfesional; }
    public void setIdProfesional(int idProfesional)        { this.idProfesional = idProfesional; }

    public LocalDate getFechaInicio()                              { return fechaInicio; }
    public void setFechaInicio(LocalDate fechaInicio)              { this.fechaInicio = fechaInicio; }

    public LocalDate getFechaFin()                         { return fechaFin; }
    public void setFechaFin(LocalDate fechaFin)            { this.fechaFin = fechaFin; }

    public String getMotivo()                              { return motivo; }
    public void setMotivo(String motivo)                   { this.motivo = motivo; }

    public Integer getIdSustituto()                                { return idSustituto; }
    public void setIdSustituto(Integer idSustituto)                { this.idSustituto = idSustituto; }

    @Override
    public String toString() {
        return "Vacacion{id=" + id + ", inicio=" + fechaInicio +
               ", fin=" + fechaFin + ", motivo='" + motivo + "'}";
    }
}
