-- ============================================================
--  FooDiet – Base de Datos Corregida y Documentada
--  IES Font de San Lluis · 1 DAW PID 2025-2026
--  Revisión: Mayo 2025
-- ============================================================
-- CAMBIOS GLOBALES APLICADOS:
--   · Naming unificado a snake_case minúsculas en todas las tablas
--   · VARCHAR ajustados a longitudes realistas
--   · NOT NULL aplicado en todos los campos obligatorios
--   · CHECK añadido donde se enumeran valores posibles
--   · ON DELETE / ON UPDATE definidos en todas las FK
--   · Tablas nuevas: usuario, alergia, paciente_alergia,
--     menu_diario, factura, horario_profesional
--   · Campos añadidos: modalidad en citas, altura/sexo/email
--     en paciente, fecha_fin en plan_dietetico,
--     macronutrientes en alimento, id_paciente en panel_progreso
-- ============================================================

DROP DATABASE IF EXISTS FooDiet;
CREATE DATABASE FooDiet
    CHARACTER SET utf8mb4
    COLLATE utf8mb4_spanish_ci;
USE FooDiet;


-- ============================================================
-- BLOQUE 0 · AUTENTICACIÓN Y ROLES
-- ============================================================
-- Tabla nueva: no existía en la versión original.
-- El diagrama de clases tenía la clase Usuario con login() y
-- logout() pero no había tabla correspondiente en el SQL.
-- Necesaria para el módulo de autenticación (requisito del proyecto).
-- ============================================================

CREATE TABLE usuario (
    id_usuario   INT          NOT NULL AUTO_INCREMENT,
    email        VARCHAR(100) NOT NULL,               -- ampliado de 30 a 100
    password_hash VARCHAR(255) NOT NULL,              -- hash bcrypt, nunca plano
    rol          VARCHAR(20)  NOT NULL,
    activo       TINYINT(1)   NOT NULL DEFAULT 1,
    fecha_alta   DATE         NOT NULL,
    CONSTRAINT PK_usuario      PRIMARY KEY (id_usuario),
    CONSTRAINT UQ_usuario_email UNIQUE (email),
    CONSTRAINT CHK_usuario_rol  CHECK (rol IN ('paciente', 'nutricionista', 'entrenador', 'dietista', 'administrativo'))
);


-- ============================================================
-- BLOQUE 1 · PACIENTES
-- ============================================================
-- Correcciones aplicadas:
--   · "dirección" → "direccion" (sin tilde, evita errores de encoding)
--   · dni NOT NULL (es el identificador real del paciente)
--   · Añadidos: telefono, email, sexo, altura, fecha_registro
--   · altura necesaria para calcular IMC (objetivo del proyecto)
--   · id_usuario FK para vincular con el sistema de login
--   · tipo_paciente gestiona la herencia del diagrama de clases
--     (PacienteJubilado, PacienteAdulto, PacienteJoven) mediante
--     herencia de tabla única (Single Table Inheritance)
-- ============================================================

CREATE TABLE paciente (
    id_paciente     INT          NOT NULL AUTO_INCREMENT,
    id_usuario      INT          NOT NULL,
    dni             VARCHAR(9)   NOT NULL,
    nombre          VARCHAR(50)  NOT NULL,             
    apellidos       VARCHAR(100) NOT NULL,             
    telefono        VARCHAR(15)  NOT NULL,             
    email           VARCHAR(100) NOT NULL,            
    direccion       VARCHAR(150) NOT NULL,             
    fecha_nac       DATE         NOT NULL,
    sexo            CHAR(1)      NOT NULL,            
    altura          DECIMAL(5,2) NOT NULL,            
    tipo_paciente   VARCHAR(20)  NOT NULL DEFAULT 'adulto',
    fecha_registro  DATE         NOT NULL,             
    CONSTRAINT PK_paciente         PRIMARY KEY (id_paciente),
    CONSTRAINT UQ_paciente_dni     UNIQUE (dni),
    CONSTRAINT UQ_paciente_email   UNIQUE (email),
    CONSTRAINT FK_paciente_usuario FOREIGN KEY (id_usuario) REFERENCES usuario (id_usuario)
        ON DELETE RESTRICT
        ON UPDATE CASCADE,
    CONSTRAINT CHK_paciente_sexo   CHECK (sexo IN ('M', 'F', 'X')),
    CONSTRAINT CHK_tipo_paciente   CHECK (tipo_paciente IN ('jubilado', 'adulto', 'joven'))
);

-- Tabla nueva: alergias e intolerancias
-- El proyecto indica explícitamente "control de alergias,
-- intolerancias y restricciones alimentarias" como requisito.
-- No existía ninguna representación en el SQL original.
CREATE TABLE alergia (
    id_alergia  INT          NOT NULL AUTO_INCREMENT,
    nombre      VARCHAR(100) NOT NULL,
    tipo        VARCHAR(20)  NOT NULL,
    descripcion VARCHAR(200),
    CONSTRAINT PK_alergia      PRIMARY KEY (id_alergia),
    CONSTRAINT CHK_alergia_tipo CHECK (tipo IN ('alergia', 'intolerancia', 'restriccion'))
);

-- Tabla nueva: relación N:M entre paciente y alergia
CREATE TABLE paciente_alergia (
    id_paciente INT NOT NULL,
    id_alergia  INT NOT NULL,
    severidad   VARCHAR(20) NOT NULL DEFAULT 'moderada',
    CONSTRAINT PK_paciente_alergia   PRIMARY KEY (id_paciente, id_alergia),
    CONSTRAINT FK_pac_alergia_pac    FOREIGN KEY (id_paciente) REFERENCES paciente (id_paciente)
        ON DELETE CASCADE ON UPDATE CASCADE,
    CONSTRAINT FK_pac_alergia_aler   FOREIGN KEY (id_alergia)  REFERENCES alergia (id_alergia)
        ON DELETE CASCADE ON UPDATE CASCADE,
    CONSTRAINT CHK_severidad         CHECK (severidad IN ('leve', 'moderada', 'grave'))
);


-- ============================================================
-- BLOQUE 2 · PROFESIONALES
-- ============================================================
-- Correcciones aplicadas:
--   · email ampliado de 30 a 100 caracteres
--   · nombre en especialidades ampliado de 20 a 100
--   · descripcion en especialidades ampliado de 50 a 200
--   · Añadido campo "activo" para baja lógica sin borrar registros
--   · Añadido campo "fecha_alta"
--   · id_usuario FK para vincular con login
-- ============================================================

CREATE TABLE especialidad (                           -- nombre en singular (convención)
    id_especialidad INT          NOT NULL AUTO_INCREMENT,
    nombre          VARCHAR(100) NOT NULL,            -- ampliado de 20 a 100
    descripcion     VARCHAR(200),                    -- ampliado de 50 a 200
    CONSTRAINT PK_especialidad PRIMARY KEY (id_especialidad)
);

