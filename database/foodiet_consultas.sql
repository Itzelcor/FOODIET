use FooDiet;

-- ============================================================================
-- 1. LISTA DE PACIENTES ORDENADA POR ALTURA (Básica)
-- Obtiene los nombres, apellidos y altura de todos los pacientes varones, 
-- ordenados de mayor a menor altura.
-- ============================================================================
SELECT nombre, apellidos, altura 
FROM paciente
WHERE sexo = 'M'
ORDER BY altura DESC;


-- ============================================================================
-- 2. CANTIDAD DE PROFESIONALES POR ESPECIALIDAD (Agrupación)
-- Cuenta cuántos profesionales activos hay en cada especialidad, mostrando 
-- solo aquellas áreas que tengan al menos un profesional.
-- ============================================================================
SELECT id_especialidad, COUNT(id_profesional) AS total_profesionales
FROM profesional
WHERE activo = 1
GROUP BY id_especialidad
ORDER BY total_profesionales DESC;


-- ============================================================================
-- 3. PACIENTES CON ALERGIAS GRAVES (Join N:M)
-- Muestra el nombre del paciente y el nombre de la aleraia únicamente para 
-- los casos donde la severidad sea 'grave'.
-- ============================================================================
SELECT p.nombre, p.apellidos, a.nombre AS nombre_alergia
FROM paciente p
INNER JOIN paciente_alergia pa ON p.id_paciente = pa.id_paciente
INNER JOIN alergia a ON pa.id_alergia = a.id_alergia
WHERE pa.severidad = 'grave';


-- ============================================================================
-- 4. PLANES DIETÉTICOS CON MÁS DE 2500 CALORÍAS (Filtrado y orden)
-- Muestra los objetivos y las calorías de los planes alimenticios más 
-- calóricos que están actualmente activos.
-- ============================================================================
SELECT objetivo, calorias_diarias, fecha_inicio
FROM plan_dietetico
WHERE estado = 'activo' AND calorias_diarias > 2500
ORDER BY calorias_diarias DESC
LIMIT 5;


-- ============================================================================
-- 5. FACTURAS PENDIENTES DE ALTO IMPORTE (Restricción y orden)
-- Selecciona las facturas que aún no se han pagado y cuyo importe supera 
-- los 100 euros, mostrando primero las más caras.
-- ============================================================================
SELECT id_factura, id_paciente, importe, fecha_emision
FROM factura
WHERE estado_pago = 'pendiente' AND importe > 100.00
ORDER BY importe DESC;


-- ============================================================================
-- 6. ALIMENTOS MÁS CALÓRICOS POR CADA TOMA (Group By + Having)
-- Busca los tipos de comida (desayuno, cena, etc.) que promedien más de 
-- 500 calorías entre sus alimentos individuales.
-- ============================================================================
SELECT c.tipo, AVG(a.calorias) AS promedio_calorias
FROM comida c
INNER JOIN alimento a ON c.id_comida = a.id_comida
GROUP BY c.tipo
HAVING AVG(a.calorias) > 500
ORDER BY promedio_calorias DESC;


-- ============================================================================
-- 7. CITAS AGENDADAS PARA UN DÍA ESPECÍFICO (Join estándar)
-- Muestra la hora, el nombre del paciente y el del profesional para todas 
-- las citas programadas en una fecha concreta.
-- ============================================================================
SELECT c.hora_cita, p.nombre AS paciente, pr.nom_completo AS profesional, c.modalidad
FROM cita c
INNER JOIN paciente p ON c.id_paciente = p.id_paciente
INNER JOIN profesional pr ON c.id_profesional = pr.id_profesional
WHERE c.fecha_cita = '2026-06-20' AND c.estado_cita = 'pendiente'
ORDER BY c.hora_cita ASC;


-- ============================================================================
-- 8. PACIENTES QUE HAN SUPERADO EL PESO MEDIO (Subconsulta en Where)
-- Selecciona a los pacientes cuyos registros de progreso individuales hayan 
-- marcado un peso mayor que el peso medio de toda la clínica.
-- ============================================================================
SELECT DISTINCT id_paciente, peso, fecha
FROM progreso
WHERE peso > (SELECT AVG(peso) FROM progreso)
ORDER BY peso DESC;


-- ============================================================================
-- 9. TOTAL DE DINERO RECAUDADO POR MÉTODO DE PAGO (Agrupación)
-- Suma el importe total de las facturas que ya han sido pagadas, 
-- agrupándolas por el método de pago utilizado.
-- ============================================================================
SELECT metodo_pago, SUM(importe) AS total_recaudado
FROM factura
WHERE estado_pago = 'pagada'
GROUP BY metodo_pago
ORDER BY total_recaudado DESC;


-- ============================================================================
-- 10. PACIENTES CON MÁS DE 3 CITAS CANCELADAS (Group By + Having)
-- Identifica a los pacientes que acumulen más de 3 citas en estado 'cancelada'.
-- ============================================================================
SELECT id_paciente, COUNT(id_cita) AS citas_canceladas
FROM cita
WHERE estado_cita = 'cancelada'
GROUP BY id_paciente
HAVING COUNT(id_cita) > 3
ORDER BY citas_canceladas DESC;


-- ============================================================================
-- 11. ALIMENTOS ASIGNADOS A UN PACIENTE ESPECÍFICO (Triple Join)
-- Muestra la lista de alimentos que tiene asignados el id_paciente = 5 
-- en sus menús diarios activos.
-- ============================================================================
SELECT a.nombre, a.cantidad, a.unidad, c.tipo AS momento_comida
FROM plan_dietetico pd
INNER JOIN menu_diario md ON pd.id_plan = md.id_plan
INNER JOIN comida c ON md.id_menu = c.id_menu
INNER JOIN alimento a ON c.id_comida = a.id_comida
WHERE pd.id_paciente = 5 AND pd.estado = 'activo';


-- ============================================================================
-- 12. PROFESIONALES QUE TRABAJAN LOS LUNES POR LA MAÑANA (Filtrado de tiempo)
-- Obtiene los nombres de los profesionales que tienen turnos laborales los 
-- lunes (día 1) antes de las 14:00 de la tarde.
-- ============================================================================
SELECT DISTINCT p.nom_completo, hp.hora_inicio, hp.hora_fin
FROM profesional p
INNER JOIN horario_profesional hp ON p.id_profesional = hp.id_profesional
WHERE hp.dia_semana = 1 AND hp.tipo = 'laboral' AND hp.hora_inicio < '14:00:00'
ORDER BY hp.hora_inicio ASC;


-- ============================================================================
-- 13. MÉTRICAS FÍSICAS MÁS RECIENTES QUE ESTÁN EN RIESGO (Subconsulta + In)
-- Busca los registros de métricas de tipo 'grasa_corporal' o 'imc' cuyo valor 
-- supera el umbral de 30, ordenados por fecha.
-- ============================================================================
SELECT id_registro, tipo_metrica, valor, fecha_calculo
FROM registro_metrica
WHERE tipo_metrica IN ('imc', 'grasa_corporal') AND valor > 30.00
ORDER BY fecha_calculo DESC
LIMIT 10;
