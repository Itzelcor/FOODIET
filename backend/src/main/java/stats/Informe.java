package stats;

/**
 * Subsistema: Estadística
 * Clase que representa un informe generado a partir de una Estadistica.
 */
public class Informe {

    private static int contadorId = 1;

    private int idInforme;
    private String titulo;
    private String fecha;
    private String tipo; // "MENSUAL", "TRIMESTRAL", "ANUAL"
    private String generadoPor; // nombre del administrador
    private Estadistica estadistica;

    public Informe(String titulo, String fecha, String tipo,
            String generadoPor, Estadistica estadistica) {
        this.idInforme = contadorId++;
        this.titulo = titulo;
        this.fecha = fecha;
        this.tipo = tipo.toUpperCase();
        this.generadoPor = generadoPor;
        this.estadistica = estadistica;
    }

    // Genera y muestra el informe completo por pantalla
    public void generarInforme() {
        System.out.println("\n" + "=".repeat(58));
        System.out.println("  INFORME #" + idInforme + " - " + tipo);
        System.out.println("  " + titulo.toUpperCase());
        System.out.println("  Fecha        : " + fecha);
        System.out.println("  Generado por : " + generadoPor);
        System.out.println("=".repeat(58));

        estadistica.mostrarResumen();
        estadistica.mostrarMatriz();

        System.out.println("\n  Conclusiones:");
        System.out.println("  - Mejor paciente     : " + estadistica.mejorPaciente());
        System.out.println("  - Pacientes que bajaron peso: " + estadistica.pacientesQueBajaronPeso() + " de "
                + estadistica.getTotalPacientes());
        System.out.println("  - IMC medio clínica  : " + estadistica.imcMedio());
        System.out.println("=".repeat(58));
    }

    // Tratamiento de cadenas: cabecera del informe como String
    public String getCabecera() {
        return "INFORME #" + idInforme + " [" + tipo + "] - " + titulo.toUpperCase() + " | Fecha: " + fecha
                + " | Generado por: " + generadoPor.toUpperCase();
    }

    // Tratamiento de cadenas: comprueba si el tipo coincide
    public boolean esDeTipo(String tipoBuscar) {
        boolean coincide = false;
        if (tipo != null) {
            coincide = tipo.equalsIgnoreCase(tipoBuscar.trim());
        }
        return coincide;
    }

    public int getIdInforme() {
        return idInforme;
    }

    public String getTitulo() {
        return titulo;
    }

    public String getFecha() {
        return fecha;
    }

    public String getTipo() {
        return tipo;
    }

    @Override
    public String toString() {
        return getCabecera();
    }
}
