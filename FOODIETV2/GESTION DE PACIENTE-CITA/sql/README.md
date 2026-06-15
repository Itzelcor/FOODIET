# FooDiet — Sistema Integral de Gestión Nutricional y Dietética

Aplicación de escritorio desarrollada en Java con integración de base de datos MySQL y frontend web para la administración de consultas de dietética y nutrición. Permite gestionar pacientes, citas (presenciales y online), profesionales sanitarios, planes alimenticios personalizados, pagos y estadísticas del negocio, con persistencia relacional y exportación a ficheros de texto.

---

## Módulo: Programación

### Lenguaje

- **Java 17+** (JDK 17 o superior)
- Paradigma: **Programación Orientada a Objetos (POO)** con herencia, polimorfismo y encapsulamiento
- Librería estándar: `java.io`, `java.sql`, `java.util`

### Arquitectura del Código

El backend se organiza en **4 paquetes** dentro de `src/com/foodiet/`:

```
src/com/foodiet/
├── FoodietApp.java                  # Punto de entrada (main)
├── modelo/                          # Clases del dominio
│   ├── Paciente.java                #   Abstracta: IMC, nombreCompleto, tipoPaciente abstracto
│   ├── PacienteJoven.java           #   Subtipo que retorna "Joven"
│   ├── PacienteAdulto.java          #   Subtipo que retorna "Adulto"
│   ├── PacienteJubilado.java        #   Subtipo que retorna "Jubilado"
│   ├── Cita.java                    #   Fecha, modalidad, estado, mostrarCita()
│   ├── CitaOnline.java              #   Hereda Cita + plataforma y enlaceVideollamada
│   ├── CitaPresencial.java          #   Hereda Cita + sala y direccionClinica
│   ├── ProfesionalClinica.java      #   Nombre, especialidad, años experiencia
│   ├── PlanAlimenticio.java         #   Fechas, objetivo, calorías diarias
│   ├── ServicioClinica.java         #   Catálogo fijo con array + variable N
│   └── Usuario.java                 #   Autenticación y roles
├── datos/                           # Capa de persistencia (JDBC)
│   ├── ConexionBD.java              #   Singleton de conexión MySQL
│   └── GestorBD.java                #   CRUD unificado con PreparedStatement
├── gestion/                         # Lógica de negocio
│   ├── GestionClinica.java          #   Arrays con variable N (pacientes, citas, etc.)
│   ├── Estadisticas.java            #   Consultas agregadas SQL (COUNT, SUM, GROUP BY)
│   └── GestorArchivos.java          #   Exportación TXT y log del sistema
└── presentacion/                    # Interfaz de consola
    └── MenuPrincipal.java           #   Menús anidados por módulo funcional
```

### Clases y Métodos Clave

| Clase | Método destacado | Descripción |
|---|---|---|
| `Paciente` | `calcularIMC()` | `peso / (altura²)` — método concreto en clase abstracta |
| `Paciente` | `tipoPaciente()` | Método abstracto implementado por cada subtipo |
| `Cita` | `mostrarCita()` | Renderiza toda la información de la cita como cadena |
| `CitaOnline` | `mostrarCita()` | Extiende al padre añadiendo plataforma y enlace |
| `CitaPresencial` | `mostrarCita()` | Extiende al padre añadiendo sala y dirección |
| `GestionClinica` | `añadirPaciente()` | Inserta en BD y almacena en array local con N |
| `GestionClinica` | `buscarPacientePorNombre()` | Búsqueda lineal con `equalsIgnoreCase` |
| `GestionClinica` | `eliminarPaciente()` | Borra en BD, desplaza elementos en el array, decrementa N |
| `GestorBD` | `insertarCita()` | `PreparedStatement` con `RETURN_GENERATED_KEYS` |
| `GestorBD` | `autenticarUsuario()` | Consulta con `SHA2(?, 256)` para hash de contraseña |
| `ServicioClinica` | `obtenerServicios()` | Construye array de 6 servicios con variable `N` |
| `Estadisticas` | `mostrarIngresosPorNutricionista()` | `LEFT JOIN` con agregación y ordenación |

