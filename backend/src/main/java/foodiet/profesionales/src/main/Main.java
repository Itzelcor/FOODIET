package main;
import java.util.List;
import java.util.Scanner;

import dao.AgendaDAO;
import dao.AsignacionPacienteDAO;
import dao.ProfesionalDAO;
import dao.SustitucionDAO;
import model.Agenda;
import model.AsignacionPaciente;
import model.Profesional;
import model.Rol;
import model.Sustitucion;
import model.TipoEvento;
 
/**
 * Punto de entrada del subsistema de Gestión del Equipo Profesional — NutriPlus.
 *
 * Simula un acceso por rol: el usuario selecciona con qué perfil entra,
 * y el sistema muestra únicamente las opciones permitidas para ese rol.
 *
 * Roles y permisos:
 *  DIRECTIVO      → Acceso total: profesionales, asignaciones, agenda, sustituciones
 *  NUTRICIONISTA  → Solo su agenda y sus pacientes asignados
 *  ADMINISTRATIVO → Gestión de agenda y sustituciones
 */
public class Main {
 
    // DAOs globales — se instancian una vez
    static ProfesionalDAO      profesionalDAO      = new ProfesionalDAO();
    static AsignacionPacienteDAO asignacionDAO     = new AsignacionPacienteDAO();
    static AgendaDAO           agendaDAO           = new AgendaDAO();
    static SustitucionDAO      sustitucionDAO      = new SustitucionDAO();
 
    static Scanner scanner = new Scanner(System.in);
 
    // =========================================================================
    // MAIN
    // =========================================================================
    public static void main(String[] args) {
 
        System.out.println("=================================================");
        System.out.println("   FooDiet - Gestión del Equipo Profesional    ");
        System.out.println("=================================================");
 
        // --- Paso 1: Seleccionar profesional (simula el inicio de sesión) ---
        Profesional profesionalActual = seleccionarProfesional();
 
        if (profesionalActual == null) {
            System.out.println("No se encontró el profesional. Saliendo.");
            return;
        }
 
        System.out.println("\nBienvenido/a, " + profesionalActual.getNombre()
                           + " [" + profesionalActual.getRol() + "]");
 
        // --- Paso 2: Mostrar menú según el rol ---
        boolean salir = false;
        while (!salir) {
            mostrarMenuPorRol(profesionalActual.getRol());
            int opcion = leerEntero("Elige una opción: ");
 
            switch (profesionalActual.getRol()) {
 
                case DIRECTIVO:
                    salir = gestionarMenuDirectivo(opcion, profesionalActual);
                    break;
 
                case NUTRICIONISTA:
                    salir = gestionarMenuNutricionista(opcion, profesionalActual);
                    break;
 
                case ADMINISTRATIVO:
                    salir = gestionarMenuAdministrativo(opcion, profesionalActual);
                    break;
            }
        }
 
        System.out.println("\nSesión cerrada. ¡Hasta pronto!");
        scanner.close();
    }
 
    // =========================================================================
    // SELECCIÓN DE PROFESIONAL (simula login por ID)
    // =========================================================================
    private static Profesional seleccionarProfesional() {
        System.out.println("\nIntroduce tu ID de profesional para acceder: ");
        int id = leerEntero("> ");
        Profesional p = profesionalDAO.buscarPorId(id);
 
        if (p == null) {
            System.out.println("No se encontró ningún profesional con ID " + id);
        }
        return p;
    }
 
    // =========================================================================
    // MENÚS POR ROL
    // =========================================================================
    private static void mostrarMenuPorRol(Rol rol) {
        System.out.println("\n─────────────────────────────────");
        System.out.println("  MENÚ - " + rol);
        System.out.println("─────────────────────────────────");
 
        switch (rol) {
            case DIRECTIVO:
                System.out.println("1. Gestión de Profesionales");
                System.out.println("2. Asignación de Pacientes");
                System.out.println("3. Gestión de Agenda");
                System.out.println("4. Gestión de Sustituciones");
                System.out.println("0. Cerrar sesión");
                break;
 
            case NUTRICIONISTA:
                // Solo ve sus pacientes asignados y su agenda
                System.out.println("1. Ver mis pacientes asignados");
                System.out.println("2. Ver mi agenda");
                System.out.println("0. Cerrar sesión");
                break;
 
            case ADMINISTRATIVO:
                // Gestión de agenda y sustituciones, sin datos sensibles
                System.out.println("1. Gestión de Agenda");
                System.out.println("2. Gestión de Sustituciones");
                System.out.println("0. Cerrar sesión");
                break;
        }
    }
 
