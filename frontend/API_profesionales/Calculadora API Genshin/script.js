document.addEventListener("DOMContentLoaded", async () => {
    await cargarDatos();
    inicializarToggle();
});

// ─── 1. CARGA AMBOS JSON EN PARALELO ──────────────────────────────────────────

async function cargarDatos() {
    try {
        // Promise.all lanza las dos peticiones a la vez en lugar de esperar una y luego la otra
        const [respPersonajes, respLevelCosts] = await Promise.all([
            fetch("data/personajes.json"),
            fetch("data/levelCosts.json")
        ]);

        if (!respPersonajes.ok) throw new Error("No se pudo cargar personajes.json");
        if (!respLevelCosts.ok) throw new Error("No se pudo cargar levelCosts.json");

        const personajes = await respPersonajes.json();
        const levelCosts = await respLevelCosts.json();

        // Guardamos ambos globalmente
        window.levelCosts = levelCosts;

        // Ordenamos los personajes alfabéticamente y rellenamos el select
        personajes.sort((a, b) => a.name.localeCompare(b.name));

        const select = document.getElementById("select-personaje");
        select.innerHTML = "";

        personajes.forEach(personaje => {
            const opcion = document.createElement("option");
            opcion.value = personaje.name;
            opcion.textContent = personaje.name;
            select.appendChild(opcion);
        });

        window.datosPersonajes = personajes;

    } catch (error) {
        console.error("Error cargando datos:", error);
        const select = document.getElementById("select-personaje");
        select.innerHTML = "<option>Error al cargar datos</option>";
    }
}

// ─── 2. LÓGICA DEL TOGGLE DE ASCENSIÓN ────────────────────────────────────────

// Niveles que tienen ascensión (el 90 no tiene)
const BREAKPOINTS_ASCENSION = [20, 40, 50, 60, 70, 80];

function inicializarToggle() {
    const selectObjetivo = document.getElementById("nivel-objetivo");
    selectObjetivo.addEventListener("change", actualizarToggle);
    actualizarToggle(); // lo ejecutamos al cargar para el valor por defecto
}

function actualizarToggle() {
    const nivelObjetivo = parseInt(document.getElementById("nivel-objetivo").value);
    const grupoToggle   = document.getElementById("grupo-ascension");
    const textoToggle   = document.getElementById("toggle-texto");

    if (BREAKPOINTS_ASCENSION.includes(nivelObjetivo)) {
        // El nivel objetivo tiene ascensión → mostramos el toggle
        grupoToggle.classList.remove("hidden");
        textoToggle.textContent = `Include ascension at Lv.${nivelObjetivo}`;
    } else {
        // Nivel 90: no hay ascensión → ocultamos el toggle
        grupoToggle.classList.add("hidden");
    }
}

// ─── 3. CUANDO EL USUARIO PULSA CALCULAR ──────────────────────────────────────

document.getElementById("btn-calcular").addEventListener("click", () => {
    const nombreElegido   = document.getElementById("select-personaje").value;
    const nivelActual     = parseInt(document.getElementById("nivel-actual").value);
    const nivelObjetivo   = parseInt(document.getElementById("nivel-objetivo").value);
    const ascenderAlFinal = document.getElementById("ascender-al-final").checked;

    const seccion = document.getElementById("resultado-section");
    const div     = document.getElementById("resultado");

    if (nivelActual >= nivelObjetivo) {
        seccion.classList.remove("hidden");
        div.innerHTML = `<p class="mensaje-error">El nivel objetivo debe ser mayor que el nivel actual.</p>`;
        return;
    }

    const personaje = window.datosPersonajes.find(p => p.name === nombreElegido);

    if (!personaje) {
        console.error("Personaje no encontrado");
        return;
    }

    const materiales = calcularMateriales(personaje, nivelActual, nivelObjetivo, ascenderAlFinal);
    mostrarResultado(materiales, nombreElegido, nivelActual, nivelObjetivo, ascenderAlFinal);
});

// ─── 4. CÁLCULO COMBINADO: EXP + ASCENSIÓN ────────────────────────────────────

