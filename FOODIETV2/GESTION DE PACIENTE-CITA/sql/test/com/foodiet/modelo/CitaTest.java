package com.foodiet.modelo;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

public class CitaTest {

    private PacienteAdulto crearPaciente() {
        PacienteAdulto p = new PacienteAdulto(1, "Carlos", "Perez", 42, 82.0, 1.78,
                "656789012", "carlos@email.com", "Colesterol", "Reducir peso");
        return p;
    }

    private ProfesionalClinica crearProfesional() {
        ProfesionalClinica pro = new ProfesionalClinica(1, "Daniel", "Dimitrov",
                "Nutricion Deportiva", 14, "Lunes-Viernes 9:00-18:00", "612345678");
        return pro;
    }

    @Test
    public void testCitaOnlineConstructor() {
        PacienteAdulto p = crearPaciente();
        ProfesionalClinica pro = crearProfesional();
        CitaOnline cita = new CitaOnline(p, pro, 15, 3, 2026, "Consulta inicial", "Zoom");
        assertEquals("online", cita.getModalidad());
        assertEquals("pendiente", cita.getEstado());
        assertEquals("Zoom", cita.getPlataforma());
        assertEquals("https://foodiet.virtual/cita-0", cita.getEnlaceVideollamada());
        assertEquals(0, cita.getId());
        assertEquals(0, cita.getCodigoCita());
    }

    @Test
    public void testCitaPresencialConstructor() {
        PacienteAdulto p = crearPaciente();
        ProfesionalClinica pro = crearProfesional();
        CitaPresencial cita = new CitaPresencial(p, pro, 10, 2, 2026, "Revision", "Sala 3");
        assertEquals("presencial", cita.getModalidad());
        assertEquals("pendiente", cita.getEstado());
        assertEquals("Sala 3", cita.getSala());
        assertEquals("Calle Nutrici\u00f3n, 42 - Valencia", cita.getDireccionClinica());
        assertEquals("", cita.getEnlaceOnline());
    }

    @Test
    public void testCitaOnlineConstructorCompleto() {
        PacienteAdulto p = crearPaciente();
        ProfesionalClinica pro = crearProfesional();
        CitaOnline cita = new CitaOnline(5, p, pro, 20, 4, 2026, "confirmada",
                "Seguimiento", "https://foodiet.virtual/5", "Meet", "https://meet.foodiet/5");
        assertEquals(5, cita.getId());
        assertEquals(5, cita.getCodigoCita());
        assertEquals("online", cita.getModalidad());
        assertEquals("confirmada", cita.getEstado());
        assertEquals("Meet", cita.getPlataforma());
        assertEquals("https://meet.foodiet/5", cita.getEnlaceVideollamada());
    }

    @Test
    public void testCitaPresencialConstructorCompleto() {
        PacienteAdulto p = crearPaciente();
        ProfesionalClinica pro = crearProfesional();
        CitaPresencial cita = new CitaPresencial(3, p, pro, 5, 6, 2026, "completada",
                "Final", "", "Sala 1", "Calle Mayor 10 - Valencia");
        assertEquals(3, cita.getId());
        assertEquals("presencial", cita.getModalidad());
        assertEquals("completada", cita.getEstado());
        assertEquals("Sala 1", cita.getSala());
        assertEquals("Calle Mayor 10 - Valencia", cita.getDireccionClinica());
    }

    @Test
    public void testCitaBaseConstructor() {
        Cita cita = new Cita();
        assertEquals(0, cita.getId());
        assertEquals(0, cita.getCodigoCita());
        assertEquals("pendiente", cita.getEstado());
        assertEquals("presencial", cita.getModalidad());
        assertEquals("", cita.getMotivo());
        assertEquals("", cita.getEnlaceOnline());
    }

    @Test
    public void testFechaFormato() {
        PacienteAdulto p = crearPaciente();
        ProfesionalClinica pro = crearProfesional();
        CitaPresencial cita = new CitaPresencial(p, pro, 5, 3, 2026, "Prueba", "Sala X");
        assertEquals("05/03/2026", cita.fecha());
        CitaOnline cita2 = new CitaOnline(p, pro, 15, 12, 2026, "Prueba", "Zoom");
        assertEquals("15/12/2026", cita2.fecha());
    }

    @Test
    public void testFechaUnDigito() {
        PacienteAdulto p = crearPaciente();
        ProfesionalClinica pro = crearProfesional();
        CitaPresencial cita = new CitaPresencial(p, pro, 1, 1, 2026, "Año nuevo", "Sala A");
        assertEquals("01/01/2026", cita.fecha());
    }

    @Test
    public void testGetDiaMesAnyo() {
        PacienteAdulto p = crearPaciente();
        ProfesionalClinica pro = crearProfesional();
        CitaPresencial cita = new CitaPresencial(p, pro, 10, 2, 2026, "Test", "Sala B");
        assertEquals(10, cita.getDia());
        assertEquals(2, cita.getMes());
        assertEquals(2026, cita.getAnyo());
    }

