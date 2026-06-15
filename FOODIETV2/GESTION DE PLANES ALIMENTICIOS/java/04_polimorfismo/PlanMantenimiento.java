public class PlanMantenimiento extends PlanAlimentacion {
    public PlanMantenimiento(String paciente, int calorias) {
        super(paciente, calorias);
    }

    public void mostrarPlan() {
        System.out.println(paciente + ": plan de mantenimiento, " + calorias + " kcal al dia");
    }
}
