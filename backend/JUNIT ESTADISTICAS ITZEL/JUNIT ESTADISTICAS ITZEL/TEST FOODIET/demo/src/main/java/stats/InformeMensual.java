package stats;

public class InformeMensual extends Informe {

    private String fechaInicio;
    private String fechaFin;
    private String categoria;

    public InformeMensual(int id, String fechaInicio, String fechaFin,
                          String generadoPor, String categoria) {
        super("Informe Mensual", fechaInicio, "MENSUAL", generadoPor,
              new Estadistica(new String[]{"N/A"}, new double[][]{{0, 0, 0, 0}}, 0, 0));
        this.fechaInicio = fechaInicio;
        this.fechaFin    = fechaFin;
        this.categoria   = categoria;
    }

    @Override
    public void generarInforme() {
        System.out.println("  [MENSUAL]     " + fechaInicio + " → " + fechaFin
                + " | Categoría: " + categoria);
    }
}
