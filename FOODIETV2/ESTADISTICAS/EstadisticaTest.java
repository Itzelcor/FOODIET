package stats;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class EstadisticaTest {

    private Estadistica stats;

    // Columnas: [0]pesoInicial [1]pesoActual [2]IMC [3]diferenciaPeso
    private static final String[] NOMBRES = {"ANA GARCIA", "LUIS TORRES", "MARIA LOPEZ"};
    private static final double[][] MATRIZ = {
        {80.0, 75.0, 27.55, -5.0},  // ANA: bajó 5 kg
        {90.0, 92.0, 28.40,  2.0},  // LUIS: subió 2 kg
        {60.0, 58.0, 20.07, -2.0}   // MARIA: bajó 2 kg
    };

    @BeforeEach
    void setUp() {
        stats = new Estadistica(NOMBRES, MATRIZ, 4, 2);
    }

    // ── imcMedio() ──────────────────────────────────────────────────

    // Caso nominal: tres pacientes con IMC conocido
    @Test
    void testImcMedioNominal() {
        double suma = MATRIZ[0][2] + MATRIZ[1][2] + MATRIZ[2][2];
        double esperado = Math.round((suma / 3) * 100.0) / 100.0;
        assertEquals(esperado, stats.imcMedio(), "El IMC medio no coincide con el esperado");
    }

    // Caso límite: sin pacientes el IMC medio debe ser 0.0
    @Test
    void testImcMedioSinPacientes() {
        Estadistica statsVacia = new Estadistica(new String[]{}, new double[][]{}, 0, 0);
        assertEquals(0.0, statsVacia.imcMedio(), "El IMC medio sin pacientes debe ser 0.0");
    }

    // Caso límite: un único paciente — el IMC medio es su propio IMC
    @Test
    void testImcMedioUnPaciente() {
        String[] nombres = {"SOLO PACIENTE"};
        double[][] matriz = {{70.0, 68.0, 24.50, -2.0}};
        Estadistica statsUno = new Estadistica(nombres, matriz, 1, 1);
        assertEquals(24.50, statsUno.imcMedio(), "Con un solo paciente el IMC medio es su IMC");
    }

    // ── pacientesQueBajaronPeso() ───────────────────────────────────

    // Caso nominal: Ana y Maria bajaron, Luis subió → 2
    @Test
    void testPacientesQueBajaronPesoNominal() {
        assertEquals(2, stats.pacientesQueBajaronPeso(), "Deben ser 2 los pacientes que bajaron peso");
    }

    // Caso erróneo: ningún paciente baja de peso → 0
    @Test
    void testNadieHaBajadoPeso() {
        String[] nombres = {"JOSE RUIZ", "MARTA VEGA"};
        double[][] matriz = {
            {70.0, 72.0, 25.0, 2.0},
            {80.0, 85.0, 27.0, 5.0}
        };
        Estadistica statsSubida = new Estadistica(nombres, matriz, 2, 2);
        assertEquals(0, statsSubida.pacientesQueBajaronPeso(), "No debe haber pacientes que bajaron peso");
    }

    // Caso límite: lista vacía de pacientes → 0
    @Test
    void testPacientesQueBajaronPesoListaVacia() {
        Estadistica statsVacia = new Estadistica(new String[]{}, new double[][]{}, 0, 0);
        assertEquals(0, statsVacia.pacientesQueBajaronPeso(), "Sin pacientes debe devolver 0");
    }

    // ── mejorPaciente() ─────────────────────────────────────────────

    // Caso nominal: Ana bajó 5 kg y es la que más bajó → mejor paciente
    @Test
    void testMejorPacienteNominal() {
        assertEquals("ANA GARCIA", stats.mejorPaciente(), "El mejor paciente debe ser ANA GARCIA");
    }

    // Caso límite: sin pacientes debe devolver "Sin datos"
    @Test
    void testMejorPacienteSinDatos() {
        Estadistica statsVacia = new Estadistica(new String[]{}, new double[][]{}, 0, 0);
        assertEquals("Sin datos", statsVacia.mejorPaciente(), "Sin pacientes debe devolver 'Sin datos'");
    }

    // Caso límite: un único paciente — él mismo es el mejor
    @Test
    void testMejorPacienteConUnPaciente() {
        String[] nombres = {"JUAN SOLO"};
        double[][] matriz = {{75.0, 70.0, 24.0, -5.0}};
        Estadistica statsUno = new Estadistica(nombres, matriz, 1, 1);
        assertEquals("JUAN SOLO", statsUno.mejorPaciente(), "Con un solo paciente debe devolver su nombre");
    }
}
