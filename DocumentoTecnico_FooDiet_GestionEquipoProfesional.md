# FooDiet — Gestión del Equipo Profesional
### Documento Técnico del Subsistema
**Sergio · 1º DAW · IES Font de San Lluis · 2025-2026**

---

## 1. Introducción

### 1.1 Descripción general del proyecto

FooDiet es una plataforma web integral diseñada para la gestión de una clínica de nutrición y dietética personalizada. El sistema permite a los pacientes registrarse, solicitar consultas con nutricionistas, recibir planes de alimentación adaptados a sus necesidades y realizar un seguimiento continuo de sus progresos. La plataforma cubre tanto la atención presencial como la modalidad online.

El proyecto se desarrolla en el marco del módulo del proyecto intermodular de 1º DAW del IES Font de San Lluis durante el curso 2025-2026. Está dividido en subsistemas, cada uno responsabilidad de un alumno diferente, que en conjunto conforman la solución completa.

### 1.2 Subsistemas del sistema

Foodiet se articula en cuatro subsistemas principales:

- **Gestión de Pacientes y Citas** — registro de pacientes, reserva de consultas y gestión de pagos.
- **Gestión de Planes de Alimentación** — creación de dietas, seguimiento nutricional y control de alergias.
- **Gestión del Equipo Profesional** — perfiles de profesionales, agenda, vacaciones y control de acceso. *(Este subsistema)*
- **Administración y Estadísticas** — métricas de negocio, informes y exportación de datos.

### 1.3 Ámbito del subsistema

Este documento describe exclusivamente el subsistema de Gestión del Equipo Profesional, que cubre las siguientes funcionalidades definidas en el guion del proyecto:

- Registro y mantenimiento de perfiles de profesionales (nombre, especialidad, horarios, formación).
- Asignación de pacientes a nutricionistas.
- Gestión de agenda semanal, vacaciones y sustituciones.
- Control de acceso a los historiales de pacientes según el rol del profesional.

---

## 2. Modelo de datos

### 2.1 Entidades del subsistema

El modelo de datos se ha diseñado siguiendo los principios de normalización hasta la Tercera Forma Normal (3FN), evitando redundancias y garantizando la integridad referencial mediante claves foráneas.

| Entidad / Tabla | Atributos principales | Relaciones |
|---|---|---|
| `profesionales` | id_profesional (PK), nombre, apellidos, email, telefono, especialidad, formacion, fecha_alta, activo | 1:N con horarios, vacaciones, asignaciones |
| `roles` | id_rol (PK), nombre_rol (DIRECTIVO, NUTRICIONISTA, ADMINISTRATIVO) | N:M con profesionales via profesional_rol |
| `profesional_rol` | id_profesional (FK), id_rol (FK) | Tabla intermedia N:M |
| `horarios` | id_horario (PK), id_profesional (FK), dia_semana, hora_inicio, hora_fin | N:1 con profesionales |
| `vacaciones_sustituciones` | id_vacacion (PK), id_profesional (FK), id_sustituto (FK), fecha_inicio, fecha_fin, motivo, tipo | N:1 con profesionales (profesional y sustituto) |
| `asignaciones_paciente` | id_asignacion (PK), id_profesional (FK), id_paciente (FK\*), fecha_asignacion, activa | N:1 con profesionales; FK\* pendiente de Dani |

> **Nota:** La clave foránea `id_paciente` de la tabla `asignaciones_paciente` referencia la tabla `pacientes` del subsistema de Gestión de Pacientes y Citas (responsabilidad de Dani). Esta dependencia está marcada con un comentario `TODO` en el script SQL y se resolverá una vez confirmada la estructura de dicha tabla.

### 2.2 Diagrama Entidad-Relación (ERD)

```
Diagrama E/R
  profesionales ||--o{ profesional_rol         : "tiene"
  roles         ||--o{ profesional_rol         : "asignado a"
  profesionales ||--o{ horarios                : "trabaja en"
  profesionales ||--o{ vacaciones_sustituciones: "solicita"
  profesionales ||--o{ asignaciones_paciente   : "atiende"
  pacientes     ||--o{ asignaciones_paciente   : "es asignado (* TODO)"
```

### 2.3 Decisiones de diseño

**Tabla intermedia `profesional_rol`:** la relación entre profesionales y roles es de tipo N:M, ya que un profesional puede tener más de un rol y un rol puede asignarse a varios profesionales. Se ha optado por una tabla de unión explícita para respetar la normalización.

**Campo `id_sustituto` en `vacaciones_sustituciones`:** esta columna es una clave foránea que referencia la misma tabla `profesionales`, permitiendo registrar qué profesional cubre a otro durante su ausencia sin necesidad de crear una tabla adicional.

**Campo `activo` en `profesionales`:** en lugar de eliminar físicamente un profesional, se marca como inactivo. Esto preserva el historial de asignaciones y citas anteriores.