    @Test
    public void testIdPacienteIdProfesional() {
        PacienteAdulto p = crearPaciente();
        ProfesionalClinica pro = crearProfesional();
        CitaPresencial cita = new CitaPresencial(p, pro, 10, 2, 2026, "Test", "Sala B");
        assertEquals(1, cita.getIdPaciente());
        assertEquals(1, cita.getIdProfesional());
    }

    @Test
    public void testSettersCita() {
        PacienteAdulto p = crearPaciente();
        ProfesionalClinica pro = crearProfesional();
        CitaPresencial cita = new CitaPresencial(p, pro, 10, 2, 2026, "Test", "Sala B");
        cita.setId(99);
        cita.setCodigoCita(88);
        cita.setEstado("cancelada");
        cita.setModalidad("online");
        cita.setMotivo("Nuevo motivo");
        cita.setEnlaceOnline("https://enlace.test");
        cita.setIdPaciente(7);
        cita.setIdProfesional(8);
        cita.setPaciente(null);
        cita.setProfesional(null);
        assertEquals(99, cita.getId());
        assertEquals(88, cita.getCodigoCita());
        assertEquals("cancelada", cita.getEstado());
        assertEquals("online", cita.getModalidad());
        assertEquals("Nuevo motivo", cita.getMotivo());
        assertEquals("https://enlace.test", cita.getEnlaceOnline());
        assertEquals(7, cita.getIdPaciente());
        assertEquals(8, cita.getIdProfesional());
    }

    @Test
    public void testMostrarCitaOnline() {
        PacienteAdulto p = crearPaciente();
        ProfesionalClinica pro = crearProfesional();
        CitaOnline cita = new CitaOnline(p, pro, 15, 3, 2026, "Consulta", "Zoom");
        String resultado = cita.mostrarCita();
        assertTrue(resultado.contains("Cita N\u00ba 0"));
        assertTrue(resultado.contains("15/03/2026"));
        assertTrue(resultado.contains("Carlos Perez"));
        assertTrue(resultado.contains("Daniel Dimitrov"));
        assertTrue(resultado.contains("online"));
        assertTrue(resultado.contains("pendiente"));
        assertTrue(resultado.contains("Zoom"));
        assertTrue(resultado.contains("https://foodiet.virtual/cita-0"));
    }

    @Test
    public void testMostrarCitaPresencial() {
        PacienteAdulto p = crearPaciente();
        ProfesionalClinica pro = crearProfesional();
        CitaPresencial cita = new CitaPresencial(p, pro, 10, 2, 2026, "Revision", "Sala 3");
        String resultado = cita.mostrarCita();
        assertTrue(resultado.contains("Cita N\u00ba 0"));
        assertTrue(resultado.contains("10/02/2026"));
        assertTrue(resultado.contains("presencial"));
        assertTrue(resultado.contains("Sala 3"));
        assertTrue(resultado.contains("Calle Nutrici\u00f3n, 42 - Valencia"));
    }

    @Test
    public void testMostrarCitaConEnlaceOnline() {
        PacienteAdulto p = crearPaciente();
        ProfesionalClinica pro = crearProfesional();
        CitaPresencial cita = new CitaPresencial(p, pro, 10, 2, 2026, "Test", "Sala X");
        cita.setModalidad("online");
        cita.setEnlaceOnline("https://foodiet.virtual/test");
        String resultado = cita.mostrarCita();
        assertTrue(resultado.contains("https://foodiet.virtual/test"));
    }

    @Test
    public void testCitaOnlineMostrarCitaConId() {
        PacienteAdulto p = crearPaciente();
        ProfesionalClinica pro = crearProfesional();
        CitaOnline cita = new CitaOnline(7, p, pro, 20, 4, 2026, "confirmada",
                "Seguimiento", "https://foodiet.virtual/7", "Meet", "https://meet.foodiet/7");
        String resultado = cita.mostrarCita();
        assertTrue(resultado.contains("Cita N\u00ba 7"));

    }

    @Test
    public void testCitaPresencialMostrarCitaConId() {
        PacienteAdulto p = crearPaciente();
        ProfesionalClinica pro = crearProfesional();
        CitaPresencial cita = new CitaPresencial(3, p, pro, 5, 6, 2026, "completada",
                "Final", "", "Sala 1", "Calle Mayor 10 - Valencia");
        String resultado = cita.mostrarCita();
        assertTrue(resultado.contains("Cita N\u00ba 3"));
    }

    @Test
    public void testGetPacienteYProfesional() {
        PacienteAdulto p = crearPaciente();
        ProfesionalClinica pro = crearProfesional();
        CitaPresencial cita = new CitaPresencial(p, pro, 10, 2, 2026, "Test", "Sala B");
        assertSame(p, cita.getPaciente());
        assertSame(pro, cita.getProfesional());
    }
}
