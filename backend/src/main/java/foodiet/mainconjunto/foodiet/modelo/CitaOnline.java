package foodiet.modelo;

public class CitaOnline extends Cita {

    private String plataforma;
    private String enlaceVideollamada;

    public CitaOnline(Paciente paciente, ProfesionalClinica profesional, int dia, int mes, int anyo,
                      String motivo, String plataforma) {
        super(paciente, profesional, dia, mes, anyo, "online", motivo);
        this.plataforma = plataforma;
        this.enlaceVideollamada = "https://foodiet.virtual/cita-" + getId();
    }

    public CitaOnline(int id, Paciente paciente, ProfesionalClinica profesional,
                      int dia, int mes, int anyo, String estado,
                      String motivo, String enlaceOnline,
                      String plataforma, String enlaceVideollamada) {
        super(id, paciente, profesional, dia, mes, anyo, "online", estado, motivo, enlaceOnline);
        this.plataforma = plataforma;
        this.enlaceVideollamada = enlaceVideollamada;
    }

    public String getPlataforma() {
        return this.plataforma;
    }

    public void setPlataforma(String plataforma) {
        this.plataforma = plataforma;
    }

    public String getEnlaceVideollamada() {
        return this.enlaceVideollamada;
    }

    @Override
    public String mostrarCita() {
        return super.mostrarCita() + " | Plataforma: " + plataforma + " | Enlace: " + enlaceVideollamada;
    }
}
