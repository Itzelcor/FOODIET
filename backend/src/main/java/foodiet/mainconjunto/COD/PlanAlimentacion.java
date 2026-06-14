public abstract class PlanAlimentacion {

    String paciente;
    int calorias;

    public PlanAlimentacion(String paciente, int calorias) {
        this.paciente = paciente;
        this.calorias = calorias;
    }

    public abstract void mostrarPlan();
}