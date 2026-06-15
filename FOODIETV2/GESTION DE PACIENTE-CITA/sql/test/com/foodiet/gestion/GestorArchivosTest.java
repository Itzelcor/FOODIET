package com.foodiet.gestion;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import com.foodiet.modelo.*;
import java.util.ArrayList;

public class GestorArchivosTest {

    private GestorArchivos gestor;

    @BeforeEach
    public void setUp() {
        gestor = new GestorArchivos();
    }

    @Test
    public void testLeerConfiguracionDevuelveContenido() {
        String contenido = gestor.leerConfiguracion();
        assertNotNull(contenido);
        assertTrue(contenido.length() > 0);
    }

    @Test
    public void testEscribirLogYLeerLog() {
        String marca = "TEST_MARCA_UNICA_" + System.currentTimeMillis();
        boolean escrito = gestor.escribirLog(marca);
        assertTrue(escrito);
        String log = gestor.leerLog();
        assertTrue(log.contains(marca));
    }

    @Test
    public void testExportarCitasListaVacia() {
        ArrayList<Cita> lista = new ArrayList<Cita>();
        boolean resultado = gestor.exportarCitas(lista);
        assertTrue(resultado);
    }

    @Test
    public void testExportarCitasConDatos() {
        PacienteAdulto p = new PacienteAdulto(1, "Carlos", "Perez", 42, 82.0, 1.78,
                "656789012", "carlos@email.com", "Colesterol", "Reducir peso");
        ProfesionalClinica pro = new ProfesionalClinica(1, "Daniel", "Dimitrov",
                "Nutricion Deportiva", 14, "", "612345678");
        CitaPresencial cita = new CitaPresencial(p, pro, 10, 2, 2026, "Revision", "Sala 3");
        ArrayList<Cita> citas = new ArrayList<Cita>();
        citas.add(cita);
        boolean resultado = gestor.exportarCitas(citas);
        assertTrue(resultado);
    }

    @Test
    public void testGetRutas() {
        assertNotNull(gestor.getRutaCitas());
        assertNotNull(gestor.getRutaConfig());
        assertNotNull(gestor.getRutaLog());
        assertTrue(gestor.getRutaCitas().contains("volcado_citas.txt"));
        assertTrue(gestor.getRutaConfig().contains("configuracion.txt"));
        assertTrue(gestor.getRutaLog().contains("log_sistema.txt"));
    }

    @Test
    public void testLeerLogNoEsNull() {
        String log = gestor.leerLog();
        assertNotNull(log);
    }
}
