package com.foodiet.presentacion;

import com.foodiet.datos.GestorBD;
import com.foodiet.gestion.Estadisticas;
import com.foodiet.gestion.GestionClinica;
import com.foodiet.gestion.GestorArchivos;
import com.foodiet.modelo.*;
import java.util.ArrayList;
import java.util.Scanner;

public class MenuPrincipal {

    private GestionClinica gestion;
    private GestorBD gestorBD;
    private GestorArchivos gestorArchivos;
    private Estadisticas estadisticas;
    private Scanner scanner;

    public MenuPrincipal(GestionClinica gestion, Scanner scanner) {
        this.gestion = gestion;
        this.gestorBD = gestion.getGestorBD();
        this.gestorArchivos = new GestorArchivos();
        this.estadisticas = new Estadisticas();
        this.scanner = scanner;
    }

    public void menuPacientes() {
        boolean salir = false;
        while (!salir) {
            System.out.println("\n--- GESTION DE PACIENTES ---");
            System.out.println("1. Registrar paciente");
            System.out.println("2. Listar pacientes");
            System.out.println("3. Buscar paciente");
            System.out.println("4. Eliminar paciente");
            System.out.println("5. Volver");
            System.out.print("Opcion: ");
            String op = scanner.nextLine();

            if (op.equals("1")) {
                registrarPaciente();
            } else if (op.equals("2")) {
                gestion.mostrarPacientes();
            } else if (op.equals("3")) {
                buscarPaciente();
            } else if (op.equals("4")) {
                eliminarPaciente();
            } else if (op.equals("5")) {
                salir = true;
            } else {
                System.out.println("Opcion no valida");
            }
        }
    }

    private void registrarPaciente() {
        System.out.println("\n--- REGISTRAR PACIENTE ---");
        System.out.print("Nombre: ");
        String nombre = scanner.nextLine();
        System.out.print("Apellido: ");
        String apellido = scanner.nextLine();
        System.out.print("Edad: ");
        int edad = Integer.parseInt(scanner.nextLine());
        System.out.print("Peso (kg): ");
        double peso = Double.parseDouble(scanner.nextLine());
        System.out.print("Altura (m): ");
        double altura = Double.parseDouble(scanner.nextLine());
        System.out.print("Telefono (9 digitos): ");
        String telefono = scanner.nextLine();
        System.out.print("Email: ");
        String email = scanner.nextLine();
        System.out.print("Historial medico: ");
        String historial = scanner.nextLine();
        System.out.print("Objetivos: ");
        String objetivos = scanner.nextLine();
        System.out.print("Tipo (Joven/Adulto/Jubilado): ");
        String tipo = scanner.nextLine();

        Paciente p;
        switch (tipo.toLowerCase()) {
            case "joven":
                p = new PacienteJoven(nombre, apellido, edad, peso, altura,
                                      telefono, email, historial, objetivos);
                break;
            case "jubilado":
                p = new PacienteJubilado(nombre, apellido, edad, peso, altura,
                                         telefono, email, historial, objetivos);
                break;
            default:
                p = new PacienteAdulto(nombre, apellido, edad, peso, altura,
                                       telefono, email, historial, objetivos);
                break;
        }
        p.setIdUsuario(1);

        if (gestion.añadirPaciente(p)) {
            gestorArchivos.escribirLog("Paciente registrado: " + p.nombreCompleto());
            System.out.println("Paciente registrado. IMC: " + String.format("%.2f", p.calcularIMC()));
        } else {
            System.out.println("Error: capacidad maxima alcanzada");
        }
    }

    private void buscarPaciente() {
        System.out.print("Nombre: ");
        String nombre = scanner.nextLine();
        System.out.print("Apellido: ");
        String apellido = scanner.nextLine();
        int idx = gestion.buscarPacientePorNombre(nombre, apellido);

        if (idx >= 0) {
            Paciente p = gestion.getPaciente(idx);
            System.out.println("Paciente: " + p.nombreCompleto() +
                " | Edad: " + p.getEdad() +
                " | IMC: " + String.format("%.2f", p.calcularIMC()) +
                " | Tipo: " + p.tipoPaciente());
        } else {
            System.out.println("Paciente no encontrado");
        }
    }

    private void eliminarPaciente() {
        System.out.print("Nombre: ");
        String nombre = scanner.nextLine();
        System.out.print("Apellido: ");
        String apellido = scanner.nextLine();
        int idx = gestion.buscarPacientePorNombre(nombre, apellido);

        if (idx >= 0) {
            if (gestion.eliminarPaciente(idx)) {
                gestorArchivos.escribirLog("Paciente eliminado: " + nombre + " " + apellido);
                System.out.println("Paciente eliminado");
            } else {
                System.out.println("Error al eliminar");
            }
        } else {
            System.out.println("Paciente no encontrado");
        }
    }

