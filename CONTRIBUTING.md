# Guía de Contribución – FOODIET
**Repositorio principal:** Itzelcor/FOODIET  
**Mantenedora:** Itzel Bethania  
**Equipo:** Daniel, Andrei, Sergio, Itzel, Octavian

Antes de subir cualquier cosa al repositorio, lee esta guía entera. Es corta y te ahorra conflictos.

---

##  Estructura de carpetas

```
FOODIET/
│
├── 📁 frontend/
│   ├── 📁 estadisticas/        ← Itzel
│   ├── 📁 citas y pacientes              ← (dani)
│   ├── 📁 profesianales        ← Sergio
│   ├── 📁 planes/              ← Tavi
│   └── 📁 css/
│       └── estilos.css         ← CSS compartido (no tocar sin avisar)
│
├── 📁 database/
│   ├── SQL_00_BASE.sql
│   ├── SQL_01_DANI_pacientes.sql
│   ├── SQL_02_SERGIO_profesionales_citas.sql
│   ├── SQL_03_TAVI_planes.sql
│   └── SQL_04_ITZEL_estadisticas.sql
│
├── 📁 backend/
│   ├── 📁 src/
│   │   ├── 📁 main/java/foodiet/
│   │   │   ├── 📁 estadisticas/    ← Itzel
│   │   │   ├── 📁 profesionales     ← sergio
│   │   │   ├── 📁 pacientes/       ← Dani
│   │   │   └── 📁 planes/          ← Tavi
│   │   └── 📁 test/java/foodiet/
│   │       ├── 📁 estadisticas/    ← Itzel
│   │       ├── 📁 citas/           ←Dani
│   │       ├── 📁 pacientes/       ← Dani
│   │       └── 📁 planes/          ← Tavi
│   └── pom.xml
│
└── 📁 docs/
    ├── README.md
    ├── CONTRIBUTING.md
    ├── MemoriaFoodiet.md
    └── Estadisticas_HU_Requisitos.md
```

>  **Regla de oro:** cada uno trabaja en su propia carpeta. Si necesitas tocar la de otro, avisa primero.

---

## Trabajar con Fork

Si contribuyes desde un fork (copia del repositorio en tu cuenta), sigue estos pasos.

### 1 — Hacer el fork

1. Ve a `https://github.com/Itzelcor/FOODIET`
2. Clic en **"Fork"** arriba a la derecha
3. Selecciona tu cuenta como destino
4. Clic en **"Create fork"**

### 2 — Clonar tu fork en local

```bash
git clone https://github.com/TU-USUARIO/FOODIET.git
cd FOODIET
```

### 3 — Vincular el repositorio original como upstream

Esto es fundamental para poder recibir los cambios del repositorio principal.

```bash
git remote add upstream https://github.com/Itzelcor/FOODIET.git
```

Verifica que tienes los dos remotos:

```bash
git remote -v
```

Deberías ver:
```
origin    https://github.com/TU-USUARIO/FOODIET.git (fetch)
origin    https://github.com/TU-USUARIO/FOODIET.git (push)
upstream  https://github.com/Itzelcor/FOODIET.git (fetch)
upstream  https://github.com/Itzelcor/FOODIET.git (push)
```

---

##  Mantener tu fork actualizado

Cada vez que alguien suba cambios al repositorio principal, tienes que sincronizar tu fork. Hazlo **antes de ponerte a trabajar**.

```bash
# 1. Baja los cambios del repositorio principal
git fetch upstream

# 2. Ve a development
git checkout development

# 3. Incorpora los cambios
git merge upstream/development

# 4. Sube los cambios a tu fork
git push origin development
```

> Si no haces esto antes de trabajar, tu fork se quedará desactualizado y tendrás conflictos al abrir la PR.

---

##  Ramas

| Rama | Para qué sirve |
|---|---|
| `main` | Código en producción. Solo Itzel (Release Manager) hace merge aquí |
| `development` | Integración. Aquí van a parar todas las features aprobadas |
| `feature/nombre` | Tu trabajo. Una rama por funcionalidad |

### Nombrar tu rama

```
feature/subsistema-descripcion
```

Ejemplos correctos:
```
feature/estadisticas-dashboard
feature/database-pacientes
feature/tests-estadisticas
feature/planes-menu-diario
```

