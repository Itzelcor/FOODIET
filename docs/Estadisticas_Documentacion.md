<p align="center">
  <img src="./foodietlogo.jpg" alt="Logo FooDiet" width="150"/>
</p>

<h1 align="center">📊 Subsistema de Estadísticas</h1>
<p align="center"><i>FooDiet · Itzel Bethania · QC Manager · 1º DAW 2025-2026</i></p>

---

## Índice

1. [¿Qué hace este subsistema?](#qué-hace-este-subsistema)
2. [Historias de usuario](#historias-de-usuario)
3. [Frontend — páginas desarrolladas](#frontend--páginas-desarrolladas)
4. [Base de datos](#base-de-datos)
5. [Pruebas unitarias con JUnit](#pruebas-unitarias-con-junit)
6. [Cómo ejecutarlo](#cómo-ejecutarlo)

---

## ¿Qué hace este subsistema?

El subsistema de Estadísticas muestra al administrador cómo va la clínica: cuántos pacientes hay, qué citas se han hecho, cuánto se ha ingresado y cómo evoluciona el peso de cada paciente.

Lo que permite hacer:
- Ver un panel resumen con métricas del mes
- Generar informes filtrando por fechas y profesional
- Ver la evolución de peso e IMC de un paciente concreto
- Exportar los datos en PDF o Excel

> [!NOTE]
> Este subsistema es de solo lectura para pacientes y nutricionistas. Solo el administrador puede ver las métricas económicas y el rendimiento de los profesionales.

---

## Historias de usuario

Las historias describen qué necesita cada tipo de usuario del sistema.

### Paciente

| Código | Historia | Criterio clave |
|---|---|---|
| HU-01 | Ver gráfica de evolución de peso e IMC | Solo ve sus propios datos |
| HU-02 | Ver panel de métricas actuales | Muestra variación respecto al registro anterior |
| HU-03 | Consultar historial de citas | Filtrable por estado y fecha |

### Nutricionista

| Código | Historia | Criterio clave |
|---|---|---|
| HU-04 | Ver progreso de sus pacientes | Solo ve los pacientes que tiene asignados |
| HU-05 | Comparar progreso entre pacientes | Hasta 5 pacientes a la vez |
| HU-06 | Ver estadísticas de sus citas | Desglose presencial/online |
| HU-07 | Exportar informe de un paciente | PDF o Excel, rango de fechas elegible |

### Administrador

| Código | Historia | Criterio clave |
|---|---|---|
| HU-08 | Ver panel global de la clínica | Pacientes activos, citas del mes, ingresos |
| HU-09 | Ver ingresos por profesional y servicio | Solo el administrador tiene acceso |
| HU-10 | Ver rendimiento de cada profesional | Tabla ordenable por cualquier columna |
| HU-11 | Ver crecimiento mensual de pacientes | Gráfica con altas y bajas |
| HU-12 | Exportar cualquier informe | PDF con gráficas, Excel con datos editables |

> [!TIP]
> Los criterios de aceptación completos están en `docs/Estadisticas_HU_Requisitos.md`.

---

## Frontend — páginas desarrolladas

Cuatro páginas en Bootstrap 5 que siguen el mismo navbar, footer y hoja de estilos que el resto del equipo.

### `estadisticas-dashboard.html` — Panel principal

Muestra las métricas clave del mes en tarjetas y una gráfica de barras con la evolución de citas.

```html
<!-- Ejemplo de tarjeta de métrica -->
<div class="metrica-card">
    <div class="numero">48</div>
    <div class="etiqueta">Total Pacientes</div>
</div>
```

### `estadisticas-generar.html` — Formulario de informe

El administrador elige el rango de fechas, el profesional y el tipo de informe (mensual, trimestral o anual).

```html
<form class="form-section" action="estadisticas-informe.html">
    <h4>Período del Informe</h4>
    <input type="date" class="form-control" id="fechaInicio" required>
    <input type="date" class="form-control" id="fechaFin" required>

    <select class="form-select" id="profesional">
        <option value="">Todos los profesionales</option>
    </select>
</form>
```

### `estadisticas-informe.html` — Resultados por profesional

Tabla con citas, ingresos y estado de cada dietista. Incluye botones para ver el detalle y exportar.

```html
<table class="table-custom">
    <thead>
        <tr>
            <th>Dietista</th>
            <th>Citas</th>
            <th>Ingresos</th>
            <th>Estado</th>
            <th>Acción</th>
        </tr>
    </thead>
</table>
```

### `estadisticas-progreso.html` — Progreso del paciente

Muestra el peso, IMC, % de grasa y perímetro del paciente con variación respecto al registro anterior. Incluye un formulario para añadir nuevos registros.

> [!WARNING]
> Esta página no debe ser accesible si el usuario no ha iniciado sesión. Implementar control de acceso en el backend antes de conectarla.

### Estilos compartidos

Todas las páginas usan `css/estilos.css` con la paleta verde acordada por el equipo:

```css
:root {
    --verde-principal: #2e7d32;
    --verde-oscuro:    #1b5e20;
    --verde-claro:     #4caf50;
    --gris-claro:      #f5f5f5;
}
```

---

## Base de datos

Las tablas del subsistema de Estadísticas dependen de las tablas de Pacientes y Planes — hay que ejecutar esos scripts antes.

### Tablas del subsistema

| Tabla | Para qué sirve |
|---|---|
| `progreso` | Registros periódicos de peso e IMC por paciente |
| `panel_progreso` | Panel de métricas actuales del paciente |
| `registro_metrica` | Histórico de métricas específicas (% grasa, perímetro…) |
| `informe` | Metadatos de cada informe generado |

### Script de creación

```sql
-- Tabla progreso
CREATE TABLE progreso (
    id_progreso   INT          NOT NULL AUTO_INCREMENT,
    id_paciente   INT          NOT NULL,
    id_plan       INT          NOT NULL,
    fecha         DATE         NOT NULL,
    peso          DECIMAL(5,2) NOT NULL,
    imc           DECIMAL(5,2),
    observaciones TEXT,
    CONSTRAINT PK_progreso          PRIMARY KEY (id_progreso),
    CONSTRAINT FK_progreso_paciente FOREIGN KEY (id_paciente)
        REFERENCES paciente (id_paciente) ON DELETE RESTRICT,
    CONSTRAINT CHK_progreso_peso    CHECK (peso > 0)
);

-- Tabla panel_progreso
CREATE TABLE panel_progreso (
    id_panel            INT  NOT NULL AUTO_INCREMENT,
    id_paciente         INT  NOT NULL,
    fecha_actualizacion DATE NOT NULL,
    CONSTRAINT PK_panel_progreso PRIMARY KEY (id_panel),
    CONSTRAINT FK_panel_paciente FOREIGN KEY (id_paciente)
        REFERENCES paciente (id_paciente) ON DELETE RESTRICT
);

-- Tabla registro_metrica
CREATE TABLE registro_metrica (
    id_registro   INT           NOT NULL AUTO_INCREMENT,
    id_progreso   INT           NOT NULL,
    id_panel      INT           NOT NULL,
    tipo_metrica  VARCHAR(50)   NOT NULL,
    valor         DECIMAL(10,2) NOT NULL,
    fecha_calculo DATE          NOT NULL,
    CONSTRAINT PK_registro_metrica PRIMARY KEY (id_registro),
    CONSTRAINT CHK_reg_met_tipo CHECK (tipo_metrica IN
        ('imc','peso','grasa_corporal','masa_muscular','perimetro_cintura','otro'))
);
```

> [!WARNING]
> Ejecuta primero `SQL_00_BASE.sql`, `SQL_01_DANI_pacientes.sql` y `SQL_03_TAVI_planes.sql` antes de este script. Las FK fallarán si las tablas referenciadas no existen.

---

## Pruebas unitarias con JUnit

**Clase testeada:** `Estadistica.java`  
**Paquete:** `stats`  
**Framework:** JUnit 5 con Maven  
**Resultado:** ✅ 9 tests · 0 fallos · 0 errores

### Métodos probados

#### `imcMedio()` — calcula el IMC medio de todos los pacientes

```java
// Caso nominal: tres pacientes con IMC conocido
@Test
void testImcMedioNominal() {
    double suma = MATRIZ[0][2] + MATRIZ[1][2] + MATRIZ[2][2];
    double esperado = Math.round((suma / 3) * 100.0) / 100.0;
    assertEquals(esperado, stats.imcMedio(),
        "El IMC medio no coincide con el esperado");
}

// Caso límite: sin pacientes debe devolver 0.0
@Test
void testImcMedioSinPacientes() {
    Estadistica statsVacia = new Estadistica(
        new String[]{}, new double[][]{}, 0, 0);
    assertEquals(0.0, statsVacia.imcMedio(),
        "El IMC medio sin pacientes debe ser 0.0");
}
```

#### `pacientesQueBajaronPeso()` — cuenta cuántos bajaron de peso

```java
// Caso nominal: Ana y Maria bajaron, Luis subió → espera 2
@Test
void testPacientesQueBajaronPesoNominal() {
    assertEquals(2, stats.pacientesQueBajaronPeso(),
        "Deben ser 2 los pacientes que bajaron peso");
}

// Caso erróneo: nadie baja de peso → espera 0
@Test
void testNadieHaBajadoPeso() {
    double[][] matriz = {
        {70.0, 72.0, 25.0, 2.0},
        {80.0, 85.0, 27.0, 5.0}
    };
    Estadistica statsSubida = new Estadistica(
        new String[]{"JOSE", "MARTA"}, matriz, 2, 2);
    assertEquals(0, statsSubida.pacientesQueBajaronPeso(),
        "No debe haber pacientes que bajaron peso");
}
```

#### `mejorPaciente()` — devuelve el paciente con mayor pérdida de peso

```java
// Caso nominal: Ana bajó 5 kg → es la mejor
@Test
void testMejorPacienteNominal() {
    assertEquals("ANA GARCIA", stats.mejorPaciente(),
        "El mejor paciente debe ser ANA GARCIA");
}

// Caso límite: sin pacientes devuelve "Sin datos"
@Test
void testMejorPacienteSinDatos() {
    Estadistica statsVacia = new Estadistica(
        new String[]{}, new double[][]{}, 0, 0);
    assertEquals("Sin datos", statsVacia.mejorPaciente(),
        "Sin pacientes debe devolver 'Sin datos'");
}
```

### Datos de prueba usados

```java
private static final String[] NOMBRES = {
    "ANA GARCIA", "LUIS TORRES", "MARIA LOPEZ"
};

// Columnas: [0]pesoInicial [1]pesoActual [2]IMC [3]diferenciaPeso
private static final double[][] MATRIZ = {
    {80.0, 75.0, 27.55, -5.0},  // ANA: bajó 5 kg
    {90.0, 92.0, 28.40,  2.0},  // LUIS: subió 2 kg
    {60.0, 58.0, 20.07, -2.0}   // MARIA: bajó 2 kg
};
```

---

## Cómo ejecutarlo

### Frontend

Abre cualquiera de los `.html` en el navegador o usa Live Server en VSCode. No necesita servidor.

```bash
# Con Live Server desde VSCode
# Clic derecho en estadisticas-dashboard.html → Open with Live Server
```

### Base de datos

```sql
-- En MySQL Workbench o terminal:
source SQL_00_BASE.sql;
source SQL_01_DANI_pacientes.sql;
source SQL_03_TAVI_planes.sql;
source SQL_04_ITZEL_estadisticas.sql;
```

### Tests

```bash
cd backend
mvn test
```

Resultado esperado:

```
[INFO] Tests run: 9, Failures: 0, Errors: 0, Skipped: 0
[INFO] BUILD SUCCESS
```

> [!CAUTION]
> Necesitas Java 17+ y Maven instalado. Si `mvn` no se reconoce, añádelo al PATH del sistema.

---

<p align="center">
<i>Subsistema desarrollado por <a href="https://github.com/Itzelcor">@Itzelcor</a> · FooDiet · IES Font de San Lluis · 1º DAW 2025-2026</i>
</p>
