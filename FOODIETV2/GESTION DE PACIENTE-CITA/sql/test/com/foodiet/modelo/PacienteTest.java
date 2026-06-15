package com.foodiet.modelo;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

public class PacienteTest {

    @Test
    public void testJovenConstructorSimple() {
        PacienteJoven p = new PacienteJoven("Ana", "Lopez", 19, 58.0, 1.65);
        assertEquals("Ana", p.getNombre());
        assertEquals("Lopez", p.getApellido());
        assertEquals(19, p.getEdad());
        assertEquals(58.0, p.getPeso(), 0.001);
        assertEquals(1.65, p.getAltura(), 0.001);
        assertEquals(0, p.getId());
        assertEquals(0, p.getIdUsuario());
    }

    @Test
    public void testJovenTipoPaciente() {
        PacienteJoven p = new PacienteJoven("Ana", "Lopez", 19, 58.0, 1.65);
        assertEquals("Joven", p.tipoPaciente());
    }

    @Test
    public void testAdultoConstructorSimple() {
        PacienteAdulto p = new PacienteAdulto("Carlos", "Perez", 42, 82.0, 1.78);
        assertEquals("Carlos", p.getNombre());
        assertEquals("Perez", p.getApellido());
        assertEquals(42, p.getEdad());
        assertEquals(82.0, p.getPeso(), 0.001);
        assertEquals(1.78, p.getAltura(), 0.001);
    }

    @Test
    public void testAdultoTipoPaciente() {
        PacienteAdulto p = new PacienteAdulto("Carlos", "Perez", 42, 82.0, 1.78);
        assertEquals("Adulto", p.tipoPaciente());
    }

    @Test
    public void testJubiladoConstructorSimple() {
        PacienteJubilado p = new PacienteJubilado("Manuel", "Garcia", 71, 74.0, 1.70);
        assertEquals("Manuel", p.getNombre());
        assertEquals("Garcia", p.getApellido());
        assertEquals(71, p.getEdad());
    }

    @Test
    public void testJubiladoTipoPaciente() {
        PacienteJubilado p = new PacienteJubilado("Manuel", "Garcia", 71, 74.0, 1.70);
        assertEquals("Jubilado", p.tipoPaciente());
    }

    @Test
    public void testConstructorCompleto() {
        PacienteAdulto p = new PacienteAdulto(5, "Laura", "Martinez", 35, 63.0, 1.68,
                "612345678", "laura@email.com", "Asma", "Mejorar resistencia");
        assertEquals(5, p.getId());
        assertEquals(1, p.getIdUsuario());
        assertEquals("Laura", p.getNombre());
        assertEquals("Martinez", p.getApellido());
        assertEquals(35, p.getEdad());
        assertEquals(63.0, p.getPeso(), 0.001);
        assertEquals(1.68, p.getAltura(), 0.001);
        assertEquals("612345678", p.getTelefono());
        assertEquals("laura@email.com", p.getEmail());
        assertEquals("Asma", p.getHistorialMedico());
        assertEquals("Mejorar resistencia", p.getObjetivosNutricionales());
    }

    @Test
    public void testConstructorConCampos() {
        PacienteJoven p = new PacienteJoven("Ana", "Lopez", 19, 58.0, 1.65,
                "645678901", "ana@email.com", "Sin antecedentes", "Mejorar habitos");
        assertEquals("Ana", p.getNombre());
        assertEquals("Lopez", p.getApellido());
        assertEquals(19, p.getEdad());
        assertEquals("645678901", p.getTelefono());
        assertEquals("ana@email.com", p.getEmail());
        assertEquals("Sin antecedentes", p.getHistorialMedico());
        assertEquals("Mejorar habitos", p.getObjetivosNutricionales());
    }

    @Test
    public void testCalcularIMC() {
        PacienteAdulto p = new PacienteAdulto("Carlos", "Perez", 42, 82.0, 1.78);
        double imcEsperado = 82.0 / (1.78 * 1.78);
        assertEquals(imcEsperado, p.calcularIMC(), 0.001);
    }

    @Test
    public void testCalcularIMCPesoBajo() {
        PacienteJoven p = new PacienteJoven("Ana", "Lopez", 19, 50.0, 1.70);
        double imcEsperado = 50.0 / (1.70 * 1.70);
        assertEquals(imcEsperado, p.calcularIMC(), 0.001);
    }

    @Test
    public void testNombreCompleto() {
        PacienteAdulto p = new PacienteAdulto("Carlos", "Perez", 42, 82.0, 1.78);
        assertEquals("Carlos Perez", p.nombreCompleto());
    }

    @Test
    public void testToStringAdulto() {
        PacienteAdulto p = new PacienteAdulto("Carlos", "Perez", 42, 82.0, 1.78);
        assertEquals("Carlos Perez (Adulto)", p.toString());
    }

    @Test
    public void testToStringJoven() {
        PacienteJoven p = new PacienteJoven("Ana", "Lopez", 19, 58.0, 1.65);
        assertEquals("Ana Lopez (Joven)", p.toString());
    }

    @Test
    public void testToStringJubilado() {
        PacienteJubilado p = new PacienteJubilado("Manuel", "Garcia", 71, 74.0, 1.70);
        assertEquals("Manuel Garcia (Jubilado)", p.toString());
    }

    @Test
    public void testSetters() {
        PacienteAdulto p = new PacienteAdulto("Carlos", "Perez", 42, 82.0, 1.78);
        p.setId(10);
        p.setIdUsuario(3);
        p.setNombre("CarlosMod");
        p.setApellido("PerezMod");
        p.setEdad(43);
        p.setPeso(80.0);
        p.setAltura(1.80);
        p.setTelefono("600000000");
        p.setEmail("mod@email.com");
        p.setHistorialMedico("Ninguno");
        p.setObjetivosNutricionales("Mantener");
        assertEquals(10, p.getId());
        assertEquals(3, p.getIdUsuario());
        assertEquals("CarlosMod", p.getNombre());
        assertEquals("PerezMod", p.getApellido());
        assertEquals(43, p.getEdad());
        assertEquals(80.0, p.getPeso(), 0.001);
        assertEquals(1.80, p.getAltura(), 0.001);
        assertEquals("600000000", p.getTelefono());
        assertEquals("mod@email.com", p.getEmail());
        assertEquals("Ninguno", p.getHistorialMedico());
        assertEquals("Mantener", p.getObjetivosNutricionales());
    }

    @Test
    public void testCalcularIMCDiferentesValores() {
        PacienteAdulto[] casos = new PacienteAdulto[3];
        int casosN = 0;
        casos[casosN] = new PacienteAdulto("A", "B", 30, 70.0, 1.75);
        casosN++;
        casos[casosN] = new PacienteAdulto("C", "D", 40, 90.0, 1.80);
        casosN++;
        casos[casosN] = new PacienteAdulto("E", "F", 25, 55.0, 1.60);
        casosN++;
        double[] esperados = new double[3];
        esperados[0] = 70.0 / (1.75 * 1.75);
        esperados[1] = 90.0 / (1.80 * 1.80);
        esperados[2] = 55.0 / (1.60 * 1.60);
        for (int i = 0; i < casosN; i++) {
            assertEquals(esperados[i], casos[i].calcularIMC(), 0.001);
        }
    }
}
