package foodiet.util;

import foodiet.modelo.Horario;
import foodiet.modelo.Vacacion;

/**
 * Utilidades de validación para el subsistema de Gestión del Equipo Profesional.
 * Contiene la lógica de negocio separada del acceso a datos.
 */
public class ValidadorProfesional {

    // Roles válidos del sistema
    public static final String ROL_DIRECTIVO       = "DIRECTIVO";
    public static final String ROL_NUTRICIONISTA   = "NUTRICIONISTA";
    public static final String ROL_ADMINISTRATIVO  = "ADMINISTRATIVO";

    /**
     * Comprueba que un nombre no sea nulo ni vacío.
     */
    public static boolean esNombreValido(String nombre) {
        return nombre != null && !nombre.trim().isEmpty();
    }

    /**
     * Comprueba que el email tenga un formato mínimo válido (contiene @ y punto).
     */
    public static boolean esEmailValido(String email) {
        return email != null && email.contains("@") && email.contains(".");
    }

    /**
     * Comprueba que el rol sea uno de los tres permitidos en el sistema.
     */
    public static boolean esRolValido(String rol) {
        return ROL_DIRECTIVO.equals(rol)
            || ROL_NUTRICIONISTA.equals(rol)
            || ROL_ADMINISTRATIVO.equals(rol);
    }

    /**
     * Comprueba que la hora de inicio sea anterior a la hora de fin en un horario.
     * Las horas se comparan como cadenas en formato HH:mm (válido para ordenación lexicográfica).
     */
    public static boolean esHorarioValido(Horario horario) {
        if (horario == null) return false;
        if (horario.getHoraInicio() == null || horario.getHoraFin() == null) return false;
        // Comparación lexicográfica funciona correctamente para formato HH:mm
        return horario.getHoraInicio().compareTo(horario.getHoraFin()) < 0;
    }

    /**
     * Comprueba que las fechas de una vacación sean coherentes
     * (inicio no puede ser posterior al fin).
     */
    public static boolean esPeriodoVacacionValido(Vacacion vacacion) {
        if (vacacion == null) return false;
        if (vacacion.getFechaInicio() == null || vacacion.getFechaFin() == null) return false;
        return !vacacion.getFechaInicio().isAfter(vacacion.getFechaFin());
    }

    /**
     * Comprueba si un profesional con el rol dado puede ver los datos de otro rol.
     * Regla: solo el DIRECTIVO tiene acceso completo.
     * El resto solo puede consultar su propia información.
     */
    public static boolean tieneAccesoTotal(String rol) {
        return ROL_DIRECTIVO.equals(rol);
    }
}
