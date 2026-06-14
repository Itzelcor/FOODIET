package com.foodiet.gestion;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import com.foodiet.modelo.*;

public class GestionClinicaTest {

    private GestionClinica gestion;

    @BeforeEach
    public void setUp() {
        gestion = new GestionClinica();
    }

    @Test
    public void testValoresInicialesCero() {
        assertEquals(0, gestion.getPacientesN());
        assertEquals(0, gestion.getProfesionalesN());
        assertEquals(0, gestion.getCitasN());
        assertEquals(0, gestion.getPlanesN());
    }

    @Test
    public void testAñadirProfesionalIncrementaN() {
        ProfesionalClinica p = new ProfesionalClinica("Daniel", "Dimitrov",
                "Nutricion Deportiva", 14, "", "612345678");
        boolean resultado = gestion.añadirProfesional(p);
        assertTrue(resultado);
        assertEquals(1, gestion.getProfesionalesN());
    }

    @Test
    public void testAñadirMultiplesProfesionales() {
        int total = 5;
        int añadidosN = 0;
        for (int i = 0; i < total; i++) {
            ProfesionalClinica p = new ProfesionalClinica("Nom" + i, "Ape" + i,
                    "Especialidad", i, "Horario", "Tel" + i);
            if (gestion.añadirProfesional(p)) {
                añadidosN++;
            }
        }
        assertEquals(total, añadidosN);
        assertEquals(total, gestion.getProfesionalesN());
    }

    @Test
    public void testGetProfesionalDevuelveCorrecto() {
        ProfesionalClinica p1 = new ProfesionalClinica("Daniel", "Dimitrov",
                "Nutricion Deportiva", 14, "", "612345678");
        ProfesionalClinica p2 = new ProfesionalClinica("Andrei", "Veres",
                "Nutricion Clinica", 12, "", "623456789");
        gestion.añadirProfesional(p1);
        gestion.añadirProfesional(p2);
        ProfesionalClinica obtenido = gestion.getProfesional(0);
        assertEquals("Daniel", obtenido.getNombre());
        assertEquals("Dimitrov", obtenido.getApellido());
        obtenido = gestion.getProfesional(1);
        assertEquals("Andrei", obtenido.getNombre());
    }

    @Test
    public void testGetProfesionalIndiceInvalidoDevuelveNull() {
        ProfesionalClinica resultado = gestion.getProfesional(0);
        assertNull(resultado);
        resultado = gestion.getProfesional(-1);
        assertNull(resultado);
        resultado = gestion.getProfesional(100);
        assertNull(resultado);
    }

    @Test
    public void testGetPacienteIndiceInvalidoDevuelveNull() {
        assertNull(gestion.getPaciente(0));
        assertNull(gestion.getPaciente(-1));
    }

    @Test
    public void testGetCitaIndiceInvalidoDevuelveNull() {
        assertNull(gestion.getCita(0));
        assertNull(gestion.getCita(-1));
    }

    @Test
    public void testGetPlanIndiceInvalidoDevuelveNull() {
        assertNull(gestion.getPlan(0));
        assertNull(gestion.getPlan(-1));
    }

    @Test
    public void testAñadirProfesionalHastaLimite() {
        int maximo = 20;
        int contadorN = 0;
        for (int i = 0; i < maximo + 5; i++) {
            ProfesionalClinica p = new ProfesionalClinica("Nom" + i, "Ape" + i,
                    "Esp", i % 10, "Hor", "Tel" + i);
            if (gestion.añadirProfesional(p)) {
                contadorN++;
            }
        }
        assertEquals(maximo, contadorN);
        assertEquals(maximo, gestion.getProfesionalesN());
    }

    @Test
    public void testGetGestorBDNoEsNull() {
        assertNotNull(gestion.getGestorBD());
    }

    @Test
    public void testAñadirYRecuperarVariosProfesionales() {
        int total = 3;
        String[][] datos = new String[3][3];
        datos[0][0] = "Daniel";
        datos[0][1] = "Dimitrov";
        datos[0][2] = "Nutricion Deportiva";
        datos[1][0] = "Andrei";
        datos[1][1] = "Veres";
        datos[1][2] = "Nutricion Clinica";
        datos[2][0] = "Sergio";
        datos[2][1] = "Gonzalez";
        datos[2][2] = "Dietetica General";
        int añadidosN = 0;
        for (int i = 0; i < total; i++) {
            ProfesionalClinica p = new ProfesionalClinica(datos[i][0], datos[i][1], datos[i][2], 10, "", "");
            if (gestion.añadirProfesional(p)) {
                añadidosN++;
            }
        }
        assertEquals(total, añadidosN);
        for (int i = 0; i < total; i++) {
            ProfesionalClinica p = gestion.getProfesional(i);
            assertEquals(datos[i][0], p.getNombre());
        }
    }
}
