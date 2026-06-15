var API = 'https://pokeapi.co/api/v2';

// ── 1. Cargar listado al abrir la página ──────────────────────
fetch(API + '/pokemon?limit=20')
    .then(function(respuesta) {
        return respuesta.json();
    })
    .then(function(datos) {
        var tbody = document.getElementById('cuerpo-tabla');
        tbody.innerHTML = '';

        for (var i = 0; i < datos.results.length; i++) {
            var pokemon = datos.results[i];
            var partes  = pokemon.url.split('/');
            var id      = partes[partes.length - 2];
            var nombre  = pokemon.name;

            var fila = document.createElement('tr');
            fila.innerHTML =
                '<td>' + id + '</td>' +
                '<td>' + nombre + '</td>';
            tbody.appendChild(fila);
        }
    });

// ── 2. Buscar Pokémon por nombre ──────────────────────────────
function buscarPokemon() {
    var nombre    = document.getElementById('input-nombre').value.toLowerCase().trim();
    var resultado = document.getElementById('resultado');
    var errorMsg  = document.getElementById('error-msg');

    resultado.style.display = 'none';
    errorMsg.style.display  = 'none';

    if (nombre === '') return;

    fetch(API + '/pokemon/' + nombre)
        .then(function(respuesta) {
            if (!respuesta.ok) { throw new Error('No encontrado'); }
            return respuesta.json();
        })
        .then(function(datos) {
            document.getElementById('img-pokemon').src =
                datos.sprites.front_default;

            document.getElementById('res-nombre').textContent =
                datos.name;

            document.getElementById('res-altura').textContent =
                datos.height + ' dm';

            document.getElementById('res-peso').textContent =
                datos.weight + ' hg';

            document.getElementById('res-tipo').textContent =
                datos.types[0].type.name;

            resultado.style.display = 'block';
        })
        .catch(function() {
            errorMsg.style.display = 'block';
        });
}