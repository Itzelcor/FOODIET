package foodiet.logica;

import foodiet.modelo.Horario;
import foodiet.modelo.Vacacion;
import foodiet.util.ValidadorProfesional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests unitarios para la lógica de negocio del subsistema.
 *
 * Qué se comprueba aquí:
 * - Validación de campos obligatorios (nombre, email, rol).
 * - Validación de horarios (inicio antes que fin).
 * - Validación de períodos de vacaciones (fechas coherentes).
 * - Control de acceso por rol (DIRECTIVO vs resto).
 *
 * Todos los tests siguen el patrón Arrange → Act → Assert.
 */
@DisplayName("Tests de validación y lógica de negocio")
class ValidadorProfesionalTest {

    // =======================================================
    // BLOQUE 1 — Validación de nombre
    // =======================================================

    @Test
    @DisplayName("Un nombre con texto es válido")
    void esNombreValido_conTextoNormal_devuelveTrue() {
        assertTrue(ValidadorProfesional.esNombreValido("Laura"));
    }

    @Test
    @DisplayName("Un nombre null no es válido")
    void esNombreValido_conNull_devuelveFalse() {
        assertFalse(ValidadorProfesional.esNombreValido(null));
    }

    @Test
    @DisplayName("Un nombre vacío no es válido")
    void esNombreValido_conCadenaVacia_devuelveFalse() {
        assertFalse(ValidadorProfesional.esNombreValido(""));
    }

    @Test
    @DisplayName("Un nombre con solo espacios no es válido")
    void esNombreValido_conSoloEspacios_devuelveFalse() {
        assertFalse(ValidadorProfesional.esNombreValido("   "));
    }

    // =======================================================
    // BLOQUE 2 — Validación de email
    // =======================================================

    @Test
    @DisplayName("Un email con @ y punto es válido")
    void esEmailValido_conFormatoCorrecto_devuelveTrue() {
        assertTrue(ValidadorProfesional.esEmailValido("laura@foodiet.com"));
    }

    @Test
    @DisplayName("Un email sin @ no es válido")
    void esEmailValido_sinArroba_devuelveFalse() {
        assertFalse(ValidadorProfesional.esEmailValido("laurafoodiet.com"));
    }

    @Test
    @DisplayName("Un email sin punto no es válido")
    void esEmailValido_sinPunto_devuelveFalse() {
        assertFalse(ValidadorProfesional.esEmailValido("laura@foodietcom"));
    }

    @Test
    @DisplayName("Un email null no es válido")
    void esEmailValido_conNull_devuelveFalse() {
        assertFalse(ValidadorProfesional.esEmailValido(null));
    }

    // =======================================================
    // BLOQUE 3 — Validación de rol
    // =======================================================

    @Test
    @DisplayName("El rol DIRECTIVO es válido")
    void esRolValido_directivo_devuelveTrue() {
        assertTrue(ValidadorProfesional.esRolValido("DIRECTIVO"));
    }

    @Test
    @DisplayName("El rol NUTRICIONISTA es válido")
    void esRolValido_nutricionista_devuelveTrue() {
        assertTrue(ValidadorProfesional.esRolValido("NUTRICIONISTA"));
    }

    @Test
    @DisplayName("El rol ADMINISTRATIVO es válido")
    void esRolValido_administrativo_devuelveTrue() {
        assertTrue(ValidadorProfesional.esRolValido("ADMINISTRATIVO"));
    }

    @Test
    @DisplayName("Un rol desconocido no es válido")
    void esRolValido_rolDesconocido_devuelveFalse() {
        assertFalse(ValidadorProfesional.esRolValido("JEFE"));
    }

    @Test
    @DisplayName("Un rol null no es válido")
    void esRolValido_null_devuelveFalse() {
        assertFalse(ValidadorProfesional.esRolValido(null));
    }

    @Test
    @DisplayName("Un rol en minúsculas no es válido — el sistema diferencia mayúsculas")
    void esRolValido_enMinusculas_devuelveFalse() {
        assertFalse(ValidadorProfesional.esRolValido("directivo"));
    }

    // =======================================================
    // BLOQUE 4 — Validación de horarios
    // =======================================================

    @Test
    @DisplayName("Un horario con inicio anterior al fin es válido")
    void esHorarioValido_inicioAntesQueFin_devuelveTrue() {
        // Arrange
        Horario horario = new Horario(1, 1, "Lunes", "09:00", "14:00");

        // Act & Assert
        assertTrue(ValidadorProfesional.esHorarioValido(horario));
    }

