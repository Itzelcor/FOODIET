-- =============================================================================
-- FOODIET — Subsistema: Gestión del Equipo Profesional
-- Base de datos: FooDiet
-- Motor: MySQL 8+
-- Autores: 1º DAW — Proyecto Intermodular NutriPlus
-- =============================================================================

CREATE DATABASE IF NOT EXISTS FooDiet
  CHARACTER SET utf8mb4
  COLLATE utf8mb4_unicode_ci;

USE FooDiet;

-- =============================================================================
-- NOTA SOBRE ORDEN DE CREACIÓN
-- Las tablas con FK se crean después de las tablas que referencian.
-- Orden: profesionales → agenda → asignaciones_paciente → sustituciones
-- =============================================================================


-- -----------------------------------------------------------------------------
-- Tabla: profesionales
-- Almacena los datos de cada miembro del equipo de la clínica.
-- El campo 'rol' controla qué puede ver y hacer cada profesional en el sistema.
-- -----------------------------------------------------------------------------
CREATE TABLE IF NOT EXISTS profesionales (
    id           INT           NOT NULL AUTO_INCREMENT,
    nombre       VARCHAR(100)  NOT NULL,
    apellidos    VARCHAR(100)  NOT NULL,
    especialidad VARCHAR(100),
    formacion    VARCHAR(200),
    email        VARCHAR(150)  NOT NULL,
    telefono     VARCHAR(20),
    rol          ENUM('DIRECTIVO', 'NUTRICIONISTA', 'ADMINISTRATIVO') NOT NULL,

    CONSTRAINT pk_profesionales PRIMARY KEY (id),
    CONSTRAINT uq_profesionales_email UNIQUE (email)
);


-- -----------------------------------------------------------------------------
-- Tabla: agenda
-- Registra los eventos de cada profesional: consultas, vacaciones y 
-- sustituciones. Un profesional puede tener muchos eventos (1:N).
-- -----------------------------------------------------------------------------
CREATE TABLE IF NOT EXISTS agenda (
    id             INT          NOT NULL AUTO_INCREMENT,
    id_profesional INT          NOT NULL,
    fecha          DATE         NOT NULL,
    tipo_evento    ENUM('CONSULTA', 'VACACIONES', 'SUSTITUCION') NOT NULL,
    descripcion    VARCHAR(300),

    CONSTRAINT pk_agenda PRIMARY KEY (id),
    CONSTRAINT fk_agenda_profesional
        FOREIGN KEY (id_profesional)
        REFERENCES profesionales (id)
        ON DELETE CASCADE
        ON UPDATE CASCADE
);


-- -----------------------------------------------------------------------------
-- Tabla: asignaciones_paciente
-- Relaciona cada paciente con el profesional que lleva su caso de forma estable.
-- Actúa como tabla intermedia de la relación N:M entre profesionales y pacientes.
--
-- TODO: Cuando tu compañero (Dani) te pase su tabla de pacientes, completa:
--   1. Descomenta la línea FOREIGN KEY de id_paciente
--   2. Sustituye 'pacientes' por el nombre exacto de su tabla
--   3. Sustituye 'id' por el nombre exacto de su clave primaria si fuera distinto
-- -----------------------------------------------------------------------------
CREATE TABLE IF NOT EXISTS asignaciones_paciente (
    id               INT  NOT NULL AUTO_INCREMENT,
    id_profesional   INT  NOT NULL,
    id_paciente      INT  NOT NULL,     -- FK pendiente → tabla de Dani
    fecha_asignacion DATE NOT NULL,

    CONSTRAINT pk_asignaciones PRIMARY KEY (id),
    CONSTRAINT fk_asignaciones_profesional
        FOREIGN KEY (id_profesional)
        REFERENCES profesionales (id)
        ON DELETE CASCADE
        ON UPDATE CASCADE

    -- -------------------------------------------------------------------------
    -- TODO: Descomentar cuando Dani confirme el nombre de su tabla y su PK
    -- ,CONSTRAINT fk_asignaciones_paciente
    --     FOREIGN KEY (id_paciente)
    --     REFERENCES pacientes (id)   -- <-- cambiar 'pacientes' si es necesario
    --     ON DELETE CASCADE
    --     ON UPDATE CASCADE
    -- -------------------------------------------------------------------------
);