### Lógica de Negocio y Algoritmos

- **Gestión de arrays con variable N**: `GestionClinica` mantiene arrays tradicionales (`Paciente[]`, `Cita[]`, etc.) y una variable `N` por cada uno que indica cuántas posiciones están ocupadas realmente, cumpliendo restricciones de programación (sin `break` fuera de `switch`, sin `continue`, un solo `return` por método, sin modificar la variable de control del bucle `for`).

```java
// Ejemplo: inserción controlada por variable N en GestionClinica
public boolean añadirPaciente(Paciente p) {
    boolean resultado = false;
    if (pacientesN < MAX_PACIENTES) {
        int idBD = gestorBD.insertarPaciente(p);
        if (idBD > 0) {
            p.setId(idBD);
            this.pacientes[pacientesN] = p;
            this.pacientesN++;
            resultado = true;
        }
    }
    return resultado;
}
```

- **Polimorfismo con paciente**: `Paciente` es abstracta; `PacienteJoven`, `PacienteAdulto` y `PacienteJubilado` implementan `tipoPaciente()` y heredan `calcularIMC()` y `nombreCompleto()`.

```java
Paciente p = new PacienteJoven("Ana", "Lopez", 19, 58.0, 1.65);
System.out.println(p.tipoPaciente());  // "Joven"
System.out.println(p.calcularIMC());   // 58.0 / (1.65 * 1.65)
```

- **Polimorfismo con cita**: `CitaOnline` y `CitaPresencial` sobrescriben `mostrarCita()` añadiendo datos específicos de cada modalidad.

- **Auto-ID desde BD**: Tras insertar, el ID generado por MySQL se recupera mediante `getGeneratedKeys()` y se asigna al objeto en memoria.

```java
PreparedStatement ps = conexion.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
ps.executeUpdate();
ResultSet rs = ps.getGeneratedKeys();
if (rs.next()) { idGenerado = rs.getInt(1); }
```

- **Catálogo con variable N**: `ServicioClinica.obtenerServicios()` usa un array de 6 posiciones y una variable `N` en lugar de `ArrayList`.

```java
int N = 0;
ServicioClinica[] lista = new ServicioClinica[6];
while (N < lista.length) {
    lista[N] = new ServicioClinica(datos[N][0], ...);
    N++;
}
```

---

## Módulo: Base de Datos

### Sistema Gestor

- **MySQL 8.0+**
- Conector JDBC: `com.mysql.cj.jdbc.Driver`
- Conexión desde Java mediante `DriverManager.getConnection()` con patrón Singleton en `ConexionBD.java`

```java
// ConexionBD.java — Configuración por defecto
private static final String URL = "jdbc:mysql://localhost:3306/Foodiet";
private static final String USUARIO = "root";
private static final String CONTRASEÑA = "";
```

### Estructura de la Base de Datos

El esquema relacional se compone de **7 tablas** en la base de datos `Foodiet`:

| Tabla | Propósito | Clave foránea |
|---|---|---|
| `usuarios` | Autenticación con roles (administrador, nutricionista, paciente) | — |
| `pacientes` | Datos personales, clínicos y tipo (Joven/Adulto/Jubilado) | `id_usuario → usuarios` |
| `profesionales` | Nutricionistas con especialidad, horario, experiencia | `id_usuario → usuarios` |
| `citas` | Reservas con fecha, hora, modalidad y estado | `id_paciente → pacientes`, `id_profesional → profesionales` |
| `pagos` | Facturación asociada a cada cita | `id_cita → citas` |
| `planes_alimentacion` | Planes con objetivo, descripción y calorías | `id_paciente → pacientes`, `id_profesional → profesionales` |
| `seguimiento_planes` | Evolución de peso e IMC por plan | `id_plan → planes_alimentacion` |

### Diagrama de Relaciones

