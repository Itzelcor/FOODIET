import foodiet.modelo.Paciente;
import foodiet.modelo.PacienteJoven;
import foodiet.modelo.PacienteAdulto;
import foodiet.modelo.PacienteJubilado;
import foodiet.modelo.Cita;
import foodiet.modelo.CitaOnline;
import foodiet.modelo.CitaPresencial;
import foodiet.modelo.ProfesionalClinica;
import foodiet.modelo.ServicioClinica;
import foodiet.modelo.PlanAlimenticio;
import foodiet.gestion.GestionClinica;
import foodiet.gestion.GestorArchivos;
import foodiet.gestion.Estadisticas;
import foodiet.datos.ConexionBD;
import hacer_main_comun.Estadisticas.src.modelo.Metrica;
import hacer_main_comun.Estadisticas.src.modelo.MetricaPeso;
import hacer_main_comun.Estadisticas.src.modelo.MetricaIMC;
import hacer_main_comun.Estadisticas.src.modelo.MetricaGrasa;
import hacer_main_comun.Estadisticas.src.modelo.Estadistica;
import hacer_main_comun.Estadisticas.src.modelo.Informe;
import hacer_main_comun.Estadisticas.src.modelo.Progreso;
import hacer_main_comun.Estadisticas.src.modelo.GestorFicheros;
import hacer_main_comun.Estadisticas.src.dao.ProgresoDAO;
import hacer_main_comun.Gestion_del_Equipo_Profesional.src.model.Profesional;
import hacer_main_comun.Gestion_del_Equipo_Profesional.src.model.Rol;
import hacer_main_comun.Gestion_del_Equipo_Profesional.src.model.AsignacionPaciente;
import hacer_main_comun.Gestion_del_Equipo_Profesional.src.model.Agenda;
import hacer_main_comun.Gestion_del_Equipo_Profesional.src.model.TipoEvento;
import hacer_main_comun.Gestion_del_Equipo_Profesional.src.model.Sustitucion;
import java.util.ArrayList;
import java.util.Scanner;

public class Main {

    private static Scanner scanner = new Scanner(System.in);
    private static GestionClinica gestion = new GestionClinica();
    private static GestorArchivos gestorArchivos = new GestorArchivos();
    private static Estadisticas estadisticas = null;
    private static GestorAuditoria gestorAuditoria = new GestorAuditoria();
    private static Administrador admin = new Administrador(1, "Marina Soler", "marina@foodiet.com", "ALTO");
    private static SubsistemaAdministrador subsistemaAdmin = new SubsistemaAdministrador(gestorAuditoria);

    public static void main(String[] args) {
        inicializarEstadisticas();
        cargarDatosEjemplo();
        gestorAuditoria.registrarAccion("Inicio del sistema FooDiet");
        gestorArchivos.escribirLog("Sistema iniciado");

        boolean salir = false;
        while (!salir) {
            System.out.println("\n========================================");
            System.out.println("   MENU PRINCIPAL - FOODIET");
            System.out.println("========================================");
            System.out.println("1. Gestion de pacientes");
            System.out.println("2. Gestion de citas");
            System.out.println("3. Gestion de planes alimenticios");
            System.out.println("4. Estadisticas (BD)");
            System.out.println("5. Servicios de la clinica");
            System.out.println("6. Metricas (polimorfismo)");
            System.out.println("7. Seguridad y auditoria");
            System.out.println("8. Archivos (TXT)");
            System.out.println("9. Equipo profesional");
            System.out.println("10. Exportar informe de progreso");
            System.out.println("11. Salir");
            System.out.print("Seleccione una opcion: ");
            String opcion = scanner.nextLine();

            switch (opcion) {
                case "1":
                    menuPacientes();
                    break;
                case "2":
                    menuCitas();
                    break;
                case "3":
                    menuPlanes();
                    break;
                case "4":
                    menuEstadisticas();
                    break;
                case "5":
                    mostrarServicios();
                    break;
                case "6":
                    menuMetricas();
                    break;
                case "7":
                    menuSeguridad();
                    break;
                case "8":
                    menuArchivos();
                    break;
                case "9":
                    menuEquipoProfesional();
                    break;
                case "10":
                    menuExportarProgreso();
                    break;
                case "11":
                    salir = cerrarSistema();
                    break;
                default:
                    System.out.println("Opcion no valida");
                    break;
            }
        }
    }

