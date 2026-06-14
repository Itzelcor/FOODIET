package hacer_main_comun.Gestion_del_Equipo_Profesional.src.model;
/**
 * Define los roles disponibles para los profesionales de FooDiet.
 * El rol determina qué opciones del sistema puede ver y usar cada profesional.
 *
 * DIRECTIVO → Acceso total (datos personales, asignaciones, agenda, sustituciones, gestión completa)
 * NUTRICIONISTA → Acceso al historial dietético de sus pacientes asignados
 * ADMINISTRATIVO → Gestión de agenda y sustituciones
 */
public enum Rol {
    DIRECTIVO,
    NUTRICIONISTA,
    ADMINISTRATIVO
}
