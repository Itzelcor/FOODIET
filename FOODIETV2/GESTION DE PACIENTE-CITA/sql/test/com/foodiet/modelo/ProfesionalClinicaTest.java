package com.foodiet.modelo;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

public class ProfesionalClinicaTest {

    @Test
    public void testConstructorCompleto() {
        ProfesionalClinica p = new ProfesionalClinica(3, "Andrei", "Veres",
                "Nutricion Clinica", 12, "Lunes-Viernes 10:00-19:00", "623456789");
        assertEquals(3, p.getId());
        assertEquals("Andrei", p.getNombre());
        assertEquals("Veres", p.getApellido());
        assertEquals("Nutricion Clinica", p.getEspecialidad());
        assertEquals(12, p.getAnyosExperiencia());
        assertEquals("Lunes-Viernes 10:00-19:00", p.getHorario());
        assertEquals("623456789", p.getTelefono());
    }

    @Test
    public void testConstructorSimple() {
        ProfesionalClinica p = new ProfesionalClinica("Sergio", "Gonzalez",
                "Dietetica General", 16, "Lunes-Viernes 8:00-17:00", "634567890");
        assertEquals(0, p.getId());
        assertEquals("Sergio", p.getNombre());
        assertEquals("Gonzalez", p.getApellido());
        assertEquals("Dietetica General", p.getEspecialidad());
        assertEquals(16, p.getAnyosExperiencia());
    }

    @Test
    public void testNombreCompleto() {
        ProfesionalClinica p = new ProfesionalClinica("Daniel", "Dimitrov",
                "Nutricion Deportiva", 14, "", "612345678");
        assertEquals("Daniel Dimitrov", p.nombreCompleto());
    }

    @Test
    public void testSetId() {
        ProfesionalClinica p = new ProfesionalClinica("Test", "User",
                "General", 5, "Horario", "600000000");
        p.setId(10);
        assertEquals(10, p.getId());
    }

    @Test
    public void testMostrarInfoSinHorario() {
        ProfesionalClinica p = new ProfesionalClinica("Daniel", "Dimitrov",
                "Nutricion Deportiva", 14, "", "612345678");
        p.mostrarInfo();
        assertTrue(p.getHorario().isEmpty());
    }

    @Test
    public void testGettersAdicionales() {
        ProfesionalClinica p = new ProfesionalClinica(5, "Laura", "Sanchez",
                "Nutricion Pediatrica", 8, "Martes-Jueves 9:00-15:00", "645678901");
        assertEquals("Laura", p.getNombre());
        assertEquals("Sanchez", p.getApellido());
        assertEquals("Nutricion Pediatrica", p.getEspecialidad());
        assertEquals(8, p.getAnyosExperiencia());
        assertEquals("Martes-Jueves 9:00-15:00", p.getHorario());
        assertEquals("645678901", p.getTelefono());
    }
}
