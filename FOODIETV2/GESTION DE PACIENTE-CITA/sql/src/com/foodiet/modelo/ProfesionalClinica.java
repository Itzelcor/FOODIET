package com.foodiet.modelo;

public class ProfesionalClinica {

    private int id;
    private String nombre;
    private String apellido;
    private String especialidad;
    private int anyosExperiencia;
    private String horario;
    private String telefono;

    public ProfesionalClinica(int id, String nombre, String apellido, String especialidad,
                              int anyosExperiencia, String horario, String telefono) {
        this.id = id;
        this.nombre = nombre;
        this.apellido = apellido;
        this.especialidad = especialidad;
        this.anyosExperiencia = anyosExperiencia;
        this.horario = horario;
        this.telefono = telefono;
    }

    public ProfesionalClinica(String nombre, String apellido, String especialidad,
                              int anyosExperiencia, String horario, String telefono) {
        this.id = 0;
        this.nombre = nombre;
        this.apellido = apellido;
        this.especialidad = especialidad;
        this.anyosExperiencia = anyosExperiencia;
        this.horario = horario;
        this.telefono = telefono;
    }

    public int getId() {
        return this.id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String nombreCompleto() {
        return nombre + " " + apellido;
    }

    public void mostrarInfo() {
        String info = "Nombre: " + nombre + " " + apellido +
                " | Especialidad: " + especialidad +
                " | A\u00f1os de experiencia: " + anyosExperiencia;
        if (!horario.isEmpty()) {
            info = info + " | Horario: " + horario;
        }
        System.out.println(info);
    }

    public String getNombre() {
        return this.nombre;
    }

    public String getApellido() {
        return this.apellido;
    }

    public String getEspecialidad() {
        return this.especialidad;
    }

    public int getAnyosExperiencia() {
        return this.anyosExperiencia;
    }

    public String getHorario() {
        return this.horario;
    }

    public String getTelefono() {
        return this.telefono;
    }
}
