const fs = require("fs");
const path = require("path");

const API_BASE = "https://api.hakush.in/hsr/data/en";
const DATA_DIR = path.join(__dirname, "..", "data");
const OUTPUT_FILE = path.join(DATA_DIR, "personajesStarRail.json");

async function pedirJSON(url) {
    const respuesta = await fetch(url);

    if (!respuesta.ok) {
        throw new Error(`Error al conectar con la API: ${respuesta.status} ${respuesta.statusText}`);
    }

    return respuesta.json();
}

function normalizarListaPersonajes(datos) {
    if (Array.isArray(datos)) return datos;
    if (Array.isArray(datos.items)) return datos.items;
    if (datos.data && Array.isArray(datos.data.items)) return datos.data.items;

    // Hakush suele devolver un objeto con IDs como claves.
    if (datos && typeof datos === "object") {
        return Object.entries(datos).map(([id, personaje]) => ({
            id,
            ...personaje
        }));
    }

    return [];
}

function leerNombre(valor) {
    if (!valor) return null;
    if (typeof valor === "string") return valor;
    if (typeof valor === "object") {
        return valor.en || valor.name || valor.Name || valor.text || valor.value || null;
    }
    return null;
}

function normalizarMaterial(material) {
    if (!material || typeof material !== "object") return null;

    const nombre = leerNombre(material.name)
        || leerNombre(material.Name)
        || leerNombre(material.item?.name)
        || leerNombre(material.Item?.Name)
        || material.id
        || material.itemId
        || material.ItemID;

    const cantidad = material.value
        ?? material.count
        ?? material.Count
        ?? material.num
        ?? material.Num
        ?? material.amount
        ?? material.Amount;

    if (!nombre || !cantidad) return null;

    return {
        name: String(nombre),
        value: Number(cantidad)
    };
}

function normalizarMaterialesFase(fase) {
    if (!fase) return [];

    const lista = Array.isArray(fase)
        ? fase
        : fase.materials || fase.Materials || fase.items || fase.Items || fase.cost || fase.Cost || [];

    return lista
        .map(normalizarMaterial)
        .filter(Boolean);
}

function normalizarAscensiones(personaje) {
    const candidato = personaje.ascension_materials
        || personaje.ascensions
        || personaje.Ascensions
        || personaje.promotions
        || personaje.Promotions
        || personaje.promotion
        || personaje.Promotion
        || personaje.materials?.ascension
        || personaje.Materials?.Ascension;

    const niveles = [20, 40, 50, 60, 70, 80];
    const ascensiones = {};

    if (Array.isArray(candidato)) {
        candidato.slice(0, niveles.length).forEach((fase, index) => {
            ascensiones[`level_${niveles[index]}`] = normalizarMaterialesFase(fase);
        });
        return ascensiones;
    }

    if (candidato && typeof candidato === "object") {
        niveles.forEach(nivel => {
            const clavesPosibles = [
                `level_${nivel}`,
                String(nivel),
                `Lv${nivel}`,
                `lv${nivel}`
            ];

            const fase = clavesPosibles
                .map(clave => candidato[clave])
                .find(Boolean);

            ascensiones[`level_${nivel}`] = normalizarMaterialesFase(fase);
        });
    }

    return ascensiones;
}

function normalizarPersonaje(id, resumen, detalle) {
    const datos = detalle.data?.item || detalle.data || detalle;

    return {
        id: String(id),
        name: leerNombre(datos.name) || leerNombre(datos.Name) || leerNombre(resumen.name) || leerNombre(resumen.Name) || String(id),
        rarity: datos.rarity || datos.Rarity || resumen.rarity || resumen.Rarity || null,
        path: leerNombre(datos.path) || leerNombre(datos.Path) || leerNombre(resumen.path) || leerNombre(resumen.Path) || null,
        element: leerNombre(datos.element) || leerNombre(datos.Element) || leerNombre(resumen.element) || leerNombre(resumen.Element) || null,
        ascension_materials: normalizarAscensiones(datos),
        raw: datos
    };
}

async function fetchPersonajesStarRail() {
    try {
        const listaBruta = await pedirJSON(`${API_BASE}/character.json`);
        const listaPersonajes = normalizarListaPersonajes(listaBruta);

        console.log(`Encontrados ${listaPersonajes.length} personajes. Descargando datos...`);

        const personajesCompletos = [];

        for (const resumen of listaPersonajes) {
            const id = resumen.id || resumen.ID || resumen.avatarId || resumen.AvatarID;

            if (!id) {
                console.warn("Personaje sin ID, saltando...");
                continue;
            }

            const detalle = await pedirJSON(`${API_BASE}/character/${id}.json`);
            const personaje = normalizarPersonaje(id, resumen, detalle);

            personajesCompletos.push(personaje);
            console.log(`OK ${personaje.name}`);
        }

        personajesCompletos.sort((a, b) => a.name.localeCompare(b.name));

        if (!fs.existsSync(DATA_DIR)) {
            fs.mkdirSync(DATA_DIR, { recursive: true });
        }

        fs.writeFileSync(OUTPUT_FILE, JSON.stringify(personajesCompletos, null, 2), "utf-8");

        console.log(`\nListo. ${personajesCompletos.length} personajes guardados en ${OUTPUT_FILE}`);
    } catch (error) {
        console.error("Algo salio mal:", error);
    }
}

fetchPersonajesStarRail();