```
usuarios (1) ──→ pacientes (1) ──→ citas (1) ──→ pagos
    │                                    │
    └────→ profesionales (1) ───────────┘
                                        │
                              planes_alimentacion ──→ seguimiento_planes
```

### Scripts SQL

| Archivo | Contenido |
|---|---|
| `sql/foodiet_completo.sql` | `CREATE DATABASE`, `CREATE TABLE` con claves y restricciones, `INSERT` con datos de ejemplo (3 profesionales, 3 pacientes, 4 citas, 3 pagos, 2 planes, 4 seguimientos) |
| `sql/foodiet_consultas.sql` | Batería de consultas `SELECT` analíticas (historial pacientes, ingresos por profesional, distribución de modalidad, próximas citas) |
| `sql/SQL_01_DANI_pacientes.sql` | Script auxiliar adicional |

### Ejemplos de Consultas SQL Representativas

```sql
-- Insertar paciente con tipo polimórfico
INSERT INTO pacientes (id_usuario, nombre, apellido, edad, peso, altura,
    telefono, historial_medico, objetivos_nutricionales, tipo_paciente)
VALUES (5, 'Ana', 'López', 19, 58.00, 1.65,
    '645678901', 'Sin antecedentes', 'Mejorar hábitos alimenticios', 'Joven');

-- Ingresos agregados por nutricionista (JOIN + GROUP BY)
SELECT CONCAT(pr.nombre, ' ', pr.apellido) AS nutricionista,
       COUNT(c.id_cita) AS citas,
       COALESCE(SUM(p.monto), 0) AS ingresos
FROM profesionales pr
LEFT JOIN citas c ON pr.id_profesional = c.id_profesional AND c.estado = 'completada'
LEFT JOIN pagos p ON c.id_cita = p.id_cita AND p.estado_pago = 'pagado'
GROUP BY pr.id_profesional
ORDER BY ingresos DESC;

-- Próximas citas en 7 días
SELECT CONCAT(p.nombre, ' ', p.apellido) AS paciente,
       CONCAT(pr.nombre, ' ', pr.apellido) AS nutri,
       c.fecha_cita, c.hora_cita, c.modalidad
FROM citas c
JOIN pacientes p ON c.id_paciente = p.id_paciente
JOIN profesionales pr ON c.id_profesional = pr.id_profesional
WHERE c.estado IN ('pendiente', 'confirmada')
  AND c.fecha_cita BETWEEN CURDATE() AND DATE_ADD(CURDATE(), INTERVAL 7 DAY)
ORDER BY c.fecha_cita;
```

---

## Módulo: Entornos de Desarrollo (Testing)

### Framework de Pruebas

- **JUnit 5** (Jupiter API) — `org.junit.jupiter.api`
- 8 clases de prueba ubicadas en `test/com/foodiet/`

### Clases de Test y Cobertura

| Clase de Test | Paquete | Métodos probados |
|---|---|---|
| `PacienteTest` | `modelo` | Constructores (3 subtipos), `tipoPaciente()`, `calcularIMC()`, `nombreCompleto()`, `toString()`, getters/setters, array con `N` |
| `CitaTest` | `modelo` | Constructores (online/presencial/base), `mostrarCita()`, `fecha()`, getters/setters, polimorfismo |
| `ServicioClinicaTest` | `modelo` | `obtenerServicios()` con array + `N`, `mostrarServicio()`, verificación de 6 servicios con 3 presenciales y 3 online |
| `ProfesionalClinicaTest` | `modelo` | Constructores, `nombreCompleto()`, getters |
| `PlanAlimenticioTest` | `modelo` | Constructores, `mostrarPlan()`, setters, caso con `fechaFin = null` |
| `UsuarioTest` | `modelo` | Constructor con 3 roles, `toString()`, `isActivo()` |
| `GestorArchivosTest` | `gestion` | `leerConfiguracion()`, `escribirLog()` + `leerLog()`, `exportarCitas()`, getters de rutas |
| `GestionClinicaTest` | `gestion` | Arrays con `N` (añadir profesional incrementa N, límite 20, índices inválidos devuelven null), valores iniciales a 0 |

