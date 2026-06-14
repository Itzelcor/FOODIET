package foodiet.modelo;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests unitarios para la clase Profesional.
 *
 * Qué se comprueba aquí:
 * - Que el constructor con parámetros asigna los valores correctamente.
 * - Que los getters devuelven los valores esperados.
 * - Que los setters modifican los valores correctamente.
 * - Que el constructor vacío crea un objeto sin errores.
 */
@DisplayName("Tests del modelo Profesional")
class ProfesionalTest {

    // -------------------------------------------------------
    // PASO 1 — Constructor con parámetros
    // -------------------------------------------------------

    @Test
    @DisplayName("El constructor con parámetros asigna todos los campos correctamente")
    void constructorConParametros_asignaValoresCorrectos() {
        // Arrange
        int idEsperado = 1;
        String nombreEsperado = "Laura";
        String apellidosEsperado = "García Pérez";
        String emailEsperado = "laura@foodiet.com";
        String telefonoEsperado = "612345678";
        String especialidadEsperada = "Nutrición clínica";
        String rolEsperado = "NUTRICIONISTA";
        String formacionEsperada = "Grado en Nutrición";

        // Act
        Profesional profesional = new Profesional(
            idEsperado, nombreEsperado, apellidosEsperado,
            emailEsperado, telefonoEsperado,
            especialidadEsperada, rolEsperado, formacionEsperada
        );

        // Assert
        assertEquals(idEsperado,           profesional.getId());
        assertEquals(nombreEsperado,        profesional.getNombre());
        assertEquals(apellidosEsperado,     profesional.getApellidos());
        assertEquals(emailEsperado,         profesional.getEmail());
        assertEquals(telefonoEsperado,      profesional.getTelefono());
        assertEquals(especialidadEsperada,  profesional.getEspecialidad());
        assertEquals(rolEsperado,           profesional.getRol());
        assertEquals(formacionEsperada,     profesional.getFormacion());
    }

    @Test
    @DisplayName("El constructor vacío crea un objeto sin lanzar excepciones")
    void constructorVacio_creaObjetoSinErrores() {
        // Act & Assert — si lanza excepción, el test falla automáticamente
        Profesional profesional = new Profesional();
        assertNotNull(profesional);
    }

    // -------------------------------------------------------
    // PASO 2 — Setters y Getters
    // -------------------------------------------------------

    @Test
    @DisplayName("Los setters modifican correctamente los valores del profesional")
    void setters_modificanValoresCorrectamente() {
        // Arrange
        Profesional profesional = new Profesional();

        // Act
        profesional.setId(5);
        profesional.setNombre("Carlos");
        profesional.setApellidos("Ruiz López");
        profesional.setEmail("carlos@foodiet.com");
        profesional.setTelefono("699000111");
        profesional.setEspecialidad("Dietética");
        profesional.setRol("ADMINISTRATIVO");
        profesional.setFormacion("Ciclo en Dietética");

        // Assert
        assertEquals(5,                      profesional.getId());
        assertEquals("Carlos",               profesional.getNombre());
        assertEquals("Ruiz López",           profesional.getApellidos());
        assertEquals("carlos@foodiet.com",   profesional.getEmail());
        assertEquals("699000111",            profesional.getTelefono());
        assertEquals("Dietética",            profesional.getEspecialidad());
        assertEquals("ADMINISTRATIVO",       profesional.getRol());
        assertEquals("Ciclo en Dietética",   profesional.getFormacion());
    }

    @Test
    @DisplayName("El método toString no devuelve null ni una cadena vacía")
    void toString_devuelveCadenaNoVacia() {
        // Arrange
        Profesional profesional = new Profesional(
            1, "Ana", "Martínez", "ana@foodiet.com",
            "600000001", "Nutrición", "DIRECTIVO", "Máster en Nutrición"
        );

        // Act
        String resultado = profesional.toString();

        // Assert
        assertNotNull(resultado);
        assertFalse(resultado.isEmpty());
    }
}
