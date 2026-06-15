<p align="center">
  <img src="./foodietlogo.jpg" alt="Logo FooDiet" width="150"/>
</p>

<h1 align="center">📊 Subsistema de Estadísticas</h1>
<p align="center"><i>FooDiet · Itzel Bethania Córdoba Herrera · QC Manager · 1º DAW 2025-2026</i></p>

---

## Índice

1. [¿Qué hace este subsistema?](#qué-hace-este-subsistema)
2. [Historias de usuario](#historias-de-usuario)
3. [Frontend — páginas desarrolladas](#frontend--páginas-desarrolladas)
4. [API Externa — Genshin Impact API](#api-externa--genshin-impact-api)
5. [Programa Java con BD y Ficheros](#programa-java-con-bd-y-ficheros)
6. [Base de datos](#base-de-datos)
7. [Consultas SQL](#consultas-sql)
8. [Pruebas unitarias con JUnit](#pruebas-unitarias-con-junit)
9. [Cómo ejecutarlo](#cómo-ejecutarlo)

---

## ¿Qué hace este subsistema?

El subsistema de Estadísticas muestra al administrador cómo va la clínica: cuántos pacientes hay, qué citas se han hecho, cuánto se ha ingresado y cómo evoluciona el peso de cada paciente.

Lo que permite hacer:
- Ver un panel resumen con métricas del mes
- Generar informes filtrando por fechas y profesional
- Ver la evolución de peso e IMC de un paciente concreto
- Exportar los datos en PDF o Excel
- Consumir datos de una API externa (Genshin Impact API)
- Gestionar registros de progreso desde consola con conexión a MySQL

> [!NOTE]
> Este subsistema es de solo lectura para pacientes y nutricionistas. Solo el administrador puede ver las métricas económicas y el rendimiento de los profesionales.

---

## Historias de usuario

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

---

## Frontend — páginas desarrolladas

Cuatro páginas con la plantilla **SB Admin Bootstrap** adaptada a FooDiet, con navbar lateral verde y la misma paleta del equipo.

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
    <input type="date" class="form-control" id="fechaInicio" required>
    <input type="date" class="form-control" id="fechaFin" required>
    <select class="form-select" id="profesional">
        <option value="">Todos los profesionales</option>
    </select>
</form>
```

### `estadisticas-informe.html` — Resultados por profesional

Tabla con citas, ingresos y estado de cada dietista. Incluye botones para ver el detalle y exportar.

### `estadisticas-progreso.html` — Progreso del paciente

Muestra el peso, IMC, % de grasa y perímetro del paciente con variación respecto al registro anterior.

> [!WARNING]
> Esta página no debe ser accesible si el usuario no ha iniciado sesión. Implementar control de acceso en el backend antes de conectarla.

### Estilos compartidos

Todas las páginas usan `css/styles.css` con la paleta verde de FooDiet:

```css
:root {
    --verde-principal: #2e7d32;
    --verde-oscuro:    #1b5e20;
    --verde-claro:     #4caf50;
}
```

---

## API Externa — Genshin Impact API

**Página:** `api-pokemon.html`  
**API utilizada:** [genshin.jmp.blue](https://genshin.jmp.blue) — gratuita, sin clave, con imágenes

> [!NOTE]
> El profesor indica que los datos de la API externa no tienen por qué corresponder con la temática de la aplicación. Se usa Genshin Impact API porque es pública, sin registro y devuelve imágenes.

### ¿Qué hace la página?

La página tiene **dos funcionalidades** implementadas con `fetch()` y manipulación del DOM:

#### 1. Listado de personajes (carga automática)

Al abrir la página, hace una primera petición para obtener la lista de IDs:

```javascript
fetch('https://genshin.jmp.blue/characters')
    .then(function(respuesta) { return respuesta.json(); })
    .then(function(ids) {
        // carga los detalles de los primeros 20 personajes
        for (var i = 0; i < 20; i++) {
            cargarFila(ids[i], i);
        }
    });
```

Luego por cada personaje hace una segunda petición para obtener sus detalles (nombre, elemento, arma y rareza) y los muestra en la tabla:

```javascript
function cargarFila(id, indice) {
    fetch('https://genshin.jmp.blue/characters/' + id)
        .then(function(r) { return r.json(); })
        .then(function(datos) {
            // rellena la fila de la tabla con nombre, visión, arma y rareza
        });
}
```

#### 2. Buscador por nombre con imagen

El usuario escribe el nombre de un personaje en inglés (ej: `albedo`, `diluc`, `hu-tao`) y hace clic en **Buscar**. La función realiza dos cosas:

- Pide los datos del personaje al endpoint `/characters/{nombre}`
- Carga su imagen desde el endpoint `/characters/{nombre}/icon`

```javascript
function buscarPersonaje() {
    var nombre = document.getElementById('input-nombre').value.toLowerCase();
    nombre = nombre.replace(/ /g, '-'); // "hu tao" → "hu-tao"

    fetch('https://genshin.jmp.blue/characters/' + nombre)
        .then(function(respuesta) {
            if (!respuesta.ok) { throw new Error('No encontrado'); }
            return respuesta.json();
        })
        .then(function(datos) {
            // imagen: endpoint devuelve la imagen directamente
            document.getElementById('img-personaje').src =
                'https://genshin.jmp.blue/characters/' + nombre + '/icon';

            // datos mostrados en el DOM
            document.getElementById('res-nombre').textContent  = datos.name;
            document.getElementById('res-vision').textContent  = datos.vision;
            document.getElementById('res-arma').textContent    = datos.weapon;
            document.getElementById('res-nacion').textContent  = datos.nation;
            document.getElementById('res-rareza').textContent  = datos.rarity;
        })
        .catch(function() {
            document.getElementById('error').style.display = 'block';
        });
}
```

### Endpoints utilizados

| Endpoint | Qué devuelve |
|---|---|
| `GET /characters` | Array con todos los IDs de personajes |
| `GET /characters/{id}` | JSON con nombre, elemento, arma, nación, rareza |
| `GET /characters/{id}/icon` | Imagen PNG del personaje |

### Requisitos de la tarea que cumple

| Requisito | Cómo se cumple |
|---|---|
| Listado de datos en tabla | Tabla con 20 personajes cargada al abrir la página |
| Input + botón de búsqueda | Campo de texto + botón "Buscar" con `fetch` al pulsar |
| Devolver una imagen | El endpoint `/icon` devuelve la imagen del personaje |
| Diseño acorde Bootstrap | Misma plantilla SB Admin que las otras páginas |

---

## Programa Java con BD y Ficheros

**Archivo principal:** `Main.java`  
**Paquete:** `stats`  
**Conexión:** JDBC con MySQL (base de datos FooDiet)

El programa muestra un menú de 9 opciones por consola usando `Scanner`. Integra tres capas:

### Clases del programa

| Clase | Función |
|---|---|
| `ConexionBD.java` | Conecta con MySQL mediante JDBC. Solo crea una conexión y la reutiliza |
| `Progreso.java` | Modelo de datos: representa un registro de la tabla `progreso` |
| `ProgresoDAO.java` | CRUD completo sobre la tabla `progreso` con `PreparedStatement` |
| `GestorFicheros.java` | Exporta informes a `.txt` y carga métricas desde `.csv` |
| `Metrica.java` | Clase abstracta con subclases `MetricaPeso` e `MetricaIMC` |
| `Main.java` | Menú por consola con `Scanner` y `ArrayList<Metrica>` |

### Menú de opciones

```
--- MENÚ PRINCIPAL ---
1. Ver todos los registros de progreso        ← lista de BD en ArrayList
2. Ver progreso de un paciente                ← filtro por ID
3. Añadir nuevo registro de progreso          ← INSERT en BD
4. Actualizar peso e IMC de un registro       ← UPDATE en BD
5. Eliminar un registro                       ← DELETE con confirmación
6. Ver estadísticas con ArrayList y polimorfismo
7. Exportar informe a fichero .txt            ← ficheros
8. Cargar métricas desde fichero .csv         ← ficheros + polimorfismo
9. Crear fichero CSV de ejemplo
0. Salir
```

### Polimorfismo con ArrayList

La opción 6 demuestra polimorfismo: crea un `ArrayList<Metrica>` con objetos de tipo `MetricaPeso` y `MetricaIMC`, y llama a `mostrar()` sobre todos ellos sin saber el tipo concreto:

```java
ArrayList<Metrica> metricas = new ArrayList<>();
metricas.add(new MetricaPeso("Paciente 1", 75.0, 75.0, "2026-06-12"));
metricas.add(new MetricaIMC("Paciente 1", 75.0, 1.65, "2026-06-12"));

// Polimorfismo: cada subclase ejecuta su propio calcular()
for (Metrica m : metricas) {
    m.mostrar();
}
```

### Conexión a la BD

```java
// ConexionBD.java
private static final String URL      = "jdbc:mysql://localhost:3306/FooDiet";
private static final String USUARIO  = "root";
private static final String PASSWORD = "";  // cambiar por tu contraseña
```

> [!CAUTION]
> Cambia el campo `PASSWORD` por tu contraseña de MySQL antes de ejecutar.

---

## Base de datos

Las tablas del subsistema de Estadísticas dependen de las tablas de Pacientes y Planes.

### Tablas del subsistema

| Tabla | Para qué sirve |
|---|---|
| `progreso` | Registros periódicos de peso e IMC por paciente |
| `panel_progreso` | Panel de métricas actuales del paciente |
| `registro_metrica` | Histórico de métricas (% grasa, perímetro, masa muscular…) |
| `informe` | Metadatos de cada informe generado |

### Script de creación

```sql
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

CREATE TABLE panel_progreso (
    id_panel            INT  NOT NULL AUTO_INCREMENT,
    id_paciente         INT  NOT NULL,
    fecha_actualizacion DATE NOT NULL,
    CONSTRAINT PK_panel_progreso PRIMARY KEY (id_panel),
    CONSTRAINT FK_panel_paciente FOREIGN KEY (id_paciente)
        REFERENCES paciente (id_paciente) ON DELETE RESTRICT
);

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
> Ejecuta primero `SQL_00_BASE.sql`, `SQL_01_DANI_pacientes.sql` y `SQL_03_TAVI_planes.sql`.

---

## Consultas SQL

Se han desarrollado 5 tipos de consultas sobre las tablas del subsistema:

| Tipo | Qué hace |
|---|---|
| Simple | `AVG`, `MAX`, `MIN` del IMC y peso de todos los registros |
| `GROUP BY` + `HAVING` | Kg perdidos por paciente, solo los que bajaron |
| Subconsulta | Pacientes con IMC por encima de la media de la clínica |
| Multitabla | Informe completo con `JOIN` entre 5 tablas (paciente, plan, profesional, progreso inicial y actual) |
| `INSERT` con `SELECT` | Crea informes automáticamente para pacientes con progreso en un período |
| `UPDATE` con `SELECT` | Actualiza el panel de pacientes con registros recientes |
| `DELETE` con `SELECT` | Elimina informes duplicados conservando el más reciente |

> [!NOTE]
> Las consultas no usan `CURDATE()` ni `ROUND()` porque no se han trabajado en clase. Se usan fechas fijas y valores sin redondear.

---

## Pruebas unitarias con JUnit

**Clase testeada:** `Estadistica.java`  
**Framework:** JUnit 5 con Maven  
**Resultado:** ✅ 9 tests · 0 fallos · 0 errores

### Métodos probados

#### `imcMedio()` — IMC medio de todos los pacientes

```java
@Test
void testImcMedioNominal() {
    double suma = MATRIZ[0][2] + MATRIZ[1][2] + MATRIZ[2][2];
    double esperado = Math.round((suma / 3) * 100.0) / 100.0;
    assertEquals(esperado, stats.imcMedio());
}

@Test
void testImcMedioSinPacientes() {
    Estadistica statsVacia = new Estadistica(new String[]{}, new double[][]{}, 0, 0);
    assertEquals(0.0, statsVacia.imcMedio());
}
```

#### `pacientesQueBajaronPeso()` — cuántos pacientes bajaron de peso

```java
@Test
void testPacientesQueBajaronPesoNominal() {
    assertEquals(2, stats.pacientesQueBajaronPeso());
}
```

#### `mejorPaciente()` — paciente con mayor pérdida de peso

```java
@Test
void testMejorPacienteNominal() {
    assertEquals("ANA GARCIA", stats.mejorPaciente());
}
```

### Datos de prueba

```java
private static final String[] NOMBRES = {"ANA GARCIA", "LUIS TORRES", "MARIA LOPEZ"};

// [pesoInicial, pesoActual, IMC, diferenciaPeso]
private static final double[][] MATRIZ = {
    {80.0, 75.0, 27.55, -5.0},  // ANA: bajó 5 kg
    {90.0, 92.0, 28.40,  2.0},  // LUIS: subió 2 kg
    {60.0, 58.0, 20.07, -2.0}   // MARIA: bajó 2 kg
};
```

---

## Cómo ejecutarlo

### Frontend

Abre los `.html` en el navegador o con Live Server en VSCode. No necesita servidor.

### Base de datos

```sql
source SQL_00_BASE.sql;
source SQL_01_DANI_pacientes.sql;
source SQL_03_TAVI_planes.sql;
source SQL_04_ITZEL_estadisticas.sql;
source FooDiet_Datos_Prueba.sql;
```

### Programa Java

```bash
cd FOODIETV2/ESTADISTICAS/
mvn clean install
mvn exec:java -Dexec.mainClass="stats.Main"
```

### Tests

```bash
mvn test
```

Resultado esperado:

```
[INFO] Tests run: 9, Failures: 0, Errors: 0, Skipped: 0
[INFO] BUILD SUCCESS
```

> [!CAUTION]
> Necesitas Java 17+ y Maven. Cambia la contraseña en `ConexionBD.java` antes de ejecutar.

---

<p align="center">
<i>Subsistema desarrollado por <a href="https://github.com/Itzelcor">@Itzelcor</a> · FooDiet · IES Font de San Lluis · 1º DAW 2025-2026</i>
</p>
