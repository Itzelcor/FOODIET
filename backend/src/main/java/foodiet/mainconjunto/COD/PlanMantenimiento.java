public class PlanMantenimiento extends PlanAlimentacion {

    public PlanMantenimiento(String paciente, int calorias) {
        super(paciente, calorias);
    }

    @Override
    public void mostrarPlan() {
        System.out.println(
                paciente +
                        " - mantenimiento - " +
                        calorias +
                        " kcal");
    }
}