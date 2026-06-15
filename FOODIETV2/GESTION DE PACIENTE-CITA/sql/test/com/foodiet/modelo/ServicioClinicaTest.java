package com.foodiet.modelo;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

public class ServicioClinicaTest {

    @Test
    public void testObtenerServiciosArrayConN() {
        ServicioClinica[] servicios = ServicioClinica.obtenerServicios();
        int N = 0;
        for (int i = 0; i < servicios.length; i++) {
            if (servicios[i] != null) {
                N++;
            }
        }
        assertEquals(servicios.length, N);
        assertEquals(6, N);
    }

    @Test
    public void testMostrarServicioFormato() {
        ServicioClinica s = new ServicioClinica("Consulta Presencial",
                "Evaluacion completa", 50.00, "presencial");
        String resultado = s.mostrarServicio();
        assertTrue(resultado.contains("Consulta Presencial"));
        assertTrue(resultado.contains("50,00\u20ac") || resultado.contains("50.00\u20ac"));
        assertTrue(resultado.contains("presencial"));
    }

    @Test
    public void testTodosLosServiciosTienenDatos() {
        ServicioClinica[] servicios = ServicioClinica.obtenerServicios();
        int N = servicios.length;
        double totalPrecio = 0;
        for (int i = 0; i < N; i++) {
            assertNotNull(servicios[i]);
            String texto = servicios[i].mostrarServicio();
            assertTrue(texto.length() > 0);
            totalPrecio = totalPrecio + (i + 1);
        }
        assertTrue(N > 0);
    }

    @Test
    public void testPrimerServicioEsConsultaPresencial() {
        ServicioClinica[] servicios = ServicioClinica.obtenerServicios();
        assertTrue(servicios[0].mostrarServicio().contains("Consulta Presencial"));
    }

    @Test
    public void testUltimoServicioEsSeguimientoMensual() {
        ServicioClinica[] servicios = ServicioClinica.obtenerServicios();
        int N = servicios.length;
        String ultimo = servicios[N - 1].mostrarServicio();
        assertTrue(ultimo.contains("Seguimiento Mensual"));
    }

    @Test
    public void testServiciosOnlineYPresencial() {
        ServicioClinica[] servicios = ServicioClinica.obtenerServicios();
        int modalidadPresencialN = 0;
        int modalidadOnlineN = 0;
        int N = servicios.length;
        for (int i = 0; i < N; i++) {
            String texto = servicios[i].mostrarServicio();
            if (texto.contains("presencial")) {
                modalidadPresencialN++;
            }
            if (texto.contains("online")) {
                modalidadOnlineN++;
            }
        }
        assertEquals(3, modalidadPresencialN);
        assertEquals(3, modalidadOnlineN);
    }

    @Test
    public void testServiciosTienenPrecioPositivo() {
        ServicioClinica s1 = new ServicioClinica("A", "B", 0.01, "presencial");
        String resultado = s1.mostrarServicio();
        assertTrue(resultado.contains("0,01") || resultado.contains("0.01"));
    }
}
