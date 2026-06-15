package stats;

/**
 * Métrica de peso. Calcula la diferencia entre el peso actual
 * y el peso inicial del paciente.
 */
public class MetricaPeso extends Metrica {

    private double pesoInicial;
    private double pesoActual;

    public MetricaPeso(String nombrePaciente, double pesoInicial,
                       double pesoActual, String fecha) {
        super(nombrePaciente, pesoActual, fecha);
        this.pesoInicial = pesoInicial;
        this.pesoActual  = pesoActual;
    }

    @Override
    public String calcular() {
        double diferencia = Math.round((pesoActual - pesoInicial) * 100.0) / 100.0;
        String resultado;
        if (diferencia < 0) {
            resultado = "Bajó " + Math.abs(diferencia) + " kg (de "
                    + pesoInicial + " → " + pesoActual + " kg)";
        } else if (diferencia > 0) {
            resultado = "Subió " + diferencia + " kg (de "
                    + pesoInicial + " → " + pesoActual + " kg)";
        } else {
            resultado = "Sin cambio — peso estable en " + pesoActual + " kg";
        }
        return resultado;
    }

    @Override
    public String getTipo() {
        return "PESO";
    }

    public double getDiferencia() {
        return Math.round((pesoActual - pesoInicial) * 100.0) / 100.0;
    }

    public double getPesoInicial() { return pesoInicial; }
    public double getPesoActual()  { return pesoActual; }
}
