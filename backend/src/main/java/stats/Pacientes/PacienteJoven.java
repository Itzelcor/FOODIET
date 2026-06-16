package stats.Pacientes;

public class PacienteJoven extends Paciente {

    private boolean esMenorEdad;

    public PacienteJoven(int idPaciente, String dni, String nombre, String apellidos,
                         String telefono, String email, String direccion,
                         String fechaNac, String sexo, double altura, boolean esMenorEdad) {
        super(idPaciente, dni, nombre, apellidos, telefono, email, direccion, fechaNac, sexo, altura);
        this.esMenorEdad = esMenorEdad;
    }

    @Override
    public String tipoPaciente() {
        return "Joven";
    }

    public boolean isEsMenorEdad() { return esMenorEdad; }
}
