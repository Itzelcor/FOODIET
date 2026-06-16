<p align="center">
  <img src="./foodietlogo.jpg" alt="Logo FooDiet" width="150"/>
</p>

<h1 align="center">🛠️ Subsistema de Administración</h1>
<p align="center"><i>FooDiet · Andrei Veres · Developer · 1º DAW 2025-2026</i></p>

---

## Índice

1. [¿Qué hace este subsistema?](#qué-hace-este-subsistema)
2. [Historias de usuario](#historias-de-usuario)
3. [Frontend — panel de administración](#frontend--panel-de-administración)
4. [Backend en Java](#backend-en-java)
5. [Base de datos](#base-de-datos)
6. [Cómo ejecutarlo](#cómo-ejecutarlo)

---

## ¿Qué hace este subsistema?

El subsistema de Administración es el centro de control de FooDiet. Desde aquí, el administrador gestiona los profesionales de la clínica, controla el acceso al sistema y supervisa la actividad general.

Lo que permite hacer:
- Acceder al panel de control con visión global de la clínica
- Gestionar altas, bajas y modificaciones de profesionales
- Controlar roles y permisos de acceso
- Generar y revisar informes del sistema

> [!NOTE]
> Solo los usuarios con rol `administrativo` pueden acceder a este panel. Pacientes y nutricionistas no tienen acceso a ninguna de estas vistas.

---

## Historias de usuario

| Código | Historia | Actor | Criterio clave |
|---|---|---|---|
| HU-A01 | Acceder al panel de administración | Administrador | Solo accesible con rol `administrativo` |
| HU-A02 | Ver listado de profesionales | Administrador | Con nombre, especialidad y estado activo/inactivo |
| HU-A03 | Dar de alta un profesional | Administrador | Formulario con validación de datos |
| HU-A04 | Dar de baja a un profesional | Administrador | Baja lógica, no se borran los datos |
| HU-A05 | Editar datos de un profesional | Administrador | Cambios guardados inmediatamente |
| HU-A06 | Gestionar roles de usuario | Administrador | Cambio de rol con confirmación |

> [!TIP]
> La baja de un profesional no elimina su historial de citas ni sus pacientes asignados. Solo marca el campo `activo = 0` en la base de datos.

---

## Frontend — panel de administración

El panel está construido con **HTML5** y **Bootstrap 5**, siguiendo la misma hoja de estilos que el resto del equipo.

### Páginas desarrolladas

- **`admin-panel.html`** — vista principal con accesos rápidos y resumen de la clínica
- **`admin-profesionales.html`** — tabla de profesionales con opciones de alta, baja y edición
- **`admin-usuario.html`** — gestión de roles y permisos de acceso

### Ejemplo de tabla de profesionales

```html
<table class="table-custom">
    <thead>
        <tr>
            <th>Nombre</th>
            <th>Especialidad</th>
            <th>Email</th>
            <th>Estado</th>
            <th>Acciones</th>
        </tr>
    </thead>
    <tbody>
        <tr>
            <td data-label="Nombre">Laura Gómez</td>
            <td data-label="Especialidad">Nutrición clínica</td>
            <td data-label="Email">laura@foodiet.com</td>
            <td data-label="Estado">
                <span class="badge-estado badge-confirmada">Activo</span>
            </td>
            <td data-label="Acciones">
                <button class="btn btn-sm btn-outline-secondary">Editar</button>
                <button class="btn btn-sm btn-outline-danger">Dar de baja</button>
            </td>
        </tr>
    </tbody>
</table>
```

### Formulario de alta de profesional

```html
<form class="form-section">
    <h4>Datos del Profesional</h4>
    <div class="mb-3">
        <label for="nombre" class="form-label">Nombre completo</label>
        <input type="text" class="form-control" id="nombre" required>
    </div>
    <div class="mb-3">
        <label for="especialidad" class="form-label">Especialidad</label>
        <select class="form-select" id="especialidad">
            <option value="">Selecciona una especialidad...</option>
            <option value="1">Nutrición clínica</option>
            <option value="2">Nutrición deportiva</option>
            <option value="3">Dietética general</option>
        </select>
    </div>
    <div class="mb-3">
        <label for="email" class="form-label">Email</label>
        <input type="email" class="form-control" id="email" required>
    </div>
    <div class="text-center mt-4">
        <button type="submit" class="btn btn-primary btn-lg px-5">
            Dar de alta
        </button>
    </div>
</form>
```

> [!WARNING]
> El formulario valida que el email no esté ya registrado antes de crear el usuario. Si el email existe, muestra un aviso y no guarda nada.

### Estilos compartidos

El panel usa las mismas variables CSS que el resto del proyecto:

```css
:root {
    --verde-principal: #2e7d32;
    --verde-oscuro:    #1b5e20;
    --verde-claro:     #4caf50;
    --gris-claro:      #f5f5f5;
}

.navbar-custom {
    background: linear-gradient(135deg, var(--verde-oscuro), var(--verde-principal));
}
```

---

## Backend en Java

El backend del subsistema de Administración gestiona la autenticación y las operaciones sobre los profesionales.

### Clase `Administrador`

Extiende `Persona` y añade la lógica de autenticación y control de acceso.

```java
public class Administrador extends Persona {
    private String  rol;
    private String  contrasena;
    private boolean activo;

    public Administrador(String nombre, String apellido, String dni,
                         int edad, String email, String contrasena) {
        super(nombre, apellido, dni, edad, email);
        this.rol       = "ADMINISTRADOR";
        this.contrasena = contrasena;
        this.activo    = true;
    }

    // Valida que la contraseña tenga mínimo 8 caracteres
    public boolean contrasenaValida() {
        return contrasena != null && contrasena.trim().length() >= 8;
    }

    // Comprueba credenciales de acceso
    public boolean validarAcceso(String emailIntento, String contrasenaIntento) {
        return email.equalsIgnoreCase(emailIntento.trim())
            && contrasena.equals(contrasenaIntento);
    }

    public void desactivar() { this.activo = false; }
    public void activar()    { this.activo = true;  }
}
```

### Clase `GestorAdmin`

Gestiona las operaciones CRUD sobre los profesionales de la clínica.

```java
public class GestorAdmin {
    private ArrayList<ProfesionalClinica> profesionales;

    public void agregarProfesional(ProfesionalClinica p) {
        profesionales.add(p);
    }

    public void eliminarProfesional(int id) {
        profesionales.removeIf(p -> p.getId() == id);
    }

    public ProfesionalClinica buscarProfesional(int id) {
        return profesionales.stream()
            .filter(p -> p.getId() == id)
            .findFirst()
            .orElse(null);
    }
}
```

---

## Base de datos

El subsistema de Administración usa principalmente las tablas `usuario`, `profesional` y `especialidad`.

```sql
-- Tabla usuario: gestiona el acceso al sistema
CREATE TABLE usuario (
    id_usuario    INT          NOT NULL AUTO_INCREMENT,
    email         VARCHAR(100) NOT NULL,
    password_hash VARCHAR(255) NOT NULL,
    rol           VARCHAR(20)  NOT NULL,
    activo        TINYINT(1)   NOT NULL DEFAULT 1,
    fecha_alta    DATE         NOT NULL,
    CONSTRAINT PK_usuario      PRIMARY KEY (id_usuario),
    CONSTRAINT UQ_usuario_email UNIQUE (email),
    CONSTRAINT CHK_usuario_rol  CHECK (rol IN (
        'paciente','nutricionista','entrenador','dietista','administrativo'))
);

-- Tabla profesional: datos de los profesionales de la clínica
CREATE TABLE profesional (
    id_profesional  INT          NOT NULL AUTO_INCREMENT,
    id_usuario      INT          NOT NULL,
    id_especialidad INT,
    nom_completo    VARCHAR(100) NOT NULL,
    email           VARCHAR(100) NOT NULL UNIQUE,
    telefono        CHAR(9),
    anos_exp        INT          NOT NULL DEFAULT 0,
    activo          TINYINT(1)   NOT NULL DEFAULT 1,
    fecha_alta      DATE         NOT NULL,
    CONSTRAINT PK_profesional   PRIMARY KEY (id_profesional),
    CONSTRAINT FK_prof_usuario  FOREIGN KEY (id_usuario)
        REFERENCES usuario (id_usuario) ON DELETE RESTRICT
);
```

> [!NOTE]
> El campo `activo` permite dar de baja a un profesional sin borrar su historial. Cuando `activo = 0`, el profesional no aparece en los listados del sistema pero sus citas y pacientes siguen accesibles para el administrador.

---

## Cómo ejecutarlo

### Frontend

```bash
# Abre directamente en el navegador o con Live Server en VSCode
# Clic derecho en admin-panel.html → Open with Live Server
```

### Base de datos

```sql
-- Ejecuta en este orden:
source SQL_00_BASE.sql;          -- crea la tabla usuario
source SQL_02_SERGIO_profesionales_citas.sql;  -- crea profesional y especialidad
```

### Ejemplo de inserción de datos de prueba

```sql
INSERT INTO usuario (email, password_hash, rol, activo, fecha_alta)
VALUES ('admin@foodiet.com', '$2b$12$hash...', 'administrativo', 1, '2026-01-01');

INSERT INTO profesional (id_usuario, nom_completo, email, anos_exp, fecha_alta)
VALUES (1, 'Carlos Ruiz Mora', 'carlos@foodiet.com', 10, '2026-01-01');
```

> [!CAUTION]
> Nunca guardes contraseñas en texto plano en la base de datos. El campo `password_hash` debe contener siempre un hash bcrypt generado desde el backend.

---

<p align="center">
<i>Subsistema desarrollado por <a href="https://github.com/d-reii">@d-reii</a> · FooDiet · IES Font de San Lluis · 1º DAW 2025-2026</i>
</p>
