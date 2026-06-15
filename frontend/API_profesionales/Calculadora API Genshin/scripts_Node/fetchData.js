// fs es un módulo nativo de Node.js, no hay que instalar nada
const fs = require("fs");

async function fetchPersonajes() {
    try {
        // 1. Pedimos la lista de todos los personajes
        const respuesta = await fetch("https://genshin.jmp.blue/characters");

        if (!respuesta.ok) {
            throw new Error(`Error al conectar con la API: ${respuesta.status}`);
        }

        const listaPersonajes = await respuesta.json();
        // listaPersonajes es un array de nombres: ["hu-tao", "ayaka", ...]

        console.log(`Encontrados ${listaPersonajes.length} personajes. Descargando datos...`);

        // 2. Por cada nombre, pedimos los datos completos de ese personaje
        const personajesCompletos = [];

        for (const nombre of listaPersonajes) {
            const respuestaPersonaje = await fetch(`https://genshin.jmp.blue/characters/${nombre}`);

            if (!respuestaPersonaje.ok) {
                console.warn(`No se pudo obtener: ${nombre}`);
                continue; // si falla uno, saltamos al siguiente
            }

            const datos = await respuestaPersonaje.json();
            personajesCompletos.push(datos);

            console.log(`✓ ${nombre}`);
        }

        // 3. Convertimos el array a string JSON y lo escribimos en disco
        const json = JSON.stringify(personajesCompletos, null, 2);
        // null, 2 → formatea el JSON con 2 espacios de indentación, para que sea legible

        fs.writeFileSync("data/personajes.json", json, "utf-8");

        console.log(`\nListo. ${personajesCompletos.length} personajes guardados en data/personajes.json`);

    } catch (error) {
        console.error("Algo salió mal:", error);
    }
}

if (!fs.existsSync("data")) {
    fs.mkdirSync("data");
}

fetchPersonajes();