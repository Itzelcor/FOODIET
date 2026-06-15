package hacer_main_comun.Estadisticas.src.modelo;

/**
 * Clase abstracta base para todas las métricas del subsistema de Estadísticas.
 * Cada métrica concreta debe implementar calcular() y getTipo().
 */
public abstract class Metrica {

    protected String nombrePaciente;
    protected double valor;
    protected String fecha;

    public Metrica(String nombrePaciente, double valor, String fecha) {
        this.nombrePaciente = nombrePaciente;
        this.valor          = valor;
        this.fecha          = fecha;
    }

    // Método abstracto: cada subclase calcula e interpreta su valor de forma diferente
    public abstract String calcular();

    // Método abstracto: devuelve el tipo de métrica
    public abstract String getTipo();

    // Método concreto compartido por todas las subclases
    public void mostrar() {
        System.out.println("[" + getTipo() + "] " + nombrePaciente
                + " | Fecha: " + fecha
                + " | Resultado: " + calcular());
    }

    public String getNombrePaciente() { return nombrePaciente; }
    public double getValor()          { return valor; }
    public String getFecha()          { return fecha; }

    @Override
    public String toString() {
        return getTipo() + " - " + nombrePaciente + " (" + fecha + "): " + calcular();
    }
}
