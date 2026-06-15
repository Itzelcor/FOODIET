package foodiet.modelo;

public class PlanAlimenticio {

    private int id;
    private int idPaciente;
    private int idProfesional;
    private String fechaInicio;
    private String fechaFin;
    private String objetivo;
    private String descripcion;
    private int caloriasDiarias;

    public PlanAlimenticio(int id, int idPaciente, int idProfesional,
                           String fechaInicio, String fechaFin,
                           String objetivo, String descripcion,
                           int caloriasDiarias) {
        this.id = id;
        this.idPaciente = idPaciente;
        this.idProfesional = idProfesional;
        this.fechaInicio = fechaInicio;
        this.fechaFin = fechaFin;
        this.objetivo = objetivo;
        this.descripcion = descripcion;
        this.caloriasDiarias = caloriasDiarias;
    }

    public PlanAlimenticio(int idPaciente, int idProfesional,
                           String fechaInicio, String fechaFin,
                           String objetivo, String descripcion,
                           int caloriasDiarias) {
        this.id = 0;
        this.idPaciente = idPaciente;
        this.idProfesional = idProfesional;
        this.fechaInicio = fechaInicio;
        this.fechaFin = fechaFin;
        this.objetivo = objetivo;
        this.descripcion = descripcion;
        this.caloriasDiarias = caloriasDiarias;
    }

    public int getId() {
        return this.id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getIdPaciente() {
        return this.idPaciente;
    }

    public void setIdPaciente(int idPaciente) {
        this.idPaciente = idPaciente;
    }

    public int getIdProfesional() {
        return this.idProfesional;
    }

    public void setIdProfesional(int idProfesional) {
        this.idProfesional = idProfesional;
    }

    public String getFechaInicio() {
        return this.fechaInicio;
    }

    public void setFechaInicio(String fechaInicio) {
        this.fechaInicio = fechaInicio;
    }

    public String getFechaFin() {
        return this.fechaFin;
    }

    public void setFechaFin(String fechaFin) {
        this.fechaFin = fechaFin;
    }

    public String getObjetivo() {
        return this.objetivo;
    }

    public void setObjetivo(String objetivo) {
        this.objetivo = objetivo;
    }

    public String getDescripcion() {
        return this.descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public int getCaloriasDiarias() {
        return this.caloriasDiarias;
    }

    public void setCaloriasDiarias(int caloriasDiarias) {
        this.caloriasDiarias = caloriasDiarias;
    }

    public String mostrarPlan() {
        String resultado = "Plan N\u00ba " + this.id +
                " | Paciente ID: " + idPaciente +
                " | Profesional ID: " + idProfesional +
                " | Inicio: " + fechaInicio +
                " | Fin: " + (fechaFin != null ? fechaFin : "Sin fecha fin") +
                " | Objetivo: " + objetivo +
                " | Calor\u00edas: " + caloriasDiarias + " kcal/d\u00eda";
        return resultado;
    }
}
