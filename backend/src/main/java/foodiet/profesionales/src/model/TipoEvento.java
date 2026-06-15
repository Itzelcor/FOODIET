package model;
/**
 * Tipos de evento que puede tener un profesional en su agenda.
 *
 * CONSULTA → Cita con un paciente
 * VACACIONES → Período de ausencia planificado
 * SUSTITUCION → El profesional cubre a un compañero ausente
 */
public enum TipoEvento {
    CONSULTA,
    VACACIONES,
    SUSTITUCION
}
