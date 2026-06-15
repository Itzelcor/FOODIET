package hacer_main_comun.Estadisticas.src.modelo;


/**
 * Representa un registro de progreso de un paciente
 * de la tabla 'progreso' de la base de datos FooDiet.
 */
public class Progreso {

    private int    idProgreso;
    private int    idPaciente;
    private int    idPlan;
    private String fecha;
    private double peso;
    private double imc;
    private String observaciones;

    // Constructor completo
    public Progreso(int idProgreso, int idPaciente, int idPlan,
                    String fecha, double peso, double imc, String observaciones) {
        this.idProgreso    = idProgreso;
        this.idPaciente    = idPaciente;
        this.idPlan        = idPlan;
        this.fecha         = fecha;
        this.peso          = peso;
        this.imc           = imc;
        this.observaciones = observaciones;
    }

    // Constructor sin id (para inserciones nuevas)
    public Progreso(int idPaciente, int idPlan, String fecha,
                    double peso, double imc, String observaciones) {
        this.idPaciente    = idPaciente;
        this.idPlan        = idPlan;
        this.fecha         = fecha;
        this.peso          = peso;
        this.imc           = imc;
        this.observaciones = observaciones;
    }

    // Getters
    public int    getIdProgreso()    { return idProgreso; }
    public int    getIdPaciente()    { return idPaciente; }
    public int    getIdPlan()        { return idPlan; }
    public String getFecha()         { return fecha; }
    public double getPeso()          { return peso; }
    public double getImc()           { return imc; }
    public String getObservaciones() { return observaciones; }

    // Setters
    public void setPeso(double peso)          { this.peso = peso; }
    public void setImc(double imc)            { this.imc = imc; }
    public void setObservaciones(String obs)  { this.observaciones = obs; }

    @Override
    public String toString() {
        return "ID: " + idProgreso
             + " | Paciente: " + idPaciente
             + " | Fecha: " + fecha
             + " | Peso: " + peso + " kg"
             + " | IMC: " + imc
             + " | Obs: " + observaciones;
    }
}