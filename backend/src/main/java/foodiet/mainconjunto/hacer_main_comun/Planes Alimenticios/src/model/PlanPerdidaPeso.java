public class PlanPerdidaPeso extends PlanAlimentacion {

    public PlanPerdidaPeso(String paciente, int calorias) {
        super(paciente, calorias);
    }

    @Override
    public void mostrarPlan() {
        System.out.println(
                paciente +
                        " - perdida peso - " +
                        calorias +
                        " kcal");
    }
}