-- -----------------------------------------------------------------------------
-- Tabla: sustituciones
-- Registra los períodos en que un profesional cubre la ausencia de otro.
-- Contiene dos FK a la misma tabla profesionales (auto-relación):
--   - id_prof_ausente   → el profesional que se ausenta
--   - id_prof_sustituto → el profesional que lo cubre
-- -----------------------------------------------------------------------------
CREATE TABLE IF NOT EXISTS sustituciones (
    id                  INT          NOT NULL AUTO_INCREMENT,
    id_prof_ausente     INT          NOT NULL,
    id_prof_sustituto   INT          NOT NULL,
    fecha_inicio        DATE         NOT NULL,
    fecha_fin           DATE         NOT NULL,
    motivo              VARCHAR(200),

    CONSTRAINT pk_sustituciones PRIMARY KEY (id),
    CONSTRAINT fk_sust_ausente
        FOREIGN KEY (id_prof_ausente)
        REFERENCES profesionales (id)
        ON DELETE CASCADE
        ON UPDATE CASCADE,
    CONSTRAINT fk_sust_sustituto
        FOREIGN KEY (id_prof_sustituto)
        REFERENCES profesionales (id)
        ON DELETE CASCADE
        ON UPDATE CASCADE
);


-- =============================================================================
-- DATOS DE PRUEBA
-- Insertar datos mínimos para verificar que el modelo funciona correctamente.
-- =============================================================================

-- Profesionales de ejemplo
INSERT INTO profesionales (nombre, apellidos, especialidad, formacion, email, telefono, rol)
VALUES
    ('Laura',   'Martínez López',  'Nutrición clínica',    'Grado en Dietética',            'laura.martinez@foodiet.es',  '600111222', 'DIRECTIVO'),
    ('Carlos',  'García Ruiz',     'Dietética deportiva',  'Grado en Ciencias de la Salud', 'carlos.garcia@foodiet.es',   '600333444', 'NUTRICIONISTA'),
    ('Ana',     'Pérez Sánchez',   'Nutrición infantil',   'Máster en Nutrición',           'ana.perez@foodiet.es',       '600555666', 'NUTRICIONISTA'),
    ('Miguel',  'Torres Blanco',   NULL,                   NULL,                            'miguel.torres@foodiet.es',   '600777888', 'ADMINISTRATIVO');

-- Eventos de agenda de ejemplo
INSERT INTO agenda (id_profesional, fecha, tipo_evento, descripcion)
VALUES
    (2, '2025-06-02', 'CONSULTA',    'Primera consulta paciente nuevo'),
    (2, '2025-06-10', 'VACACIONES',  'Vacaciones de verano — semana 1'),
    (3, '2025-06-02', 'CONSULTA',    'Seguimiento mensual'),
    (4, '2025-06-03', 'SUSTITUCION', 'Cobertura de Carlos durante sus vacaciones');

-- Asignaciones de pacientes de ejemplo
-- (id_paciente pendiente de confirmar con Dani — se usan valores ficticios)
INSERT INTO asignaciones_paciente (id_profesional, id_paciente, fecha_asignacion)
VALUES
    (2, 1, '2025-01-15'),
    (2, 2, '2025-02-03'),
    (3, 3, '2025-01-20'),
    (3, 4, '2025-03-10');

-- Sustituciones de ejemplo
INSERT INTO sustituciones (id_prof_ausente, id_prof_sustituto, fecha_inicio, fecha_fin, motivo)
VALUES
    (2, 3, '2025-06-10', '2025-06-17', 'Vacaciones'),
    (3, 2, '2025-07-01', '2025-07-05', 'Baja médica');


-- =============================================================================
-- CONSULTAS DE VERIFICACIÓN
-- Ejecutar estas consultas para comprobar que el modelo funciona.
-- =============================================================================

-- Ver todos los profesionales con su rol
-- SELECT id, nombre, apellidos, rol FROM profesionales;

-- Ver la agenda completa ordenada por fecha
-- SELECT p.nombre, a.fecha, a.tipo_evento, a.descripcion
-- FROM agenda a
-- JOIN profesionales p ON a.id_profesional = p.id
-- ORDER BY a.fecha;

-- Ver qué pacientes tiene asignados cada profesional
-- SELECT p.nombre, p.apellidos, ap.id_paciente, ap.fecha_asignacion
-- FROM asignaciones_paciente ap
-- JOIN profesionales p ON ap.id_profesional = p.id;

-- Ver las sustituciones activas con nombres de ambos profesionales
-- SELECT
--     ausente.nombre   AS profesional_ausente,
--     sust.nombre      AS profesional_sustituto,
--     s.fecha_inicio,
--     s.fecha_fin,
--     s.motivo
-- FROM sustituciones s
-- JOIN profesionales ausente ON s.id_prof_ausente   = ausente.id
-- JOIN profesionales sust    ON s.id_prof_sustituto = sust.id;
