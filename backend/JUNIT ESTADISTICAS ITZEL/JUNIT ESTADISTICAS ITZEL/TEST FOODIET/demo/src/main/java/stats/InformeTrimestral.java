package stats;

public class InformeTrimestral extends Informe {

    private String fechaInicio;
    private String fechaFin;
    private String categoria;

    public InformeTrimestral(int id, String fechaInicio, String fechaFin,
                             String generadoPor, String categoria) {
        super("Informe Trimestral", fechaInicio, "TRIMESTRAL", generadoPor,
              new Estadistica(new String[]{"N/A"}, new double[][]{{0, 0, 0, 0}}, 0, 0));
        this.fechaInicio = fechaInicio;
        this.fechaFin    = fechaFin;
        this.categoria   = categoria;
    }

    @Override
    public void generarInforme() {
        System.out.println("  [TRIMESTRAL]  " + fechaInicio + " → " + fechaFin
                + " | Categoría: " + categoria);
    }
}
