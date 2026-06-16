package stats.Planes;

/**
 * Clase que representa un plan dietético.
 * Subsistema: Gestión de Planes Alimenticios — Octavian Matei
 */
public class PlanDietetico {

    private int    idPlan;
    private int    idPaciente;
    private int    idNutricionista;
    private String objetivo;
    private int    caloriasDiarias;
    private String fechaInicio;
    private String fechaFin;
    private String estado;

    public PlanDietetico(int idPlan, int idPaciente, int idNutricionista,
                         String objetivo, int caloriasDiarias,
                         String fechaInicio, String fechaFin, String estado) {
        this.idPlan          = idPlan;
        this.idPaciente      = idPaciente;
        this.idNutricionista = idNutricionista;
        this.objetivo        = objetivo;
        this.caloriasDiarias = caloriasDiarias;
        this.fechaInicio     = fechaInicio;
        this.fechaFin        = fechaFin;
        this.estado          = estado;
    }

    // Getters
    public int    getIdPlan()          { return idPlan; }
    public int    getIdPaciente()      { return idPaciente; }
    public int    getIdNutricionista() { return idNutricionista; }
    public String getObjetivo()        { return objetivo; }
    public int    getCaloriasDiarias() { return caloriasDiarias; }
    public String getFechaInicio()     { return fechaInicio; }
    public String getFechaFin()        { return fechaFin; }
    public String getEstado()          { return estado; }

    // Setters
    public void setIdPlan(int id)           { this.idPlan = id; }
    public void setEstado(String estado)    { this.estado = estado; }
    public void setObjetivo(String obj)     { this.objetivo = obj; }

    @Override
    public String toString() {
        return "Plan ID: " + idPlan
             + " | Paciente: " + idPaciente
             + " | Objetivo: " + objetivo
             + " | " + caloriasDiarias + " kcal/día"
             + " | Estado: " + estado;
    }
}