    public void menuCitas() {
        boolean salir = false;
        while (!salir) {
            System.out.println("\n--- GESTION DE CITAS ---");
            System.out.println("1. Solicitar cita");
            System.out.println("2. Listar citas");
            System.out.println("3. Buscar cita");
            System.out.println("4. Cancelar cita");
            System.out.println("5. Volver");
            System.out.print("Opcion: ");
            String op = scanner.nextLine();

            if (op.equals("1")) {
                solicitarCita();
            } else if (op.equals("2")) {
                gestion.mostrarCitas();
            } else if (op.equals("3")) {
                buscarCita();
            } else if (op.equals("4")) {
                cancelarCita();
            } else if (op.equals("5")) {
                salir = true;
            } else {
                System.out.println("Opcion no valida");
            }
        }
    }

    private void solicitarCita() {
        if (gestion.getPacientesN() == 0) {
            System.out.println("No hay pacientes registrados");
        } else if (gestion.getProfesionalesN() == 0) {
            System.out.println("No hay profesionales disponibles");
        } else {
            System.out.println("Pacientes:");
            for (int i = 0; i < gestion.getPacientesN(); i++) {
                System.out.println((i + 1) + ". " + gestion.getPaciente(i).toString());
            }
            System.out.print("Seleccione: ");
            int idxPac = Integer.parseInt(scanner.nextLine()) - 1;

            System.out.println("Profesionales:");
            for (int i = 0; i < gestion.getProfesionalesN(); i++) {
                System.out.print((i + 1) + ". ");
                gestion.getProfesional(i).mostrarInfo();
            }
            System.out.print("Seleccione: ");
            int idxPro = Integer.parseInt(scanner.nextLine()) - 1;

            System.out.print("Dia: ");
            int dia = Integer.parseInt(scanner.nextLine());
            System.out.print("Mes: ");
            int mes = Integer.parseInt(scanner.nextLine());
            System.out.print("Anyo: ");
            int anyo = Integer.parseInt(scanner.nextLine());
            System.out.print("Modalidad (presencial/online): ");
            String mod = scanner.nextLine();
            System.out.print("Motivo: ");
            String motivo = scanner.nextLine();

            Paciente pac = gestion.getPaciente(idxPac);
            ProfesionalClinica pro = gestion.getProfesional(idxPro);
            int idPac = pac.getId();
            int idPro = pro.getId();

            Cita cita;
            if (mod.equalsIgnoreCase("online")) {
                System.out.print("Plataforma (Zoom/Meet/Teams): ");
                String plataforma = scanner.nextLine();
                cita = new CitaOnline(pac, pro, dia, mes, anyo, motivo, plataforma);
            } else {
                System.out.print("Sala: ");
                String sala = scanner.nextLine();
                cita = new CitaPresencial(pac, pro, dia, mes, anyo, motivo, sala);
            }
            cita.setIdPaciente(idPac);
            cita.setIdProfesional(idPro);

            if (gestion.añadirCita(cita)) {
                gestorArchivos.escribirLog("Cita creada: " + cita.mostrarCita());
                System.out.println("Cita registrada:");
                System.out.println(cita.mostrarCita());
            } else {
                System.out.println("Error: capacidad maxima alcanzada");
            }
        }
    }

    private void buscarCita() {
        System.out.print("Codigo de cita: ");
        int codigo = Integer.parseInt(scanner.nextLine());
        Cita c = gestion.buscarCitaPorCodigo(codigo);

        if (c != null) {
            System.out.println(c.mostrarCita());
        } else {
            System.out.println("Cita no encontrada");
        }
    }

    private void cancelarCita() {
        System.out.print("Codigo de cita: ");
        int codigo = Integer.parseInt(scanner.nextLine());
        Cita c = gestion.buscarCitaPorCodigo(codigo);

        if (c != null) {
            int idx = -1;
            for (int i = 0; i < gestion.getCitasN(); i++) {
                if (gestion.getCita(i).getCodigoCita() == codigo) {
                    idx = i;
                }
            }
            if (idx >= 0 && gestion.cancelarCita(idx)) {
                gestorArchivos.escribirLog("Cita cancelada: #" + codigo);
                System.out.println("Cita cancelada");
            } else {
                System.out.println("Error al cancelar");
            }
        } else {
            System.out.println("Cita no encontrada");
        }
    }

    public void menuPlanes() {
        boolean salir = false;
        while (!salir) {
            System.out.println("\n--- PLANES ALIMENTICIOS ---");
            System.out.println("1. Crear plan");
            System.out.println("2. Listar planes");
            System.out.println("3. Buscar plan por ID");
            System.out.println("4. Eliminar plan");
            System.out.println("5. Volver");
            System.out.print("Opcion: ");
            String op = scanner.nextLine();

            if (op.equals("1")) {
                crearPlan();
            } else if (op.equals("2")) {
                gestion.mostrarPlanes();
            } else if (op.equals("3")) {
                buscarPlan();
            } else if (op.equals("4")) {
                eliminarPlan();
            } else if (op.equals("5")) {
                salir = true;
            } else {
                System.out.println("Opcion no valida");
            }
        }
    }