### Ejemplo de Test (PacienteTest — array con variable N)

```java
@Test
public void testCalcularIMCDiferentesValores() {
    PacienteAdulto[] casos = new PacienteAdulto[3];
    int casosN = 0;
    casos[casosN] = new PacienteAdulto("A", "B", 30, 70.0, 1.75);
    casosN++;
    casos[casosN] = new PacienteAdulto("C", "D", 40, 90.0, 1.80);
    casosN++;
    casos[casosN] = new PacienteAdulto("E", "F", 25, 55.0, 1.60);
    casosN++;
    double[] esperados = new double[3];
    esperados[0] = 70.0 / (1.75 * 1.75);
    esperados[1] = 90.0 / (1.80 * 1.80);
    esperados[2] = 55.0 / (1.60 * 1.60);
    for (int i = 0; i < casosN; i++) {
        assertEquals(esperados[i], casos[i].calcularIMC(), 0.001);
    }
}
```

### Herramientas y Control de Versiones

- **Control de versiones**: Git (rama `main`)
- **Fichero `.gitignore`**: excluye `*.class`, `*.jar`, `*.log`, `build/`, `target/`, `.idea/`, `.classpath`, `.project`, `*.iml`
- **Entorno**: El proyecto se compila desde terminal con `javac` y se ejecuta con `java`. Compatible con cualquier IDE (VS Code, IntelliJ IDEA, Eclipse).

---

## Módulo: Lenguaje de Marcas

### Formatos de Estructuración de la Información

El proyecto utiliza **3 lenguajes de marcas** diferenciados:

#### 1. HTML5 — Frontend Web

La interfaz de usuario web consta de **8 páginas HTML5** con semántica de etiquetas moderna (`<nav>`, `<header>`, `<section>`, `<footer>`) y un archivo de estilos CSS3 personalizado.

```
frontend/
├── index.html                 # Página de inicio con hero y propuesta de valor
├── quienes-somos.html         # Información del equipo profesional
├── servicios.html             # Catálogo de servicios y tarifas
├── contacto.html              # Formulario de contacto
├── acceso.html                # Login dual (paciente / nutricionista)
├── registro-paciente.html     # Formulario de alta con datos físicos
├── solicitar-cita.html        # Reserva de citas con selector de modalidad
├── historial-paciente.html    # Seguimiento de citas y plan activo
└── css/estilos.css            # Paleta verde corporativa con variables CSS
```

La hoja de estilos define una **paleta corporativa** mediante variables CSS:

```css
:root {
    --verde-principal: #2e7d32;
    --verde-claro: #4caf50;
    --verde-oscuro: #1b5e20;
    --blanco: #ffffff;
    --gris-claro: #f5f5f5;
    --gris-oscuro: #333333;
    --rojo: #e53935;
    --naranja: #fb8c00;
    --azul: #1565c0;
}
```

Incluye diseño responsivo con `@media (max-width: 768px)` y componentes Bootstrap 5 (navbar, cards, tabs, badges).

#### 2. Markdown — Documentación

- `README.md`: Documentación principal del proyecto (este archivo).
- `Documentacion.md`: Documentación complementaria con índice, integrantes y capturas.

#### 3. Archivos de Configuración y Datos (TXT)

El módulo `GestorArchivos.java` gestiona tres ficheros de texto plano:

```
archivos/
├── configuracion.txt          # Parámetros de conexión a BD (formato clave=valor)
├── volcado_citas.txt          # Exportación de citas generada por GestorArchivos
└── log_sistema.txt            # Traza de eventos (creación automática con fecha/hora)
```

**configuracion.txt** (formato tipo `.ini`):
```ini
# CONFIGURACION FOODIET
BD_HOST=localhost
BD_PUERTO=3306
BD_NOMBRE=Foodiet
BD_USUARIO=root
BD_PASSWORD=
```

