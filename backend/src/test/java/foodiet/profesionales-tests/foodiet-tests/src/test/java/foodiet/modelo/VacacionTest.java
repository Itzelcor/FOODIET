package foodiet.modelo;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests unitarios para la clase Vacacion.
 *
 * Qué se comprueba aquí:
 * - Que el constructor asigna los campos correctamente.
 * - Que el campo idSustituto puede ser null (sustituto no asignado).
 * - Que los setters funcionan correctamente.
 */
@DisplayName("Tests del modelo Vacacion")
class VacacionTest {

    @Test
    @DisplayName("El constructor con parámetros asigna todos los campos correctamente")
    void constructorConParametros_asignaValoresCorrectos() {
        // Arrange
        LocalDate inicio = LocalDate.of(2025, 8, 1);
        LocalDate fin    = LocalDate.of(2025, 8, 31);

        // Act
        Vacacion vacacion = new Vacacion(1, 2, inicio, fin, "VACACIONES", 5);

        // Assert
        assertEquals(1,             vacacion.getId());
        assertEquals(2,             vacacion.getIdProfesional());
        assertEquals(inicio,        vacacion.getFechaInicio());
        assertEquals(fin,           vacacion.getFechaFin());
        assertEquals("VACACIONES",  vacacion.getMotivo());
        assertEquals(5,             vacacion.getIdSustituto());
    }

    @Test
    @DisplayName("El idSustituto puede ser null si no hay sustituto asignado")
    void idSustituto_puedeSerNull() {
        // Arrange
        LocalDate inicio = LocalDate.of(2025, 12, 23);
        LocalDate fin    = LocalDate.of(2025, 12, 31);

        // Act
        Vacacion vacacion = new Vacacion(2, 3, inicio, fin, "BAJA", null);

        // Assert — null es un valor válido para idSustituto
        assertNull(vacacion.getIdSustituto());
    }

    @Test
    @DisplayName("Los setters modifican correctamente los campos de la vacación")
    void setters_modificanValoresCorrectamente() {
        // Arrange
        Vacacion vacacion = new Vacacion();
        LocalDate inicio = LocalDate.of(2026, 1, 10);
        LocalDate fin    = LocalDate.of(2026, 1, 20);

        // Act
        vacacion.setId(10);
        vacacion.setIdProfesional(4);
        vacacion.setFechaInicio(inicio);
        vacacion.setFechaFin(fin);
        vacacion.setMotivo("SUSTITUCIÓN");
        vacacion.setIdSustituto(8);

        // Assert
        assertEquals(10,            vacacion.getId());
        assertEquals(4,             vacacion.getIdProfesional());
        assertEquals(inicio,        vacacion.getFechaInicio());
        assertEquals(fin,           vacacion.getFechaFin());
        assertEquals("SUSTITUCIÓN", vacacion.getMotivo());
        assertEquals(8,             vacacion.getIdSustituto());
    }
}
