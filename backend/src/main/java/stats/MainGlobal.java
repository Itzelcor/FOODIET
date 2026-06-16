package stats;

import stats.Pacientes.*;
import stats.Profesionales.*;
import stats.Planes.*;
import stats.Usuarios.*;
import stats.Administracion.*;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * Main Global — menú completo de todos los subsistemas de FooDiet.
 * FooDiet · IES Font de San Lluis · 1º DAW · 2025-2026
 */

    public class MainGlobal {
 
    private static final String URL      = "jdbc:mysql://localhost:3306/FooDiet";
    private static final String USUARIO  = "root";
    private static final String PASSWORD = "1903";
 
    static Scanner    scanner  = new Scanner(System.in);
    static Connection conexion;
 
    public static void main(String[] args) {
 
        System.out.println("╔══════════════════════════════════════════╗");
        System.out.println("║       FOODIET — SISTEMA DE GESTIÓN      ║");
        System.out.println("║    IES Font de San Lluis · 1º DAW        ║");
        System.out.println("╚══════════════════════════════════════════╝");
 
        boolean conectado = false;
        try {
            conexion  = DriverManager.getConnection(URL, USUARIO, PASSWORD);
            conectado = true;
            System.out.println("\n✓ Conexión con FooDiet establecida.\n");
        } catch (SQLException e) {
            System.out.println("✗ Error de conexión: " + e.getMessage());
        }
 
        if (conectado) {
            int opcion = -1;
            while (opcion != 0) {
                mostrarMenuPrincipal();
                opcion = leerEntero("Elige una opción: ");
                switch (opcion) {
                    case 1: menuUsuarios();       break;
                    case 2: menuPacientes();      break;
                    case 3: menuProfesionales();  break;
                    case 4: menuPlanes();          break;
                    case 5: menuEstadisticas();    break;
                    case 6: menuAdministracion();  break;
                    case 0: System.out.println("\nCerrando FooDiet..."); break;
                    default: System.out.println("Opción no válida.");
                }
            }
            try {
                conexion.close();
                System.out.println("✓ Conexión cerrada. ¡Hasta pronto!");
            } catch (SQLException e) {
                System.out.println("Error al cerrar: " + e.getMessage());
            }
        }
    }
 
    static void mostrarMenuPrincipal() {
        System.out.println("\n══════════════════════════════════════════");
        System.out.println("              MENÚ PRINCIPAL              ");
        System.out.println("══════════════════════════════════════════");
        System.out.println("1. Usuarios");
        System.out.println("2. Pacientes y Citas");
        System.out.println("3. Profesionales");
        System.out.println("4. Planes Alimenticios");
        System.out.println("5. Estadísticas");
        System.out.println("6. Administración");
        System.out.println("0. Salir");
        System.out.println("══════════════════════════════════════════");
    }
 
    // ── USUARIOS ──────────────────────────────────────────────────
    static void menuUsuarios() {
        UsuarioDAO dao = new UsuarioDAO(conexion);
        int opcion     = -1;
        while (opcion != 0) {
            System.out.println("\n--- USUARIOS ---");
            System.out.println("1. Ver todos");
            System.out.println("2. Buscar por email");
            System.out.println("3. Desactivar usuario");
            System.out.println("4. Eliminar usuario");
            System.out.println("5. Exportar informe");
            System.out.println("0. Volver");
            opcion = leerEntero("Opción: ");
            switch (opcion) {
                case 1: {
                    ArrayList<Usuario> lista = dao.listarTodos();
                    for (Usuario u : lista) { System.out.println("  " + u); }
                    System.out.println("Total: " + lista.size());
                    break;
                }
                case 2: {
                    String email = leerTexto("Email: ");
                    Usuario u    = dao.buscarPorEmail(email);
                    if (u != null) { System.out.println("  " + u); }
                    else { System.out.println("  No encontrado."); }
                    break;
                }
                case 3: {
                    int id = leerEntero("ID del usuario a desactivar: ");
                    dao.desactivar(id);
                    break;
                }
                case 4: {
                    int id = leerEntero("ID del usuario a eliminar: ");
                    dao.eliminar(id);
                    break;
                }
                case 5: {
                    exportarInforme(dao.listarTodos(), "Informe de Usuarios",
                        "id,email,rol,activo,fecha_alta",
                        new String[]{"ID","Email","Rol","Activo","Alta"},
                        "informe_usuarios");
                    break;
                }
                case 0: break;
                default: System.out.println("Opción no válida.");
            }
        }
    }
 
    // ── PACIENTES ─────────────────────────────────────────────────
    static void menuPacientes() {
        PacienteDAO pacienteDAO = new PacienteDAO(conexion);
        CitaDAO     citaDAO     = new CitaDAO(conexion);
        UsuarioDAO  usuarioDAO  = new UsuarioDAO(conexion);
        int opcion = -1;
 
        while (opcion != 0) {
            System.out.println("\n--- PACIENTES Y CITAS ---");
            System.out.println("1. Ver todos los pacientes");
            System.out.println("2. Buscar paciente por ID");
            System.out.println("3. Añadir paciente");
            System.out.println("4. Actualizar paciente");
            System.out.println("5. Eliminar paciente");
            System.out.println("6. Ver citas de un paciente");
            System.out.println("7. Nueva cita");
            System.out.println("8. Cancelar cita");
            System.out.println("9. Exportar informe");
            System.out.println("0. Volver");
            opcion = leerEntero("Opción: ");
            switch (opcion) {
                case 1: {
                    ArrayList<Paciente> lista = pacienteDAO.listarTodos();
                    for (Paciente p : lista) { System.out.println("  " + p); }
                    System.out.println("Total: " + lista.size());
                    break;
                }
                case 2: {
                    int id     = leerEntero("ID del paciente: ");
                    Paciente p = pacienteDAO.buscarPorId(id);
                    if (p != null) { System.out.println("  " + p); }
                    else { System.out.println("  No encontrado."); }
                    break;
                }
                case 3: {
                    System.out.println("  [Primero se crea el usuario de acceso]");
                    String email    = leerTexto("Email de acceso: ");
                    String password = leerTexto("Contraseña: ");
                    String fechaHoy = java.time.LocalDate.now().toString();
 
                    int idUsuario = usuarioDAO.insertarYObtenerID(
                            email, password, "paciente", fechaHoy);
 
                    if (idUsuario != -1) {
                        String dni      = leerTexto("DNI: ");
                        String nombre   = leerTexto("Nombre: ");
                        String apellidos = leerTexto("Apellidos: ");
                        String telefono  = leerTexto("Teléfono: ");
                        String direccion = leerTexto("Dirección: ");
                        String fechaNac  = leerTexto("Fecha nacimiento (YYYY-MM-DD): ");
                        String sexo      = leerTexto("Sexo (M/F): ");
                        double altura    = leerDecimal("Altura (m): ");
                        String tipo      = leerTexto("Tipo (joven/adulto/jubilado): ");
 
                        Paciente nuevo;
                        if (tipo.toLowerCase().equals("joven")) {
                            nuevo = new PacienteJoven(0, dni, nombre, apellidos,
                                    telefono, email, direccion, fechaNac, sexo, altura, false);
                        } else if (tipo.toLowerCase().equals("jubilado")) {
                            nuevo = new PacienteJubilado(0, dni, nombre, apellidos,
                                    telefono, email, direccion, fechaNac, sexo, altura, false);
                        } else {
                            nuevo = new PacienteAdulto(0, dni, nombre, apellidos,
                                    telefono, email, direccion, fechaNac, sexo, altura);
                        }
                        pacienteDAO.insertar(nuevo, idUsuario);
                    } else {
                        System.out.println("No se pudo crear el usuario. Operación cancelada.");
                    }
                    break;
                }
                case 4: {
                    int    id       = leerEntero("ID del paciente: ");
                    String telefono = leerTexto("Nuevo teléfono: ");
                    String email    = leerTexto("Nuevo email: ");
                    pacienteDAO.actualizar(id, telefono, email);
                    break;
                }
                case 5: {
                    int id = leerEntero("ID del paciente a eliminar: ");
                    pacienteDAO.eliminar(id);
                    break;
                }
                case 6: {
                    int id = leerEntero("ID del paciente: ");
                    ArrayList<Cita> citas = citaDAO.listarPorPaciente(id);
                    for (Cita c : citas) { System.out.println("  " + c); }
                    System.out.println("Total: " + citas.size());
                    break;
                }
                case 7: {
                    int    idPac     = leerEntero("ID paciente: ");
                    int    idProf    = leerEntero("ID profesional: ");
                    String fecha     = leerTexto("Fecha (YYYY-MM-DD): ");
                    String hora      = leerTexto("Hora (HH:MM): ");
                    String modalidad = leerTexto("Modalidad (presencial/online): ");
                    String motivo    = leerTexto("Motivo: ");
                    citaDAO.insertar(new Cita(0, idPac, idProf, fecha, hora,
                                             "pendiente", modalidad, motivo, ""));
                    break;
                }
                case 8: {
                    int id = leerEntero("ID de la cita a cancelar: ");
                    citaDAO.cancelar(id);
                    break;
                }
                case 9: {
                    exportarInforme(pacienteDAO.listarTodos(), "Informe de Pacientes",
                        "id,nombre,apellidos,dni,telefono,email,tipo",
                        new String[]{"ID","Nombre","Apellidos","DNI","Teléfono","Email","Tipo"},
                        "informe_pacientes");
                    break;
                }
                case 0: break;
                default: System.out.println("Opción no válida.");
            }
        }
    }
 
    // ── PROFESIONALES ─────────────────────────────────────────────
    static void menuProfesionales() {
        ProfesionalDAO dao       = new ProfesionalDAO(conexion);
        UsuarioDAO     usuariDAO = new UsuarioDAO(conexion);
        int opcion = -1;
 
        while (opcion != 0) {
            System.out.println("\n--- PROFESIONALES ---");
            System.out.println("1. Ver todos los profesionales");
            System.out.println("2. Ver profesionales activos");
            System.out.println("3. Añadir profesional");
            System.out.println("4. Dar de baja");
            System.out.println("5. Eliminar profesional");
            System.out.println("6. Exportar informe");
            System.out.println("0. Volver");
            opcion = leerEntero("Opción: ");
            switch (opcion) {
                case 1: {
                    ArrayList<ProfesionalClinica> lista = dao.listarTodos();
                    for (ProfesionalClinica p : lista) { System.out.println("  " + p); }
                    System.out.println("Total: " + lista.size());
                    break;
                }
                case 2: {
                    ArrayList<ProfesionalClinica> lista = dao.listarActivos();
                    for (ProfesionalClinica p : lista) { System.out.println("  " + p); }
                    System.out.println("Total: " + lista.size());
                    break;
                }
                case 3: {
                    System.out.println("  [Primero se crea el usuario de acceso]");
                    String email    = leerTexto("Email de acceso: ");
                    String password = leerTexto("Contraseña: ");
                    String rol      = leerTexto("Rol (dietista/nutricionista/entrenador/administrativo): ");
                    String fechaHoy = java.time.LocalDate.now().toString();
 
                    int idUsuario = usuariDAO.insertarYObtenerID(
                            email, password, rol, fechaHoy);
 
                    if (idUsuario != -1) {
                        String nombre   = leerTexto("Nombre completo: ");
                        String telefono = leerTexto("Teléfono: ");
                        int    exp      = leerEntero("Años de experiencia: ");
 
                        ProfesionalClinica nuevo;
                        if (rol.equals("dietista")) {
                            nuevo = new Dietista(0, nombre, "", email, telefono, exp, true);
                        } else if (rol.equals("entrenador")) {
                            nuevo = new Entrenador(0, nombre, "", email, telefono, exp, true);
                        } else if (rol.equals("administrativo")) {
                            nuevo = new Administrativo(0, nombre, "", email, telefono, exp, true);
                        } else {
                            nuevo = new Nutricionista(0, nombre, "", email, telefono, exp, true);
                        }
                        dao.insertar(nuevo, idUsuario);
                    } else {
                        System.out.println("No se pudo crear el usuario. Operación cancelada.");
                    }
                    break;
                }
                case 4: {
                    int id = leerEntero("ID del profesional a dar de baja: ");
                    dao.darDeBaja(id);
                    break;
                }
                case 5: {
                    int id = leerEntero("ID del profesional a eliminar: ");
                    dao.eliminar(id);
                    break;
                }
                case 6: {
                    exportarInforme(dao.listarTodos(), "Informe de Profesionales",
                        "id,nombre,rol,email,experiencia,activo",
                        new String[]{"ID","Nombre","Rol","Email","Exp.","Activo"},
                        "informe_profesionales");
                    break;
                }
                case 0: break;
                default: System.out.println("Opción no válida.");
            }
        }
    }
 
    // ── PLANES ────────────────────────────────────────────────────
    static void menuPlanes() {
        PlanDAO dao = new PlanDAO(conexion);
        int opcion  = -1;
        while (opcion != 0) {
            System.out.println("\n--- PLANES ALIMENTICIOS ---");
            System.out.println("1. Ver todos los planes");
            System.out.println("2. Ver planes de un paciente");
            System.out.println("3. Añadir plan");
            System.out.println("4. Actualizar plan");
            System.out.println("5. Eliminar plan");
            System.out.println("6. Ver comidas de un menú");
            System.out.println("7. Exportar informe");
            System.out.println("0. Volver");
            opcion = leerEntero("Opción: ");
            switch (opcion) {
                case 1: {
                    ArrayList<PlanDietetico> lista = dao.listarTodos();
                    for (PlanDietetico p : lista) { System.out.println("  " + p); }
                    System.out.println("Total: " + lista.size());
                    break;
                }
                case 2: {
                    int id = leerEntero("ID del paciente: ");
                    ArrayList<PlanDietetico> lista = dao.listarPorPaciente(id);
                    for (PlanDietetico p : lista) { System.out.println("  " + p); }
                    System.out.println("Total: " + lista.size());
                    break;
                }
                case 3: {
                    int    idPac  = leerEntero("ID paciente: ");
                    int    idNut  = leerEntero("ID nutricionista: ");
                    String obj    = leerTexto("Objetivo: ");
                    int    kcal   = leerEntero("Calorías diarias: ");
                    String inicio = leerTexto("Fecha inicio (YYYY-MM-DD): ");
                    String fin    = leerTexto("Fecha fin (YYYY-MM-DD): ");
                    dao.insertar(new PlanDietetico(0, idPac, idNut, obj, kcal, inicio, fin, "activo"));
                    break;
                }
                case 4: {
                    int    id   = leerEntero("ID del plan: ");
                    String obj  = leerTexto("Nuevo objetivo: ");
                    int    kcal = leerEntero("Nuevas calorías: ");
                    dao.actualizar(id, obj, kcal);
                    break;
                }
                case 5: {
                    int id = leerEntero("ID del plan a eliminar: ");
                    dao.eliminar(id);
                    break;
                }
                case 6: {
                    int id = leerEntero("ID del menú: ");
                    ArrayList<Comida> comidas = dao.listarComidasPorMenu(id);
                    for (Comida c : comidas) { System.out.println("  " + c); }
                    System.out.println("Total: " + comidas.size());
                    break;
                }
                case 7: {
                    exportarInforme(dao.listarTodos(), "Informe de Planes",
                        "id,id_paciente,objetivo,calorias,fecha_inicio,fecha_fin,estado",
                        new String[]{"ID","Paciente","Objetivo","Kcal/día","Inicio","Fin","Estado"},
                        "informe_planes");
                    break;
                }
                case 0: break;
                default: System.out.println("Opción no válida.");
            }
        }
    }
 
    // ── ESTADÍSTICAS ──────────────────────────────────────────────
    static void menuEstadisticas() {
        ProgresoDAO dao = new ProgresoDAO();
        int opcion      = -1;
        while (opcion != 0) {
            System.out.println("\n--- ESTADÍSTICAS ---");
            System.out.println("1. Ver todos los registros de progreso");
            System.out.println("2. Ver progreso de un paciente");
            System.out.println("3. Añadir registro de progreso");
            System.out.println("4. Actualizar peso e IMC");
            System.out.println("5. Eliminar registro");
            System.out.println("6. Ver resumen estadístico");
            System.out.println("7. Exportar informe");
            System.out.println("0. Volver");
            opcion = leerEntero("Opción: ");
            switch (opcion) {
                case 1: {
                    ArrayList<Progreso> lista = dao.listarTodos();
                    for (Progreso p : lista) { System.out.println("  " + p); }
                    System.out.println("Total: " + lista.size());
                    break;
                }
                case 2: {
                    int id = leerEntero("ID del paciente: ");
                    ArrayList<Progreso> lista = dao.listarPorPaciente(id);
                    for (Progreso p : lista) { System.out.println("  " + p); }
                    System.out.println("Total: " + lista.size());
                    break;
                }
                case 3: {
                    System.out.println("  IDs de pacientes disponibles: usa opción 1 de Pacientes para verlos.");
                    int    idPac  = leerEntero("ID paciente (debe existir en BD): ");
                    int    idPlan = leerEntero("ID plan (debe existir en BD): ");
                    String fecha  = leerTexto("Fecha (YYYY-MM-DD): ");
                    double peso   = leerDecimal("Peso (kg): ");
                    double imc    = leerDecimal("IMC: ");
                    String obs    = leerTexto("Observaciones: ");
                    dao.insertar(new Progreso(idPac, idPlan, fecha, peso, imc, obs));
                    break;
                }
                case 4: {
                    int    id   = leerEntero("ID del registro: ");
                    double peso = leerDecimal("Nuevo peso (kg): ");
                    double imc  = leerDecimal("Nuevo IMC: ");
                    dao.actualizar(id, peso, imc);
                    break;
                }
                case 5: {
                    int id = leerEntero("ID del registro a eliminar: ");
                    dao.eliminar(id);
                    break;
                }
                case 6: {
                    ArrayList<Progreso> lista = dao.listarTodos();
                    if (lista.isEmpty()) {
                        System.out.println("No hay datos.");
                    } else {
                        double totalPeso = 0;
                        double totalImc  = 0;
                        int    conImc    = 0;
                        for (Progreso p : lista) {
                            totalPeso += p.getPeso();
                            if (p.getImc() > 0) {
                                totalImc += p.getImc();
                                conImc++;
                            }
                        }
                        System.out.println("Peso medio : " + String.format("%.2f", totalPeso / lista.size()) + " kg");
                        if (conImc > 0) {
                            System.out.println("IMC medio  : " + String.format("%.2f", totalImc / conImc));
                        }
                        System.out.println("Total regs : " + lista.size());
                    }
                    break;
                }
                case 7: {
                    exportarInforme(dao.listarTodos(), "Informe de Estadísticas",
                        "id,id_paciente,id_plan,fecha,peso,imc,observaciones",
                        new String[]{"ID","Paciente","Plan","Fecha","Peso","IMC","Observaciones"},
                        "informe_estadisticas");
                    break;
                }
                case 0: break;
                default: System.out.println("Opción no válida.");
            }
        }
    }
 
    // ── ADMINISTRACIÓN ────────────────────────────────────────────
    static void menuAdministracion() {
        FacturaDAO facturaDAO = new FacturaDAO(conexion);
        UsuarioDAO usuarioDAO = new UsuarioDAO(conexion);
        int opcion = -1;
 
        while (opcion != 0) {
            System.out.println("\n--- ADMINISTRACIÓN ---");
            System.out.println("1. Ver todas las facturas");
            System.out.println("2. Ver facturas pendientes");
            System.out.println("3. Ver facturas de un paciente");
            System.out.println("4. Nueva factura");
            System.out.println("5. Marcar factura como pagada");
            System.out.println("6. Eliminar factura");
            System.out.println("7. Ver total de ingresos");
            System.out.println("8. Gestión de usuarios");
            System.out.println("9. Exportar informe de facturas");
            System.out.println("0. Volver");
            opcion = leerEntero("Opción: ");
            switch (opcion) {
                case 1: {
                    ArrayList<Factura> lista = facturaDAO.listarTodas();
                    for (Factura f : lista) { System.out.println("  " + f); }
                    System.out.println("Total: " + lista.size());
                    break;
                }
                case 2: {
                    ArrayList<Factura> lista = facturaDAO.listarPendientes();
                    for (Factura f : lista) { System.out.println("  " + f); }
                    System.out.println("Total pendientes: " + lista.size());
                    break;
                }
                case 3: {
                    int id = leerEntero("ID del paciente: ");
                    ArrayList<Factura> lista = facturaDAO.listarPorPaciente(id);
                    for (Factura f : lista) { System.out.println("  " + f); }
                    System.out.println("Total: " + lista.size());
                    break;
                }
                case 4: {
                    int    idCita  = leerEntero("ID de la cita: ");
                    int    idPac   = leerEntero("ID del paciente: ");
                    double importe = leerDecimal("Importe (€): ");
                    String metodo  = leerTexto("Método de pago (tarjeta/efectivo/transferencia): ");
                    String fecha   = leerTexto("Fecha de emisión (YYYY-MM-DD): ");
                    facturaDAO.insertar(new Factura(0, idCita, idPac, importe,
                                       "pendiente", metodo, fecha, null));
                    break;
                }
                case 5: {
                    int    id     = leerEntero("ID de la factura: ");
                    String metodo = leerTexto("Método de pago: ");
                    String fecha  = leerTexto("Fecha de pago (YYYY-MM-DD): ");
                    facturaDAO.marcarPagada(id, metodo, fecha);
                    break;
                }
                case 6: {
                    int id = leerEntero("ID de la factura a eliminar: ");
                    facturaDAO.eliminar(id);
                    break;
                }
                case 7: {
                    double total = facturaDAO.calcularTotalIngresos();
                    System.out.println("Total ingresos cobrados: " + String.format("%.2f", total) + " €");
                    break;
                }
                case 8: {
                    ArrayList<Usuario> usuarios = usuarioDAO.listarTodos();
                    for (Usuario u : usuarios) { System.out.println("  " + u); }
                    System.out.println("Total: " + usuarios.size());
                    break;
                }
                case 9: {
                    exportarInforme(facturaDAO.listarTodas(), "Informe de Facturas",
                        "id,id_cita,id_paciente,importe,estado,metodo,fecha_emision,fecha_pago",
                        new String[]{"ID","Cita","Paciente","Importe","Estado","Método","Emisión","Pago"},
                        "informe_facturas");
                    break;
                }
                case 0: break;
                default: System.out.println("Opción no válida.");
            }
        }
    }
 
    // ── EXPORTAR ──────────────────────────────────────────────────
    static void exportarInforme(ArrayList<?> lista, String titulo,
                                String cabeceraCsv, String[] columnasHtml,
                                String nombreBase) {
        System.out.println("\n¿En qué formato exportar?");
        System.out.println("1. TXT   2. CSV   3. HTML   4. Los tres");
        int fmt = leerEntero("Formato: ");
 
        boolean txt  = (fmt == 1 || fmt == 4);
        boolean csv  = (fmt == 2 || fmt == 4);
        boolean html = (fmt == 3 || fmt == 4);
 
        if (txt)  { GestorFicherosGlobal.exportarTxt(lista, titulo, nombreBase + ".txt"); }
        if (csv)  { GestorFicherosGlobal.exportarCsv(lista, cabeceraCsv, nombreBase + ".csv"); }
        if (html) { GestorFicherosGlobal.exportarHtml(lista, titulo, columnasHtml, nombreBase + ".html"); }
        if (!txt && !csv && !html) { System.out.println("Formato no válido."); }
    }
 
    // ── UTILIDADES ────────────────────────────────────────────────
    static int leerEntero(String mensaje) {
        int     valor  = 0;
        boolean valido = false;
        while (!valido) {
            System.out.print(mensaje);
            try {
                valor  = Integer.parseInt(scanner.nextLine().trim());
                valido = true;
            } catch (NumberFormatException e) {
                System.out.println("Introduce un número entero válido.");
            }
        }
        return valor;
    }
 
    static double leerDecimal(String mensaje) {
        double  valor  = 0;
        boolean valido = false;
        while (!valido) {
            System.out.print(mensaje);
            try {
                valor  = Double.parseDouble(scanner.nextLine().trim().replace(",", "."));
                valido = true;
            } catch (NumberFormatException e) {
                System.out.println("Introduce un número decimal válido.");
            }
        }
        return valor;
    }
 
    static String leerTexto(String mensaje) {
        System.out.print(mensaje);
        return scanner.nextLine().trim();
    }
}