-- ============================================================
-- FOODIET - SISTEMA INTEGRAL PARA CONSULTAS DE DIETÉTICA
-- Script completo: Creación, inserción, consultas, vistas y procedimientos
-- ============================================================

-- ==================== CREACIÓN DE TABLAS ====================

CREATE DATABASE IF NOT EXISTS Foodiet;
USE Foodiet;

-- Tabla de usuarios (autenticación y roles)
CREATE TABLE usuarios (
    id_usuario INT AUTO_INCREMENT PRIMARY KEY,
    nombre_usuario VARCHAR(50) UNIQUE NOT NULL,
    email VARCHAR(100) UNIQUE NOT NULL,
    contraseña_hash VARCHAR(255) NOT NULL,
    rol ENUM('administrador', 'nutricionista', 'paciente') NOT NULL DEFAULT 'paciente',
    fecha_registro DATETIME DEFAULT CURRENT_TIMESTAMP,
    activo BOOLEAN DEFAULT TRUE
);

-- Tabla de pacientes
CREATE TABLE pacientes (
    id_paciente INT AUTO_INCREMENT PRIMARY KEY,
    id_usuario INT UNIQUE NOT NULL,
    nombre VARCHAR(50) NOT NULL,
    apellido VARCHAR(50) NOT NULL,
    edad INT NOT NULL CHECK (edad > 0 AND edad < 150),
    peso DECIMAL(5,2) NOT NULL CHECK (peso > 0),
    altura DECIMAL(4,2) NOT NULL CHECK (altura > 0 AND altura < 3),
    telefono VARCHAR(20),
    direccion VARCHAR(255),
    historial_medico TEXT,
    objetivos_nutricionales TEXT,
    tipo_paciente ENUM('Joven', 'Adulto', 'Jubilado') NOT NULL DEFAULT 'Adulto',
    fecha_registro DATETIME DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (id_usuario) REFERENCES usuarios(id_usuario) ON DELETE CASCADE
);

-- Tabla de profesionales (nutricionistas)
CREATE TABLE profesionales (
    id_profesional INT AUTO_INCREMENT PRIMARY KEY,
    id_usuario INT UNIQUE NOT NULL,
    nombre VARCHAR(50) NOT NULL,
    apellido VARCHAR(50) NOT NULL,
    especialidad VARCHAR(100) NOT NULL,
    años_experiencia INT NOT NULL CHECK (años_experiencia >= 0),
    horario VARCHAR(255),
    telefono VARCHAR(20),
    activo BOOLEAN DEFAULT TRUE,
    FOREIGN KEY (id_usuario) REFERENCES usuarios(id_usuario) ON DELETE CASCADE
);

-- Tabla de citas (presenciales y online)
CREATE TABLE citas (
    id_cita INT AUTO_INCREMENT PRIMARY KEY,
    id_paciente INT NOT NULL,
    id_profesional INT NOT NULL,
    fecha_cita DATE NOT NULL,
    hora_cita TIME NOT NULL,
    modalidad ENUM('presencial', 'online') NOT NULL,
    estado ENUM('pendiente', 'confirmada', 'completada', 'cancelada') NOT NULL DEFAULT 'pendiente',
    motivo VARCHAR(500),
    enlace_online VARCHAR(255),
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (id_paciente) REFERENCES pacientes(id_paciente) ON DELETE CASCADE,
    FOREIGN KEY (id_profesional) REFERENCES profesionales(id_profesional) ON DELETE CASCADE
);

-- Tabla de pagos y facturación
CREATE TABLE pagos (
    id_pago INT AUTO_INCREMENT PRIMARY KEY,
    id_cita INT NOT NULL UNIQUE,
    monto DECIMAL(8,2) NOT NULL CHECK (monto > 0),
    metodo_pago ENUM('efectivo', 'tarjeta', 'transferencia', 'bizum') NOT NULL,
    estado_pago ENUM('pendiente', 'pagado', 'reembolsado') NOT NULL DEFAULT 'pendiente',
    fecha_pago DATETIME,
    numero_factura VARCHAR(50) UNIQUE,
    FOREIGN KEY (id_cita) REFERENCES citas(id_cita) ON DELETE CASCADE
);

-- Tabla de planes de alimentación
CREATE TABLE planes_alimentacion (
    id_plan INT AUTO_INCREMENT PRIMARY KEY,
    id_paciente INT NOT NULL,
    id_profesional INT NOT NULL,
    fecha_inicio DATE NOT NULL,
    fecha_fin DATE,
    objetivo VARCHAR(255),
    descripcion TEXT,
    calorias_diarias INT,
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (id_paciente) REFERENCES pacientes(id_paciente) ON DELETE CASCADE,
    FOREIGN KEY (id_profesional) REFERENCES profesionales(id_profesional) ON DELETE CASCADE
);

