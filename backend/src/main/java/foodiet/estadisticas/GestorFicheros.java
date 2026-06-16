package stats;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Gestiona la lectura y escritura de ficheros de texto
 * para el subsistema de Estadísticas de FooDiet.
 */
public class GestorFicheros {

    // ── EXPORTAR INFORME A .TXT ───────────────────────────────────
    /**
     * Exporta la lista de progresos a un fichero de texto.
     */
    public static void exportarInforme(ArrayList<Progreso> lista, String nombreFichero) {
        try {
            FileWriter fw     = new FileWriter(nombreFichero);
            BufferedWriter bw = new BufferedWriter(fw);

            bw.write("========================================");
            bw.newLine();
            bw.write("  INFORME DE PROGRESO - FooDiet");
            bw.newLine();
            bw.write("  Subsistema de Estadísticas - Itzel");
            bw.newLine();
            bw.write("========================================");
            bw.newLine();
            bw.newLine();

            if (lista.isEmpty()) {
                bw.write("No hay registros para exportar.");
                bw.newLine();
            } else {
                for (int i = 0; i < lista.size(); i++) {
                    bw.write(lista.get(i).toString());
                    bw.newLine();
                }
                bw.newLine();
                bw.write("Total de registros: " + lista.size());
                bw.newLine();
            }

            bw.close();
            System.out.println("Informe exportado correctamente a: " + nombreFichero);

        } catch (IOException e) {
            System.out.println("Error al exportar el fichero: " + e.getMessage());
        }
    }

    // ── CARGAR MÉTRICAS DESDE CSV ─────────────────────────────────
    /**
     * Lee un fichero CSV con métricas y devuelve ArrayList<Metrica>.
     * Formato de cada línea: tipo,nombre,valor,fecha
     * Ejemplo: PESO,Ana Garcia,75.0,2026-06-12
     * Ejemplo: IMC,Ana Garcia,27.5,2026-06-12
     *
     * Aquí se usa POLIMORFISMO: según el tipo crea
     * MetricaPeso o MetricaIMC.
     */
    public static ArrayList<Metrica> cargarMetricasCSV(String nombreFichero) {
        ArrayList<Metrica> lista = new ArrayList<>();

        try {
            FileReader fr     = new FileReader(nombreFichero);
            BufferedReader br = new BufferedReader(fr);

            String linea = br.readLine(); // saltar cabecera si existe

            while ((linea = br.readLine()) != null) {
                if (linea.trim().isEmpty()) continue;

                String[] partes = linea.split(",");

                if (partes.length < 4) {
                    System.out.println("Línea con formato incorrecto: " + linea);
                    continue;
                }

                String tipo   = partes[0].trim().toUpperCase();
                String nombre = partes[1].trim();
                double valor  = Double.parseDouble(partes[2].trim());
                String fecha  = partes[3].trim();

                // POLIMORFISMO: creamos el tipo correcto de Metrica
                if (tipo.equals("PESO")) {
                    lista.add(new MetricaPeso(nombre, valor, valor, fecha));
                } else if (tipo.equals("IMC")) {
                    lista.add(new MetricaIMC(nombre, valor, 1.65, fecha));
                } else {
                    System.out.println("Tipo de métrica desconocido: " + tipo);
                }
            }

            br.close();
            System.out.println("Métricas cargadas desde: " + nombreFichero);

        } catch (IOException e) {
            System.out.println("Error al leer el fichero: " + e.getMessage());
        } catch (NumberFormatException e) {
            System.out.println("Error en el formato numérico del fichero.");
        }

        return lista;
    }

    // ── CREAR FICHERO CSV DE EJEMPLO ──────────────────────────────
    /**
     * Crea un fichero CSV de ejemplo para poder probarlo.
     */
    public static void crearFicheroEjemplo(String nombreFichero) {
        try {
            FileWriter fw     = new FileWriter(nombreFichero);
            BufferedWriter bw = new BufferedWriter(fw);

            bw.write("tipo,nombre,valor,fecha");
            bw.newLine();
            bw.write("PESO,Ana Garcia,75.0,2026-06-12");
            bw.newLine();
            bw.write("IMC,Ana Garcia,27.55,2026-06-12");
            bw.newLine();
            bw.write("PESO,Luis Torres,82.0,2026-06-12");
            bw.newLine();
            bw.write("IMC,Luis Torres,25.87,2026-06-12");
            bw.newLine();
            bw.write("PESO,Maria Lopez,66.5,2026-04-10");
            bw.newLine();

            bw.close();
            System.out.println("Fichero de ejemplo creado: " + nombreFichero);

        } catch (IOException e) {
            System.out.println("Error al crear el fichero: " + e.getMessage());
        }
    }
}