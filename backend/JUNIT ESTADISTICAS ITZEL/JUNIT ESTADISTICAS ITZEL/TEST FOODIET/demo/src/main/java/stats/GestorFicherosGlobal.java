package stats;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import stats.Progreso;
import stats.Pacientes.Paciente;
import stats.Profesionales.ProfesionalClinica;
import stats.Planes.PlanDietetico;
import stats.Administracion.Factura;
import stats.Usuarios.Usuario;

/**
 * Gestiona la lectura y escritura de ficheros para todos los subsistemas.
 * Formatos: .txt, .csv, .html
 * FooDiet · IES Font de San Lluis · 1º DAW · 2025-2026
 */
public class GestorFicherosGlobal {

    // ── EXPORTAR A TXT ────────────────────────────────────────────
    public static void exportarTxt(ArrayList<?> lista, String titulo, String nombreFichero) {
        try {
            FileWriter     fw = new FileWriter(nombreFichero);
            BufferedWriter bw = new BufferedWriter(fw);

            bw.write("============================================================");
            bw.newLine();
            bw.write("  FOODIET — " + titulo.toUpperCase());
            bw.newLine();
            bw.write("  IES Font de San Lluis · 1º DAW · 2025-2026");
            bw.newLine();
            bw.write("============================================================");
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
            System.out.println("✓ Informe TXT exportado: " + nombreFichero);

        } catch (IOException e) {
            System.out.println("Error al exportar TXT: " + e.getMessage());
        }
    }

    // ── EXPORTAR A CSV ────────────────────────────────────────────
    public static void exportarCsv(ArrayList<?> lista, String cabecera, String nombreFichero) {
        try {
            FileWriter     fw = new FileWriter(nombreFichero);
            BufferedWriter bw = new BufferedWriter(fw);

            bw.write(cabecera);
            bw.newLine();

            for (int i = 0; i < lista.size(); i++) {
                Object obj = lista.get(i);

                // Si el objeto tiene toCSV() lo usamos, si no usamos toString()
                if (obj instanceof stats.Progreso) {
                    stats.Progreso p = (stats.Progreso) obj;
                    bw.write(p.getIdProgreso() + "," + p.getIdPaciente() + ","
                           + p.getIdPlan() + "," + p.getFecha() + ","
                           + p.getPeso() + "," + p.getImc() + ","
                           + p.getObservaciones());
                } else if (obj instanceof Paciente) {
                    Paciente p = (Paciente) obj;
                    bw.write(p.getIdPaciente() + "," + p.getDni() + ","
                           + p.getNombre() + "," + p.getApellidos() + ","
                           + p.getTelefono() + "," + p.getEmail() + ","
                           + p.tipoPaciente());
                } else if (obj instanceof ProfesionalClinica) {
                    ProfesionalClinica p = (ProfesionalClinica) obj;
                    bw.write(p.getIdProfesional() + "," + p.nombreCompleto() + ","
                           + p.getRol() + "," + p.getEmail() + ","
                           + p.getAnosExp() + "," + p.isActivo());
                } else if (obj instanceof PlanDietetico) {
                    PlanDietetico p = (PlanDietetico) obj;
                    bw.write(p.getIdPlan() + "," + p.getIdPaciente() + ","
                           + p.getObjetivo() + "," + p.getCaloriasDiarias() + ","
                           + p.getFechaInicio() + "," + p.getFechaFin() + ","
                           + p.getEstado());
                } else if (obj instanceof Factura) {
                    Factura f = (Factura) obj;
                    bw.write(f.toCSV());
                } else if (obj instanceof Usuario) {
                    Usuario u = (Usuario) obj;
                    bw.write(u.getIdUsuario() + "," + u.getEmail() + ","
                           + u.getRol() + "," + u.isActivo());
                } else {
                    bw.write(obj.toString());
                }
                bw.newLine();
            }

            bw.close();
            System.out.println("✓ Informe CSV exportado: " + nombreFichero);

        } catch (IOException e) {
            System.out.println("Error al exportar CSV: " + e.getMessage());
        }
    }