CREATE TABLE profesional (                           -- nombre en singular (convención)
    id_profesional  INT          NOT NULL AUTO_INCREMENT,
    id_usuario      INT          NOT NULL,
    id_especialidad INT,
    nom_completo    VARCHAR(100) NOT NULL,
    email           VARCHAR(100) NOT NULL UNIQUE,    -- ampliado de 30 a 100
    telefono        CHAR(9),
    anos_exp        INT          NOT NULL DEFAULT 0,
    activo          TINYINT(1)   NOT NULL DEFAULT 1, -- campo nuevo: baja lógica
    fecha_alta      DATE         NOT NULL,           -- campo nuevo
    CONSTRAINT PK_profesional         PRIMARY KEY (id_profesional),
    CONSTRAINT FK_prof_usuario        FOREIGN KEY (id_usuario)      REFERENCES usuario (id_usuario)
        ON DELETE RESTRICT  ON UPDATE CASCADE,
    CONSTRAINT FK_prof_especialidad   FOREIGN KEY (id_especialidad) REFERENCES especialidad (id_especialidad)
        ON DELETE SET NULL  ON UPDATE CASCADE
);

-- Tabla nueva: gestión de horarios, vacaciones y sustituciones
-- Requisito del subsistema "Gestión del Equipo Profesional".
-- No existía en el SQL original.
CREATE TABLE horario_profesional (
    id_horario      INT         NOT NULL AUTO_INCREMENT,
    id_profesional  INT         NOT NULL,
    dia_semana      TINYINT     NOT NULL,            -- 1=lunes … 7=domingo
    hora_inicio     TIME        NOT NULL,
    hora_fin        TIME        NOT NULL,
    tipo            VARCHAR(20) NOT NULL DEFAULT 'laboral',
    CONSTRAINT PK_horario           PRIMARY KEY (id_horario),
    CONSTRAINT FK_horario_prof      FOREIGN KEY (id_profesional) REFERENCES profesional (id_profesional)
        ON DELETE CASCADE ON UPDATE CASCADE,
    CONSTRAINT CHK_horario_tipo     CHECK (tipo IN ('laboral', 'vacaciones', 'sustitucion', 'festivo')),
    CONSTRAINT CHK_horario_horas    CHECK (hora_fin > hora_inicio)
);


-- ============================================================
-- BLOQUE 3 · PLANES DIETÉTICOS
-- ============================================================
-- Correcciones aplicadas:
--   · estado con CHECK (era varchar libre sin restricción)
--   · Añadida fecha_fin (faltaba en la versión original)
--   · FK con ON DELETE / ON UPDATE explícitos
-- ============================================================

CREATE TABLE plan_dietetico (
    id_plan         INT          NOT NULL AUTO_INCREMENT,
    id_paciente     INT          NOT NULL,
    id_nutricionista INT         NOT NULL,
    objetivo        VARCHAR(200),
    calorias_diarias INT,
    fecha_inicio    DATE         NOT NULL,
    fecha_fin       DATE,                            -- campo nuevo: faltaba
    estado          VARCHAR(20)  NOT NULL DEFAULT 'activo',
    CONSTRAINT PK_plan              PRIMARY KEY (id_plan),
    CONSTRAINT FK_plan_paciente     FOREIGN KEY (id_paciente)      REFERENCES paciente    (id_paciente)
        ON DELETE RESTRICT ON UPDATE CASCADE,
    CONSTRAINT FK_plan_nutricionista FOREIGN KEY (id_nutricionista) REFERENCES profesional (id_profesional)
        ON DELETE RESTRICT ON UPDATE CASCADE,
    CONSTRAINT CHK_plan_estado      CHECK (estado IN ('activo', 'completado', 'pausado', 'cancelado')),
    CONSTRAINT CHK_plan_fechas      CHECK (fecha_fin IS NULL OR fecha_fin >= fecha_inicio)
);

-- Tabla nueva: menú diario
-- Aparecía en el diagrama de clases entre PlanAlimentacion y
-- Comida, pero no estaba creada en el SQL original.
-- Permite organizar las comidas por día dentro del plan.
CREATE TABLE menu_diario (
    id_menu     INT         NOT NULL AUTO_INCREMENT,
    id_plan     INT         NOT NULL,
    dia_numero  INT         NOT NULL,               -- día 1, 2, 3… dentro del plan
    dia_semana  VARCHAR(10) NOT NULL,
    descripcion VARCHAR(200),
    CONSTRAINT PK_menu          PRIMARY KEY (id_menu),
    CONSTRAINT FK_menu_plan     FOREIGN KEY (id_plan) REFERENCES plan_dietetico (id_plan)
        ON DELETE CASCADE ON UPDATE CASCADE,
    CONSTRAINT CHK_menu_dia     CHECK (dia_semana IN ('lunes','martes','miercoles','jueves','viernes','sabado','domingo'))
);

CREATE TABLE comida (
    id_comida   INT          NOT NULL AUTO_INCREMENT,
    id_menu     INT          NOT NULL,              -- ahora FK a menu_diario (antes a plan)
    nombre      VARCHAR(100) NOT NULL,
    tipo        VARCHAR(20)  NOT NULL,
    calorias    INT          NOT NULL,              -- NOT NULL: es un dato clave
    CONSTRAINT PK_comida        PRIMARY KEY (id_comida),
    CONSTRAINT FK_comida_menu   FOREIGN KEY (id_menu) REFERENCES menu_diario (id_menu)
        ON DELETE CASCADE ON UPDATE CASCADE,
    CONSTRAINT CHK_comida_tipo  CHECK (tipo IN ('desayuno','almuerzo','comida','merienda','cena','snack'))
);

CREATE TABLE alimento (
    id_alimento    INT          NOT NULL AUTO_INCREMENT,
    id_comida      INT          NOT NULL,
    nombre         VARCHAR(100) NOT NULL,
    cantidad       DECIMAL(8,2) NOT NULL,           -- era INT; cambiado a DECIMAL para 0.5 kg, 250 g…
    unidad         VARCHAR(20)  NOT NULL,
    calorias       INT,
    proteinas      DECIMAL(6,2),                   -- campo nuevo del diagrama de clases
    carbohidratos  DECIMAL(6,2),                   -- campo nuevo del diagrama de clases
    grasas         DECIMAL(6,2),                   -- campo nuevo del diagrama de clases
    CONSTRAINT PK_alimento        PRIMARY KEY (id_alimento),
    CONSTRAINT FK_alimento_comida FOREIGN KEY (id_comida) REFERENCES comida (id_comida)
        ON DELETE CASCADE ON UPDATE CASCADE,
    CONSTRAINT CHK_alimento_cant  CHECK (cantidad > 0)
);


