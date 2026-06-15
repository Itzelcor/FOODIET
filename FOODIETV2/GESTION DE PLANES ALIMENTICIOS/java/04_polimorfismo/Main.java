public class Main {
    public static void main(String[] args) {
        PlanAlimentacion[] planes = new PlanAlimentacion[2];

        planes[0] = new PlanPerdidaPeso("Ana Garcia", 1800);
        planes[1] = new PlanMantenimiento("Carlos Ruiz", 2200);

        for (int i = 0; i < planes.length; i++) {
            planes[i].mostrarPlan();
        }
    }
}
