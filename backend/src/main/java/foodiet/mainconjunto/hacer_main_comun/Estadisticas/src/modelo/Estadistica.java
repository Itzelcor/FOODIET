package hacer_main_comun.Estadisticas.src.modelo;

public class Estadistica {

    private double[][] matrizDatos; // MATRIZ principal
    private String[] nombres; // vector de nombres de pacientes
    private int totalPacientes;
    private int totalCitas;
    private int planesActivos;

    public Estadistica(String[] nombres, double[][] matrizDatos,
            int totalCitas, int planesActivos) {
        this.nombres = nombres;
        this.matrizDatos = matrizDatos;
        this.totalPacientes = nombres.length;
        this.totalCitas = totalCitas;
        this.planesActivos = planesActivos;
    }

    // Calcula el IMC medio de todos los pacientes
    public double imcMedio() {
        double suma = 0;
        for (int i = 0; i < totalPacientes; i++) {
            suma += matrizDatos[i][2];
        }
        double media = 0;
        if (totalPacientes > 0) {
            media = Math.round((suma / totalPacientes) * 100.0) / 100.0;
        }
        return media;
    }

    // Calcula el peso medio actual de todos los pacientes
    public double pesoMedioActual() {
        double suma = 0;
        for (int i = 0; i < totalPacientes; i++) {
            suma += matrizDatos[i][1];
        }
        double media = 0;
        if (totalPacientes > 0) {
            media = Math.round((suma / totalPacientes) * 100.0) / 100.0;
        }
        return media;
    }

    // Cuenta cuántos pacientes han bajado de peso (diferencia negativa)
    public int pacientesQueBajaronPeso() {
        int contador = 0;
        for (int i = 0; i < totalPacientes; i++) {
            if (matrizDatos[i][3] < 0) {
                contador++;
            }
        }
        return contador;
    }

    // Devuelve el nombre del paciente con mayor pérdida de peso
    public String mejorPaciente() {
        String resultado = "Sin datos";
        if (totalPacientes > 0) {
            int indiceMejor = 0;
            for (int i = 1; i < totalPacientes; i++) {
                if (matrizDatos[i][3] < matrizDatos[indiceMejor][3]) {
                    indiceMejor = i;
                }
            }
            resultado = nombres[indiceMejor];
        }
        return resultado;
    }

    // Muestra la matriz completa de estadísticas por paciente
    public void mostrarMatriz() {
        System.out.println("--------------MATRIZ DE ESTADÍSTICAS POR PACIENTE-------------------");
        System.out.printf(" %-20s %8s %8s %7s %8s %n",
                "Paciente", "P.Ini", "P.Act", "IMC", "Dif.kg");
        for (int i = 0; i < totalPacientes; i++) {
            System.out.printf(" %-20s %8.2f %8.2f %7.2f %8.2f %n", nombres[i],
                    matrizDatos[i][0], matrizDatos[i][1], matrizDatos[i][2], matrizDatos[i][3]);
        }
    }

    // Muestra el resumen global de la clínica
    public void mostrarResumen() {
        System.out.println("---------RESUMEN ESTADÍSTICO - FOODIET -----------------");
        System.out.printf("  Total pacientes      : %-12d %n", totalPacientes);
        System.out.printf("  Total citas          : %-12d %n", totalCitas);
        System.out.printf("  Planes activos       : %-12d %n", planesActivos);
        System.out.printf("  IMC medio            : %-12.2f %n", imcMedio());
        System.out.printf("  Peso medio actual    : %-9.2f kg %n", pesoMedioActual());
        System.out.printf("  Bajaron de peso      : %-12d %n", pacientesQueBajaronPeso());
        System.out.printf("  Mejor paciente       : %-12s %n", mejorPaciente());
    }

    public int getTotalPacientes() {
        return totalPacientes;
    }

    public int getTotalCitas() {
        return totalCitas;
    }

    public int getPlanesActivos() {
        return planesActivos;
    }

    public double[][] getMatrizDatos() {
        return matrizDatos;
    }

    public String[] getNombres() {
        return nombres;
    }
}
