-- ============================================================
-- FooDiet – Base de Datos Completa y Batería de Consultas SELECT
-- IES Font de San Lluis · 1 DAW PID 2025-2026
-- ============================================================
USE FooDiet;
-- ============================================================
--           BATERÍA COMPLETA DE CONSULTAS SELECT
-- ============================================================

-- SELECT 1.1: Listado básico de pacientes activos con sus credenciales de acceso y tipo de segmento.
SELECT 
    p.id_paciente,
    p.nombre, 
    p.apellidos, 
    u.email AS email_cuenta, 
    p.telefono,
    p.tipo_paciente,
    u.activo
FROM paciente p
JOIN usuario u ON p.id_usuario = u.id_usuario
WHERE u.activo = 1;

-- SELECT 1.2: Pacientes y sus respectivas alergias, intolerancias o restricciones alimentarias asociadas, ordenados por severidad.
SELECT 
    p.id_paciente,
    CONCAT(p.nombre, ' ', p.apellidos) AS paciente,
    a.nombre AS alergia_restriccion,
    a.tipo AS tipo_alergia,
    pa.severidad
FROM paciente p
JOIN paciente_alergia pa ON p.id_paciente = pa.id_paciente
JOIN alergia a ON pa.id_alergia = a.id_alergia
ORDER BY FIELD(pa.severidad, 'grave', 'moderada', 'leve');

-- SELECT 2.1: Listado de profesionales con su respectiva especialidad médica y años de experiencia.
SELECT 
    pr.id_profesional,
    pr.nom_completo AS profesional,
    e.nombre AS especialidad,
    pr.anos_exp,
    pr.email AS email_profesional
FROM profesional pr
LEFT JOIN especialidad e ON pr.id_especialidad = e.id_especialidad
WHERE pr.activo = 1;

-- SELECT 2.2: Horario y agenda laboral por día de la semana de todos los profesionales activos.
SELECT 
    pr.nom_completo AS profesional,
    CASE hp.dia_semana
        WHEN 1 THEN 'Lunes' WHEN 2 THEN 'Martes' WHEN 3 THEN 'Miércoles'
        WHEN 4 THEN 'Jueves' WHEN 5 THEN 'Viernes' WHEN 6 THEN 'Sábado' WHEN 7 THEN 'Domingo'
    END AS dia_laborable,
    hp.hora_inicio,
    hp.hora_fin,
    hp.tipo AS tipo_jornada
FROM profesional pr
JOIN horario_profesional hp ON pr.id_profesional = hp.id_profesional
WHERE pr.activo = 1 AND hp.tipo = 'laboral'
ORDER BY pr.id_profesional, hp.dia_semana, hp.hora_inicio;

-- SELECT 3.1: Resumen de macronutrientes y calorías agregadas por tipo de comida para un día específico de un plan.
SELECT 
    pd.id_plan,
    CONCAT(p.nombre, ' ', p.apellidos) AS paciente,
    md.dia_semana,
    c.tipo AS franja_comida,
    c.nombre AS menu_propuesto,
    SUM(a.calorias) AS total_kcal,
    SUM(a.proteinas) AS total_proteinas_g,
    SUM(a.carbohidratos) AS total_carbohidratos_g,
    SUM(a.grasas) AS total_grasas_g
FROM plan_dietetico pd
JOIN paciente p ON pd.id_paciente = p.id_paciente
JOIN menu_diario md ON pd.id_plan = md.id_plan
JOIN comida c ON md.id_menu = c.id_menu
JOIN alimento a ON c.id_comida = a.id_comida
WHERE pd.estado = 'activo'
GROUP BY pd.id_plan, p.nombre, p.apellidos, md.dia_semana, c.tipo, c.nombre
ORDER BY pd.id_plan, FIELD(md.dia_semana, 'lunes', 'martes', 'miercoles', 'jueves', 'viernes', 'sabado', 'domingo'), c.tipo;

-- SELECT 3.2: Desglose pormenorizado de los alimentos individuales que componen un menú concreto dentro de un plan.
SELECT 
    pd.id_plan,
    md.dia_semana,
    c.tipo AS tipo_comida,
    a.nombre AS alimento,
    a.cantidad,
    a.unidad,
    a.calorias AS kcal_alimento
FROM plan_dietetico pd
JOIN menu_diario md ON pd.id_plan = md.id_plan
JOIN comida c ON md.id_menu = c.id_menu
JOIN alimento a ON c.id_comida = a.id_comida
WHERE pd.id_plan = 1 -- Filtrable dinámicamente por la aplicación
ORDER BY md.dia_numero, c.tipo;