    // =========================================================================
    // GESTIÓN MENÚ DIRECTIVO
    // =========================================================================
    private static boolean gestionarMenuDirectivo(int opcion, Profesional actual) {
        switch (opcion) {
            case 1: menuProfesionales(); break;
            case 2: menuAsignaciones();  break;
            case 3: menuAgenda(actual, true); break;  // true = acceso total
            case 4: menuSustituciones(); break;
            case 0: return true;
            default: System.out.println("Opción no válida.");
        }
        return false;
    }
 
    // =========================================================================
    // GESTIÓN MENÚ NUTRICIONISTA
    // =========================================================================
    private static boolean gestionarMenuNutricionista(int opcion, Profesional actual) {
        switch (opcion) {
            case 1:
                // Solo ve sus propios pacientes
                System.out.println("\n--- Mis pacientes asignados ---");
                List<AsignacionPaciente> misAsignaciones =
                        asignacionDAO.listarPorProfesional(actual.getId());
                if (misAsignaciones.isEmpty()) {
                    System.out.println("No tienes pacientes asignados.");
                } else {
                    misAsignaciones.forEach(System.out::println);
                }
                break;
 
            case 2:
                // Solo ve su propia agenda
                menuAgenda(actual, false); // false = solo su agenda
                break;
 
            case 0: return true;
            default: System.out.println("Opción no válida.");
        }
        return false;
    }
 
    // =========================================================================
    // GESTIÓN MENÚ ADMINISTRATIVO
    // =========================================================================
    private static boolean gestionarMenuAdministrativo(int opcion, Profesional actual) {
        switch (opcion) {
            case 1: menuAgenda(actual, true); break;   // ve agenda de todos
            case 2: menuSustituciones(); break;
            case 0: return true;
            default: System.out.println("Opción no válida.");
        }
        return false;
    }
 
    // =========================================================================
    // SUBMENÚ: PROFESIONALES (solo DIRECTIVO)
    // =========================================================================
    private static void menuProfesionales() {
        System.out.println("\n══ GESTIÓN DE PROFESIONALES ══");
        System.out.println("1. Listar todos");
        System.out.println("2. Buscar por ID");
        System.out.println("3. Añadir nuevo profesional");
        System.out.println("4. Actualizar profesional");
        System.out.println("5. Eliminar profesional");
        System.out.println("0. Volver");
 
        int op = leerEntero("> ");
        switch (op) {
            case 1:
                List<Profesional> todos = profesionalDAO.listarTodos();
                if (todos.isEmpty()) System.out.println("No hay profesionales registrados.");
                else todos.forEach(System.out::println);
                break;
 
            case 2:
                int idBuscar = leerEntero("ID a buscar: ");
                Profesional encontrado = profesionalDAO.buscarPorId(idBuscar);
                System.out.println(encontrado != null ? encontrado : "No encontrado.");
                break;
 
            case 3:
                Profesional nuevo = pedirDatosProfesional();
                boolean creado = profesionalDAO.crear(nuevo);
                System.out.println(creado ? " Profesional añadido." : " Error al añadir.");
                break;
 
            case 4:
                int idActualizar = leerEntero("ID del profesional a actualizar: ");
                Profesional existente = profesionalDAO.buscarPorId(idActualizar);
                if (existente == null) {
                    System.out.println("No encontrado.");
                    break;
                }
                System.out.println("Datos actuales:\n" + existente);
                Profesional actualizado = pedirDatosProfesional();
                actualizado.setId(idActualizar);
                System.out.println(profesionalDAO.actualizar(actualizado)
                        ? " Profesional actualizado." : " Error al actualizar.");
                break;
 
            case 5:
                int idEliminar = leerEntero("ID del profesional a eliminar: ");
                System.out.println(profesionalDAO.eliminar(idEliminar)
                        ? " Profesional eliminado." : " Error al eliminar.");
                break;
 
            case 0: break;
            default: System.out.println("Opción no válida.");
        }
    }
 
