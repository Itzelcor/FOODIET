public class AsignacionPlan {

    private Paciente paciente;
    private PlanAlimenticio plan;

    public AsignacionPlan(Paciente paciente, PlanAlimenticio plan) {
        this.paciente = paciente;
        this.plan = plan;
    }

    public Paciente getPaciente() {
        return paciente;
    }

    public PlanAlimenticio getPlan() {
        return plan;
    }
}