function calcularMateriales(personaje, nivelActual, nivelObjetivo, ascenderAlFinal) {
    const acumulador = {};

    // Función auxiliar para sumar al acumulador sin repetir la lógica
    function sumar(nombre, cantidad) {
        if (!cantidad || cantidad <= 0) return; // ignoramos valores a 0
        acumulador[nombre] = (acumulador[nombre] || 0) + cantidad;
    }

    // ── 4a. Costes de EXP y Mora por tramos de nivel ──────────────────────────
    // Cada tramo cubre los niveles entre dos breakpoints
    const tramosEXP = [
        [1, 20], [20, 40], [40, 50], [50, 60], [60, 70], [70, 80], [80, 90]
    ];

    tramosEXP.forEach(([inicio, fin]) => {
        // Incluimos el tramo si está completamente dentro del rango del usuario
        if (inicio >= nivelActual && fin <= nivelObjetivo) {
            const clave  = `${inicio}_${fin}`;
            const costes = window.levelCosts[clave];
            if (!costes) return;

            Object.entries(costes).forEach(([nombre, cantidad]) => {
                sumar(nombre, cantidad);
            });
        }
    });

    // ── 4b. Materiales de ascensión ───────────────────────────────────────────
    const todasLasFases = ["level_20", "level_40", "level_50", "level_60", "level_70", "level_80"];

    todasLasFases.forEach(fase => {
        const numeroFase = parseInt(fase.replace("level_", ""));

        // Los breakpoints intermedios SIEMPRE se incluyen (no puedes saltártelos)
        const esIntermedio = numeroFase > nivelActual && numeroFase < nivelObjetivo;

        // El breakpoint final solo se incluye si el usuario quiere ascender al llegar
        const esObjetivoYAscender = numeroFase === nivelObjetivo && ascenderAlFinal;

        if (!esIntermedio && !esObjetivoYAscender) return;

        const materialesFase = personaje.ascension_materials[fase];
        if (!materialesFase) return;

        materialesFase.forEach(material => {
            sumar(material.name, material.value);
        });
    });

    return acumulador;
}

// ─── 5. MUESTRA EL RESULTADO EN EL DOM ────────────────────────────────────────

function mostrarResultado(acumulador, nombre, nivelActual, nivelObjetivo, ascenderAlFinal) {
    const seccion = document.getElementById("resultado-section");
    const div     = document.getElementById("resultado");
    const titulo  = document.getElementById("resultado-titulo");

    seccion.classList.remove("hidden");

    if (Object.keys(acumulador).length === 0) {
        div.innerHTML = `<p class="mensaje-error">No hay materiales para ese rango.</p>`;
        return;
    }

    // Subtítulo indicando si incluye ascensión final o no
    const tieneAscensionFinal = BREAKPOINTS_ASCENSION.includes(nivelObjetivo);
    const etiquetaAscension   = tieneAscensionFinal
        ? (ascenderAlFinal ? " — Ascended" : " — Not ascended")
        : "";

    titulo.textContent = `${nombre} — Lv.${nivelActual} → Lv.${nivelObjetivo}${etiquetaAscension}`;

    // Mora primero, libros de EXP después, el resto alfabético
    const ORDEN_FIJO = ["Mora", "Hero's Wit", "Adventurer's Experience", "Wanderer's Advice"];

    const entradas = Object.entries(acumulador);
    entradas.sort(([a], [b]) => {
        const ia = ORDEN_FIJO.indexOf(a);
        const ib = ORDEN_FIJO.indexOf(b);
        if (ia !== -1 && ib !== -1) return ia - ib;
        if (ia !== -1) return -1;
        if (ib !== -1) return 1;
        return a.localeCompare(b);
    });

    div.innerHTML = entradas.map(([nombre, cantidad]) => {
        const esMora  = nombre === "Mora";
        const esLibro = ["Hero's Wit", "Adventurer's Experience", "Wanderer's Advice"].includes(nombre);
        return `
            <div class="material-card ${esMora ? "mora" : ""} ${esLibro ? "libro" : ""}">
                <span class="material-name">${nombre}</span>
                <span class="material-value">×${cantidad.toLocaleString()}</span>
            </div>
        `;
    }).join("");

    seccion.scrollIntoView({ behavior: "smooth", block: "start" });
}