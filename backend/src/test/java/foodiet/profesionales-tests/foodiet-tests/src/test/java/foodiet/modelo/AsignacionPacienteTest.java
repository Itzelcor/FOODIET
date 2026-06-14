package foodiet.modelo;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests unitarios para la clase AsignacionPaciente.
 *
 * Qué se comprueba aquí:
 * - Que el constructor asigna los valores correctamente.
 * - Que los setters funcionan.
 * - Que la relación profesional–paciente queda reflejada correctamente.
 *
 * NOTA: El campo idPaciente es FK pendiente de confirmar con el subsistema
 * de pacientes (Dani). Aquí se prueba solo a nivel de modelo Java.
 */
@DisplayName("Tests del modelo AsignacionPaciente")
class AsignacionPacienteTest {

    @Test
    @DisplayName("El constructor con parámetros asigna todos los campos correctamente")
    void constructorConParametros_asignaValoresCorrectos() {
        // Arrange
        LocalDate fecha = LocalDate.of(2025, 6, 15);

        // Act
        AsignacionPaciente asignacion = new AsignacionPaciente(
            1, 2, 10, fecha, "Revisión mensual"
        );

        // Assert
        assertEquals(1,                  asignacion.getId());
        assertEquals(2,                  asignacion.getIdProfesional());
        assertEquals(10,                 asignacion.getIdPaciente());
        assertEquals(fecha,              asignacion.getFechaAsignacion());
        assertEquals("Revisión mensual", asignacion.getObservaciones());
    }

    @Test
    @DisplayName("El constructor vacío crea un objeto sin errores")
    void constructorVacio_creaObjetoSinErrores() {
        AsignacionPaciente asignacion = new AsignacionPaciente();
        assertNotNull(asignacion);
    }

    @Test
    @DisplayName("Los setters modifican correctamente los campos de la asignación")
    void setters_modificanValoresCorrectamente() {
        // Arrange
        AsignacionPaciente asignacion = new AsignacionPaciente();
        LocalDate fecha = LocalDate.of(2026, 3, 1);

        // Act
        asignacion.setId(5);
        asignacion.setIdProfesional(3);
        asignacion.setIdPaciente(20);
        asignacion.setFechaAsignacion(fecha);
        asignacion.setObservaciones("Primer control");

        // Assert
        assertEquals(5,               asignacion.getId());
        assertEquals(3,               asignacion.getIdProfesional());
        assertEquals(20,              asignacion.getIdPaciente());
        assertEquals(fecha,           asignacion.getFechaAsignacion());
        assertEquals("Primer control", asignacion.getObservaciones());
    }

    @Test
    @DisplayName("Las observaciones pueden ser null sin errores")
    void observaciones_puedenSerNull() {
        // Arrange
        LocalDate fecha = LocalDate.now();

        // Act
        AsignacionPaciente asignacion = new AsignacionPaciente(
            1, 1, 5, fecha, null
        );

        // Assert
        assertNull(asignacion.getObservaciones());
    }
}