**Ejemplo de exportación** (`volcado_citas.txt`):
```
=== VOLCADO DE CITAS FOODIET ===
Total citas: 2

Cita Nº 0 | Fecha: 10/02/2026 | Paciente: Ana Lopez | ...
Cita Nº 1 | Fecha: 12/12/2026 | Paciente: Carlos Perez | ...
```

El log del sistema se genera con marca temporal:
```
2026-06-14T12:30:00 - Sistema iniciado
2026-06-14T12:30:05 - Sesion iniciada: admin
2026-06-14T12:30:10 - Paciente registrado: Ana Lopez
```

---

## Instrucciones de Ejecución

### 1. Requisitos Previos

- **Java JDK 17+** instalado y configurado en el `PATH`
- **MySQL Server 8.0+** instalado y en ejecución
- **Conector JDBC** (`mysql-connector-java-x.x.x.jar`) — descargar desde [MySQL Connectors](https://dev.mysql.com/downloads/connector/j/) y colocarlo en una carpeta `lib/` en la raíz del proyecto, o añadirlo al classpath manualmente

### 2. Configurar la Base de Datos

```bash
# Desde la raíz del proyecto, ejecutar el script SQL completo
mysql -u root -p < sql/foodiet_completo.sql
```

Este script crea la base de datos `Foodiet`, las 7 tablas con sus relaciones y restricciones, e inserta datos de ejemplo:
- **3 usuarios** nutricionistas y **3 pacientes**
- **3 profesionales** asociados a los nutricionistas
- **3 pacientes** de distintos tipos (Joven, Adulto, Jubilado)
- **4 citas** (2 presenciales, 2 online) con distintos estados
- **3 pagos** asociados a citas
- **2 planes alimenticios** con **4 registros de seguimiento**

### 3. Verificar Credenciales de Conexión

Si tu MySQL usa credenciales distintas a `root`/vacío, editar el archivo:

**`src/com/foodiet/datos/ConexionBD.java`**:

```java
private static final String URL = "jdbc:mysql://localhost:3306/Foodiet";
private static final String USUARIO = "root";
private static final String CONTRASEÑA = "";  // Cambiar si es necesario
```

### 4. Compilar el Proyecto

#### Con el script `build.bat` (Windows):

```bash
build.bat
```

#### Manualmente:

```bash
dir /s /B src\*.java > sources.txt
javac -cp "src;lib\*;." -d build @sources.txt
del sources.txt
```

### 5. Ejecutar la Aplicación

#### Con el script `run.bat` (Windows):

```bash
run.bat
```

#### Manualmente:

```bash
java -cp "build;lib\*" com.foodiet.FoodietApp
```

### 6. Usuarios de Prueba para Iniciar Sesión

| Usuario | Contraseña | Rol |
|---|---|---|
| `admin` | `admin123` | administrador |
| `dietista1` | `pass123` | nutricionista |
| `dietista2` | `pass123` | nutricionista |
| `dietista3` | `pass123` | nutricionista |
| `ana.lopez` | `pass123` | paciente |
| `carlos.perez` | `pass123` | paciente |
| `manuel.garcia` | `pass123` | paciente |

### 7. Estructura de Navegación de la Aplicación

```
FoodietApp.java (main)
│
├── Login / Registro de usuario
│
└── Panel Principal
    ├── 1. Pacientes  →  Registrar, Listar, Buscar, Eliminar
    ├── 2. Citas      →  Solicitar, Listar, Buscar, Cancelar
    ├── 3. Planes     →  Crear, Listar, Buscar, Eliminar
    ├── 4. Estadísticas → Resumen, Ingresos, Modalidad, Próximas
    ├── 5. Servicios    → Catálogo de 6 servicios
    ├── 6. Archivos     → Exportar citas, Ver configuración, Ver log
    ├── 7. Profesionales → Solo administrador
    └── Cerrar sesión
```

---

*Proyecto desarrollado para la asignatura de Fundamentos del Software — IES Font de San Lluís · Curso 2025–2026*

**Equipo:** Andrei Veres, Daniel Dimitrov, Octavian Matei, Itzel Bethania, Sergio Jiménez
