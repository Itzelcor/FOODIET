package com.foodiet.modelo;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

public class UsuarioTest {

    @Test
    public void testConstructor() {
        Usuario u = new Usuario(1, "admin", "admin@foodiet.com", "hash123", "administrador");
        assertEquals(1, u.getIdUsuario());
        assertEquals("admin", u.getNombreUsuario());
        assertEquals("admin@foodiet.com", u.getEmail());
        assertEquals("hash123", u.getContrase\u00f1aHash());
        assertEquals("administrador", u.getRol());
        assertTrue(u.isActivo());
    }

    @Test
    public void testUsuarioNutricionista() {
        Usuario u = new Usuario(2, "dietista1", "dietista@foodiet.com", "hash456", "nutricionista");
        assertEquals("dietista1", u.getNombreUsuario());
        assertEquals("nutricionista", u.getRol());
        assertTrue(u.isActivo());
    }

    @Test
    public void testUsuarioPaciente() {
        Usuario u = new Usuario(3, "ana.lopez", "ana@email.com", "hash789", "paciente");
        assertEquals("ana.lopez", u.getNombreUsuario());
        assertEquals("paciente", u.getRol());
    }

    @Test
    public void testToString() {
        Usuario u = new Usuario(1, "admin", "admin@foodiet.com", "hash123", "administrador");
        assertEquals("admin (administrador)", u.toString());
    }
}