-- Tabla de seguimiento de avances
CREATE TABLE seguimiento_planes (
    id_seguimiento INT AUTO_INCREMENT PRIMARY KEY,
    id_plan INT NOT NULL,
    fecha DATE NOT NULL,
    peso DECIMAL(5,2),
    imc DECIMAL(4,2),
    observaciones TEXT,
    FOREIGN KEY (id_plan) REFERENCES planes_alimentacion(id_plan) ON DELETE CASCADE
);

-- ==================== INSERCIÓN DE DATOS ====================

-- Usuarios
INSERT INTO usuarios (nombre_usuario, email, contraseña_hash, rol) VALUES
('admin', 'admin@foodiet.com', SHA2('admin123', 256), 'administrador'),
('dietista1', 'dietista1@foodiet.com', SHA2('pass123', 256), 'nutricionista'),
('dietista2', 'dietista2@foodiet.com', SHA2('pass123', 256), 'nutricionista'),
('dietista3', 'dietista3@foodiet.com', SHA2('pass123', 256), 'nutricionista'),
('ana.lopez', 'ana.lopez@email.com', SHA2('pass123', 256), 'paciente'),
('carlos.perez', 'carlos.perez@email.com', SHA2('pass123', 256), 'paciente'),
('manuel.garcia', 'manuel.garcia@email.com', SHA2('pass123', 256), 'paciente');

-- Profesionales
INSERT INTO profesionales (id_usuario, nombre, apellido, especialidad, años_experiencia, horario, telefono) VALUES
(2, 'Daniel', 'Dimitrov', 'Nutrición Deportiva', 14, 'Lunes-Viernes 9:00-18:00', '612345678'),
(3, 'Andrei', 'Veres', 'Nutrición Clínica', 12, 'Lunes-Viernes 10:00-19:00', '623456789'),
(4, 'Sergio', 'Gonzalez', 'Dietética General', 16, 'Lunes-Viernes 8:00-17:00', '634567890');

-- Pacientes
INSERT INTO pacientes (id_usuario, nombre, apellido, edad, peso, altura, telefono, historial_medico, objetivos_nutricionales, tipo_paciente) VALUES
(5, 'Ana', 'López', 19, 58.00, 1.65, '645678901', 'Sin antecedentes', 'Mejorar hábitos alimenticios', 'Joven'),
(6, 'Carlos', 'Pérez', 42, 82.00, 1.78, '656789012', 'Colesterol elevado', 'Reducir peso y colesterol', 'Adulto'),
(7, 'Manuel', 'García', 71, 74.00, 1.70, '667890123', 'Hipertensión', 'Controlar presión arterial', 'Jubilado');

-- Citas
INSERT INTO citas (id_paciente, id_profesional, fecha_cita, hora_cita, modalidad, estado, motivo) VALUES
(1, 1, '2026-02-10', '10:00:00', 'presencial', 'completada', 'Consulta inicial de nutrición'),
(2, 3, '2026-12-12', '16:30:00', 'online', 'pendiente', 'Seguimiento de plan alimenticio'),
(1, 2, '2026-03-05', '11:00:00', 'online', 'confirmada', 'Revisión de progreso'),
(3, 1, '2026-04-15', '09:30:00', 'presencial', 'pendiente', 'Control de hipertensión mediante dieta');

-- Pagos
INSERT INTO pagos (id_cita, monto, metodo_pago, estado_pago, fecha_pago, numero_factura) VALUES
(1, 50.00, 'tarjeta', 'pagado', '2026-02-10 10:15:00', 'FAC-2026-001'),
(2, 40.00, 'bizum', 'pendiente', NULL, NULL),
(3, 45.00, 'tarjeta', 'pagado', '2026-03-05 11:10:00', 'FAC-2026-002');

-- Planes de alimentación
INSERT INTO planes_alimentacion (id_paciente, id_profesional, fecha_inicio, fecha_fin, objetivo, descripcion, calorias_diarias) VALUES
(1, 1, '2026-02-10', '2026-05-10', 'Mejora hábitos alimenticios', 'Plan equilibrado rico en frutas y verduras', 2000),
(2, 3, '2025-12-12', '2026-03-12', 'Reducción de peso', 'Dieta hipocalórica controlada', 1800);

