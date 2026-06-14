package foodiet.modelo;

public class PacienteJubilado extends Paciente {

    public PacienteJubilado(String nombre, String apellido, int edad, double peso, double altura) {
        super(nombre, apellido, edad, peso, altura);
    }

    public PacienteJubilado(String nombre, String apellido, int edad, double peso, double altura,
                            String telefono, String email, String historial, String objetivos) {
        super(0, nombre, apellido, edad, peso, altura, telefono, email, historial, objetivos);
    }

    public PacienteJubilado(int id, String nombre, String apellido, int edad, double peso, double altura,
                            String telefono, String email, String historial, String objetivos) {
        super(id, nombre, apellido, edad, peso, altura, telefono, email, historial, objetivos);
    }

    @Override
    public String tipoPaciente() {
        return "Jubilado";
    }
}
