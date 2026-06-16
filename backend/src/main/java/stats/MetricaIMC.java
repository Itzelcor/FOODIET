package stats;

/**
 * Métrica de IMC. Calcula el Índice de Masa Corporal a partir
 * del peso actual y la altura del paciente.
 */
public class MetricaIMC extends Metrica {

    private double altura; // en metros

    public MetricaIMC(String nombrePaciente, double peso,
                      double altura, String fecha) {
        super(nombrePaciente, peso, fecha);
        this.altura = altura;
    }

    @Override
    public String calcular() {
        double imc = Math.round((valor / (altura * altura)) * 100.0) / 100.0;
        String clasificacion = clasificarIMC(imc);
        return "IMC: " + imc + " → " + clasificacion;
    }

    @Override
    public String getTipo() {
        return "IMC";
    }

    // Polimorfismo: clasificación propia de esta métrica
    private String clasificarIMC(double imc) {
        if (imc < 18.5) return "Bajo peso";
        if (imc < 25.0) return "Peso normal";
        if (imc < 30.0) return "Sobrepeso";
        return "Obesidad";
    }

    public double getIMC() {
        return Math.round((valor / (altura * altura)) * 100.0) / 100.0;
    }

    public double getAltura() { return altura; }
}
