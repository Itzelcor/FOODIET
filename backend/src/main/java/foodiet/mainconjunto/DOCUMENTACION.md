# Documentación del Proyecto

## Estructura de Paquetes

Cada paquete coincide con la ruta de carpeta donde residen los archivos fuente.

```
P:\danie\mainconjunto\
│
├── Main.java                          (paquete por defecto)
├── PrincipalAdmin.java                (paquete por defecto)
│
├── foodiet\
│   ├── FoodietApp.java                → package foodiet
│   ├── modelo\                        → package foodiet.modelo
│   │   ├── Paciente.java              (abstracta)
│   │   ├── PacienteJoven.java         (extends Paciente)
│   │   ├── PacienteAdulto.java        (extends Paciente)
│   │   ├── PacienteJubilado.java      (extends Paciente)
│   │   ├── Cita.java                  (abstracta)
│   │   ├── CitaOnline.java            (extends Cita)
│   │   ├── CitaPresencial.java        (extends Cita)
│   │   ├── ProfesionalClinica.java
│   │   ├── ServicioClinica.java
│   │   ├── PlanAlimenticio.java
│   │   └── Usuario.java
│   ├── gestion\                       → package foodiet.gestion
│   │   ├── GestionClinica.java
│   │   ├── GestorArchivos.java
│   │   └── Estadisticas.java
│   ├── datos\                         → package foodiet.datos
│   │   ├── ConexionBD.java            (JDBC)
│   │   └── GestorBD.java
│   └── presentacion\                  → package foodiet.presentacion
│       └── MenuPrincipal.java
│
├── hacer_main_comun\
│   ├── Estadisticas\src\              → package hacer_main_comun.Estadisticas.src
│   │   ├── modelo\
│   │   │   ├── Metrica.java           (abstracta)
│   │   │   ├── MetricaPeso.java       (extends Metrica)
│   │   │   ├── MetricaIMC.java        (extends Metrica)
│   │   │   ├── MetricaGrasa.java      (extends Metrica)
│   │   │   ├── Progreso.java
│   │   │   ├── Estadistica.java
│   │   │   ├── Informe.java
│   │   │   └── GestorFicheros.java
│   │   ├── dao\                       → ...Estadisticas.src.dao
│   │   │   └── ProgresoDAO.java
│   │   └── conexionDB\                → ...Estadisticas.src.conexionDB
│   │       └── ConexionBD.java
│   │
│   └── Gestion_del_Equipo_Profesional\src\
│                                       → package hacer_main_comun.Gestion_del_Equipo_Profesional.src
│       ├── model\
│       │   ├── Profesional.java
│       │   ├── Rol.java               (enum)
│       │   ├── AsignacionPaciente.java
│       │   ├── Agenda.java
│       │   ├── TipoEvento.java        (enum)
│       │   ├── Sustitucion.java
│       │   └── GestorProfesionales.java
│       ├── dao\
│       │   ├── ProfesionalDAO.java
│       │   ├── AsignacionPacienteDAO.java
│       │   ├── AgendaDAO.java
│       │   └── SustitucionDAO.java
│       ├── db\
│       │   └── ConexionDB.java
│       └── main\
│           └── Main.java
│
├── COD\                               (paquete por defecto)
│   ├── Plan.java                      (implements Serializable)
│   ├── PlanAlimentacion.java          (abstracta)
│   ├── PlanPerdidaPeso.java           (extends PlanAlimentacion)
│   ├── PlanMantenimiento.java         (extends PlanAlimentacion)
│   └── Main.java
│
└── mysql-connector-j-9.2.0\           (librería JDBC)
```

## Descripción de Clases

### foodiet (Gestión Clínica de Nutrición)

