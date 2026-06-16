package stats;

/**
 * Métrica de porcentaje de grasa corporal.
 * Evalúa el nivel de grasa según rangos saludables por sexo.
 */
public class MetricaGrasa extends Metrica {

    private char sexo; // 'M' o 'F'

    public MetricaGrasa(String nombrePaciente, double porcentajeGrasa,
                        char sexo, String fecha) {
        super(nombrePaciente, porcentajeGrasa, fecha);
        this.sexo = Character.toUpperCase(sexo);
    }

    @Override
    public String calcular() {
        String nivel = evaluarGrasa(valor, sexo);
        return "% Grasa: " + valor + "% → " + nivel;
    }

    @Override
    public String getTipo() {
        return "GRASA_CORPORAL";
    }

    // Polimorfismo: evaluación propia diferenciada por sexo
    private String evaluarGrasa(double porcentaje, char sexo) {
        if (sexo == 'M') {
            if (porcentaje < 6)  return "Grasa esencial";
            if (porcentaje < 14) return "Atlético";
            if (porcentaje < 18) return "En forma";
            if (porcentaje < 25) return "Aceptable";
            return "Obesidad";
        } else {
            if (porcentaje < 14) return "Grasa esencial";
            if (porcentaje < 21) return "Atlético";
            if (porcentaje < 25) return "En forma";
            if (porcentaje < 32) return "Aceptable";
            return "Obesidad";
        }
    }

    public char getSexo() { return sexo; }
}