-- ============================================================
-- BLOQUE 4 · CITAS Y PAGOS
-- ============================================================
-- Correcciones aplicadas:
--   · Añadido campo "modalidad" (presencial/online): requisito
--     explícito del documento del proyecto
--   · FK con ON DELETE / ON UPDATE explícitos
-- ============================================================

CREATE TABLE cita (                                 
    id_cita         INT          NOT NULL AUTO_INCREMENT,
    id_paciente     INT          NOT NULL,
    id_profesional  INT          NOT NULL,
    id_plan         INT,                           
    fecha_cita      DATE         NOT NULL,
    hora_cita       TIME         NOT NULL,
    estado_cita     VARCHAR(20)  NOT NULL DEFAULT 'pendiente',
    modalidad       VARCHAR(15)  NOT NULL DEFAULT 'presencial', 
    motivo_consulta VARCHAR(150),
    observacion     VARCHAR(300),
    CONSTRAINT PK_cita              PRIMARY KEY (id_cita),
    CONSTRAINT FK_cita_paciente     FOREIGN KEY (id_paciente)    REFERENCES paciente    (id_paciente)
        ON DELETE RESTRICT ON UPDATE CASCADE,
    CONSTRAINT FK_cita_profesional  FOREIGN KEY (id_profesional) REFERENCES profesional (id_profesional)
        ON DELETE RESTRICT ON UPDATE CASCADE,
    CONSTRAINT FK_cita_plan         FOREIGN KEY (id_plan)        REFERENCES plan_dietetico (id_plan)
        ON DELETE SET NULL ON UPDATE CASCADE,
    CONSTRAINT CHK_cita_estado      CHECK (estado_cita IN ('pendiente','completada','cancelada')),
    CONSTRAINT CHK_cita_modalidad   CHECK (modalidad   IN ('presencial','online'))
);

-- Tabla nueva: gestión de pagos y facturación
-- Requisito explícito del subsistema "Gestión de Pacientes y Citas".
-- No existía ninguna representación en el SQL original.
CREATE TABLE factura (
    id_factura      INT            NOT NULL AUTO_INCREMENT,
    id_cita         INT            NOT NULL,
    id_paciente     INT            NOT NULL,
    importe         DECIMAL(8,2)   NOT NULL,
    estado_pago     VARCHAR(20)    NOT NULL DEFAULT 'pendiente',
    metodo_pago     VARCHAR(30),
    fecha_emision   DATE           NOT NULL,
    fecha_pago      DATE,
    CONSTRAINT PK_factura           PRIMARY KEY (id_factura),
    CONSTRAINT FK_factura_cita      FOREIGN KEY (id_cita)      REFERENCES cita     (id_cita)
        ON DELETE RESTRICT ON UPDATE CASCADE,
    CONSTRAINT FK_factura_paciente  FOREIGN KEY (id_paciente)  REFERENCES paciente (id_paciente)
        ON DELETE RESTRICT ON UPDATE CASCADE,
    CONSTRAINT CHK_factura_estado   CHECK (estado_pago IN ('pendiente','pagada','cancelada','devuelta')),
    CONSTRAINT CHK_factura_importe  CHECK (importe > 0)
);


-- ============================================================
-- BLOQUE 5 · SEGUIMIENTO Y PROGRESO
-- ============================================================
-- Correcciones aplicadas:
--   · fecha NOT NULL en progreso
--   · CHECK peso > 0
--   · Añadido imc DECIMAL para almacenar el valor calculado
--   · panel_progreso ahora tiene id_paciente (faltaba)
--   · REGISTRO_MET con tipo_metrica para diferenciar métricas
-- ============================================================

CREATE TABLE progreso (
    id_progreso  INT          NOT NULL AUTO_INCREMENT,
    id_paciente  INT          NOT NULL,
    id_plan      INT          NOT NULL,
    fecha        DATE         NOT NULL,             -- antes nullable; ahora NOT NULL
    peso         DECIMAL(5,2) NOT NULL,
    imc          DECIMAL(5,2),                      -- campo nuevo: calculado en app
    observaciones TEXT,
    CONSTRAINT PK_progreso          PRIMARY KEY (id_progreso),
    CONSTRAINT FK_progreso_paciente FOREIGN KEY (id_paciente) REFERENCES paciente       (id_paciente)
        ON DELETE RESTRICT ON UPDATE CASCADE,
    CONSTRAINT FK_progreso_plan     FOREIGN KEY (id_plan)     REFERENCES plan_dietetico (id_plan)
        ON DELETE RESTRICT ON UPDATE CASCADE,
    CONSTRAINT CHK_progreso_peso    CHECK (peso > 0)
);

CREATE TABLE panel_progreso (
    id_panel            INT  NOT NULL AUTO_INCREMENT,
    id_paciente         INT  NOT NULL,              -- campo nuevo: faltaba la FK
    fecha_actualizacion DATE NOT NULL,
    CONSTRAINT PK_panel_progreso    PRIMARY KEY (id_panel),
    CONSTRAINT FK_panel_paciente    FOREIGN KEY (id_paciente) REFERENCES paciente (id_paciente)
        ON DELETE RESTRICT ON UPDATE CASCADE
);

-- REGISTRO_MET: se añade tipo_metrica para saber qué se mide
-- (IMC, % grasa, masa muscular, etc.)
CREATE TABLE registro_metrica (                     -- renombrado a snake_case
    id_registro     INT          NOT NULL AUTO_INCREMENT,
    id_progreso     INT          NOT NULL,
    id_panel        INT          NOT NULL,
    tipo_metrica    VARCHAR(50)  NOT NULL,           -- campo nuevo
    valor           DECIMAL(10,2) NOT NULL,
    fecha_calculo   DATE         NOT NULL,
    CONSTRAINT PK_registro_metrica      PRIMARY KEY (id_registro),
    CONSTRAINT FK_reg_met_progreso      FOREIGN KEY (id_progreso) REFERENCES progreso      (id_progreso)
        ON DELETE RESTRICT ON UPDATE CASCADE,
    CONSTRAINT FK_reg_met_panel         FOREIGN KEY (id_panel)    REFERENCES panel_progreso (id_panel)
        ON DELETE RESTRICT ON UPDATE CASCADE,
    CONSTRAINT CHK_reg_met_tipo         CHECK (tipo_metrica IN ('imc','peso','grasa_corporal','masa_muscular','perimetro_cintura','otro'))
);


