package foodiet.modelo;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests unitarios para la clase Horario.
 *
 * Qué se comprueba aquí:
 * - Que el constructor asigna los valores correctamente.
 * - Que los setters funcionan.
 * - Que se pueden representar días y franjas horarias.
 */
@DisplayName("Tests del modelo Horario")
class HorarioTest {

    @Test
    @DisplayName("El constructor con parámetros asigna todos los campos correctamente")
    void constructorConParametros_asignaValoresCorrectos() {
        // Arrange & Act
        Horario horario = new Horario(1, 3, "Lunes", "09:00", "14:00");

        // Assert
        assertEquals(1,        horario.getId());
        assertEquals(3,        horario.getIdProfesional());
        assertEquals("Lunes",  horario.getDiaSemana());
        assertEquals("09:00",  horario.getHoraInicio());
        assertEquals("14:00",  horario.getHoraFin());
    }

    @Test
    @DisplayName("El constructor vacío crea un objeto sin errores")
    void constructorVacio_creaObjetoSinErrores() {
        Horario horario = new Horario();
        assertNotNull(horario);
    }

    @Test
    @DisplayName("Los setters modifican correctamente los campos del horario")
    void setters_modificanValoresCorrectamente() {
        // Arrange
        Horario horario = new Horario();

        // Act
        horario.setId(2);
        horario.setIdProfesional(7);
        horario.setDiaSemana("Miércoles");
        horario.setHoraInicio("16:00");
        horario.setHoraFin("20:00");

        // Assert
        assertEquals(2,             horario.getId());
        assertEquals(7,             horario.getIdProfesional());
        assertEquals("Miércoles",   horario.getDiaSemana());
        assertEquals("16:00",       horario.getHoraInicio());
        assertEquals("20:00",       horario.getHoraFin());
    }

    @Test
    @DisplayName("El método toString no devuelve null ni cadena vacía")
    void toString_devuelveCadenaNoVacia() {
        Horario horario = new Horario(1, 2, "Viernes", "08:00", "15:00");
        assertNotNull(horario.toString());
        assertFalse(horario.toString().isEmpty());
    }
}
