package com.foodiet.gestion;

import com.foodiet.modelo.Cita;
import com.foodiet.modelo.Paciente;
import com.foodiet.modelo.PlanAlimenticio;
import com.foodiet.modelo.ProfesionalClinica;
import com.foodiet.datos.GestorBD;
import java.util.ArrayList;

public class GestionClinica {

    private static final int MAX_PACIENTES = 100;
    private static final int MAX_PROFESIONALES = 20;
    private static final int MAX_CITAS = 200;
    private static final int MAX_PLANES = 100;

    private Paciente[] pacientes;
    private int pacientesN;

    private ProfesionalClinica[] profesionales;
    private int profesionalesN;

    private Cita[] citas;
    private int citasN;

    private PlanAlimenticio[] planes;
    private int planesN;

    private GestorBD gestorBD;

    public GestionClinica() {
        this.pacientes = new Paciente[MAX_PACIENTES];
        this.pacientesN = 0;
        this.profesionales = new ProfesionalClinica[MAX_PROFESIONALES];
        this.profesionalesN = 0;
        this.citas = new Cita[MAX_CITAS];
        this.citasN = 0;
        this.planes = new PlanAlimenticio[MAX_PLANES];
        this.planesN = 0;
        this.gestorBD = new GestorBD();
    }

    public boolean añadirPaciente(Paciente p) {
        boolean resultado = false;
        if (pacientesN < MAX_PACIENTES) {
            int idBD = gestorBD.insertarPaciente(p);
            if (idBD > 0) {
                p.setId(idBD);
                this.pacientes[pacientesN] = p;
                this.pacientesN++;
                resultado = true;
            }
        }
        return resultado;
    }

    public boolean añadirProfesional(ProfesionalClinica p) {
        boolean resultado = false;
        if (profesionalesN < MAX_PROFESIONALES) {
            this.profesionales[profesionalesN] = p;
            this.profesionalesN++;
            resultado = true;
        }
        return resultado;
    }

    public boolean añadirCita(Cita c) {
        boolean resultado = false;
        if (citasN < MAX_CITAS) {
            int idBD = gestorBD.insertarCita(c);
            if (idBD > 0) {
                c.setId(idBD);
                c.setCodigoCita(idBD);
                this.citas[citasN] = c;
                this.citasN++;
                resultado = true;
            }
        }
        return resultado;
    }

    public boolean añadirPlan(PlanAlimenticio plan) {
        boolean resultado = false;
        if (planesN < MAX_PLANES) {
            int idBD = gestorBD.insertarPlan(plan);
            if (idBD > 0) {
                plan.setId(idBD);
                this.planes[planesN] = plan;
                this.planesN++;
                resultado = true;
            }
        }
        return resultado;
    }

    public int getPacientesN() {
        return this.pacientesN;
    }

    public Paciente getPaciente(int indice) {
        Paciente resultado = null;
        if (indice >= 0 && indice < pacientesN) {
            resultado = this.pacientes[indice];
        }
        return resultado;
    }

    public ProfesionalClinica getProfesional(int indice) {
        ProfesionalClinica resultado = null;
        if (indice >= 0 && indice < profesionalesN) {
            resultado = this.profesionales[indice];
        }
        return resultado;
    }

    public int getProfesionalesN() {
        return this.profesionalesN;
    }

    public int getCitasN() {
        return this.citasN;
    }

    public Cita getCita(int indice) {
        Cita resultado = null;
        if (indice >= 0 && indice < citasN) {
            resultado = this.citas[indice];
        }
        return resultado;
    }

    public int getPlanesN() {
        return this.planesN;
    }

    public PlanAlimenticio getPlan(int indice) {
        PlanAlimenticio resultado = null;
        if (indice >= 0 && indice < planesN) {
            resultado = this.planes[indice];
        }
        return resultado;
    }

    public GestorBD getGestorBD() {
        return this.gestorBD;
    }

    public void mostrarPacientes() {
        System.out.println("LISTA DE PACIENTES (" + pacientesN + ")");
        for (int i = 0; i < pacientesN; i++) {
            Paciente p = pacientes[i];
            System.out.println(p.toString());
            System.out.printf("IMC: %.2f%n", p.calcularIMC());
            System.out.println();
        }
    }

    public void mostrarProfesionales() {
        System.out.println("LISTA DE PROFESIONALES (" + profesionalesN + ")");
        for (int i = 0; i < profesionalesN; i++) {
            profesionales[i].mostrarInfo();
        }
    }

    public void mostrarCitas() {
        System.out.println("LISTA DE CITAS (" + citasN + ")");
        for (int i = 0; i < citasN; i++) {
            System.out.println(citas[i].mostrarCita());
        }
    }

    public void mostrarPlanes() {
        System.out.println("LISTA DE PLANES ALIMENTICIOS (" + planesN + ")");
        for (int i = 0; i < planesN; i++) {
            System.out.println(planes[i].mostrarPlan());
        }
    }

    public Cita buscarCitaPorCodigo(int codigo) {
        Cita resultado = null;
        for (int i = 0; i < citasN; i++) {
            if (citas[i].getCodigoCita() == codigo) {
                resultado = citas[i];
            }
        }
        return resultado;
    }

    public int buscarPacientePorNombre(String nombre, String apellido) {
        int indice = -1;
        for (int i = 0; i < pacientesN; i++) {
            if (pacientes[i].getNombre().equalsIgnoreCase(nombre) &&
                pacientes[i].getApellido().equalsIgnoreCase(apellido)) {
                indice = i;
            }
        }
        return indice;
    }

    public boolean eliminarPaciente(int indice) {
        boolean resultado = false;
        if (indice >= 0 && indice < pacientesN) {
            int idPaciente = pacientes[indice].getId();
            if (gestorBD.eliminarPaciente(idPaciente)) {
                for (int i = indice; i < pacientesN - 1; i++) {
                    pacientes[i] = pacientes[i + 1];
                }
                pacientes[pacientesN - 1] = null;
                pacientesN--;
                resultado = true;
            }
        }
        return resultado;
    }

    public boolean cancelarCita(int indice) {
        boolean resultado = false;
        if (indice >= 0 && indice < citasN) {
            Cita c = citas[indice];
            c.setEstado("cancelada");
            if (gestorBD.actualizarEstadoCita(c.getId(), "cancelada")) {
                resultado = true;
            }
        }
        return resultado;
    }

    public boolean eliminarPlan(int indice) {
        boolean resultado = false;
        if (indice >= 0 && indice < planesN) {
            int idPlan = planes[indice].getId();
            if (gestorBD.eliminarPlan(idPlan)) {
                for (int i = indice; i < planesN - 1; i++) {
                    planes[i] = planes[i + 1];
                }
                planes[planesN - 1] = null;
                planesN--;
                resultado = true;
            }
        }
        return resultado;
    }

    public ArrayList<Paciente> pacientesBD() {
        return gestorBD.obtenerTodosPacientes();
    }

    public ArrayList<Cita> citasBD() {
        return gestorBD.obtenerTodasCitas();
    }

    public ArrayList<PlanAlimenticio> planesBD() {
        return gestorBD.obtenerTodosPlanes();
    }
}