    private static void inicializarEstadisticas() {
        if (ConexionBD.getConexion() != null) {
            estadisticas = new Estadisticas();
            System.out.println("Estadisticas conectadas a la BD.");
        } else {
            System.out.println("Estadisticas en modo local (sin BD).");
        }
    }

    private static void cargarDatosEjemplo() {
        PacienteJoven joven = new PacienteJoven("Ana", "Lopez", 19, 58.0, 1.65);
        joven.setTelefono("645678901");
        joven.setEmail("ana@email.com");
        joven.setIdUsuario(5);

        PacienteAdulto adulto = new PacienteAdulto("Carlos", "Perez", 42, 82.0, 1.78);
        adulto.setTelefono("656789012");
        adulto.setEmail("carlos@email.com");
        adulto.setIdUsuario(6);

        PacienteJubilado jubilado = new PacienteJubilado("Manuel", "Garcia", 71, 74.0, 1.70);
        jubilado.setTelefono("667890123");
        jubilado.setEmail("manuel@email.com");
        jubilado.setIdUsuario(7);

        gestion.añadirPaciente(joven);
        gestion.añadirPaciente(adulto);
        gestion.añadirPaciente(jubilado);

        ProfesionalClinica d1 = new ProfesionalClinica("Laura", "Ruiz", "Nutricion deportiva", 8, "L-M-V 9-14", "600111222");
        ProfesionalClinica d2 = new ProfesionalClinica("Pedro", "Diaz", "Nutricion clinica", 12, "L-X-V 15-20", "600333444");
        ProfesionalClinica d3 = new ProfesionalClinica("Sergio", "Gonzalez", "Dietetica general", 5, "L-V 8-15", "600555666");

        gestion.añadirProfesional(d1);
        gestion.añadirProfesional(d2);
        gestion.añadirProfesional(d3);

        CitaPresencial c1 = new CitaPresencial(joven, d1, 15, 6, 2026, "Revision trimestral", "Sala 3");
        CitaOnline c2 = new CitaOnline(adulto, d2, 16, 6, 2026, "Primera consulta", "Zoom");

        gestion.añadirCita(c1);
        gestion.añadirCita(c2);

        PlanAlimenticio plan1 = new PlanAlimenticio(joven.getId(), d1.getId(), "01/06/2026", "01/09/2026", "Perdida de peso", "Dieta hipocalorica equilibrada", 1800);
        PlanAlimenticio plan2 = new PlanAlimenticio(adulto.getId(), d2.getId(), "01/06/2026", "01/12/2026", "Control glucemico", "Dieta para diabeticos tipo 2", 2000);

        gestion.añadirPlan(plan1);
        gestion.añadirPlan(plan2);
    }

    // ======================================================================
    // OPCION 1 - GESTION DE PACIENTES
    // ======================================================================

