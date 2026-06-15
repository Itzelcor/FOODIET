public class PlanPerdidaPeso extends PlanAlimentacion {
    public PlanPerdidaPeso(String paciente, int calorias) {
        super(paciente, calorias);
    }

    public void mostrarPlan() {
        System.out.println(paciente + ": plan de perdida de peso, " + calorias + " kcal al dia");
    }
}
