-- ============================================================
--  FooDiet – SUBSISTEMA 4: ESTADÍSTICAS
--  Desarrollado por: Itzel
--  Ejecutar después de SQL_03_TAVI_planes.sql
-- ============================================================
USE FooDiet;

-- Registro periódico de métricas del paciente
-- Correcciones: fecha NOT NULL, CHECK peso > 0, añadido imc
CREATE TABLE progreso (
    id_progreso   INT          NOT NULL AUTO_INCREMENT,
    id_paciente   INT          NOT NULL,
    id_plan       INT          NOT NULL,
    fecha         DATE         NOT NULL,
    peso          DECIMAL(5,2) NOT NULL,
    imc           DECIMAL(5,2),
    observaciones TEXT,
    CONSTRAINT PK_progreso          PRIMARY KEY (id_progreso),
    CONSTRAINT FK_progreso_paciente FOREIGN KEY (id_paciente) REFERENCES paciente       (id_paciente)
        ON DELETE RESTRICT ON UPDATE CASCADE,
    CONSTRAINT FK_progreso_plan     FOREIGN KEY (id_plan)     REFERENCES plan_dietetico (id_plan)
        ON DELETE RESTRICT ON UPDATE CASCADE,
    CONSTRAINT CHK_progreso_peso    CHECK (peso > 0)
);

-- Panel de visualización del progreso de un paciente
-- Correcciones: añadido id_paciente (faltaba en el original)
CREATE TABLE panel_progreso (
    id_panel            INT  NOT NULL AUTO_INCREMENT,
    id_paciente         INT  NOT NULL,
    fecha_actualizacion DATE NOT NULL,
    CONSTRAINT PK_panel_progreso PRIMARY KEY (id_panel),
    CONSTRAINT FK_panel_paciente FOREIGN KEY (id_paciente) REFERENCES paciente (id_paciente)
        ON DELETE RESTRICT ON UPDATE CASCADE
);

-- Registro histórico de métricas específicas (IMC, % grasa, etc.)
-- Correcciones: añadido tipo_metrica para diferenciar qué se mide
CREATE TABLE registro_metrica (
    id_registro  INT           NOT NULL AUTO_INCREMENT,
    id_progreso  INT           NOT NULL,
    id_panel     INT           NOT NULL,
    tipo_metrica VARCHAR(50)   NOT NULL,
    valor        DECIMAL(10,2) NOT NULL,
    fecha_calculo DATE         NOT NULL,
    CONSTRAINT PK_registro_metrica PRIMARY KEY (id_registro),
    CONSTRAINT FK_reg_met_progreso  FOREIGN KEY (id_progreso) REFERENCES progreso      (id_progreso)
        ON DELETE RESTRICT ON UPDATE CASCADE,
    CONSTRAINT FK_reg_met_panel     FOREIGN KEY (id_panel)    REFERENCES panel_progreso (id_panel)
        ON DELETE RESTRICT ON UPDATE CASCADE,
    CONSTRAINT CHK_reg_met_tipo     CHECK (tipo_metrica IN ('imc','peso','grasa_corporal','masa_muscular','perimetro_cintura','otro'))
);

-- Informes generados por el sistema
-- Correcciones: eliminadas FK innecesarias a comida y alimento,
-- añadido CHECK en tipo de informe
CREATE TABLE informe (
    id_informe     INT         NOT NULL AUTO_INCREMENT,
    id_paciente    INT,
    id_profesional INT,
    id_cita        INT,
    id_plan        INT,
    id_panel       INT,
    fecha_gen      DATE        NOT NULL,
    fecha_inicio   DATE        NOT NULL,
    fecha_fin      DATE        NOT NULL,
    tipo           VARCHAR(30) NOT NULL,
    CONSTRAINT PK_informe             PRIMARY KEY (id_informe),
    CONSTRAINT FK_informe_paciente    FOREIGN KEY (id_paciente)    REFERENCES paciente       (id_paciente)
        ON DELETE SET NULL ON UPDATE CASCADE,
    CONSTRAINT FK_informe_profesional FOREIGN KEY (id_profesional) REFERENCES profesional    (id_profesional)
        ON DELETE SET NULL ON UPDATE CASCADE,
    CONSTRAINT FK_informe_cita        FOREIGN KEY (id_cita)        REFERENCES cita           (id_cita)
        ON DELETE SET NULL ON UPDATE CASCADE,
    CONSTRAINT FK_informe_plan        FOREIGN KEY (id_plan)        REFERENCES plan_dietetico (id_plan)
        ON DELETE SET NULL ON UPDATE CASCADE,
    CONSTRAINT FK_informe_panel       FOREIGN KEY (id_panel)       REFERENCES panel_progreso (id_panel)
        ON DELETE SET NULL ON UPDATE CASCADE,
    CONSTRAINT CHK_informe_tipo       CHECK (tipo IN ('progreso_paciente','rendimiento_profesional','estadisticas_negocio','plan_dietetico','otro')),
    CONSTRAINT CHK_informe_fechas     CHECK (fecha_fin >= fecha_inicio)
);