-- ============================================================
-- BLOQUE 6 · INFORMES Y ESTADÍSTICAS
-- ============================================================
-- Correcciones aplicadas:
--   · Eliminadas FK a comida y alimento (no tienen sentido en un
--     informe de nivel alto; generan acoplamiento innecesario)
--   · tipo con CHECK para controlar los tipos de informe
-- ============================================================

CREATE TABLE informe (
    id_informe      INT         NOT NULL AUTO_INCREMENT,
    id_paciente     INT,
    id_profesional  INT,
    id_cita         INT,
    id_plan         INT,
    id_panel        INT,
    fecha_gen       DATE        NOT NULL,
    fecha_inicio    DATE        NOT NULL,
    fecha_fin       DATE        NOT NULL,
    tipo            VARCHAR(30) NOT NULL,
    CONSTRAINT PK_informe               PRIMARY KEY (id_informe),
    CONSTRAINT FK_informe_paciente      FOREIGN KEY (id_paciente)    REFERENCES paciente       (id_paciente)
        ON DELETE SET NULL ON UPDATE CASCADE,
    CONSTRAINT FK_informe_profesional   FOREIGN KEY (id_profesional) REFERENCES profesional    (id_profesional)
        ON DELETE SET NULL ON UPDATE CASCADE,
    CONSTRAINT FK_informe_cita          FOREIGN KEY (id_cita)        REFERENCES cita           (id_cita)
        ON DELETE SET NULL ON UPDATE CASCADE,
    CONSTRAINT FK_informe_plan          FOREIGN KEY (id_plan)        REFERENCES plan_dietetico (id_plan)
        ON DELETE SET NULL ON UPDATE CASCADE,
    CONSTRAINT FK_informe_panel         FOREIGN KEY (id_panel)       REFERENCES panel_progreso (id_panel)
        ON DELETE SET NULL ON UPDATE CASCADE,
    CONSTRAINT CHK_informe_tipo         CHECK (tipo IN ('progreso_paciente','rendimiento_profesional','estadisticas_negocio','plan_dietetico','otro')),
    CONSTRAINT CHK_informe_fechas       CHECK (fecha_fin >= fecha_inicio)
);

-- INSERTS
-- ============================================================
--  FooDiet – DATOS DE PRUEBA
--  Ejecutar DESPUÉS de SQL_Global_Corregido.sql
--  IES Font de San Lluis · 1 DAW · 2025-2026
-- ============================================================

USE FooDiet;
SET FOREIGN_KEY_CHECKS = 0;

-- ============================================================
-- 1. USUARIOS (sistema de autenticación y roles)
-- ============================================================
INSERT INTO usuario (email, password_hash, rol, activo, fecha_alta) VALUES
    ('admin@foodiet.com',   '$2b$12$abc123hashadmin',    'administrativo', 1, '2026-01-01'),
    ('laura@foodiet.com',   '$2b$12$abc123hashlau',      'nutricionista',  1, '2026-01-01'),
    ('carlos@foodiet.com',  '$2b$12$abc123hashcar',      'dietista',       1, '2026-01-01'),
    ('sergio@foodiet.com',  '$2b$12$abc123hashser',      'nutricionista',  1, '2026-01-01'),
    ('ana@gmail.com',       '$2b$12$abc123hashana',      'paciente',       1, '2026-01-15'),
    ('luis@gmail.com',      '$2b$12$abc123hashluis',     'paciente',       1, '2026-01-20'),
    ('maria@gmail.com',     '$2b$12$abc123hashmaria',    'paciente',       1, '2026-02-01'),
    ('pedro@gmail.com',     '$2b$12$abc123hashpedro',    'paciente',       1, '2026-02-10'),
    ('carmen@gmail.com',    '$2b$12$abc123hashcarmen',   'paciente',       1, '2026-02-15'),
    ('jose@gmail.com',      '$2b$12$abc123hashjose',     'paciente',       1, '2026-03-01'),
    ('isabel@gmail.com',    '$2b$12$abc123hashisabel',   'paciente',       1, '2026-03-10');

-- ============================================================
-- 2. PACIENTES
-- ============================================================
INSERT INTO paciente (id_usuario, dni, nombre, apellidos, telefono, email, direccion, fecha_nac, sexo, altura, tipo_paciente, fecha_registro) VALUES
    (5,  '12345678A', 'Ana',    'García López',    '612000001', 'ana@gmail.com',    'Calle Mayor 12, Valencia',       '1992-03-15', 'F', 1.65, 'adulto',   '2026-01-15'),
    (6,  '23456789B', 'Luis',   'Torres Vega',     '612000002', 'luis@gmail.com',   'Avenida del Puerto 34, Valencia', '1988-07-22', 'M', 1.78, 'adulto',   '2026-01-20'),
    (7,  '34567890C', 'María',  'López Ruiz',      '612000003', 'maria@gmail.com',  'Calle Colón 8, Valencia',        '1995-11-08', 'F', 1.62, 'joven',    '2026-02-01'),
    (8,  '45678901D', 'Pedro',  'Sánchez Gil',     '612000004', 'pedro@gmail.com',  'Gran Vía 56, Valencia',          '1970-04-30', 'M', 1.75, 'jubilado', '2026-02-10'),
    (9,  '56789012E', 'Carmen', 'Fernández Mora',  '612000005', 'carmen@gmail.com', 'Calle Xàtiva 23, Valencia',      '1985-09-12', 'F', 1.60, 'adulto',   '2026-02-15'),
    (10, '67890123F', 'José',   'Martínez Alba',   '612000006', 'jose@gmail.com',   'Paseo Marítimo 7, Valencia',     '1998-01-25', 'M', 1.80, 'joven',    '2026-03-01'),
    (11, '78901234G', 'Isabel', 'Romero Cruz',     '612000007', 'isabel@gmail.com', 'Calle Ruzafa 15, Valencia',      '1965-06-18', 'F', 1.58, 'jubilado', '2026-03-10');

-- ============================================================
-- 3. ALERGIAS
-- ============================================================
INSERT INTO alergia (nombre, tipo, descripcion) VALUES
    ('Gluten',      'intolerancia', 'Intolerancia al gluten presente en cereales como trigo, cebada y centeno'),
    ('Lactosa',     'intolerancia', 'Intolerancia a la lactosa presente en productos lácteos'),
    ('Frutos secos','alergia',      'Alergia a frutos secos como nueces, almendras, cacahuetes'),
    ('Marisco',     'alergia',      'Alergia a crustáceos y moluscos'),
    ('Huevo',       'alergia',      'Alergia a proteínas del huevo'),
    ('Soja',        'restriccion',  'Restricción alimentaria a productos derivados de la soja');