-- Seguimiento
INSERT INTO seguimiento_planes (id_plan, fecha, peso, imc, observaciones) VALUES
(1, '2026-02-10', 58.00, 21.30, 'Peso inicial dentro del rango saludable'),
(1, '2026-03-10', 57.50, 21.12, 'Ligera reducción, buen progreso'),
(2, '2025-12-12', 82.00, 25.88, 'Sobrepeso leve, iniciar plan'),
(2, '2026-01-12', 80.50, 25.41, 'Primera reducción exitosa');

-- ==================== CONSULTAS COMPLEJAS ====================

-- 1. Listar todas las citas con datos del paciente y profesional (JOIN múltiple)
SELECT 
    c.id_cita AS codigo,
    CONCAT(p.nombre, ' ', p.apellido) AS paciente,
    CONCAT(pr.nombre, ' ', pr.apellido) AS nutricionista,
    pr.especialidad,
    c.fecha_cita,
    c.hora_cita,
    c.modalidad,
    c.estado
FROM citas c
JOIN pacientes p ON c.id_paciente = p.id_paciente
JOIN profesionales pr ON c.id_profesional = pr.id_profesional
ORDER BY c.fecha_cita DESC;

-- 2. Ingresos totales por nutricionista (agregación con GROUP BY)
SELECT 
    CONCAT(pr.nombre, ' ', pr.apellido) AS nutricionista,
    COUNT(c.id_cita) AS citas_realizadas,
    SUM(p.monto) AS ingresos_totales,
    AVG(p.monto) AS ingreso_medio_por_cita
FROM profesionales pr
LEFT JOIN citas c ON pr.id_profesional = c.id_profesional AND c.estado = 'completada'
LEFT JOIN pagos p ON c.id_cita = p.id_cita AND p.estado_pago = 'pagado'
GROUP BY pr.id_profesional
ORDER BY ingresos_totales DESC;

-- 3. Historial completo de un paciente con evolución de peso
SELECT 
    CONCAT(p.nombre, ' ', p.apellido) AS paciente,
    pl.fecha_inicio,
    pl.objetivo,
    s.fecha AS fecha_control,
    s.peso,
    s.imc,
    s.observaciones
FROM pacientes p
JOIN planes_alimentacion pl ON p.id_paciente = pl.id_paciente
JOIN seguimiento_planes s ON pl.id_plan = s.id_plan
ORDER BY p.id_paciente, s.fecha;

-- 4. Citas próximas (próximos 7 días)
SELECT 
    CONCAT(p.nombre, ' ', p.apellido) AS paciente,
    CONCAT(pr.nombre, ' ', pr.apellido) AS nutricionista,
    c.fecha_cita,
    c.hora_cita,
    c.modalidad
FROM citas c
JOIN pacientes p ON c.id_paciente = p.id_paciente
JOIN profesionales pr ON c.id_profesional = pr.id_profesional
WHERE c.estado IN ('pendiente', 'confirmada')
  AND c.fecha_cita BETWEEN CURDATE() AND DATE_ADD(CURDATE(), INTERVAL 7 DAY)
ORDER BY c.fecha_cita, c.hora_cita;

-- 5. Porcentaje de citas por modalidad
SELECT 
    modalidad,
    COUNT(*) AS total_citas,
    ROUND(COUNT(*) * 100.0 / (SELECT COUNT(*) FROM citas), 1) AS porcentaje
FROM citas
GROUP BY modalidad;

-- ==================== VISTAS ====================

-- Vista: resumen de pacientes con su última cita
CREATE VIEW VistaResumenPacientes AS
SELECT 
    p.id_paciente,
    CONCAT(p.nombre, ' ', p.apellido) AS nombre_completo,
    p.edad,
    p.tipo_paciente,
    ROUND(p.peso / POW(p.altura, 2), 2) AS imc_actual,
    COUNT(c.id_cita) AS total_citas,
    MAX(c.fecha_cita) AS ultima_cita
FROM pacientes p
LEFT JOIN citas c ON p.id_paciente = c.id_paciente
GROUP BY p.id_paciente;

-- Vista: ingresos mensuales
CREATE VIEW VistaIngresosMensuales AS
SELECT 
    YEAR(fecha_pago) AS año,
    MONTH(fecha_pago) AS mes,
    COUNT(*) AS total_pagos,
    SUM(monto) AS ingresos_totales
FROM pagos
WHERE estado_pago = 'pagado'
GROUP BY YEAR(fecha_pago), MONTH(fecha_pago)
ORDER BY año DESC, mes DESC;
