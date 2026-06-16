package stats.Pacientes;

public class PacienteAdulto extends Paciente {

    public PacienteAdulto(int idPaciente, String dni, String nombre, String apellidos,
                          String telefono, String email, String direccion,
                          String fechaNac, String sexo, double altura) {
        super(idPaciente, dni, nombre, apellidos, telefono, email, direccion, fechaNac, sexo, altura);
    }

    @Override
    public String tipoPaciente() {
        return "Adulto";
    }
}
