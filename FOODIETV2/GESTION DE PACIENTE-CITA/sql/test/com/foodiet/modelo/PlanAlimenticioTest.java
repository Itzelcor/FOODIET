package com.foodiet.modelo;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

public class PlanAlimenticioTest {

    @Test
    public void testConstructorCompleto() {
        PlanAlimenticio plan = new PlanAlimenticio(10, 3, 1,
                "2026-01-15", "2026-03-15",
                "Perder peso", "Dieta hipocalorica", 1800);
        assertEquals(10, plan.getId());
        assertEquals(3, plan.getIdPaciente());
        assertEquals(1, plan.getIdProfesional());
        assertEquals("2026-01-15", plan.getFechaInicio());
        assertEquals("2026-03-15", plan.getFechaFin());
        assertEquals("Perder peso", plan.getObjetivo());
        assertEquals("Dieta hipocalorica", plan.getDescripcion());
        assertEquals(1800, plan.getCaloriasDiarias());
    }

    @Test
    public void testConstructorSimple() {
        PlanAlimenticio plan = new PlanAlimenticio(1, 2,
                "2026-02-01", null,
                "Mantener peso", "Dieta equilibrada", 2000);
        assertEquals(0, plan.getId());
        assertEquals(1, plan.getIdPaciente());
        assertEquals(2, plan.getIdProfesional());
        assertEquals("2026-02-01", plan.getFechaInicio());
        assertNull(plan.getFechaFin());
        assertEquals("Mantener peso", plan.getObjetivo());
        assertEquals("Dieta equilibrada", plan.getDescripcion());
        assertEquals(2000, plan.getCaloriasDiarias());
    }

    @Test
    public void testMostrarPlan() {
        PlanAlimenticio plan = new PlanAlimenticio(5, 2, 1,
                "2026-01-15", "2026-03-15",
                "Perder peso", "Dieta hipocalorica", 1800);
        String resultado = plan.mostrarPlan();
        assertTrue(resultado.contains("Plan N\u00ba 5"));
        assertTrue(resultado.contains("Paciente ID: 2"));
        assertTrue(resultado.contains("Profesional ID: 1"));
        assertTrue(resultado.contains("2026-01-15"));
        assertTrue(resultado.contains("2026-03-15"));
        assertTrue(resultado.contains("Perder peso"));
        assertTrue(resultado.contains("1800"));
    }

    @Test
    public void testMostrarPlanSinFechaFin() {
        PlanAlimenticio plan = new PlanAlimenticio(3, 1, 2,
                "2026-02-01", null,
                "Mantener peso", "Dieta equilibrada", 2000);
        String resultado = plan.mostrarPlan();
        assertTrue(resultado.contains("Sin fecha fin"));
    }

    @Test
    public void testSetters() {
        PlanAlimenticio plan = new PlanAlimenticio(1, 1, 1,
                "2026-01-01", "2026-02-01", "Obj", "Desc", 1500);
        plan.setId(20);
        plan.setIdPaciente(5);
        plan.setIdProfesional(3);
        plan.setFechaInicio("2026-03-01");
        plan.setFechaFin("2026-04-01");
        plan.setObjetivo("Nuevo objetivo");
        plan.setDescripcion("Nueva descripcion");
        plan.setCaloriasDiarias(1700);
        assertEquals(20, plan.getId());
        assertEquals(5, plan.getIdPaciente());
        assertEquals(3, plan.getIdProfesional());
        assertEquals("2026-03-01", plan.getFechaInicio());
        assertEquals("2026-04-01", plan.getFechaFin());
        assertEquals("Nuevo objetivo", plan.getObjetivo());
        assertEquals("Nueva descripcion", plan.getDescripcion());
        assertEquals(1700, plan.getCaloriasDiarias());
    }
}
