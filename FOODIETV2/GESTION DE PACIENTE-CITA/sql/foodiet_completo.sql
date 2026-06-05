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
    nombre          VARCHAR(50)  NOT NULL,             -- ampliado de 30 a 50
    apellidos       VARCHAR(100) NOT NULL,             -- "apellido" → "apellidos"
    telefono        VARCHAR(15)  NOT NULL,             -- campo nuevo, era necesario
    email           VARCHAR(100) NOT NULL,             -- campo nuevo
    direccion       VARCHAR(150) NOT NULL,             -- sin tilde; ampliado a 150
    fecha_nac       DATE         NOT NULL,
    sexo            CHAR(1)      NOT NULL,             -- campo nuevo para cálculos
    altura          DECIMAL(5,2) NOT NULL,             -- campo nuevo (metros) para IMC
    tipo_paciente   VARCHAR(20)  NOT NULL DEFAULT 'adulto', -- herencia del diagrama
    fecha_registro  DATE         NOT NULL,             -- campo nuevo: cuándo se registró
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

CREATE TABLE cita (                                 -- nombre en singular
    id_cita         INT          NOT NULL AUTO_INCREMENT,
    id_paciente     INT          NOT NULL,
    id_profesional  INT          NOT NULL,
    id_plan         INT,                            -- nullable: puede no tener plan aún
    fecha_cita      DATE         NOT NULL,
    hora_cita       TIME         NOT NULL,
    estado_cita     VARCHAR(20)  NOT NULL DEFAULT 'pendiente',
    modalidad       VARCHAR(15)  NOT NULL DEFAULT 'presencial', -- campo nuevo (requisito)
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
