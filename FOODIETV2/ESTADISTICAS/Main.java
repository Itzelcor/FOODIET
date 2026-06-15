package stats;

import java.util.ArrayList;
import java.util.Scanner;

/**
 * Programa principal del subsistema de Estadísticas.
 * Demuestra: ArrayList, conexión BD, polimorfismo y ficheros.
 *
 * FooDiet · Itzel Cordoba Herrera · 1º DAW 2025-2026
 */
public class Main {

    static Scanner scanner    = new Scanner(System.in);
    static ProgresoDAO dao    = new ProgresoDAO();

    public static void main(String[] args) {

        System.out.println("=========================================");
        System.out.println("  FOODIET — Subsistema de Estadísticas  ");
        System.out.println("  Desarrollado por: Itzel Cordoba        ");
        System.out.println("=========================================");

        int opcion = -1;

        while (opcion != 0) {
            mostrarMenu();
            opcion = leerEntero("Elige una opción: ");

            switch (opcion) {
                case 1: verTodosLosProgresos();     break;
                case 2: verProgresosPorPaciente();  break;
                case 3: añadirProgreso();            break;
                case 4: actualizarProgreso();        break;
                case 5: eliminarProgreso();          break;
                case 6: verEstadisticasConLista();  break;
                case 7: exportarInforme();           break;
                case 8: cargarMetricasFichero();    break;
                case 9: crearFicheroEjemplo();      break;
                case 0:
                    System.out.println("\nCerrando programa...");
                    ConexionBD.cerrar();
                    break;
                default:
                    System.out.println("Opción no válida. Intenta de nuevo.");
            }
        }
    }

    // ── MENÚ ──────────────────────────────────────────────────────
    static void mostrarMenu() {
        System.out.println("\n--- MENÚ PRINCIPAL ---");
        System.out.println("1. Ver todos los registros de progreso");
        System.out.println("2. Ver progreso de un paciente");
        System.out.println("3. Añadir nuevo registro de progreso");
        System.out.println("4. Actualizar peso e IMC de un registro");
        System.out.println("5. Eliminar un registro");
        System.out.println("6. Ver estadísticas con ArrayList y polimorfismo");
        System.out.println("7. Exportar informe a fichero .txt");
        System.out.println("8. Cargar métricas desde fichero .csv");
        System.out.println("9. Crear fichero CSV de ejemplo");
        System.out.println("0. Salir");
        System.out.println("----------------------");
    }

    // ── OPCIÓN 1: VER TODOS ───────────────────────────────────────
    static void verTodosLosProgresos() {
        System.out.println("\n--- TODOS LOS REGISTROS DE PROGRESO ---");
        ArrayList<Progreso> lista = dao.listarTodos();

        if (lista.isEmpty()) {
            System.out.println("No hay registros en la base de datos.");
        } else {
            for (int i = 0; i < lista.size(); i++) {
                System.out.println(lista.get(i));
            }
            System.out.println("Total: " + lista.size() + " registros.");
        }
    }

    // ── OPCIÓN 2: VER POR PACIENTE ────────────────────────────────
    static void verProgresosPorPaciente() {
        int id = leerEntero("ID del paciente: ");
        System.out.println("\n--- PROGRESO DEL PACIENTE " + id + " ---");

        ArrayList<Progreso> lista = dao.listarPorPaciente(id);

        if (lista.isEmpty()) {
            System.out.println("No se encontraron registros para ese paciente.");
        } else {
            for (Progreso p : lista) {
                System.out.println(p);
            }
        }
    }

    // ── OPCIÓN 3: AÑADIR ──────────────────────────────────────────
    static void añadirProgreso() {
        System.out.println("\n--- NUEVO REGISTRO DE PROGRESO ---");

        int    idPaciente    = leerEntero("ID del paciente: ");
        int    idPlan        = leerEntero("ID del plan: ");
        String fecha         = leerTexto("Fecha (YYYY-MM-DD): ");
        double peso          = leerDecimal("Peso en kg: ");
        double imc           = leerDecimal("IMC: ");
        String observaciones = leerTexto("Observaciones: ");

        Progreso nuevo = new Progreso(idPaciente, idPlan, fecha, peso, imc, observaciones);
        dao.insertar(nuevo);
    }

    // ── OPCIÓN 4: ACTUALIZAR ──────────────────────────────────────
    static void actualizarProgreso() {
        System.out.println("\n--- ACTUALIZAR REGISTRO ---");
        int    id      = leerEntero("ID del registro a actualizar: ");
        double peso    = leerDecimal("Nuevo peso en kg: ");
        double imc     = leerDecimal("Nuevo IMC: ");
        dao.actualizar(id, peso, imc);
    }

