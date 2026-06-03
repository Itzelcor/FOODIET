public class Main {

    public static void main(String[] args) {

        PacienteDAO pacienteDAO =
                new PacienteDAO();

        pacienteDAO.insertarPaciente(
                "Ana",
                "Lopez",
                68,
                1.65
        );

        PlanAlimenticioDAO planDAO =
                new PlanAlimenticioDAO();

        planDAO.mostrarPlanes();
    }
}