    private void crearPlan() {
        if (gestion.getPacientesN() == 0) {
            System.out.println("No hay pacientes registrados");
        } else if (gestion.getProfesionalesN() == 0) {
            System.out.println("No hay profesionales disponibles");
        } else {
            System.out.println("Pacientes:");
            for (int i = 0; i < gestion.getPacientesN(); i++) {
                System.out.println((i + 1) + ". " + gestion.getPaciente(i).toString());
            }
            System.out.print("Seleccione: ");
            int iPac = Integer.parseInt(scanner.nextLine()) - 1;

            System.out.println("Profesionales:");
            for (int i = 0; i < gestion.getProfesionalesN(); i++) {
                System.out.print((i + 1) + ". ");
                gestion.getProfesional(i).mostrarInfo();
            }
            System.out.print("Seleccione: ");
            int iPro = Integer.parseInt(scanner.nextLine()) - 1;

            System.out.print("Fecha inicio (YYYY-MM-DD): ");
            String fIni = scanner.nextLine();
            System.out.print("Fecha fin (YYYY-MM-DD): ");
            String fFin = scanner.nextLine();
            if (fFin.isEmpty()) { fFin = null; }
            System.out.print("Objetivo: ");
            String obj = scanner.nextLine();
            System.out.print("Descripcion: ");
            String desc = scanner.nextLine();
            System.out.print("Calorias diarias: ");
            int cal = Integer.parseInt(scanner.nextLine());

            PlanAlimenticio plan = new PlanAlimenticio(
                gestion.getPaciente(iPac).getId(),
                gestion.getProfesional(iPro).getId(),
                fIni, fFin, obj, desc, cal
            );

            if (gestion.añadirPlan(plan)) {
                gestorArchivos.escribirLog("Plan creado para paciente ID " + plan.getIdPaciente());
                System.out.println("Plan creado: " + plan.mostrarPlan());
            } else {
                System.out.println("Error: capacidad maxima alcanzada");
            }
        }
    }

    private void buscarPlan() {
        System.out.print("ID del plan: ");
        int id = Integer.parseInt(scanner.nextLine());
        ArrayList<PlanAlimenticio> planes = gestion.planesBD();
        boolean encontrado = false;
        for (int i = 0; i < planes.size(); i++) {
            PlanAlimenticio p = planes.get(i);
            if (p.getId() == id) {
                System.out.println(p.mostrarPlan());
                encontrado = true;
            }
        }
        if (!encontrado) {
            System.out.println("Plan no encontrado");
        }
    }

    private void eliminarPlan() {
        System.out.print("ID del plan: ");
        int id = Integer.parseInt(scanner.nextLine());
        int idx = -1;
        for (int i = 0; i < gestion.getPlanesN(); i++) {
            if (gestion.getPlan(i).getId() == id) {
                idx = i;
            }
        }
        if (idx >= 0 && gestion.eliminarPlan(idx)) {
            gestorArchivos.escribirLog("Plan eliminado: #" + id);
            System.out.println("Plan eliminado");
        } else {
            System.out.println("Plan no encontrado");
        }
    }

    public void menuEstadisticas() {
        boolean salir = false;
        while (!salir) {
            System.out.println("\n--- ESTADISTICAS ---");
            System.out.println("1. Resumen general");
            System.out.println("2. Ingresos por nutricionista");
            System.out.println("3. Citas por modalidad");
            System.out.println("4. Proximas citas (7 dias)");
            System.out.println("5. Volver");
            System.out.print("Opcion: ");
            String op = scanner.nextLine();

            if (op.equals("1")) {
                estadisticas.mostrarResumen();
            } else if (op.equals("2")) {
                estadisticas.mostrarIngresosPorNutricionista();
            } else if (op.equals("3")) {
                estadisticas.mostrarCitasPorModalidad();
            } else if (op.equals("4")) {
                estadisticas.mostrarProximasCitas();
            } else if (op.equals("5")) {
                salir = true;
            } else {
                System.out.println("Opcion no valida");
            }
        }
    }

    public void menuArchivos() {
        boolean salir = false;
        while (!salir) {
            System.out.println("\n--- MODULO DE ARCHIVOS ---");
            System.out.println("1. Exportar citas a TXT");
            System.out.println("2. Ver configuracion");
            System.out.println("3. Ver log del sistema");
            System.out.println("4. Volver");
            System.out.print("Opcion: ");
            String op = scanner.nextLine();

            if (op.equals("1")) {
                ArrayList<Cita> citasBD = gestion.citasBD();
                if (gestorArchivos.exportarCitas(citasBD)) {
                    gestorArchivos.escribirLog("Citas exportadas a TXT");
                }
            } else if (op.equals("2")) {
                System.out.println("\n--- CONFIGURACION ---");
                System.out.println(gestorArchivos.leerConfiguracion());
            } else if (op.equals("3")) {
                System.out.println("\n--- LOG DEL SISTEMA ---");
                System.out.println(gestorArchivos.leerLog());
            } else if (op.equals("4")) {
                salir = true;
            } else {
                System.out.println("Opcion no valida");
            }
        }
    }
}