    private static void menuPacientes() {
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

            switch (op) {
                case "1":
                    registrarPaciente();
                    break;
                case "2":
                    gestion.mostrarPacientes();
                    break;
                case "3":
                    buscarPaciente();
                    break;
                case "4":
                    eliminarPaciente();
                    break;
                case "5":
                    salir = true;
                    break;
                default:
                    System.out.println("Opcion no valida");
                    break;
            }
        }
    }

    private static void registrarPaciente() {
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
        System.out.print("Telefono: ");
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
                p = new PacienteJoven(nombre, apellido, edad, peso, altura, telefono, email, historial, objetivos);
                break;
            case "jubilado":
                p = new PacienteJubilado(nombre, apellido, edad, peso, altura, telefono, email, historial, objetivos);
                break;
            default:
                p = new PacienteAdulto(nombre, apellido, edad, peso, altura, telefono, email, historial, objetivos);
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

    private static void buscarPaciente() {
        System.out.print("Nombre: ");
        String nombre = scanner.nextLine();
        System.out.print("Apellido: ");
        String apellido = scanner.nextLine();
        int idx = gestion.buscarPacientePorNombre(nombre, apellido);
        if (idx >= 0) {
            Paciente p = gestion.getPaciente(idx);
            System.out.println("Paciente: " + p.nombreCompleto() + " | Edad: " + p.getEdad() + " | IMC: " + String.format("%.2f", p.calcularIMC()) + " | Tipo: " + p.tipoPaciente());
        } else {
            System.out.println("Paciente no encontrado");
        }
    }

    private static void eliminarPaciente() {
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

    // ======================================================================
    // OPCION 2 - GESTION DE CITAS
    // ======================================================================

    private static void menuCitas() {
        boolean salir = false;
        while (!salir) {
            System.out.println("\n--- GESTION DE CITAS ---");
            System.out.println("1. Solicitar cita");
            System.out.println("2. Listar citas");
            System.out.println("3. Buscar cita por codigo");
            System.out.println("4. Cancelar cita");
            System.out.println("5. Volver");
            System.out.print("Opcion: ");
            String op = scanner.nextLine();

            switch (op) {
                case "1":
                    solicitarCita();
                    break;
                case "2":
                    gestion.mostrarCitas();
                    break;
                case "3":
                    buscarCita();
                    break;
                case "4":
                    cancelarCita();
                    break;
                case "5":
                    salir = true;
                    break;
                default:
                    System.out.println("Opcion no valida");
                    break;
            }
        }
    }

    private static void solicitarCita() {
        int nPac = gestion.getPacientesN();
        int nProf = gestion.getProfesionalesN();
        if (nPac == 0) {
            System.out.println("No hay pacientes registrados");
        } else if (nProf == 0) {
            System.out.println("No hay profesionales disponibles");
        } else {
            System.out.println("Pacientes:");
            for (int i = 0; i < nPac; i++) {
                System.out.println((i + 1) + ". " + gestion.getPaciente(i).toString());
            }
            System.out.print("Seleccione: ");
            int idxPac = Integer.parseInt(scanner.nextLine()) - 1;

            System.out.println("Profesionales:");
            for (int i = 0; i < nProf; i++) {
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
            cita.setIdPaciente(pac.getId());
            cita.setIdProfesional(pro.getId());

            if (gestion.añadirCita(cita)) {
                gestorArchivos.escribirLog("Cita creada: " + cita.mostrarCita());
                System.out.println("Cita registrada:");
                System.out.println(cita.mostrarCita());
            } else {
                System.out.println("Error: capacidad maxima alcanzada");
            }
        }
    }

    private static void buscarCita() {
        System.out.print("Codigo de cita: ");
        int codigo = Integer.parseInt(scanner.nextLine());
        Cita c = gestion.buscarCitaPorCodigo(codigo);
        if (c != null) {
            System.out.println(c.mostrarCita());
        } else {
            System.out.println("Cita no encontrada");
        }
    }

    private static void cancelarCita() {
        System.out.print("Codigo de cita: ");
        int codigo = Integer.parseInt(scanner.nextLine());
        Cita c = gestion.buscarCitaPorCodigo(codigo);
        if (c != null) {
            int nCitas = gestion.getCitasN();
            int idx = -1;
            for (int i = 0; i < nCitas; i++) {
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

    // ======================================================================
    // OPCION 3 - GESTION DE PLANES ALIMENTICIOS
    // ======================================================================

    private static void menuPlanes() {
        boolean salir = false;
        while (!salir) {
            System.out.println("\n--- PLANES ALIMENTICIOS ---");
            System.out.println("1. Crear plan");
            System.out.println("2. Listar planes");
            System.out.println("3. Eliminar plan");
            System.out.println("4. Volver");
            System.out.print("Opcion: ");
            String op = scanner.nextLine();

            switch (op) {
                case "1":
                    crearPlan();
                    break;
                case "2":
                    gestion.mostrarPlanes();
                    break;
                case "3":
                    eliminarPlan();
                    break;
                case "4":
                    salir = true;
                    break;
                default:
                    System.out.println("Opcion no valida");
                    break;
            }
        }
    }

    private static void crearPlan() {
        int nPac = gestion.getPacientesN();
        int nProf = gestion.getProfesionalesN();
        if (nPac == 0) {
            System.out.println("No hay pacientes registrados");
        } else if (nProf == 0) {
            System.out.println("No hay profesionales disponibles");
        } else {
            System.out.println("Pacientes:");
            for (int i = 0; i < nPac; i++) {
                System.out.println((i + 1) + ". " + gestion.getPaciente(i).toString());
            }
            System.out.print("Seleccione: ");
            int iPac = Integer.parseInt(scanner.nextLine()) - 1;

            System.out.println("Profesionales:");
            for (int i = 0; i < nProf; i++) {
                System.out.print((i + 1) + ". ");
                gestion.getProfesional(i).mostrarInfo();
            }
            System.out.print("Seleccione: ");
            int iPro = Integer.parseInt(scanner.nextLine()) - 1;

            System.out.print("Fecha inicio (YYYY-MM-DD): ");
            String fIni = scanner.nextLine();
            System.out.print("Fecha fin (YYYY-MM-DD): ");
            String fFin = scanner.nextLine();
            if (fFin.isEmpty()) {
                fFin = null;
            }
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

    private static void eliminarPlan() {
        System.out.print("ID del plan: ");
        int id = Integer.parseInt(scanner.nextLine());
        int nPlanes = gestion.getPlanesN();
        int idx = -1;
        for (int i = 0; i < nPlanes; i++) {
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

    // ======================================================================
    // OPCION 4 - ESTADISTICAS
    // ======================================================================

    private static void menuEstadisticas() {
        if (estadisticas == null) {
            System.out.println("Estadisticas no disponibles (sin conexion BD)");
        } else {
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

                switch (op) {
                    case "1":
                        estadisticas.mostrarResumen();
                        break;
                    case "2":
                        estadisticas.mostrarIngresosPorNutricionista();
                        break;
                    case "3":
                        estadisticas.mostrarCitasPorModalidad();
                        break;
                    case "4":
                        estadisticas.mostrarProximasCitas();
                        break;
                    case "5":
                        salir = true;
                        break;
                    default:
                        System.out.println("Opcion no valida");
                        break;
                }
            }
        }
    }

    // ======================================================================
    // OPCION 5 - SERVICIOS
    // ======================================================================

    private static void mostrarServicios() {
        ServicioClinica[] servicios = ServicioClinica.obtenerServicios();
        int n = servicios.length;
        System.out.println("\n--- SERVICIOS FOODIET ---");
        for (int i = 0; i < n; i++) {
            System.out.println((i + 1) + ". " + servicios[i].mostrarServicio());
        }
    }

    // ======================================================================
    // OPCION 6 - METRICAS (polimorfismo)
    // ======================================================================

    private static void menuMetricas() {
        boolean salir = false;
        while (!salir) {
            System.out.println("\n--- METRICAS (POLIMORFISMO) ---");
            System.out.println("1. Mostrar metricas de ejemplo");
            System.out.println("2. Calcular IMC desde datos locales");
            System.out.println("3. Generar matriz estadistica e informe");
            System.out.println("4. Volver");
            System.out.print("Opcion: ");
            String op = scanner.nextLine();

            switch (op) {
                case "1":
                    mostrarMetricasEjemplo();
                    break;
                case "2":
                    calcularIMCLocal();
                    break;
                case "3":
                    generarInformeEstadistico();
                    break;
                case "4":
                    salir = true;
                    break;
                default:
                    System.out.println("Opcion no valida");
                    break;
            }
        }
    }

    private static void mostrarMetricasEjemplo() {
        Metrica mPeso = new MetricaPeso("Ana Lopez", 68.5, 65.2, "14/06/2026");
        Metrica mIMC = new MetricaIMC("Ana Lopez", 65.2, 1.65, "14/06/2026");
        Metrica mGrasa = new MetricaGrasa("Ana Lopez", 28.5, 'F', "14/06/2026");

        mPeso.mostrar();
        mIMC.mostrar();
        mGrasa.mostrar();
    }

    private static void calcularIMCLocal() {
        int nPac = gestion.getPacientesN();
        if (nPac == 0) {
            System.out.println("No hay pacientes registrados");
        } else {
            System.out.println("\n--- CALCULO DE IMC ---");
            for (int i = 0; i < nPac; i++) {
                Paciente p = gestion.getPaciente(i);
                double imc = p.calcularIMC();
                String clasificacion;
                if (imc < 18.5) {
                    clasificacion = "Bajo peso";
                } else if (imc < 25.0) {
                    clasificacion = "Peso normal";
                } else if (imc < 30.0) {
                    clasificacion = "Sobrepeso";
                } else {
                    clasificacion = "Obesidad";
                }
                System.out.println(p.nombreCompleto() + " | IMC: " + String.format("%.2f", imc) + " | " + clasificacion + " | Tipo: " + p.tipoPaciente());
            }
        }
    }

    private static void generarInformeEstadistico() {
        String[] nombres = { "Ana Lopez", "Carlos Mendez", "Rosa Garcia" };
        double[][] datos = {
            { 68.5, 65.2, 23.9, -3.3 },
            { 85.0, 82.1, 25.9, -2.9 },
            { 72.3, 71.0, 27.7, -1.3 }
        };
        Estadistica est = new Estadistica(nombres, datos, 15, 2);
        Informe inf = new Informe("Informe mensual Junio 2026", "14/06/2026", "MENSUAL", "Marina Soler", est);
        inf.generarInforme();
    }

    // ======================================================================
    // OPCION 7 - SEGURIDAD Y AUDITORIA
    // ======================================================================

    private static void menuSeguridad() {
        boolean salir = false;
        while (!salir) {
            System.out.println("\n--- SEGURIDAD Y AUDITORIA ---");
            System.out.println("1. Ejecutar controles de seguridad");
            System.out.println("2. Ver logs de auditoria");
            System.out.println("3. Exportar alertas a fichero");
            System.out.println("4. Volver");
            System.out.print("Opcion: ");
            String op = scanner.nextLine();

            switch (op) {
                case "1":
                    ejecutarControles();
                    break;
                case "2":
                    admin.revisarRegistros(gestorAuditoria);
                    break;
                case "3":
                    subsistemaAdmin.exportarAlertasUsoIncorrecto();
                    break;
                case "4":
                    salir = true;
                    break;
                default:
                    System.out.println("Opcion no valida");
                    break;
            }
        }
    }

    private static void ejecutarControles() {
        ControlAccesos ctrlAcceso = new ControlAccesos("Accesos de usuarios", 2, 3);
        ControlModificaciones ctrlMod = new ControlModificaciones("Modificacion en pacientes", true, "PACIENTES");
        ControlModificaciones ctrlNoAut = new ControlModificaciones("Cambio no autorizado en historial", false, "HISTORIAL_MEDICO");

        subsistemaAdmin.procesarControlYPersistir(ctrlAcceso);
        subsistemaAdmin.procesarControlYPersistir(ctrlMod);
        subsistemaAdmin.procesarControlYPersistir(ctrlNoAut);
    }

    // ======================================================================
    // OPCION 8 - ARCHIVOS
    // ======================================================================

    private static void menuArchivos() {
        boolean salir = false;
        while (!salir) {
            System.out.println("\n--- MODULO DE ARCHIVOS ---");
            System.out.println("1. Exportar citas a TXT");
            System.out.println("2. Ver configuracion");
            System.out.println("3. Ver log del sistema");
            System.out.println("4. Volver");
            System.out.print("Opcion: ");
            String op = scanner.nextLine();

            switch (op) {
                case "1":
                    gestorArchivos.exportarCitas(gestion.citasBD());
                    gestorArchivos.escribirLog("Citas exportadas a TXT");
                    break;
                case "2":
                    System.out.println("\n--- CONFIGURACION ---");
                    System.out.println(gestorArchivos.leerConfiguracion());
                    break;
                case "3":
                    System.out.println("\n--- LOG DEL SISTEMA ---");
                    System.out.println(gestorArchivos.leerLog());
                    break;
                case "4":
                    salir = true;
                    break;
                default:
                    System.out.println("Opcion no valida");
                    break;
            }
        }
    }

    // ======================================================================
    // OPCION 9 - EQUIPO PROFESIONAL
    // ======================================================================

    private static void menuEquipoProfesional() {
        System.out.println("\n--- EQUIPO PROFESIONAL ---");
        Profesional p1 = new Profesional("Laura", "Ruiz Lopez", "Nutricion deportiva", "Master Nutricion", "laura@foodiet.com", "600111222", Rol.NUTRICIONISTA);
        Profesional p2 = new Profesional("Pedro", "Diaz Martin", "Nutricion clinica", "Doctor Nutricion", "pedro@foodiet.com", "600333444", Rol.NUTRICIONISTA);
        Profesional p3 = new Profesional("Admin", "Sistema Foodiet", "Gestion", "Administrador", "admin@foodiet.com", "600000000", Rol.DIRECTIVO);

        System.out.println("Profesional 1: " + p1.getNombre() + " | Rol: " + p1.getRol());
        System.out.println("Profesional 2: " + p2.getNombre() + " | Rol: " + p2.getRol());
        System.out.println("Profesional 3: " + p3.getNombre() + " | Rol: " + p3.getRol());

        AsignacionPaciente a1 = new AsignacionPaciente(1, 1, "14/06/2026");
        AsignacionPaciente a2 = new AsignacionPaciente(2, 2, "14/06/2026");
        System.out.println("Asignacion 1: Profesional ID " + a1.getIdProfesional() + " -> Paciente ID " + a1.getIdPaciente());
        System.out.println("Asignacion 2: Profesional ID " + a2.getIdProfesional() + " -> Paciente ID " + a2.getIdPaciente());

        Agenda ag = new Agenda(1, "20/06/2026", TipoEvento.CONSULTA, "Consulta con Ana Lopez");
        System.out.println("Evento agenda: " + ag.getDescripcion() + " | Fecha: " + ag.getFecha() + " | Tipo: " + ag.getTipoEvento());

        Sustitucion s = new Sustitucion(1, 2, "01/07/2026", "15/07/2026", "Vacaciones de Laura");
        System.out.println("Sustitucion: Profesional " + s.getIdProfesionalTitular() + " -> " + s.getIdProfesionalSustituto() + " | Motivo: " + s.getMotivo());
    }

    // ======================================================================
    // OPCION 10 - EXPORTAR INFORME DE PROGRESO
    // ======================================================================

    private static void menuExportarProgreso() {
        System.out.println("\n--- EXPORTAR INFORME DE PROGRESO ---");
        ArrayList<Progreso> lista = new ArrayList<Progreso>();
        int nPac = gestion.getPacientesN();
        for (int i = 0; i < nPac; i++) {
            Paciente p = gestion.getPaciente(i);
            double peso = p.getPeso();
            double altura = p.getAltura();
            double imc = peso / (altura * altura);
            Progreso prog = new Progreso(i + 1, p.getId(), 1, "14/06/2026", peso, Math.round(imc * 100.0) / 100.0, "Registro desde menu");
            lista.add(prog);
        }
        GestorFicheros.exportarInforme(lista, "informe_progreso.txt");
    }

    // ======================================================================
    // SALIR
    // ======================================================================

    private static boolean cerrarSistema() {
        gestorAuditoria.registrarAccion("Sistema finalizado correctamente");
        admin.revisarRegistros(gestorAuditoria);
        admin.notificarIncidencia("Sesion cerrada sin incidencias graves");
        gestorArchivos.escribirLog("Sistema cerrado");
        System.out.println("Gracias por usar Foodiet!");
        scanner.close();
        return true;
    }
}
