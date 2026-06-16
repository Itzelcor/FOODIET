package com.foodiet.modelo;

public class Cita {

    private int id;
    private int codigoCita;
    private Paciente paciente;
    private ProfesionalClinica profesional;
    private int idPaciente;
    private int idProfesional;
    private int dia;
    private int mes;
    private int anyo;
    private String modalidad;
    private String estado;
    private String motivo;
    private String enlaceOnline;

    public Cita() {
        this.id = 0;
        this.codigoCita = 0;
        this.estado = "pendiente";
        this.modalidad = "presencial";
        this.motivo = "";
        this.enlaceOnline = "";
    }

    public Cita(int id, Paciente paciente, ProfesionalClinica profesional,
                int dia, int mes, int anyo, String modalidad, String estado,
                String motivo, String enlaceOnline) {
        this.id = id;
        this.codigoCita = id;
        this.paciente = paciente;
        this.profesional = profesional;
        this.idPaciente = paciente != null ? paciente.getId() : 0;
        this.idProfesional = profesional != null ? profesional.getId() : 0;
        this.dia = dia;
        this.mes = mes;
        this.anyo = anyo;
        this.modalidad = modalidad;
        this.estado = estado;
        this.motivo = motivo;
        this.enlaceOnline = enlaceOnline;
    }

    public Cita(Paciente paciente, ProfesionalClinica profesional, int dia, int mes, int anyo,
                String modalidad, String motivo) {
        this.id = 0;
        this.codigoCita = 0;
        this.paciente = paciente;
        this.profesional = profesional;
        this.idPaciente = paciente != null ? paciente.getId() : 0;
        this.idProfesional = profesional != null ? profesional.getId() : 0;
        this.dia = dia;
        this.mes = mes;
        this.anyo = anyo;
        this.modalidad = modalidad;
        this.estado = "pendiente";
        this.motivo = motivo;
        if (modalidad.equals("online")) {
            this.enlaceOnline = "https://foodiet.virtual/" + this.id;
        } else {
            this.enlaceOnline = "";
        }
    }

    public int getId() {
        return this.id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getCodigoCita() {
        return this.codigoCita;
    }

    public void setCodigoCita(int codigoCita) {
        this.codigoCita = codigoCita;
    }

    public String fecha() {
        return String.format("%02d/%02d/%04d", dia, mes, anyo);
    }

    public int getDia() {
        return this.dia;
    }

    public int getMes() {
        return this.mes;
    }

    public int getAnyo() {
        return this.anyo;
    }

    public Paciente getPaciente() {
        return this.paciente;
    }

    public void setPaciente(Paciente paciente) {
        this.paciente = paciente;
    }

    public ProfesionalClinica getProfesional() {
        return this.profesional;
    }

    public void setProfesional(ProfesionalClinica profesional) {
        this.profesional = profesional;
    }

    public int getIdPaciente() {
        return this.idPaciente;
    }

    public void setIdPaciente(int idPaciente) {
        this.idPaciente = idPaciente;
    }

    public int getIdProfesional() {
        return this.idProfesional;
    }

    public void setIdProfesional(int idProfesional) {
        this.idProfesional = idProfesional;
    }

    public String getModalidad() {
        return this.modalidad;
    }

    public void setModalidad(String modalidad) {
        this.modalidad = modalidad;
    }

    public String getEstado() {
        return this.estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getMotivo() {
        return this.motivo;
    }

    public void setMotivo(String motivo) {
        this.motivo = motivo;
    }

    public String getEnlaceOnline() {
        return this.enlaceOnline;
    }

    public void setEnlaceOnline(String enlaceOnline) {
        this.enlaceOnline = enlaceOnline;
    }

    public String mostrarCita() {
        String info = "Cita N\u00ba " + codigoCita +
                " | Fecha: " + fecha() +
                " | Paciente: " + paciente.nombreCompleto() +
                " | Profesional: " + profesional.nombreCompleto() +
                " (" + profesional.getEspecialidad() + ")" +
                " | Modalidad: " + modalidad +
                " | Estado: " + estado;
        if (modalidad.equals("online") && !enlaceOnline.isEmpty()) {
            info = info + " | Enlace: " + enlaceOnline;
        }
        return info;
    }
}