-- ============================================================
-- 4. PACIENTE_ALERGIA (relación N:M)
-- ============================================================
INSERT INTO paciente_alergia (id_paciente, id_alergia, severidad) VALUES
    (1, 2, 'leve'),       -- Ana: intolerancia lactosa leve
    (2, 3, 'grave'),      -- Luis: alergia frutos secos grave
    (3, 1, 'moderada'),   -- María: intolerancia gluten moderada
    (4, 2, 'moderada'),   -- Pedro: intolerancia lactosa moderada
    (5, 4, 'grave'),      -- Carmen: alergia marisco grave
    (6, 5, 'leve'),       -- José: alergia huevo leve
    (3, 6, 'leve');       -- María también: restricción soja leve

-- ============================================================
-- 5. ESPECIALIDADES
-- ============================================================
INSERT INTO especialidad (nombre, descripcion) VALUES
    ('Nutrición clínica',    'Tratamiento nutricional de enfermedades crónicas como diabetes, obesidad e hipertensión'),
    ('Nutrición deportiva',  'Planes nutricionales orientados al rendimiento físico y recuperación deportiva'),
    ('Dietética general',    'Alimentación saludable y equilibrada para la población general'),
    ('Pediatría nutricional','Nutrición infantil y adolescente, control del desarrollo y crecimiento');

-- ============================================================
-- 6. PROFESIONALES
-- ============================================================
INSERT INTO profesional (id_usuario, id_especialidad, nom_completo, email, telefono, anos_exp, activo, fecha_alta) VALUES
    (2, 1, 'Laura Gómez Ruiz',    'laura@foodiet.com',  '612100001', 7, 1, '2026-01-01'),
    (3, 2, 'Carlos Martín Pérez', 'carlos@foodiet.com', '612100002', 3, 1, '2026-01-01'),
    (4, 3, 'Sergio González Gil', 'sergio@foodiet.com', '612100003', 5, 1, '2026-01-01');

-- ============================================================
-- 7. HORARIOS DE PROFESIONALES
-- ============================================================
-- Laura: lunes a viernes 9:00-17:00, viernes hasta 14:00
INSERT INTO horario_profesional (id_profesional, dia_semana, hora_inicio, hora_fin, tipo) VALUES
    (1, 1, '09:00:00', '17:00:00', 'laboral'),
    (1, 2, '09:00:00', '17:00:00', 'laboral'),
    (1, 3, '09:00:00', '17:00:00', 'laboral'),
    (1, 4, '09:00:00', '17:00:00', 'laboral'),
    (1, 5, '09:00:00', '14:00:00', 'laboral'),
-- Carlos: lunes, miércoles y jueves 10:00-18:00
    (2, 1, '10:00:00', '18:00:00', 'laboral'),
    (2, 3, '10:00:00', '18:00:00', 'laboral'),
    (2, 4, '10:00:00', '18:00:00', 'laboral'),
-- Sergio: martes, jueves y viernes 08:00-15:00
    (3, 2, '08:00:00', '15:00:00', 'laboral'),
    (3, 4, '08:00:00', '15:00:00', 'laboral'),
    (3, 5, '08:00:00', '15:00:00', 'laboral');

-- ============================================================
-- 8. PLANES DIETÉTICOS
-- ============================================================
INSERT INTO plan_dietetico (id_paciente, id_nutricionista, objetivo, calorias_diarias, fecha_inicio, fecha_fin, estado) VALUES
    (1, 1, 'Pérdida de peso gradual y control del IMC',               1600, '2026-01-20', '2026-07-20', 'activo'),
    (2, 2, 'Mejora del rendimiento deportivo y ganancia muscular',     2800, '2026-02-01', '2026-08-01', 'activo'),
    (3, 1, 'Dieta sin gluten equilibrada para control de peso',        1800, '2026-02-10', '2026-05-10', 'completado'),
    (4, 3, 'Dieta baja en grasas saturadas para control colesterol',   1900, '2026-02-15', '2026-08-15', 'activo'),
    (5, 3, 'Plan de alimentación libre de mariscos y equilibrado',     1750, '2026-03-01', '2026-09-01', 'activo'),
    (6, 2, 'Plan de definición muscular para deportista joven',        2400, '2026-03-10', '2026-09-10', 'activo'),
    (7, 1, 'Dieta antiinflamatoria para mejorar movilidad articular',  1700, '2026-03-15', '2026-09-15', 'activo');

-- ============================================================
-- 9. MENÚS DIARIOS
-- ============================================================
-- Plan 1 (Ana): lunes y martes de ejemplo
INSERT INTO menu_diario (id_plan, dia_numero, dia_semana, descripcion) VALUES
    (1, 1, 'lunes',    'Menú bajo en calorías con proteínas magras'),
    (1, 2, 'martes',   'Menú con verduras de temporada y legumbres'),
    (1, 3, 'miercoles','Menú rico en fibra y bajo en grasas'),
    (2, 1, 'lunes',    'Menú de carga de carbohidratos pre-entreno'),
    (2, 2, 'martes',   'Menú de recuperación post-entreno'),
    (4, 1, 'lunes',    'Menú bajo en grasas saturadas'),
    (4, 2, 'martes',   'Menú con pescado azul y verduras');

-- ============================================================
-- 10. COMIDAS
-- ============================================================
INSERT INTO comida (id_menu, nombre, tipo, calorias) VALUES
    -- Menú 1 (Ana, lunes)
    (1, 'Tostadas con aguacate y tomate',         'desayuno',  280),
    (1, 'Ensalada de pollo con quinoa',            'comida',    420),
    (1, 'Yogur natural con fruta',                 'merienda',  150),
    (1, 'Salmón al horno con brócoli',             'cena',      380),
    -- Menú 2 (Ana, martes)
    (2, 'Avena con plátano y miel',               'desayuno',  320),
    (2, 'Lentejas con verduras',                   'comida',    450),
    (2, 'Manzana con almendras',                   'merienda',  180),
    (2, 'Tortilla de espinacas',                   'cena',      300),
    -- Menú 4 (Luis, lunes - deportivo)
    (4, 'Porridge con proteína y frutas del bosque','desayuno', 480),
    (4, 'Pasta integral con pechuga de pollo',     'comida',    650),
    (4, 'Batido de proteínas con plátano',         'merienda',  320),
    (4, 'Arroz con ternera y verduras salteadas',  'cena',      580);