**Control de acceso por rol en Java:** el filtrado por rol (DIRECTIVO, NUTRICIONISTA, ADMINISTRATIVO) se implementa en la capa DAO del backend, añadiendo cláusulas WHERE condicionadas al rol recibido como parámetro, sin vistas ni procedimientos almacenados, para mantener la solución dentro del alcance del nivel 1º DAW.

---

## 3. Backend — Implementación en Java

### 3.1 Estructura del proyecto

El backend está desarrollado en Java sin frameworks externos, siguiendo una arquitectura en tres capas que separa claramente las responsabilidades.

| Paquete | Descripción |
|---|---|
| `modelo` | Clases POJO que representan cada entidad de la base de datos: Profesional, Rol, Horario, VacacionSustitucion, AsignacionPaciente. |
| `dao` | Clases de acceso a datos (Data Access Object). Contienen todos los métodos CRUD y se conectan a MySQL mediante JDBC. |
| `util` | Clase de utilidad para la conexión a la base de datos (ConexionBD). Centraliza la cadena de conexión y las credenciales. |
| `main` | Clase principal con menú interactivo por consola (Scanner). Actúa como punto de entrada y demuestra el funcionamiento del sistema. |

### 3.2 Clases del modelo

Cada clase del paquete `modelo` se corresponde directamente con una tabla de la base de datos. Los atributos de la clase coinciden con las columnas de la tabla, e incluyen constructores, getters y setters.

- **Profesional** — id, nombre, apellidos, email, teléfono, especialidad, formación, fechaAlta, activo.
- **Rol** — id, nombreRol. Valores posibles: DIRECTIVO, NUTRICIONISTA, ADMINISTRATIVO.
- **Horario** — id, idProfesional, diaSemana, horaInicio, horaFin.
- **VacacionSustitucion** — id, idProfesional, idSustituto, fechaInicio, fechaFin, motivo, tipo.
- **AsignacionPaciente** — id, idProfesional, idPaciente *(TODO)*, fechaAsignacion, activa.

### 3.3 Capa DAO — operaciones CRUD

Todas las consultas se realizan mediante `PreparedStatement` para evitar inyección SQL.

| Clase DAO | Operaciones implementadas |
|---|---|
| `ProfesionalDAO` | insertar, listarTodos, listarActivos, buscarPorId, actualizar, darDeBaja (baja lógica), eliminar. |
| `RolDAO` | insertar, listarTodos, asignarRolAProfesional, obtenerRolesDeProfesional. |
| `HorarioDAO` | insertar, listarPorProfesional, actualizar, eliminar. |
| `VacacionSustitucionDAO` | insertar, listarPorProfesional, listarTodas, actualizar, eliminar. |
| `AsignacionPacienteDAO` | insertar, listarPorProfesional, listarActivas, desactivar, eliminar. |

### 3.4 Control de acceso por rol

Según el rol del usuario, los métodos DAO filtran o restringen la información disponible:

- **DIRECTIVO** — acceso completo a todos los profesionales, horarios, vacaciones y asignaciones.
- **NUTRICIONISTA** — acceso a su propia agenda, vacaciones y a los pacientes que tiene asignados.
- **ADMINISTRATIVO** — acceso a horarios y vacaciones de todos los profesionales, sin acceso a historiales clínicos.

### 3.5 Conexión a la base de datos

La clase `ConexionBD` del paquete `util` centraliza la URL de conexión, el usuario y la contraseña, y expone un método estático `getConexion()` que devuelve un objeto `Connection` de JDBC. Todos los métodos DAO obtienen la conexión a través de este único punto.

---

## 4. Base de datos

### 4.1 Script SQL

El script incluye los siguientes bloques, en el orden necesario para respetar las dependencias entre claves foráneas:

1. `CREATE DATABASE` y `USE` — creación y selección del esquema `nutriplus_db`.
2. `CREATE TABLE roles` — tabla base sin dependencias externas.
3. `CREATE TABLE profesionales` — entidad principal del subsistema.
4. `CREATE TABLE profesional_rol` — tabla intermedia N:M con FK a profesionales y roles.
5. `CREATE TABLE horarios` — FK a profesionales.
6. `CREATE TABLE vacaciones_sustituciones` — FK doble a profesionales (profesional y sustituto).
7. `CREATE TABLE asignaciones_paciente` — FK a profesionales y FK TODO a pacientes (Dani).
8. `INSERT INTO` — datos de muestra para todas las tablas.
9. Consultas `SELECT` de verificación al final del script.

### 4.2 Integridad referencial

Todas las relaciones entre tablas están definidas mediante restricciones `FOREIGN KEY` con las cláusulas `ON DELETE` y `ON UPDATE` apropiadas. La tabla `asignaciones_paciente` incluye un bloque comentado con la FK hacia la tabla `pacientes`, que se activará una vez confirmada la estructura de ese subsistema.

