package foodiet.modelo;

/**
 * Representa el horario semanal de un profesional.
 */
public class Horario {

    private int id;
    private int idProfesional;
    private String diaSemana;   // Lunes, Martes...
    private String horaInicio;  // Formato HH:mm
    private String horaFin;     // Formato HH:mm

    public Horario() {}

    public Horario(int id, int idProfesional,
                   String diaSemana, String horaInicio, String horaFin) {
        this.id             = id;
        this.idProfesional  = idProfesional;
        this.diaSemana      = diaSemana;
        this.horaInicio     = horaInicio;
        this.horaFin        = horaFin;
    }

    public int getId()                             { return id; }
    public void setId(int id)                      { this.id = id; }

    public int getIdProfesional()                          { return idProfesional; }
    public void setIdProfesional(int idProfesional)        { this.idProfesional = idProfesional; }

    public String getDiaSemana()                           { return diaSemana; }
    public void setDiaSemana(String diaSemana)             { this.diaSemana = diaSemana; }

    public String getHoraInicio()                          { return horaInicio; }
    public void setHoraInicio(String horaInicio)           { this.horaInicio = horaInicio; }

    public String getHoraFin()                     { return horaFin; }
    public void setHoraFin(String horaFin)         { this.horaFin = horaFin; }

    @Override
    public String toString() {
        return "Horario{id=" + id + ", dia='" + diaSemana +
               "', inicio='" + horaInicio + "', fin='" + horaFin + "'}";
    }
}