    // ── OPCIÓN 5: ELIMINAR ────────────────────────────────────────
    static void eliminarProgreso() {
        System.out.println("\n--- ELIMINAR REGISTRO ---");
        int id = leerEntero("ID del registro a eliminar: ");
        System.out.print("¿Seguro? (s/n): ");
        String confirmacion = scanner.nextLine().trim();
        if (confirmacion.equalsIgnoreCase("s")) {
            dao.eliminar(id);
        } else {
            System.out.println("Operación cancelada.");
        }
    }

    // ── OPCIÓN 6: ESTADÍSTICAS CON ARRAYLIST Y POLIMORFISMO ───────
    static void verEstadisticasConLista() {
        System.out.println("\n--- ESTADÍSTICAS CON ARRAYLIST Y POLIMORFISMO ---");

        // Cargamos los progresos de la BD en un ArrayList
        ArrayList<Progreso> listaProgresos = dao.listarTodos();

        if (listaProgresos.isEmpty()) {
            System.out.println("No hay datos en la BD.");
            return;
        }

        // Creamos un ArrayList de Metrica (polimorfismo)
        // Puede contener MetricaPeso, MetricaIMC, etc.
        ArrayList<Metrica> metricas = new ArrayList<>();

        for (int i = 0; i < listaProgresos.size(); i++) {
            Progreso p = listaProgresos.get(i);

            // Añadimos una MetricaPeso por cada progreso
            metricas.add(new MetricaPeso(
                "Paciente " + p.getIdPaciente(),
                p.getPeso(),
                p.getPeso(),
                p.getFecha()
            ));

            // Añadimos una MetricaIMC si tiene IMC registrado
            if (p.getImc() > 0) {
                metricas.add(new MetricaIMC(
                    "Paciente " + p.getIdPaciente(),
                    p.getPeso(),
                    1.65,
                    p.getFecha()
                ));
            }
        }

        // POLIMORFISMO: llamamos a mostrar() sin saber el tipo exacto
        System.out.println("\nMétricas calculadas:");
        for (Metrica m : metricas) {
            m.mostrar();  // cada subclase ejecuta su propio calcular()
        }

        // Estadísticas globales usando el ArrayList
        double totalPeso = 0;
        double totalImc  = 0;
        int    conImc    = 0;

        for (Progreso p : listaProgresos) {
            totalPeso += p.getPeso();
            if (p.getImc() > 0) {
                totalImc += p.getImc();
                conImc++;
            }
        }

        System.out.println("\n--- Resumen global ---");
        System.out.println("Total registros : " + listaProgresos.size());
        System.out.println("Peso medio      : " + totalPeso / listaProgresos.size() + " kg");
        if (conImc > 0) {
            System.out.println("IMC medio       : " + totalImc / conImc);
        }
    }

    // ── OPCIÓN 7: EXPORTAR INFORME ────────────────────────────────
    static void exportarInforme() {
        System.out.println("\n--- EXPORTAR INFORME ---");
        String nombre = leerTexto("Nombre del fichero (sin extensión): ");
        ArrayList<Progreso> lista = dao.listarTodos();
        GestorFicheros.exportarInforme(lista, nombre + ".txt");
    }

    // ── OPCIÓN 8: CARGAR MÉTRICAS CSV ────────────────────────────
    static void cargarMetricasFichero() {
        System.out.println("\n--- CARGAR MÉTRICAS DESDE CSV ---");
        String nombre = leerTexto("Nombre del fichero CSV: ");

        ArrayList<Metrica> metricas = GestorFicheros.cargarMetricasCSV(nombre);

        if (metricas.isEmpty()) {
            System.out.println("No se cargaron métricas.");
        } else {
            System.out.println("\nMétricas cargadas del fichero:");
            for (Metrica m : metricas) {
                m.mostrar();  // polimorfismo
            }
            System.out.println("Total: " + metricas.size() + " métricas.");
        }
    }

    // ── OPCIÓN 9: CREAR CSV DE EJEMPLO ───────────────────────────
    static void crearFicheroEjemplo() {
        String nombre = "metricas_ejemplo.csv";
        GestorFicheros.crearFicheroEjemplo(nombre);
    }

    // ── UTILIDADES DE LECTURA ─────────────────────────────────────
    static int leerEntero(String mensaje) {
        int valor = 0;
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
        double valor = 0;
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