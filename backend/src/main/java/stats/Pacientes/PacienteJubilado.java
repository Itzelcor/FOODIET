package stats.Pacientes;

public class PacienteJubilado extends Paciente {

    private boolean tieneDescuentoEspecial;

    public PacienteJubilado(int idPaciente, String dni, String nombre, String apellidos,
                            String telefono, String email, String direccion,
                            String fechaNac, String sexo, double altura,
                            boolean tieneDescuentoEspecial) {
        super(idPaciente, dni, nombre, apellidos, telefono, email, direccion, fechaNac, sexo, altura);
        this.tieneDescuentoEspecial = tieneDescuentoEspecial;
    }

    @Override
    public String tipoPaciente() {
        return "Jubilado";
    }

    public boolean isTieneDescuentoEspecial() { return tieneDescuentoEspecial; }
}
