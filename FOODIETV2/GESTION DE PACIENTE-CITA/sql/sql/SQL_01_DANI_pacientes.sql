-- ============================================================
--  FooDiet – SUBSISTEMA 1: GESTIÓN DE PACIENTES
--  Desarrollado por: Dani
--  Ejecutar después de SQL_00_BASE.sql
-- ============================================================
USE FooDiet;

-- Tabla principal de pacientes
-- Correcciones: dni NOT NULL, apellidos en plural, campos nuevos
-- (telefono, email, sexo, altura, fecha_registro), sin tildes
CREATE TABLE paciente (
    id_paciente    INT          NOT NULL AUTO_INCREMENT,
    id_usuario     INT          NOT NULL,
    dni            VARCHAR(9)   NOT NULL,
    nombre         VARCHAR(50)  NOT NULL,
    apellidos      VARCHAR(100) NOT NULL,
    telefono       VARCHAR(15)  NOT NULL,
    email          VARCHAR(100) NOT NULL,
    direccion      VARCHAR(150) NOT NULL,
    fecha_nac      DATE         NOT NULL,
    sexo           CHAR(1)      NOT NULL,
    altura         DECIMAL(5,2) NOT NULL,
    tipo_paciente  VARCHAR(20)  NOT NULL DEFAULT 'adulto',
    fecha_registro DATE         NOT NULL,
    CONSTRAINT PK_paciente         PRIMARY KEY (id_paciente),
    CONSTRAINT UQ_paciente_dni     UNIQUE (dni),
    CONSTRAINT UQ_paciente_email   UNIQUE (email),
    CONSTRAINT FK_paciente_usuario FOREIGN KEY (id_usuario) REFERENCES usuario (id_usuario)
        ON DELETE RESTRICT ON UPDATE CASCADE,
    CONSTRAINT CHK_paciente_sexo   CHECK (sexo IN ('M','F','X')),
    CONSTRAINT CHK_tipo_paciente   CHECK (tipo_paciente IN ('jubilado','adulto','joven'))
);

-- Catálogo de alergias e intolerancias
-- Tabla nueva: el proyecto lo exige pero no estaba en el SQL original
CREATE TABLE alergia (
    id_alergia  INT          NOT NULL AUTO_INCREMENT,
    nombre      VARCHAR(100) NOT NULL,
    tipo        VARCHAR(20)  NOT NULL,
    descripcion VARCHAR(200),
    CONSTRAINT PK_alergia      PRIMARY KEY (id_alergia),
    CONSTRAINT CHK_alergia_tipo CHECK (tipo IN ('alergia','intolerancia','restriccion'))
);

-- Relación N:M entre paciente y alergia
CREATE TABLE paciente_alergia (
    id_paciente INT         NOT NULL,
    id_alergia  INT         NOT NULL,
    severidad   VARCHAR(20) NOT NULL DEFAULT 'moderada',
    CONSTRAINT PK_paciente_alergia PRIMARY KEY (id_paciente, id_alergia),
    CONSTRAINT FK_pac_alergia_pac  FOREIGN KEY (id_paciente) REFERENCES paciente (id_paciente)
        ON DELETE CASCADE ON UPDATE CASCADE,
    CONSTRAINT FK_pac_alergia_aler FOREIGN KEY (id_alergia)  REFERENCES alergia  (id_alergia)
        ON DELETE CASCADE ON UPDATE CASCADE,
    CONSTRAINT CHK_severidad       CHECK (severidad IN ('leve','moderada','grave'))
);
