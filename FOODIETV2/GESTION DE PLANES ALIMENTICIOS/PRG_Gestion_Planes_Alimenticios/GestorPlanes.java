import java.util.ArrayList;

public class GestorPlanes {

    private ArrayList<PlanAlimenticio> planes;
    private ArrayList<AsignacionPlan> asignaciones;

    public GestorPlanes() {
        planes = new ArrayList<>();
        asignaciones = new ArrayList<>();
    }

    public void registrarPlan(PlanAlimenticio plan) {
        planes.add(plan);
    }

    public void asignarPlan(Paciente paciente, PlanAlimenticio plan) {
        asignaciones.add(new AsignacionPlan(paciente, plan));
    }

    public PlanAlimenticio buscarPlanPaciente(int idPaciente) {

        for (AsignacionPlan asignacion : asignaciones) {

            if (asignacion.getPaciente().getId() == idPaciente) {
                return asignacion.getPlan();
            }
        }

        return null;
    }

    public void mostrarPlanes() {

        for (PlanAlimenticio plan : planes) {
            System.out.println(plan);
        }
    }
}