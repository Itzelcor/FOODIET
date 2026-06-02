import java.util.ArrayList;

public class PlanAlimenticio {

    private int codigo;
    private String objetivo;
    private ArrayList<Comida> comidas;

    public PlanAlimenticio(int codigo, String objetivo) {
        this.codigo = codigo;
        this.objetivo = objetivo;
        this.comidas = new ArrayList<>();
    }

    public void agregarComida(Comida comida) {
        comidas.add(comida);
    }

    public int calcularCaloriasTotales() {
        int total = 0;

        for (Comida comida : comidas) {
            total += comida.getCalorias();
        }

        return total;
    }

    public int getCodigo() {
        return codigo;
    }

    public String getObjetivo() {
        return objetivo;
    }

    public ArrayList<Comida> getComidas() {
        return comidas;
    }

    @Override
    public String toString() {
        return "Plan #" + codigo +
               " | Objetivo: " + objetivo +
               " | Calorías Totales: " + calcularCaloriasTotales();
    }
}