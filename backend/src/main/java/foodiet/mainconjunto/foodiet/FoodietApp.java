package foodiet;

import foodiet.datos.GestorBD;
import foodiet.gestion.GestionClinica;
import foodiet.gestion.GestorArchivos;
import foodiet.modelo.*;
import foodiet.presentacion.MenuPrincipal;
import java.util.Scanner;

public class FoodietApp {

    public static void main(String[] args) {
        GestionClinica gestion = new GestionClinica();
        GestorBD gestorBD = gestion.getGestorBD();
        GestorArchivos archivos = new GestorArchivos();
        Scanner scanner = new Scanner(System.in);

        System.out.println("========================================");
        System.out.println("   FOODIET - Sistema de Dietetica");
        System.out.println("========================================");

        cargarDatosEjemplo(gestion);
        archivos.escribirLog("Sistema iniciado");

        boolean salirSistema = false;
        while (!salirSistema) {
            System.out.println("\n=== ACCESO AL SISTEMA ===");
            System.out.println("1. Iniciar sesion");
            System.out.println("2. Registrar usuario");
            System.out.println("3. Salir");
            System.out.print("Opcion: ");
            String op = scanner.nextLine();

            if (op.equals("1")) {
                System.out.print("Usuario: ");
                String user = scanner.nextLine();
                System.out.print("Contrasena: ");
                String pass = scanner.nextLine();
                Usuario usuario = gestorBD.autenticarUsuario(user, pass);

                if (usuario != null) {
                    archivos.escribirLog("Sesion iniciada: " + user);
                    System.out.println("Bienvenido " + usuario.getNombreUsuario() +
                                       " | Rol: " + usuario.getRol());
                    MenuPrincipal menu = new MenuPrincipal(gestion, scanner);
                    ejecutarMenu(menu, usuario, gestion, scanner);
                    archivos.escribirLog("Sesion cerrada: " + user);
                } else {
                    System.out.println("Credenciales incorrectas");
                }
            } else if (op.equals("2")) {
                System.out.print("Usuario: ");
                String nu = scanner.nextLine();
                System.out.print("Email: ");
                String em = scanner.nextLine();
                System.out.print("Contrasena: ");
                String np = scanner.nextLine();
                System.out.print("Rol (paciente/nutricionista/administrador): ");
                String rol = scanner.nextLine();
                if (gestorBD.registrarUsuario(nu, em, np, rol)) {
                    archivos.escribirLog("Usuario registrado: " + nu);
                    System.out.println("Usuario registrado");
                } else {
                    System.out.println("Error al registrar");
                }
            } else if (op.equals("3")) {
                salirSistema = true;
            } else {
                System.out.println("Opcion no valida");
            }
        }

        archivos.escribirLog("Sistema cerrado");
        System.out.println("Gracias por usar Foodiet!");
        scanner.close();
    }

    private static void ejecutarMenu(MenuPrincipal menu, Usuario usuario,
                                     GestionClinica gestion, Scanner scanner) {
        boolean salir = false;
        while (!salir) {
            System.out.println("\n=== PANEL PRINCIPAL ===");
            System.out.println("Usuario: " + usuario.getNombreUsuario() +
                               " | Rol: " + usuario.getRol());
            System.out.println("1. Pacientes");
            System.out.println("2. Citas");
            System.out.println("3. Planes alimenticios");
            System.out.println("4. Estadisticas");
            System.out.println("5. Servicios");
            System.out.println("6. Archivos (TXT)");

            int opSalida = 7;
            if (usuario.getRol().equals("administrador")) {
                System.out.println("7. Profesionales");
                opSalida = 8;
            }
            System.out.println(opSalida + ". Cerrar sesion");
            System.out.print("Opcion: ");
            String op = scanner.nextLine();

            if (op.equals("1")) {
                menu.menuPacientes();
            } else if (op.equals("2")) {
                menu.menuCitas();
            } else if (op.equals("3")) {
                menu.menuPlanes();
            } else if (op.equals("4")) {
                menu.menuEstadisticas();
            } else if (op.equals("5")) {
                ServicioClinica.mostrarTodos();
            } else if (op.equals("6")) {
                menu.menuArchivos();
            } else if (usuario.getRol().equals("administrador") && op.equals("7")) {
                gestion.mostrarProfesionales();
            } else if (op.equals(String.valueOf(opSalida))) {
                salir = true;
            } else {
                System.out.println("Opcion no valida");
            }
        }
    }

    private static void cargarDatosEjemplo(GestionClinica gestion) {
        PacienteJoven joven = new PacienteJoven("Ana", "Lopez", 19, 58.0, 1.65);
        joven.setTelefono("645678901");
        joven.setEmail("ana@email.com");
        joven.setHistorialMedico("Sin antecedentes");
        joven.setObjetivosNutricionales("Mejorar habitos alimenticios");

        PacienteAdulto adulto = new PacienteAdulto("Carlos", "Perez", 42, 82.0, 1.78);
        adulto.setTelefono("656789012");
        adulto.setEmail("carlos@email.com");
        adulto.setHistorialMedico("Colesterol elevado");
        adulto.setObjetivosNutricionales("Reducir peso y colesterol");

        PacienteJubilado jubilado = new PacienteJubilado("Manuel", "Garcia", 71, 74.0, 1.70);
        jubilado.setTelefono("667890123");
        jubilado.setEmail("manuel@email.com");
        jubilado.setHistorialMedico("Hipertension");
        jubilado.setObjetivosNutricionales("Controlar presion arterial");

        joven.setIdUsuario(5);
        adulto.setIdUsuario(6);
        jubilado.setIdUsuario(7);

        gestion.añadirPaciente(joven);
        gestion.añadirPaciente(adulto);
        gestion.añadirPaciente(jubilado);

        ProfesionalClinica d1 = new ProfesionalClinica("Daniel", "Dimitrov",
            "Nutricion Deportiva", 14, "Lunes-Viernes 9:00-18:00", "612345678");
        ProfesionalClinica d2 = new ProfesionalClinica("Andrei", "Veres",
            "Nutricion Clinica", 12, "Lunes-Viernes 10:00-19:00", "623456789");
        ProfesionalClinica d3 = new ProfesionalClinica("Sergio", "Gonzalez",
            "Dietetica General", 16, "Lunes-Viernes 8:00-17:00", "634567890");

        gestion.añadirProfesional(d1);
        gestion.añadirProfesional(d2);
        gestion.añadirProfesional(d3);

        joven.setId(1);
        adulto.setId(2);
        jubilado.setId(3);

        CitaPresencial c1 = new CitaPresencial(joven, d1, 10, 2, 2026, "Consulta inicial", "Sala 3");
        CitaOnline c2 = new CitaOnline(adulto, d3, 12, 12, 2026, "Seguimiento plan", "Zoom");

        c1.setIdPaciente(1);
        c1.setIdProfesional(1);
        c2.setIdPaciente(2);
        c2.setIdProfesional(3);

        gestion.añadirCita(c1);
        gestion.añadirCita(c2);

        System.out.println("\nDatos cargados: " + gestion.getPacientesN() +
            " pacientes, " + gestion.getProfesionalesN() +
            " profesionales, " + gestion.getCitasN() + " citas");
        System.out.println("Usuarios: admin/admin123, dietista1/pass123, ana.lopez/pass123\n");
    }
}