### 4.3 Datos de muestra

El script incluye datos de prueba suficientes para demostrar el funcionamiento del sistema: tres profesionales con distintos roles, horarios semanales completos, una solicitud de vacaciones con sustituto asignado y varias asignaciones de pacientes activas.

---

## 5. Interfaz web — Frontend

### 5.1 Tecnologías utilizadas

La interfaz web está desarrollada en HTML5 y CSS3, con Bootstrap 5 incluido mediante CDN. No se ha utilizado ningún framework JavaScript adicional, manteniendo la solución dentro del alcance del nivel académico.

### 5.2 Estructura del archivo

El frontend se entrega como un único archivo HTML bien comentado, organizado en cinco secciones funcionales:

| Sección | Contenido |
|---|---|
| Dashboard | Tarjetas de acceso rápido: total de profesionales, citas del día, vacaciones pendientes y asignaciones activas. |
| Listado de profesionales | Tabla con todos los profesionales. Botones de editar y dar de baja. Modal Bootstrap para añadir o modificar. |
| Horarios | Tabla semanal con los turnos de cada profesional. Modal para crear o editar un bloque horario. |
| Vacaciones y sustituciones | Tabla de solicitudes de ausencia con el sustituto asignado. Modal de registro de nueva solicitud. |
| Asignación de pacientes | Tabla de asignaciones activas entre profesionales y pacientes. Modal para crear o desactivar una asignación. |

### 5.3 Diseño visual

El diseño sigue la referencia visual definida en el wireframe del proyecto:

- Barra de navegación superior en verde oscuro (`#1B4D3E`) con el logotipo FooDiet.
- Contenido principal en tarjetas blancas con sombra suave sobre fondo gris claro.
- Botones de acción en verde, coherentes con la identidad visual de la clínica.
- Tablas con cabecera en verde oscuro y texto blanco, filas alternas en gris claro.
- Pie de página centrado con el texto `© 2026 FooDiet`.

### 5.4 Formularios y validación

Todos los formularios de alta y edición se presentan dentro de modales Bootstrap. Los campos obligatorios están marcados con el atributo `required` de HTML5, activando la validación nativa del navegador. Los tipos de campo (`date`, `email`, `time`, `select`) refuerzan la validación de formato en origen.

---

## 6. Coherencia entre capas

Una de las exigencias clave del proyecto es que los tres entregables sean coherentes entre sí. La siguiente tabla muestra la correspondencia directa:

| Entidad | Tabla MySQL | Clase Java | Sección frontend |
|---|---|---|---|
| Profesional | `profesionales` | `Profesional` + `ProfesionalDAO` | Listado de profesionales |
| Rol | `roles` + `profesional_rol` | `Rol` + `RolDAO` | Filtro por rol en navbar |
| Horario | `horarios` | `Horario` + `HorarioDAO` | Horarios |
| Vacación/Sustitución | `vacaciones_sustituciones` | `VacacionSustitucion` + DAO | Vacaciones y sustituciones |
| Asignación | `asignaciones_paciente` | `AsignacionPaciente` + DAO | Asignación de pacientes |

---

## 7. Dependencias e integración con otros subsistemas

El subsistema de Gestión del Equipo Profesional interactúa con el subsistema de Gestión de Pacientes y Citas (responsabilidad de Dani Dimitrov) a través de la clave foránea `id_paciente` en la tabla `asignaciones_paciente`.

Esta dependencia está señalizada en todos los entregables:

- **Script SQL:** bloque comentado con la FK y un comentario `TODO` que indica qué tabla referencia y por qué está pendiente.
- **Backend Java:** la clase `AsignacionPaciente` incluye el atributo `idPaciente` con un comentario `TODO` que documenta la dependencia.
- **Frontend:** la sección de asignaciones muestra el campo `id_paciente` como campo de texto libre hasta que se confirme la integración.

El resto del subsistema es completamente autónomo y funcional. La dependencia solo afecta a la función de asignación de pacientes a profesionales.

---

## 8. Conclusión

El subsistema de Gestión del Equipo Profesional de NutriPlus cubre todos los requisitos definidos en el guion del proyecto: registro de profesionales, gestión de horarios y vacaciones, asignación de pacientes y control de acceso por rol.

La solución se ha desarrollado con tres capas bien diferenciadas y coherentes entre sí: una base de datos MySQL normalizada, un backend en Java con CRUD completo mediante JDBC, y una interfaz web en HTML5/CSS3 con Bootstrap 5. Todas las decisiones de diseño priorizan la claridad y la simplicidad, acordes con el nivel académico de 1º DAW.

La única dependencia externa pendiente — la clave foránea con la tabla `pacientes` — está debidamente documentada y señalizada en los tres entregables, de forma que su integración futura no requerirá cambios estructurales significativos.

---

*© 2026 FooDiet / NutriPlus · IES Font de San Lluis · 1º DAW*
