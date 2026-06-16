package stats.Pacientes;

public class Cita {

    private int idCita;
    private int idPaciente;
    private int idProfesional;
    private String fecha;
    private String hora;
    private String estado;
    private String modalidad;
    private String motivoConsulta;
    private String observacion;

    public Cita(int idCita, int idPaciente, int idProfesional,
            String fecha, String hora, String estado,
            String modalidad, String motivoConsulta, String observacion) {
        this.idCita = idCita;
        this.idPaciente = idPaciente;
        this.idProfesional = idProfesional;
        this.fecha = fecha;
        this.hora = hora;
        this.estado = estado;
        this.modalidad = modalidad;
        this.motivoConsulta = motivoConsulta;
        this.observacion = observacion;
    }

    public void confirmar() {
        this.estado = "completada";
    }

    public void cancelar() {
        this.estado = "cancelada";
    }

    // Getters
    public int getIdCita() {
        return idCita;
    }

    public int getIdPaciente() {
        return idPaciente;
    }

    public int getIdProfesional() {
        return idProfesional;
    }

    public String getFecha() {
        return fecha;
    }

    public String getHora() {
        return hora;
    }

    public String getEstado() {
        return estado;
    }

    public String getModalidad() {
        return modalidad;
    }

    public String getMotivoConsulta() {
        return motivoConsulta;
    }

    public String getObservacion() {
        return observacion;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public void setObservacion(String observacion) {
        this.observacion = observacion;
    }

    @Override
    public String toString() {
        return "Cita ID: " + idCita
                + " | Paciente: " + idPaciente
                + " | Fecha: " + fecha + " " + hora
                + " | " + modalidad
                + " | Estado: " + estado;
    }
}
