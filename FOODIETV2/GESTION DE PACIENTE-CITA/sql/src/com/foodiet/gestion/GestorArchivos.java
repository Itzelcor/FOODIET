package com.foodiet.gestion;

import com.foodiet.modelo.Cita;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class GestorArchivos {

    private static final String RUTA_CITAS = "archivos/volcado_citas.txt";
    private static final String RUTA_CONFIG = "archivos/configuracion.txt";
    private static final String RUTA_LOG = "archivos/log_sistema.txt";

    public boolean exportarCitas(ArrayList<Cita> citas) {
        boolean exito = true;
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(RUTA_CITAS));
            writer.write("=== VOLCADO DE CITAS FOODIET ===\n");
            writer.write("Total citas: " + citas.size() + "\n\n");
            for (int i = 0; i < citas.size(); i++) {
                Cita c = citas.get(i);
                writer.write(c.mostrarCita() + "\n");
            }
            writer.close();
            System.out.println("Citas exportadas a " + RUTA_CITAS);
        } catch (IOException e) {
            System.out.println("Error al exportar citas - " + e.getMessage());
            exito = false;
        }
        return exito;
    }

    public String leerConfiguracion() {
        String contenido = "";
        StringBuilder sb = new StringBuilder();
        try {
            BufferedReader reader = new BufferedReader(new FileReader(RUTA_CONFIG));
            String linea = reader.readLine();
            while (linea != null) {
                sb.append(linea).append("\n");
                linea = reader.readLine();
            }
            reader.close();
            contenido = sb.toString();
        } catch (IOException e) {
            contenido = "Error al leer configuracion - " + e.getMessage();
        }
        return contenido;
    }

    public boolean escribirLog(String mensaje) {
        boolean exito = true;
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(RUTA_LOG, true));
            writer.write(java.time.LocalDateTime.now() + " - " + mensaje + "\n");
            writer.close();
        } catch (IOException e) {
            System.out.println("Error al escribir log - " + e.getMessage());
            exito = false;
        }
        return exito;
    }

    public String leerLog() {
        String contenido = "";
        StringBuilder sb = new StringBuilder();
        try {
            BufferedReader reader = new BufferedReader(new FileReader(RUTA_LOG));
            String linea = reader.readLine();
            while (linea != null) {
                sb.append(linea).append("\n");
                linea = reader.readLine();
            }
            reader.close();
            contenido = sb.toString();
        } catch (IOException e) {
            contenido = "El archivo de log aun no existe";
        }
        return contenido;
    }

    public String getRutaCitas() {
        return RUTA_CITAS;
    }

    public String getRutaConfig() {
        return RUTA_CONFIG;
    }

    public String getRutaLog() {
        return RUTA_LOG;
    }
}
