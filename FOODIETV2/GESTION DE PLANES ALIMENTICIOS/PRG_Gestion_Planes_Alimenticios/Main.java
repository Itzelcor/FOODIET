public class Main {

    public static void main(String[] args) {

        Paciente paciente = new Paciente(1, "Ana López");

        Comida desayuno = new Comida("Avena y frutas", 300);
        Comida almuerzo = new Comida("Pollo con ensalada", 500);
        Comida cena = new Comida("Pescado y verduras", 400);

        PlanAlimenticio plan =
                new PlanAlimenticio(1001, "Pérdida de peso");

        plan.agregarComida(desayuno);
        plan.agregarComida(almuerzo);
        plan.agregarComida(cena);

        GestorPlanes gestor = new GestorPlanes();

        gestor.registrarPlan(plan);
        gestor.asignarPlan(paciente, plan);

        System.out.println("=== PLANES REGISTRADOS ===");
        gestor.mostrarPlanes();

        System.out.println("\n=== PLAN DEL PACIENTE ===");

        PlanAlimenticio planPaciente =
                gestor.buscarPlanPaciente(1);

        if (planPaciente != null) {

            System.out.println(planPaciente);

            System.out.println("\nComidas:");

            for (Comida comida : planPaciente.getComidas()) {
                System.out.println("- " + comida);
            }

        } else {
            System.out.println("No existe un plan asignado.");
        }
    }
}