-- ============================================================
-- 11. ALIMENTOS
-- ============================================================
INSERT INTO alimento (id_comida, nombre, cantidad, unidad, calorias, proteinas, carbohidratos, grasas) VALUES
    -- Tostadas con aguacate (comida 1)
    (1, 'Pan integral',   2.00, 'rebanadas', 140, 5.0,  28.0, 2.0),
    (1, 'Aguacate',       0.50, 'unidad',     80, 1.0,   4.0, 7.5),
    (1, 'Tomate cherry',  6.00, 'unidades',   30, 1.0,   6.0, 0.2),
    -- Ensalada de pollo (comida 2)
    (2, 'Pechuga de pollo',120.00, 'g',      132,27.0,   0.0, 2.0),
    (2, 'Quinoa cocida',  80.00, 'g',        111, 4.0,  20.0, 1.8),
    (2, 'Lechuga mixta',  80.00, 'g',         12, 1.0,   2.0, 0.2),
    (2, 'Aceite de oliva', 1.00, 'cucharada',  90, 0.0,   0.0,10.0),
    -- Porridge deportivo (comida 9)
    (9, 'Avena en copos', 80.00, 'g',        302, 10.0, 54.0, 6.0),
    (9, 'Proteína en polvo',30.00,'g',       120, 24.0,  3.0, 1.5),
    (9, 'Plátano',         1.00, 'unidad',    89,  1.1, 23.0, 0.3),
    (9, 'Arándanos',      50.00, 'g',         29,  0.4,  7.0, 0.2),
    -- Pasta con pollo (comida 10)
    (10,'Pasta integral',150.00, 'g',        210,  8.0, 42.0, 1.5),
    (10,'Pechuga de pollo',150.00,'g',       165, 31.0,  0.0, 3.5),
    (10,'Tomate triturado',80.00,'g',         16,  0.8,  3.5, 0.2);

-- ============================================================
-- 12. CITAS
-- ============================================================
INSERT INTO cita (id_paciente, id_profesional, id_plan, fecha_cita, hora_cita, estado_cita, modalidad, motivo_consulta, observacion) VALUES
    (1, 1, 1, '2026-01-20', '10:00:00', 'completada', 'presencial', 'Primera visita y evaluación inicial', 'IMC 29.38, objetivo: perder 5 kg en 6 meses'),
    (1, 1, 1, '2026-02-15', '10:00:00', 'completada', 'presencial', 'Seguimiento mensual',                 'Bajó 0.9 kg, sigue el plan correctamente'),
    (1, 1, 1, '2026-03-15', '10:30:00', 'completada', 'online',     'Seguimiento mensual',                 'Bajó 0.8 kg, ajuste en cenas'),
    (1, 1, 1, '2026-04-15', '10:00:00', 'completada', 'presencial', 'Seguimiento mensual',                 'Bajó 1.2 kg, muy buen progreso'),
    (1, 1, 1, '2026-05-15', '10:00:00', 'completada', 'presencial', 'Seguimiento mensual',                 'Bajó 1.1 kg, plan en buen camino'),
    (1, 1, 1, '2026-06-12', '10:00:00', 'completada', 'presencial', 'Seguimiento mensual',                 'Bajó 1.0 kg, evolución muy positiva'),
    (2, 2, 2, '2026-02-01', '11:00:00', 'completada', 'presencial', 'Primera visita deportista',           'Objetivo ganancia muscular, protocolo de carga'),
    (2, 2, 2, '2026-03-01', '11:00:00', 'completada', 'online',     'Seguimiento plan deportivo',          'Ganó 1.5 kg de masa muscular'),
    (2, 2, 2, '2026-04-01', '11:00:00', 'completada', 'presencial', 'Seguimiento plan deportivo',          'Rendimiento mejorando notablemente'),
    (2, 2, 2, '2026-06-12', '11:30:00', 'pendiente',  'online',     'Seguimiento trimestral',              NULL),
    (3, 1, 3, '2026-02-10', '12:00:00', 'completada', 'presencial', 'Primera visita dieta sin gluten',     'Explicación de alimentos permitidos y prohibidos'),
    (3, 1, 3, '2026-04-10', '12:00:00', 'completada', 'presencial', 'Revisión plan sin gluten',            'Adaptación correcta, peso estable'),
    (4, 3, 4, '2026-02-15', '09:00:00', 'completada', 'presencial', 'Control colesterol y dieta',          'Colesterol total 210 mg/dL, plan bajo en grasas'),
    (4, 3, 4, '2026-04-15', '09:00:00', 'completada', 'presencial', 'Revisión analítica',                  'Colesterol bajó a 185 mg/dL, excelente'),
    (4, 3, 4, '2026-06-12', '09:30:00', 'pendiente',  'presencial', 'Revisión semestral',                  NULL),
    (5, 3, 5, '2026-03-01', '10:00:00', 'completada', 'presencial', 'Primera visita, alergias',            'Alergia grave a mariscos confirmada'),
    (5, 3, 5, '2026-05-01', '10:00:00', 'completada', 'online',     'Seguimiento plan',                    'Buena adaptación, perdió 2 kg'),
    (6, 2, 6, '2026-03-10', '16:00:00', 'completada', 'presencial', 'Plan de definición muscular',         'Deportista habitual, objetivo definición para verano'),
    (6, 2, 6, '2026-05-10', '16:00:00', 'completada', 'presencial', 'Seguimiento definición',              'Porcentaje grasa bajó del 18% al 15%'),
    (7, 1, 7, '2026-03-15', '11:00:00', 'completada', 'presencial', 'Dieta antiinflamatoria',              'Artritis leve, objetivo reducir inflamación'),
    (5, 3, 5, '2026-04-01', '10:00:00', 'cancelada',  'presencial', 'Revisión mensual',                    'Cancelada por la paciente');

-- ============================================================
-- 13. FACTURAS
-- ============================================================
INSERT INTO factura (id_cita, id_paciente, importe, estado_pago, metodo_pago, fecha_emision, fecha_pago) VALUES
    (1,  1, 60.00, 'pagada',    'tarjeta',  '2026-01-20', '2026-01-20'),
    (2,  1, 45.00, 'pagada',    'tarjeta',  '2026-02-15', '2026-02-15'),
    (3,  1, 45.00, 'pagada',    'efectivo', '2026-03-15', '2026-03-15'),
    (4,  1, 45.00, 'pagada',    'tarjeta',  '2026-04-15', '2026-04-15'),
    (5,  1, 45.00, 'pagada',    'tarjeta',  '2026-05-15', '2026-05-15'),
    (6,  1, 45.00, 'pendiente', NULL,       '2026-06-12', NULL),
    (7,  2, 70.00, 'pagada',    'tarjeta',  '2026-02-01', '2026-02-01'),
    (8,  2, 55.00, 'pagada',    'transferencia','2026-03-01','2026-03-02'),
    (9,  2, 55.00, 'pagada',    'tarjeta',  '2026-04-01', '2026-04-01'),
    (11, 3, 60.00, 'pagada',    'efectivo', '2026-02-10', '2026-02-10'),
    (12, 3, 45.00, 'pagada',    'tarjeta',  '2026-04-10', '2026-04-10'),
    (13, 4, 60.00, 'pagada',    'tarjeta',  '2026-02-15', '2026-02-15'),
    (14, 4, 45.00, 'pagada',    'tarjeta',  '2026-04-15', '2026-04-15'),
    (16, 5, 60.00, 'pagada',    'efectivo', '2026-03-01', '2026-03-01'),
    (17, 5, 45.00, 'pagada',    'tarjeta',  '2026-05-01', '2026-05-01'),
    (18, 6, 55.00, 'pagada',    'tarjeta',  '2026-03-10', '2026-03-10'),
    (19, 6, 55.00, 'pagada',    'tarjeta',  '2026-05-10', '2026-05-10'),
    (20, 7, 60.00, 'pagada',    'efectivo', '2026-03-15', '2026-03-15');