    // =========================================================================
    // SUBMENÚ: ASIGNACIONES (solo DIRECTIVO)
    // =========================================================================
    private static void menuAsignaciones() {
        System.out.println("\n══ ASIGNACIÓN DE PACIENTES ══");
        System.out.println("1. Ver todas las asignaciones");
        System.out.println("2. Ver asignaciones de un profesional");
        System.out.println("3. Crear asignación");
        System.out.println("4. Eliminar asignación");
        System.out.println("0. Volver");
 
        int op = leerEntero("> ");
        switch (op) {
            case 1:
                List<AsignacionPaciente> todas = asignacionDAO.listarTodas();
                if (todas.isEmpty()) System.out.println("No hay asignaciones.");
                else todas.forEach(System.out::println);
                break;
 
            case 2:
                int idProf = leerEntero("ID del profesional: ");
                List<AsignacionPaciente> asigs = asignacionDAO.listarPorProfesional(idProf);
                if (asigs.isEmpty()) System.out.println("Sin asignaciones.");
                else asigs.forEach(System.out::println);
                break;
 
            case 3:
                int idProfNueva = leerEntero("ID del profesional: ");
                // ======================================================
                // TODO: Cuando recibas la tabla de Pacientes de tu compañero,
                // aquí puedes añadir una consulta para listar los pacientes
                // disponibles. Por ahora se introduce el ID manualmente.
                // ======================================================
                int idPaciente  = leerEntero("ID del paciente (confirmar con compañero): ");
                String fecha    = leerTexto("Fecha de asignación (YYYY-MM-DD): ");
                AsignacionPaciente nueva = new AsignacionPaciente(idProfNueva, idPaciente, fecha);
                System.out.println(asignacionDAO.crear(nueva)
                        ? " Asignación creada." : " Error al crear.");
                break;
 
            case 4:
                int idEliminar = leerEntero("ID de la asignación a eliminar: ");
                System.out.println(asignacionDAO.eliminar(idEliminar)
                        ? " Asignación eliminada." : " Error al eliminar.");
                break;
 
            case 0: break;
            default: System.out.println("Opción no válida.");
        }
    }
 
    // =========================================================================
    // SUBMENÚ: AGENDA
    // accesoTotal = true  → ve la agenda de todos (DIRECTIVO, ADMINISTRATIVO)
    // accesoTotal = false → solo ve su propia agenda (NUTRICIONISTA)
    // =========================================================================
    private static void menuAgenda(Profesional actual, boolean accesoTotal) {
        System.out.println("\n══ GESTIÓN DE AGENDA ══");
        System.out.println("1. " + (accesoTotal ? "Ver toda la agenda" : "Ver mi agenda"));
        System.out.println("2. Añadir evento");
        System.out.println("3. Actualizar evento");
        System.out.println("4. Eliminar evento");
        System.out.println("0. Volver");
 
        int op = leerEntero("> ");
        switch (op) {
            case 1:
                List<Agenda> eventos = accesoTotal
                        ? agendaDAO.listarTodos()
                        : agendaDAO.listarPorProfesional(actual.getId());
                if (eventos.isEmpty()) System.out.println("No hay eventos en la agenda.");
                else eventos.forEach(System.out::println);
                break;
 
            case 2:
                int idProfAgenda = accesoTotal
                        ? leerEntero("ID del profesional: ")
                        : actual.getId();
                String fechaEvento = leerTexto("Fecha (YYYY-MM-DD): ");
                TipoEvento tipo    = elegirTipoEvento();
                String desc        = leerTexto("Descripción: ");
                Agenda evento      = new Agenda(idProfAgenda, fechaEvento, tipo, desc);
                System.out.println(agendaDAO.crear(evento)
                        ? " Evento añadido." : " Error al añadir.");
                break;
 
            case 3:
                int idEvento  = leerEntero("ID del evento a actualizar: ");
                int idProfUpd = accesoTotal
                        ? leerEntero("ID del profesional: ")
                        : actual.getId();
                String fechaUpd = leerTexto("Nueva fecha (YYYY-MM-DD): ");
                TipoEvento tipoUpd = elegirTipoEvento();
                String descUpd  = leerTexto("Nueva descripción: ");
                Agenda eventoUpd = new Agenda(idProfUpd, fechaUpd, tipoUpd, descUpd);
                eventoUpd.setId(idEvento);
                System.out.println(agendaDAO.actualizar(eventoUpd)
                        ? " Evento actualizado." : " Error al actualizar.");
                break;
 
            case 4:
                int idEliminar = leerEntero("ID del evento a eliminar: ");
                System.out.println(agendaDAO.eliminar(idEliminar)
                        ? " Evento eliminado." : " Error al eliminar.");
                break;
 
            case 0: break;
            default: System.out.println("Opción no válida.");
        }
    }
 