    @Test
    @DisplayName("Un horario con inicio igual al fin no es válido")
    void esHorarioValido_inicioIgualAlFin_devuelveFalse() {
        // Arrange
        Horario horario = new Horario(1, 1, "Martes", "09:00", "09:00");

        // Act & Assert
        assertFalse(ValidadorProfesional.esHorarioValido(horario));
    }

    @Test
    @DisplayName("Un horario con inicio posterior al fin no es válido")
    void esHorarioValido_inicioDespuesDeFin_devuelveFalse() {
        // Arrange
        Horario horario = new Horario(1, 1, "Miércoles", "20:00", "08:00");

        // Act & Assert
        assertFalse(ValidadorProfesional.esHorarioValido(horario));
    }

    @Test
    @DisplayName("Un horario null no es válido")
    void esHorarioValido_null_devuelveFalse() {
        assertFalse(ValidadorProfesional.esHorarioValido(null));
    }

    @Test
    @DisplayName("Un horario con horaInicio null no es válido")
    void esHorarioValido_horaInicioNull_devuelveFalse() {
        // Arrange
        Horario horario = new Horario(1, 1, "Jueves", null, "14:00");

        // Act & Assert
        assertFalse(ValidadorProfesional.esHorarioValido(horario));
    }

    // =======================================================
    // BLOQUE 5 — Validación de períodos de vacaciones
    // =======================================================

    @Test
    @DisplayName("Un período con inicio anterior al fin es válido")
    void esPeriodoVacacionValido_inicioAntesQueFin_devuelveTrue() {
        // Arrange
        Vacacion vacacion = new Vacacion(
            1, 1,
            LocalDate.of(2025, 8, 1),
            LocalDate.of(2025, 8, 31),
            "VACACIONES", null
        );

        // Act & Assert
        assertTrue(ValidadorProfesional.esPeriodoVacacionValido(vacacion));
    }

    @Test
    @DisplayName("Un período de un solo día (inicio igual a fin) es válido")
    void esPeriodoVacacionValido_unSoloDia_devuelveTrue() {
        // Arrange — una baja médica de un día es un caso real
        LocalDate hoy = LocalDate.of(2025, 10, 5);
        Vacacion vacacion = new Vacacion(1, 1, hoy, hoy, "BAJA", null);

        // Act & Assert
        assertTrue(ValidadorProfesional.esPeriodoVacacionValido(vacacion));
    }

    @Test
    @DisplayName("Un período con inicio posterior al fin no es válido")
    void esPeriodoVacacionValido_inicioDespuesDeFin_devuelveFalse() {
        // Arrange
        Vacacion vacacion = new Vacacion(
            1, 1,
            LocalDate.of(2025, 9, 30),
            LocalDate.of(2025, 9, 1),
            "VACACIONES", null
        );

        // Act & Assert
        assertFalse(ValidadorProfesional.esPeriodoVacacionValido(vacacion));
    }

    @Test
    @DisplayName("Una vacación null no es válida")
    void esPeriodoVacacionValido_null_devuelveFalse() {
        assertFalse(ValidadorProfesional.esPeriodoVacacionValido(null));
    }

    @Test
    @DisplayName("Una vacación con fechaInicio null no es válida")
    void esPeriodoVacacionValido_fechaInicioNull_devuelveFalse() {
        // Arrange
        Vacacion vacacion = new Vacacion(
            1, 1, null, LocalDate.of(2025, 8, 31), "VACACIONES", null
        );

        // Act & Assert
        assertFalse(ValidadorProfesional.esPeriodoVacacionValido(vacacion));
    }

    // =======================================================
    // BLOQUE 6 — Control de acceso por rol
    // =======================================================

    @Test
    @DisplayName("El DIRECTIVO tiene acceso total al sistema")
    void tieneAccesoTotal_directivo_devuelveTrue() {
        assertTrue(ValidadorProfesional.tieneAccesoTotal("DIRECTIVO"));
    }

    @Test
    @DisplayName("El NUTRICIONISTA no tiene acceso total")
    void tieneAccesoTotal_nutricionista_devuelveFalse() {
        assertFalse(ValidadorProfesional.tieneAccesoTotal("NUTRICIONISTA"));
    }

    @Test
    @DisplayName("El ADMINISTRATIVO no tiene acceso total")
    void tieneAccesoTotal_administrativo_devuelveFalse() {
        assertFalse(ValidadorProfesional.tieneAccesoTotal("ADMINISTRATIVO"));
    }

    @Test
    @DisplayName("Un rol null no tiene acceso total")
    void tieneAccesoTotal_null_devuelveFalse() {
        assertFalse(ValidadorProfesional.tieneAccesoTotal(null));
    }
}
