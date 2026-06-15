package foodiet.modelo;

public class CitaPresencial extends Cita {

    private String sala;
    private String direccionClinica;

    public CitaPresencial(Paciente paciente, ProfesionalClinica profesional, int dia, int mes, int anyo,
                          String motivo, String sala) {
        super(paciente, profesional, dia, mes, anyo, "presencial", motivo);
        this.sala = sala;
        this.direccionClinica = "Calle Nutrici\u00f3n, 42 - Valencia";
    }

    public CitaPresencial(int id, Paciente paciente, ProfesionalClinica profesional,
                          int dia, int mes, int anyo, String estado,
                          String motivo, String enlaceOnline,
                          String sala, String direccionClinica) {
        super(id, paciente, profesional, dia, mes, anyo, "presencial", estado, motivo, enlaceOnline);
        this.sala = sala;
        this.direccionClinica = direccionClinica;
    }

    public String getSala() {
        return this.sala;
    }

    public void setSala(String sala) {
        this.sala = sala;
    }

    public String getDireccionClinica() {
        return this.direccionClinica;
    }

    @Override
    public String mostrarCita() {
        return super.mostrarCita() + " | Sala: " + sala + " | Direcci\u00f3n: " + direccionClinica;
    }
}
