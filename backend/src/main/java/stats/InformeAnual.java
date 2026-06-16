package stats;

public class InformeAnual extends Informe {

    private String fechaInicio;
    private String fechaFin;
    private String categoria;

    public InformeAnual(int id, String fechaInicio, String fechaFin,
                        String generadoPor, String categoria) {
        super("Informe Anual", fechaInicio, "ANUAL", generadoPor,
              new Estadistica(new String[]{"N/A"}, new double[][]{{0, 0, 0, 0}}, 0, 0));
        this.fechaInicio = fechaInicio;
        this.fechaFin    = fechaFin;
        this.categoria   = categoria;
    }

    @Override
    public void generarInforme() {
        System.out.println("  [ANUAL]       " + fechaInicio + " → " + fechaFin
                + " | Categoría: " + categoria);
    }
}
