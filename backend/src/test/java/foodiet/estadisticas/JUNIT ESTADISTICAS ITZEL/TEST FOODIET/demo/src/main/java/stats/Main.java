package stats;

public class Main {

    public static void main(String[] args) {

        // ── Datos de prueba: nombres de pacientes y sus métricas de peso e IMC
        String[] nombres = {
                "ANA FERNANDEZ",
                "LUIS TORRES",
                "MARIA SANCHEZ"
        };

        // ── Matriz de datos [paciente][métrica]
        // Columnas: [0]pesoInicial [1]pesoActual [2]IMC [3]diferenciaPeso
        double[][] matrizDatos = {
                { 80.0, 77.0, 28.3, -3.0 },
                { 95.0, 92.5, 28.5, -2.5 },
                { 62.0, 61.0, 21.1, -1.0 }
        };

        // ── Crear la estadística
        Estadistica stats = new Estadistica(nombres, matrizDatos, 4, 2);

        // Mostrar la matriz y el resumen
        stats.mostrarMatriz();
        stats.mostrarResumen();

        // Crear el informe a partir de la estadística
        Informe informe = new Informe(
                "Informe mensual marzo 2025",
                "31/03/2025",
                "MENSUAL",
                "Carlos Ruiz Mora",
                stats);

        informe.generarInforme();

        // Tratamiento de cadenas
        System.out.println("\nCabecera : " + informe.getCabecera());
        System.out.println("¿Es mensual? " + informe.esDeTipo("mensual"));
    }
}