    // =========================================================================
    // SUBMENÚ: SUSTITUCIONES (DIRECTIVO y ADMINISTRATIVO)
    // =========================================================================
    private static void menuSustituciones() {
        System.out.println("\n══ GESTIÓN DE SUSTITUCIONES ══");
        System.out.println("1. Ver todas las sustituciones");
        System.out.println("2. Registrar sustitución");
        System.out.println("3. Actualizar sustitución");
        System.out.println("4. Eliminar sustitución");
        System.out.println("0. Volver");
 
        int op = leerEntero("> ");
        switch (op) {
            case 1:
                List<Sustitucion> todas = sustitucionDAO.listarTodas();
                if (todas.isEmpty()) System.out.println("No hay sustituciones registradas.");
                else todas.forEach(System.out::println);
                break;
 
            case 2:
                int idAusente   = leerEntero("ID del profesional ausente: ");
                int idSustituto = leerEntero("ID del profesional sustituto: ");
                String inicio   = leerTexto("Fecha inicio (YYYY-MM-DD): ");
                String fin      = leerTexto("Fecha fin    (YYYY-MM-DD): ");
                String motivo   = leerTexto("Motivo: ");
                Sustitucion nueva = new Sustitucion(idAusente, idSustituto, inicio, fin, motivo);
                System.out.println(sustitucionDAO.crear(nueva)
                        ? " Sustitución registrada." : " Error al registrar.");
                break;
 
            case 3:
                int idUpd       = leerEntero("ID de la sustitución a actualizar: ");
                int idAusenteUpd   = leerEntero("ID del profesional ausente: ");
                int idSustitutoUpd = leerEntero("ID del profesional sustituto: ");
                String inicioUpd   = leerTexto("Fecha inicio (YYYY-MM-DD): ");
                String finUpd      = leerTexto("Fecha fin    (YYYY-MM-DD): ");
                String motivoUpd   = leerTexto("Motivo: ");
                Sustitucion sUpd   = new Sustitucion(idAusenteUpd, idSustitutoUpd,
                                                     inicioUpd, finUpd, motivoUpd);
                sUpd.setId(idUpd);
                System.out.println(sustitucionDAO.actualizar(sUpd)
                        ? " Sustitución actualizada." : " Error al actualizar.");
                break;
 
            case 4:
                int idEliminar = leerEntero("ID de la sustitución a eliminar: ");
                System.out.println(sustitucionDAO.eliminar(idEliminar)
                        ? " Sustitución eliminada." : " Error al eliminar.");
                break;
 
            case 0: break;
            default: System.out.println("Opción no válida.");
        }
    }
 
    // =========================================================================
    // AUXILIARES — Entradas de datos y lectura por consola
    // =========================================================================
 
    /** Pide todos los datos necesarios para crear un Profesional */
    private static Profesional pedirDatosProfesional() {
        String nombre       = leerTexto("Nombre: ");
        String apellidos    = leerTexto("Apellidos: ");
        String especialidad = leerTexto("Especialidad: ");
        String formacion    = leerTexto("Formación: ");
        String email        = leerTexto("Email: ");
        String telefono     = leerTexto("Teléfono: ");
        Rol rol             = elegirRol();
        return new Profesional(nombre, apellidos, especialidad, formacion, email, telefono, rol);
    }
 
    /** Muestra opciones de rol y devuelve el elegido */
    private static Rol elegirRol() {
        System.out.println("Rol (0=DIRECTIVO, 1=NUTRICIONISTA, 2=ADMINISTRATIVO): ");
        int op = leerEntero("> ");
        switch (op) {
            case 0:  return Rol.DIRECTIVO;
            case 1:  return Rol.NUTRICIONISTA;
            default: return Rol.ADMINISTRATIVO;
        }
    }
 
    /** Muestra opciones de tipo de evento y devuelve el elegido */
    private static TipoEvento elegirTipoEvento() {
        System.out.println("Tipo de evento (0=CONSULTA, 1=VACACIONES, 2=SUSTITUCION): ");
        int op = leerEntero("> ");
        switch (op) {
            case 0:  return TipoEvento.CONSULTA;
            case 1:  return TipoEvento.VACACIONES;
            default: return TipoEvento.SUSTITUCION;
        }
    }
 
    /** Lee un entero por consola con validación básica */
    private static int leerEntero(String mensaje) {
        while (true) {
            System.out.print(mensaje);
            try {
                return Integer.parseInt(scanner.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.println("  Por favor, introduce un número entero.");
            }
        }
    }
 
    /** Lee una cadena de texto no vacía por consola */
    private static String leerTexto(String mensaje) {
        String valor = "";
        while (valor.isEmpty()) {
            System.out.print(mensaje);
            valor = scanner.nextLine().trim();
            if (valor.isEmpty()) System.out.println("  El campo no puede estar vacío.");
        }
        return valor;
    }
}