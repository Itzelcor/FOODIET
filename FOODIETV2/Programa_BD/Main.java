public class Main {

    public static void main(String[] args) {

        Persona persona1 =
                new Paciente(
                        1,
                        "Ana López"
                );

        Persona persona2 =
                new Nutricionista(
                        2,
                        "Laura Martínez"
                );

        persona1.mostrarInformacion();
        persona2.mostrarInformacion();

        PacienteDAO dao =
                new PacienteDAO();

        dao.insertarPaciente(
                (Paciente) persona1
        );
    }
}