| Clase | Paquete | Descripción |
|---|---|---|
| `Paciente` | `foodiet.modelo` | Abstracta. Almacena datos personales, peso, altura, IMC. Define `tipoPaciente()` abstracto. |
| `PacienteJoven` | `foodiet.modelo` | Extiende `Paciente`. `tipoPaciente()` devuelve `"Joven"` si edad < 30, sino `"Adulto"`. |
| `PacienteAdulto` | `foodiet.modelo` | Extiende `Paciente`. `tipoPaciente()` devuelve `"Adulto"` si edad < 65, sino `"Senior"`. |
| `PacienteJubilado` | `foodiet.modelo` | Extiende `Paciente`. `tipoPaciente()` clasifica por IMC y devuelve categorías de peso. |
| `Cita` | `foodiet.modelo` | Almacena fecha, paciente, profesional, modalidad, estado. Método `mostrarCita()`. |
| `CitaOnline` | `foodiet.modelo` | Extiende `Cita`. Añade enlace de videollamada. `mostrarCita()` incluye el enlace. |
| `CitaPresencial` | `foodiet.modelo` | Extiende `Cita`. Añade sala de consulta. `mostrarCita()` incluye la sala. |
| `ProfesionalClinica` | `foodiet.modelo` | Datos del nutricionista: nombre, especialidad, experiencia, horario, teléfono. |
| `ServicioClinica` | `foodiet.modelo` | Servicios ofrecidos (consulta, plan personalizado, etc.). Método estático `obtenerServicios()` construye array con variable N auxiliar. |
| `PlanAlimenticio` | `foodiet.modelo` | Vincula un paciente con un profesional, con objetivos, descripción y calorías diarias en un rango de fechas. |
| `GestionClinica` | `foodiet.gestion` | Clase central que gestiona arrays estáticos de pacientes, profesionales, citas y planes con capacidad máxima fija y variables N que indican posiciones ocupadas. Integra con `GestorBD`. |
| `GestorArchivos` | `foodiet.gestion` | Escribe logs a `archivos/log.txt` y lee configuración desde `archivos/config.txt`. |
| `Estadisticas` | `foodiet.gestion` | Genera estadísticas generales de la clínica a partir de los datos en `GestionClinica`. |
| `ConexionBD` | `foodiet.datos` | Conexión JDBC a MySQL (`localhost:3306/bd_foodiet`). |
| `GestorBD` | `foodiet.datos` | CRUD contra MySQL: insertar/eliminar pacientes, citas, planes; actualizar estado de citas; obtener listados. |
| `MenuPrincipal` | `foodiet.presentacion` | Interfaz de consola con menú interactivo para gestionar pacientes, citas y profesionales. |
| `FoodietApp` | `foodiet` | Punto de entrada alternativo que inicia el menú principal. |

### hacer_main_comun.Estadisticas.src (Estadísticas y Métricas)

| Clase | Paquete | Descripción |
|---|---|---|
| `Metrica` | `...src.modelo` | Abstracta. Almacena nombrePaciente, valor y fecha. Define `calcular()` y `getTipo()`. |
| `MetricaPeso` | `...src.modelo` | Extiende `Metrica`. Calcula diferencia entre peso anterior y actual; clasifica en "ha perdido", "ha ganado" o "se mantiene". |
| `MetricaIMC` | `...src.modelo` | Extiende `Metrica`. Calcula IMC (peso/altura²) y clasifica en bajo peso, normal, sobrepeso u obesidad. |
| `MetricaGrasa` | `...src.modelo` | Extiende `Metrica`. Calcula porcentaje de grasa corporal según género y clasifica. |
| `Progreso` | `...src.modelo` | DTO con idPaciente, idProfesional, fecha, pesoActual, imcActual y observaciones. |
| `Estadistica` | `...src.modelo` | Recibe una matriz `double[n][4]` con datos de pacientes. Calcula IMC medio, peso medio, mejor paciente (mayor pérdida), pacientes que bajaron de peso. Muestra matriz y resumen. |
| `Informe` | `...src.modelo` | Asocia título, fecha, tipo y una `Estadistica` para generar un reporte completo con resumen y matriz. |
| `GestorFicheros` | `...src.modelo` | Método estático `exportarInforme()` que vuelca una lista de `Progreso` a un fichero de texto. |
| `ProgresoDAO` | `...src.dao` | DAO para `Progreso`. CRUD contra MySQL. |
| `ConexionBD` | `...src.conexionDB` | Conexión JDBC a MySQL. |