-- ============================================================
-- 14. PROGRESO DE PACIENTES
-- ============================================================
INSERT INTO progreso (id_paciente, id_plan, fecha, peso, imc, observaciones) VALUES
    -- Ana (paciente 1): bajando de 80 a 75 kg
    (1, 1, '2026-01-20', 80.00, 29.38, 'Peso inicial al alta'),
    (1, 1, '2026-02-15', 79.10, 29.05, 'Primera bajada, buen comienzo'),
    (1, 1, '2026-03-15', 78.30, 28.75, 'Ajuste en cenas, sigue bajando'),
    (1, 1, '2026-04-15', 77.10, 28.31, 'Muy buen progreso este mes'),
    (1, 1, '2026-05-15', 76.00, 27.90, 'Constante y motivada'),
    (1, 1, '2026-06-12', 75.00, 27.55, 'Objetivo casi alcanzado'),
    -- Luis (paciente 2): subiendo masa muscular de 78 a 82 kg
    (2, 2, '2026-02-01', 78.00, 24.60, 'Peso inicial, composición corporal buena'),
    (2, 2, '2026-03-01', 79.50, 25.08, 'Ganancia muscular notable'),
    (2, 2, '2026-04-01', 80.80, 25.49, 'Rendimiento deportivo mejorando'),
    (2, 2, '2026-06-12', 82.00, 25.87, 'Excelente evolución'),
    -- María (paciente 3): plan completado, peso estable
    (3, 3, '2026-02-10', 68.00, 25.91, 'Peso inicial, sin gluten desde hoy'),
    (3, 3, '2026-04-10', 66.50, 25.34, 'Plan completado, peso estabilizado'),
    -- Pedro (paciente 4): bajando por colesterol
    (4, 4, '2026-02-15', 88.00, 28.73, 'Colesterol 210, empezamos dieta'),
    (4, 4, '2026-04-15', 85.50, 27.92, 'Colesterol bajó a 185, muy bien'),
    (4, 4, '2026-06-12', 83.00, 27.10, 'Objetivo a punto de alcanzarse'),
    -- Carmen (paciente 5)
    (5, 5, '2026-03-01', 70.00, 27.34, 'Primera visita, alergia mariscos confirmada'),
    (5, 5, '2026-05-01', 68.00, 26.56, 'Buena adaptación al plan'),
    -- José (paciente 6): definición muscular
    (6, 6, '2026-03-10', 75.00, 23.15, 'Inicio plan definición, 18% grasa'),
    (6, 6, '2026-05-10', 74.00, 22.84, 'Bajó al 15% grasa corporal'),
    -- Isabel (paciente 7)
    (7, 7, '2026-03-15', 72.00, 28.86, 'Inicio plan antiinflamatorio');

-- ============================================================
-- 15. PANEL DE PROGRESO
-- ============================================================
INSERT INTO panel_progreso (id_paciente, fecha_actualizacion) VALUES
    (1, '2026-06-12'),
    (2, '2026-06-12'),
    (3, '2026-04-10'),
    (4, '2026-06-12'),
    (5, '2026-05-01'),
    (6, '2026-05-10'),
    (7, '2026-03-15');

-- ============================================================
-- 16. REGISTRO DE MÉTRICAS
-- ============================================================
INSERT INTO registro_metrica (id_progreso, id_panel, tipo_metrica, valor, fecha_calculo) VALUES
    -- Ana (progreso 1-6, panel 1)
    (1, 1, 'imc',              29.38, '2026-01-20'),
    (1, 1, 'peso',             80.00, '2026-01-20'),
    (1, 1, 'grasa_corporal',   32.00, '2026-01-20'),
    (6, 1, 'imc',              27.55, '2026-06-12'),
    (6, 1, 'peso',             75.00, '2026-06-12'),
    (6, 1, 'grasa_corporal',   28.00, '2026-06-12'),
    (6, 1, 'perimetro_cintura',82.00, '2026-06-12'),
    -- Luis (progreso 7-10, panel 2)
    (7, 2,  'imc',             24.60, '2026-02-01'),
    (7, 2,  'peso',            78.00, '2026-02-01'),
    (7, 2,  'grasa_corporal',  18.00, '2026-02-01'),
    (10, 2, 'imc',             25.87, '2026-06-12'),
    (10, 2, 'peso',            82.00, '2026-06-12'),
    (10, 2, 'grasa_corporal',  14.50, '2026-06-12'),
    (10, 2, 'masa_muscular',   68.00, '2026-06-12'),
    -- Pedro (progreso 13-15, panel 4)
    (13, 4, 'imc',             28.73, '2026-02-15'),
    (13, 4, 'peso',            88.00, '2026-02-15'),
    (15, 4, 'imc',             27.10, '2026-06-12'),
    (15, 4, 'peso',            83.00, '2026-06-12'),
    -- José (progreso 18-19, panel 6)
    (18, 6, 'grasa_corporal',  18.00, '2026-03-10'),
    (19, 6, 'grasa_corporal',  15.00, '2026-05-10'),
    (19, 6, 'masa_muscular',   61.00, '2026-05-10');

-- ============================================================
-- 17. INFORMES
-- ============================================================
INSERT INTO informe (id_paciente, id_profesional, id_cita, id_plan, id_panel, fecha_gen, fecha_inicio, fecha_fin, tipo) VALUES
    (1, 1, 6,  1, 1, '2026-06-12', '2026-01-20', '2026-06-12', 'progreso_paciente'),
    (2, 2, 9,  2, 2, '2026-04-01', '2026-02-01', '2026-04-01', 'progreso_paciente'),
    (4, 3, 14, 4, 4, '2026-04-15', '2026-02-15', '2026-04-15', 'progreso_paciente'),
    (NULL, 1, NULL, NULL, NULL, '2026-06-12', '2026-06-01', '2026-06-12', 'rendimiento_profesional'),
    (NULL, NULL, NULL, NULL, NULL, '2026-06-12', '2026-06-01', '2026-06-12', 'estadisticas_negocio');

SET FOREIGN_KEY_CHECKS = 1;

