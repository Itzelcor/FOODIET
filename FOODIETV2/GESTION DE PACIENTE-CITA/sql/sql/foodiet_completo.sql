-- ============================================================
-- FOODIET - SISTEMA INTEGRAL PARA CONSULTAS DE DIETÉTICA
-- Script limpio: solo creación de tablas, relaciones e inserción
-- ============================================================

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
    email VARCHAR(100),
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
    email VARCHAR(100),
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
