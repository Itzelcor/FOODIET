DROP DATABASE IF EXISTS foodiet_planes;
CREATE DATABASE foodiet_planes;
USE foodiet_planes;

CREATE TABLE paciente (
    id_paciente INT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(80) NOT NULL,
    telefono VARCHAR(20)
);

CREATE TABLE dietista (
    id_dietista INT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(80) NOT NULL,
    especialidad VARCHAR(60)
);

CREATE TABLE plan_alimentacion (
    id_plan INT AUTO_INCREMENT PRIMARY KEY,
    id_paciente INT NOT NULL,
    id_dietista INT NOT NULL,
    objetivo VARCHAR(80) NOT NULL,
    fecha_inicio DATE NOT NULL,
    fecha_fin DATE,
    calorias_dia INT,
    observaciones VARCHAR(200),
    FOREIGN KEY (id_paciente) REFERENCES paciente(id_paciente),
    FOREIGN KEY (id_dietista) REFERENCES dietista(id_dietista)
);

CREATE TABLE comida_plan (
    id_comida INT AUTO_INCREMENT PRIMARY KEY,
    id_plan INT NOT NULL,
    dia_semana VARCHAR(20) NOT NULL,
    momento VARCHAR(20) NOT NULL,
    descripcion VARCHAR(150) NOT NULL,
    FOREIGN KEY (id_plan) REFERENCES plan_alimentacion(id_plan)
);

INSERT INTO paciente (nombre, telefono) VALUES
('Ana Garcia Lopez', '612000001'),
('Carlos Ruiz Perez', '612000002');

INSERT INTO dietista (nombre, especialidad) VALUES
('Laura Martinez', 'Nutricion clinica'),
('Carlos Soler', 'Nutricion deportiva');

INSERT INTO plan_alimentacion (id_paciente, id_dietista, objetivo, fecha_inicio, fecha_fin, calorias_dia, observaciones) VALUES
(1, 1, 'Perdida de peso', '2026-01-01', '2026-06-30', 1800, 'Reducir azucar y fritos'),
(2, 2, 'Ganancia muscular', '2026-02-15', '2026-08-15', 2400, 'Aumentar proteina');

INSERT INTO comida_plan (id_plan, dia_semana, momento, descripcion) VALUES
(1, 'Lunes', 'Desayuno', 'Avena con fruta'),
(1, 'Lunes', 'Comida', 'Pollo a la plancha con arroz'),
(1, 'Lunes', 'Cena', 'Verdura al vapor con tortilla'),
(2, 'Martes', 'Desayuno', 'Tostadas con pavo'),
(2, 'Martes', 'Comida', 'Pasta integral con atun'),
(2, 'Martes', 'Cena', 'Salmon con patata cocida');

SELECT p.nombre AS paciente, d.nombre AS dietista, pl.objetivo, pl.calorias_dia
FROM plan_alimentacion pl
JOIN paciente p ON pl.id_paciente = p.id_paciente
JOIN dietista d ON pl.id_dietista = d.id_dietista;