SELECT 'usuario'           AS tabla, COUNT(*) AS registros FROM usuario
UNION ALL
SELECT 'paciente',          COUNT(*) FROM paciente
UNION ALL
SELECT 'alergia',           COUNT(*) FROM alergia
UNION ALL
SELECT 'paciente_alergia',  COUNT(*) FROM paciente_alergia
UNION ALL
SELECT 'especialidad',      COUNT(*) FROM especialidad
UNION ALL
SELECT 'profesional',       COUNT(*) FROM profesional
UNION ALL
SELECT 'horario_profesional',COUNT(*) FROM horario_profesional
UNION ALL
SELECT 'plan_dietetico',    COUNT(*) FROM plan_dietetico
UNION ALL
SELECT 'menu_diario',       COUNT(*) FROM menu_diario
UNION ALL
SELECT 'comida',            COUNT(*) FROM comida
UNION ALL
SELECT 'alimento',          COUNT(*) FROM alimento
UNION ALL
SELECT 'cita',              COUNT(*) FROM cita
UNION ALL
SELECT 'factura',           COUNT(*) FROM factura
UNION ALL
SELECT 'progreso',          COUNT(*) FROM progreso
UNION ALL
SELECT 'panel_progreso',    COUNT(*) FROM panel_progreso
UNION ALL
SELECT 'registro_metrica',  COUNT(*) FROM registro_metrica
UNION ALL
SELECT 'informe',           COUNT(*) FROM informe;

-- ============================================================
--  FooDiet – CONSULTAS SUBSISTEMA DE ESTADÍSTICAS
--  Itzel Cordoba Herrera
--  Tablas principales: progreso, panel_progreso,
--                      registro_metrica, informe

-- 1. CONSULTA SIMPLE
--    IMC medio, máximo y mínimo de todos los pacientes
--    en sus registros de progreso
-- ============================================================

SELECT
    AVG(imc)   AS imc_medio,
    MAX(imc)   AS imc_maximo,
    MIN(imc)   AS imc_minimo,
    AVG(peso)  AS peso_medio_kg,
    COUNT(*)   AS total_registros
FROM progreso
WHERE imc IS NOT NULL;


-- ============================================================
-- 2. CONSULTA CON AGREGACIÓN (GROUP BY + HAVING)
--    Total de kg perdidos por cada paciente.
--    Solo muestra los que bajaron de peso.
-- ============================================================

SELECT
    id_paciente,
    COUNT(*)                   AS total_registros,
    MAX(peso) - MIN(peso)      AS kg_perdidos,
    MAX(imc)  - MIN(imc)       AS reduccion_imc,
    MIN(fecha)                 AS primer_registro,
    MAX(fecha)                 AS ultimo_registro
FROM progreso
GROUP BY id_paciente
HAVING MAX(peso) > MIN(peso)
ORDER BY kg_perdidos DESC;


-- ============================================================
-- 3. CONSULTA CON SUBCONSULTA
--    Pacientes cuyo IMC actual está por encima
--    de la media de la clínica
-- ============================================================

SELECT
    id_paciente,
    peso      AS peso_actual_kg,
    imc       AS imc_actual,
    fecha     AS fecha_ultimo_registro
FROM progreso
WHERE imc > (
    SELECT AVG(imc)
    FROM progreso
    WHERE imc IS NOT NULL
)
AND fecha = (
    SELECT MAX(fecha)
    FROM progreso p2
    WHERE p2.id_paciente = progreso.id_paciente
)ORDER BY imc DESC;


-- ============================================================
-- 4. CONSULTA MULTITABLA
--    Informe completo de evolución de cada paciente:
--    nombre, nutricionista, plan, peso inicial y actual
-- ============================================================

SELECT
    pa.nombre                          AS paciente,
    pa.apellidos,
    pr.nom_completo                    AS nutricionista,
    pl.objetivo                        AS plan,
    pl.estado                          AS estado_plan,
    p_ini.peso                         AS peso_inicial_kg,
    p_act.peso                         AS peso_actual_kg,
    p_ini.peso - p_act.peso            AS kg_perdidos,
    p_ini.imc                          AS imc_inicial,
    p_act.imc                          AS imc_actual,
    p_ini.fecha                        AS fecha_inicio,
    p_act.fecha                        AS fecha_ultimo_control
FROM paciente pa
JOIN plan_dietetico pl  ON pa.id_paciente      = pl.id_paciente
JOIN profesional pr     ON pl.id_nutricionista = pr.id_profesional
JOIN progreso p_ini     ON pa.id_paciente      = p_ini.id_paciente
    AND p_ini.fecha = (
        SELECT MIN(fecha) FROM progreso
        WHERE id_paciente = pa.id_paciente
    )
JOIN progreso p_act     ON pa.id_paciente      = p_act.id_paciente
    AND p_act.fecha = (
        SELECT MAX(fecha) FROM progreso
        WHERE id_paciente = pa.id_paciente
    )
ORDER BY kg_perdidos DESC;


-- ============================================================
-- 5A. INSERT CON SELECT
--     Crear un informe mensual para cada paciente
--     que tiene progreso registrado en junio de 2026
-- ============================================================

INSERT INTO informe (id_paciente, id_profesional, id_plan, id_panel, fecha_gen, fecha_inicio, fecha_fin, tipo)
SELECT DISTINCT
    pr.id_paciente,
    pl.id_nutricionista,
    pl.id_plan,
    pp.id_panel,
    '2026-06-12',
    '2026-06-01',
    '2026-06-12',
    'progreso_paciente'
FROM progreso pr
JOIN plan_dietetico pl  ON pr.id_paciente = pl.id_paciente
JOIN panel_progreso pp  ON pr.id_paciente = pp.id_paciente
WHERE pr.fecha BETWEEN '2026-06-01' AND '2026-06-12';


-- ============================================================
-- 5B. UPDATE CON SELECT
--     Actualizar la fecha del panel de progreso
--     de los pacientes que tienen registros en mayo 2026
-- ============================================================

UPDATE panel_progreso
SET fecha_actualizacion = '2026-06-12'
WHERE id_paciente IN (
    SELECT DISTINCT id_paciente
    FROM progreso
    WHERE fecha BETWEEN '2026-05-01' AND '2026-05-31'
);


-- ============================================================
-- 5C. DELETE CON SELECT
--     Eliminar informes duplicados del mismo tipo,
--     paciente y período, conservando solo el más reciente
-- ============================================================

DELETE FROM informe
WHERE id_informe NOT IN (
    SELECT max_id FROM (
        SELECT MAX(id_informe) AS max_id
        FROM informe
        GROUP BY id_paciente, tipo, fecha_inicio, fecha_fin
    ) AS ultimos
);