-- SELECT 4.1: Control de citas agendadas con su modalidad y estado del cobro de la factura.
SELECT 
    c.id_cita,
    c.fecha_cita,
    c.hora_cita,
    pac.nombre AS nombre_paciente,
    prof.nom_completo AS medico_profesional,
    c.modalidad,
    c.estado_cita,
    IFNULL(f.importe, 0.00) AS importe_factura,
    IFNULL(f.estado_pago, 'sin_facturar') AS estado_pago
FROM cita c
JOIN paciente pac ON c.id_paciente = pac.id_paciente
JOIN profesional prof ON c.id_profesional = prof.id_profesional
LEFT JOIN factura f ON c.id_cita = f.id_cita
ORDER BY c.fecha_cita DESC, c.hora_cita DESC;

-- SELECT 4.2: Resumen financiero totalizador de los ingresos percibidos desglosados según el método de pago utilizado.
SELECT 
    f.metodo_pago,
    COUNT(f.id_factura) AS volumen_transacciones,
    SUM(f.importe) AS total_recaudado
FROM factura f
WHERE f.estado_pago = 'pagada'
GROUP BY f.metodo_pago
ORDER BY total_recaudado DESC;

-- SELECT 5.1: Historial analítico de la evolución del peso y el IMC autocalculado de los pacientes.
SELECT 
    p.id_paciente,
    CONCAT(p.nombre, ' ', p.apellidos) AS paciente,
    prg.fecha AS fecha_control,
    prg.peso AS peso_kg,
    prg.imc,
    prg.observaciones
FROM progreso prg
JOIN paciente p ON prg.id_paciente = p.id_paciente
ORDER BY p.id_paciente, prg.fecha ASC;

-- SELECT 5.2: Consulta avanzada de registros métricos del panel de control de evolución física corporativa.
SELECT 
    p.nombre AS paciente,
    rm.fecha_calculo,
    rm.tipo_metrica,
    rm.valor,
    pan.fecha_actualizacion AS ultima_sincronizacion
FROM registro_metrica rm
JOIN panel_progreso pan ON rm.id_panel = pan.id_panel
JOIN paciente p ON pan.id_paciente = p.id_paciente
ORDER BY p.nombre, rm.fecha_calculo DESC, rm.tipo_metrica;


-- ------------------------------------------------------------
-- SUBSISTEMA 6: EXTRACCIÓN GLOBAL ANALÍTICA (INFORMES)
-- ------------------------------------------------------------

-- SELECT 6.1: Auditoría y trazabilidad completa de informes del sistema y sus entidades vinculadas.
SELECT 
    inf.id_informe,
    inf.fecha_gen AS fecha_generacion,
    inf.tipo AS tipo_informe,
    p.nombre AS paciente_asociado,
    pr.nom_completo AS profesional_creador
FROM informe inf
LEFT JOIN paciente p ON inf.id_paciente = p.id_paciente
LEFT JOIN profesional pr ON inf.id_profesional = pr.id_profesional
ORDER BY inf.fecha_gen DESC;

-- SELECT 6.2 (NUEVA): Métrica de rendimiento de negocio: Citas realizadas frente a canceladas por profesional.
SELECT 
    pr.nom_completo AS profesional,
    COUNT(CASE WHEN c.estado_cita = 'completada' THEN 1 END) AS citas_exitosas,
    COUNT(CASE WHEN c.estado_cita = 'cancelada' THEN 1 END) AS citas_anuladas,
    COUNT(c.id_cita) AS citas_totales
FROM profesional pr
JOIN cita c ON pr.id_profesional = c.id_profesional
GROUP BY pr.id_profesional, pr.nom_completo
ORDER BY citas_exitosas DESC;

-- SELECT 6.3 (NUEVA): Alertas de control de salud: Pacientes con alergias graves y planes activos.
SELECT 
    p.nombre AS paciente_nombre,
    p.telefono AS contacto,
    a.nombre AS sustancia_peligrosa,
    pd.objetivo AS tratamiento_actual
FROM paciente p
JOIN paciente_alergia pa ON p.id_paciente = pa.id_paciente
JOIN alergia a ON pa.id_alergia = a.id_alergia
JOIN plan_dietetico pd ON p.id_paciente = pd.id_paciente
WHERE pa.severidad = 'grave' AND pd.estado = 'activo';