### hacer_main_comun.Gestion_del_Equipo_Profesional (Gestión de Equipo)

| Clase | Paquete | Descripción |
|---|---|---|
| `Profesional` | `...src.model` | Datos del profesional: nombre, especialidad, formación, email, teléfono, `Rol`. Con getters/setters y constructor vacío para JDBC. |
| `Rol` | `...src.model` | Enum: `DIRECTIVO`, `NUTRICIONISTA`, `ADMINISTRATIVO`. |
| `AsignacionPaciente` | `...src.model` | Relación profesional-paciente con fecha de inicio. |
| `Agenda` | `...src.model` | Evento en la agenda con fecha y `TipoEvento`. |
| `TipoEvento` | `...src.model` | Enum: `CONSULTA`, `VACACIONES`, `SUSTITUCION`. |
| `Sustitucion` | `...src.model` | Reemplazo entre dos profesionales en un rango de fechas, con motivo. |
| `GestorProfesionales` | `...src.model` | Lógica de negocio: asignar pacientes, gestionar sustituciones, comprobar disponibilidad. |
| `ProfesionalDAO` | `...src.dao` | CRUD de `Profesional` contra MySQL. |
| `AsignacionPacienteDAO` | `...src.dao` | CRUD de `AsignacionPaciente` contra MySQL. |
| `AgendaDAO` | `...src.dao` | CRUD de `Agenda` contra MySQL. |
| `SustitucionDAO` | `...src.dao` | CRUD de `Sustitucion` contra MySQL. |
| `ConexionDB` | `...src.db` | Conexión JDBC a MySQL. |
| `Main` | `...src.main` | Punto de entrada independiente con menú de consola para el subsistema de equipo profesional. |

### PrincipalAdmin (Seguridad y Auditoría) — paquete por defecto

| Clase | Descripción |
|---|---|
| `Usuario` | Clase base con idUsuario, nombre, email. |
| `Administrador` | Extiende `Usuario`. Añade nivel de seguridad (ALTO/MEDIO/BAJO). Revisa registros y notifica incidencias. |
| `GestorAuditoria` | Almacena logs en `ArrayList<String>`. Permite registrar acciones y mostrar logs activos. |
| `ComponenteSeguridad` | Abstracta. Define `ejecutarControl()`. |
| `ControlAccesos` | Extiende `ComponenteSeguridad`. Comprueba si los intentos de acceso superan un umbral. |
| `ControlModificaciones` | Extiende `ComponenteSeguridad`. Comprueba si una modificación está autorizada. |
| `SubsistemaAdministrador` | Coordina controles de seguridad. Los persiste en BD (LOGS_AUDITORIA). Exporta alertas a `informe_errores.txt`. Si no hay BD, usa el ArrayList en memoria. |
| `PrincipalAdmin` | Punto de entrada independiente del subsistema de administrador. |

### COD (Planes Alimenticios) — paquete por defecto

| Clase | Descripción |
|---|---|
| `Plan` | Implementa `Serializable`. Almacena id, paciente, objetivo y calorías. Constructores para creación y para consulta MySQL. Método `mostrar()`. |
| `PlanAlimentacion` | Abstracta. Almacena paciente y calorías. Define `mostrarPlan()`. |
| `PlanPerdidaPeso` | Extiende `PlanAlimentacion`. `mostrarPlan()` muestra "perdida peso". |
| `PlanMantenimiento` | Extiende `PlanAlimentacion`. `mostrarPlan()` muestra "mantenimiento". |
| `Main` | Punto de entrada independiente. Demuestra escritura/lectura de ficheros de texto, binarios, objetos y consulta MySQL. |

## Flujo del Main Principal (`Main.java`)

El `Main.java` raíz orquesta todos los subsistemas en 15 bloques secuenciales:

1. **Auditoría** → Crea `GestorAuditoria`, `Administrador`, ejecuta controles de acceso y modificaciones vía `SubsistemaAdministrador`.
2. **Gestión Clínica** → Instancia `GestionClinica`.
3. **Pacientes** (polimorfismo) → Crea `PacienteJoven`, `PacienteAdulto`, `PacienteJubilado` y los añade a la clínica.
4. **Profesionales** → Crea dos `ProfesionalClinica` y los añade.
5. **Citas** (polimorfismo) → Crea `CitaOnline` y `CitaPresencial`, las añade.
6. **Planes Alimenticios** → Crea dos `PlanAlimenticio` vinculando pacientes con profesionales.
7. **Servicios** → `ServicioClinica.obtenerServicios()` construye array estático con variable N auxiliar y los muestra.
8. **Métricas** (polimorfismo) → Crea `MetricaPeso`, `MetricaIMC`, `MetricaGrasa` y las muestra.
9. **Estadísticas** → Construye matriz `double[n][4]` con datos de pacientes y crea `Estadistica`.
10. **Informe** → `Informe.generarInforme()` imprime resumen y matriz.
11. **Equipo Profesional** → Crea `Profesional` (con `Rol`), `AsignacionPaciente`, `Agenda` (con `TipoEvento`), `Sustitucion`.
12. **Archivos** → `GestorArchivos` escribe log y lee configuración.
13. **Exportación** → `GestorFicheros.exportarInforme()` vuelca `Progreso` a fichero.
14. **Recorrido con N auxiliar** → Itera sobre pacientes, profesionales, citas y planes usando las variables N de `GestionClinica`.
15. **Cierre** → Registra fin en auditoría, exporta alertas a `informe_errores.txt`.

## Jerarquías de Herencia

### foodiet.modelo
```
Paciente (abstracta) ← PacienteJoven, PacienteAdulto, PacienteJubilado
Cita (abstracta)     ← CitaOnline, CitaPresencial
```

### hacer_main_comun.Estadisticas.src.modelo
```
Metrica (abstracta) ← MetricaPeso, MetricaIMC, MetricaGrasa
```

### COD
```
PlanAlimentacion (abstracta) ← PlanPerdidaPeso, PlanMantenimiento
Plan (implements Serializable) — clase independiente
```

## Conflictos de Importación

En `Main.java` se omitieron dos clases por conflicto de nombre:

| Clase omitida | Motivo |
|---|---|
| `hacer_main_comun.Estadisticas.src.conexionDB.ConexionBD` | Coincide con `foodiet.datos.ConexionBD` |
| `hacer_main_comun.Gestion_del_Equipo_Profesional.src.main.Main` | Coincide con la propia clase `Main` del proyecto |

## Compilación

Todos los `.java` se compilan desde la raíz (`P:\danie\mainconjunto\`) con:

```
javac -d build -cp ".;mysql-connector-j-9.2.0\mysql-connector-j-9.2.0.jar" ^
    Main.java PrincipalAdmin.java ^
    foodiet\FoodietApp.java foodiet\modelo\*.java foodiet\gestion\*.java ^
    foodiet\datos\*.java foodiet\presentacion\MenuPrincipal.java ^
    hacer_main_comun\Estadisticas\src\modelo\*.java ^
    hacer_main_comun\Estadisticas\src\dao\ProgresoDAO.java ^
    hacer_main_comun\Estadisticas\src\conexionDB\ConexionBD.java ^
    hacer_main_comun\Gestion_del_Equipo_Profesional\src\model\*.java ^
    hacer_main_comun\Gestion_del_Equipo_Profesional\src\dao\*.java ^
    hacer_main_comun\Gestion_del_Equipo_Profesional\src\db\ConexionDB.java ^
    hacer_main_comun\Gestion_del_Equipo_Profesional\src\main\Main.java ^
    COD\Plan.java COD\PlanAlimentacion.java COD\PlanPerdidaPeso.java ^
    COD\PlanMantenimiento.java
```

**Nota**: `COD\Main.java` se omite de la compilación conjunta porque duplica la clase `Main`.

## Ejecución

```bash
java -cp "build;mysql-connector-j-9.2.0\mysql-connector-j-9.2.0.jar" Main
```

Requiere MySQL con el driver `com.mysql.cj.jdbc.Driver` en el classpath.