    // ── EXPORTAR A HTML ───────────────────────────────────────────
    public static void exportarHtml(ArrayList<?> lista, String titulo,
                                    String[] columnas, String nombreFichero) {
        try {
            FileWriter     fw = new FileWriter(nombreFichero);
            BufferedWriter bw = new BufferedWriter(fw);

            // Cabecera HTML
            bw.write("<!DOCTYPE html>");
            bw.newLine();
            bw.write("<html lang=\"es\">");
            bw.newLine();
            bw.write("<head>");
            bw.newLine();
            bw.write("  <meta charset=\"utf-8\">");
            bw.newLine();
            bw.write("  <title>" + titulo + " - FooDiet</title>");
            bw.newLine();
            bw.write("  <style>");
            bw.newLine();
            bw.write("    body { font-family: Arial, sans-serif; margin: 30px; }");
            bw.newLine();
            bw.write("    h1 { color: #2e7d32; border-bottom: 3px solid #4caf50; padding-bottom: 10px; }");
            bw.newLine();
            bw.write("    p { color: #555; }");
            bw.newLine();
            bw.write("    table { border-collapse: collapse; width: 100%; margin-top: 20px; }");
            bw.newLine();
            bw.write("    th { background-color: #2e7d32; color: white; padding: 10px; text-align: left; }");
            bw.newLine();
            bw.write("    td { padding: 8px 10px; border-bottom: 1px solid #ddd; }");
            bw.newLine();
            bw.write("    tr:nth-child(even) { background-color: #f1f8e9; }");
            bw.newLine();
            bw.write("    .total { margin-top: 15px; font-weight: bold; color: #2e7d32; }");
            bw.newLine();
            bw.write("    .footer { margin-top: 30px; color: #888; font-size: 12px; text-align: center; }");
            bw.newLine();
            bw.write("  </style>");
            bw.newLine();
            bw.write("</head>");
            bw.newLine();
            bw.write("<body>");
            bw.newLine();
            bw.write("  <h1>FooDiet — " + titulo + "</h1>");
            bw.newLine();
            bw.write("  <p>IES Font de San Lluis · 1º DAW · 2025-2026</p>");
            bw.newLine();

            // Tabla
            bw.write("  <table>");
            bw.newLine();
            bw.write("    <thead><tr>");
            for (int i = 0; i < columnas.length; i++) {
                bw.write("<th>" + columnas[i] + "</th>");
            }
            bw.write("</tr></thead>");
            bw.newLine();
            bw.write("    <tbody>");
            bw.newLine();

            if (lista.isEmpty()) {
                bw.write("      <tr><td colspan=\"" + columnas.length
                        + "\">No hay registros.</td></tr>");
                bw.newLine();
            } else {
                for (int i = 0; i < lista.size(); i++) {
                    Object obj = lista.get(i);
                    bw.write("      <tr>");

                    if (obj instanceof stats.Progreso) {
                        stats.Progreso p = (stats.Progreso) obj;
                        bw.write("<td>" + p.getIdProgreso() + "</td>"
                               + "<td>" + p.getIdPaciente() + "</td>"
                               + "<td>" + p.getFecha() + "</td>"
                               + "<td>" + p.getPeso() + " kg</td>"
                               + "<td>" + p.getImc() + "</td>"
                               + "<td>" + p.getObservaciones() + "</td>");
                    } else if (obj instanceof Paciente) {
                        Paciente p = (Paciente) obj;
                        bw.write("<td>" + p.getIdPaciente() + "</td>"
                               + "<td>" + p.nombreCompleto() + "</td>"
                               + "<td>" + p.getDni() + "</td>"
                               + "<td>" + p.getTelefono() + "</td>"
                               + "<td>" + p.getEmail() + "</td>"
                               + "<td>" + p.tipoPaciente() + "</td>");
                    } else if (obj instanceof ProfesionalClinica) {
                        ProfesionalClinica p = (ProfesionalClinica) obj;
                        bw.write("<td>" + p.getIdProfesional() + "</td>"
                               + "<td>" + p.nombreCompleto() + "</td>"
                               + "<td>" + p.getRol() + "</td>"
                               + "<td>" + p.getEmail() + "</td>"
                               + "<td>" + p.getAnosExp() + " años</td>"
                               + "<td>" + (p.isActivo() ? "Activo" : "Baja") + "</td>");
                    } else if (obj instanceof PlanDietetico) {
                        PlanDietetico p = (PlanDietetico) obj;
                        bw.write("<td>" + p.getIdPlan() + "</td>"
                               + "<td>" + p.getIdPaciente() + "</td>"
                               + "<td>" + p.getObjetivo() + "</td>"
                               + "<td>" + p.getCaloriasDiarias() + " kcal</td>"
                               + "<td>" + p.getFechaInicio() + "</td>"
                               + "<td>" + p.getEstado() + "</td>");
                    } else if (obj instanceof Factura) {
                        Factura f = (Factura) obj;
                        bw.write("<td>" + f.getIdFactura() + "</td>"
                               + "<td>" + f.getIdPaciente() + "</td>"
                               + "<td>" + f.getImporte() + " €</td>"
                               + "<td>" + f.getEstadoPago() + "</td>"
                               + "<td>" + f.getMetodoPago() + "</td>"
                               + "<td>" + f.getFechaEmision() + "</td>");
                    } else if (obj instanceof Usuario) {
                        Usuario u = (Usuario) obj;
                        bw.write("<td>" + u.getIdUsuario() + "</td>"
                               + "<td>" + u.getEmail() + "</td>"
                               + "<td>" + u.getRol() + "</td>"
                               + "<td>" + (u.isActivo() ? "Activo" : "Inactivo") + "</td>"
                               + "<td>" + u.getFechaAlta() + "</td>");
                    } else {
                        bw.write("<td colspan=\"" + columnas.length + "\">"
                               + obj.toString() + "</td>");
                    }

                    bw.write("</tr>");
                    bw.newLine();
                }
            }

            bw.write("    </tbody>");
            bw.newLine();
            bw.write("  </table>");
            bw.newLine();
            bw.write("  <p class=\"total\">Total de registros: " + lista.size() + "</p>");
            bw.newLine();
            bw.write("  <div class=\"footer\">FooDiet &copy; 2026 — Generado automáticamente</div>");
            bw.newLine();
            bw.write("</body></html>");
            bw.newLine();

            bw.close();
            System.out.println("✓ Informe HTML exportado: " + nombreFichero);

        } catch (IOException e) {
            System.out.println("Error al exportar HTML: " + e.getMessage());
        }
    }

    // ── CARGAR DESDE CSV ──────────────────────────────────────────
    public static ArrayList<String> cargarCSV(String nombreFichero) {
        ArrayList<String> lineas = new ArrayList<>();
        try {
            FileReader     fr = new FileReader(nombreFichero);
            BufferedReader br = new BufferedReader(fr);

            String linea = br.readLine(); // saltar cabecera
            linea = br.readLine();

            while (linea != null) {
                boolean lineaValida = !linea.trim().isEmpty();
                if (lineaValida) {
                    lineas.add(linea);
                }
                linea = br.readLine();
            }

            br.close();
            System.out.println("✓ CSV cargado: " + lineas.size() + " registros.");

        } catch (IOException e) {
            System.out.println("Error al leer CSV: " + e.getMessage());
        }
        return lineas;
    }
}