Ejemplos incorrectos :
```
mi-rama
arreglos
feature/cosas
estadisticas dashboard   ← sin espacios ni guiones
```

---

##  Commits

### Formato obligatorio

```
tipo: descripción breve en minúsculas
```

| Tipo | Cuándo usarlo |
|---|---|
| `feat` | Añades algo nuevo |
| `fix` | Corriges un error |
| `docs` | Solo documentación |
| `style` | Cambios de formato sin tocar la lógica |
| `db` | Cambios en SQL |
| `test` | Añades o corriges tests |

### Ejemplos correctos
```
feat: añade dashboard de estadísticas
fix: corrige FK en tabla cita
docs: actualiza memoria técnica
db: añade script SQL de pacientes
test: añade pruebas unitarias para imcMedio
```

### Ejemplos incorrectos 
```
cambios
arreglé cosas
WIP
update
feat: Añade DASHBOARD   ← sin mayúsculas en la descripción
```

---

## 🔄 Flujo completo paso a paso

### Antes de ponerte a trabajar

```bash
# Actualiza tu fork con los últimos cambios del repo principal
git fetch upstream
git checkout development
git merge upstream/development
git push origin development

# Crea tu rama de feature
git checkout -b feature/tu-rama
```

### Mientras trabajas

```bash
# NUNCA uses git add . — añade solo tus archivos
git add frontend/estadisticas/mi-archivo.html
git commit -m "tipo: descripción"
```

### Cuando termines

```bash
git push origin feature/tu-rama
```

Luego abre una **Pull Request** en GitHub desde tu fork hacia `Itzelcor/FOODIET` con base en `development`.

---

##  Pull Requests

Toda PR debe tener:

- **Título** descriptivo (mismo formato que el commit)
- **Descripción** con qué has hecho
- **Historia de Usuario** asociada (HU-01, HU-02...)
- **Base:** siempre `development`, nunca `main`

### Reglas
-  No puedes aprobar tu propia PR
-  No se mergea sin al menos una aprobación
-  Otro compañero debe revisar y aprobar antes del merge
-  Una vez mergeada, elimina la rama feature

---

## 📂 Dónde va cada tipo de archivo

| Archivo | Carpeta |
|---|---|
| `.html` | `frontend/tu-subsistema/` |
| `.css` compartido | `frontend/css/estilos.css` |
| `.sql` | `database/` |
| `.java` (clases) | `backend/src/main/java/foodiet/tu-subsistema/` |
| `.java` (tests) | `backend/src/test/java/foodiet/tu-subsistema/` |
| `.md`, documentación | `docs/` |

---

## Cosas que NO hacer nunca

```
 Trabajar directamente en main o development
 Usar git add . sin revisar qué archivos estás añadiendo
 Hacer git push --force
 Subir contraseñas, tokens o datos personales reales
 Subir carpetas target/, *.class, node_modules/
 Modificar el CSS compartido sin avisar al equipo
 Mergear sin revisión
 Olvidarte de actualizar tu fork antes de trabajar
```

---

## 🗂️ .gitignore

El repositorio tiene un `.gitignore` que evita subir archivos innecesarios:

```
target/          ← compilados de Maven
*.class          ← bytecode Java
.idea/           ← configuración de IntelliJ
.vscode/         ← configuración de VSCode
node_modules/    ← dependencias JS
.env             ← variables de entorno
```

Si ves que Git intenta subir alguno de estos archivos, no lo añadas.

---

##  Problemas frecuentes

### Mi fork está desactualizado

```bash
git fetch upstream
git checkout development
git merge upstream/development
git push origin development
```

### Hice commit en la rama equivocada

```bash
git checkout rama-correcta
git cherry-pick HASH-DEL-COMMIT
git checkout rama-equivocada
git revert HEAD
```

### Hay conflictos al hacer merge

```bash
git merge upstream/development
# Abre el archivo con conflictos, elige qué versión conservar
# Elimina los marcadores <<<<<<, ======, >>>>>>
git add archivo-resuelto
git commit -m "fix: resuelve conflicto en nombre-archivo"
```

### Git me pregunta 'Should I try again? (y/n)' sin parar

Escribe `n` y pulsa Enter hasta que pare. Es un problema de Windows con rutas largas, no afecta a tus archivos.

---

*Última actualización: Junio 2026 · Itzel Bethania*
