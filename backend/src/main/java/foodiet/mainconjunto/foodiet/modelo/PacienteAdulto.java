package foodiet.modelo;

public class PacienteAdulto extends Paciente {

    public PacienteAdulto(String nombre, String apellido, int edad, double peso, double altura) {
        super(nombre, apellido, edad, peso, altura);
    }

    public PacienteAdulto(String nombre, String apellido, int edad, double peso, double altura,
                          String telefono, String email, String historial, String objetivos) {
        super(0, nombre, apellido, edad, peso, altura, telefono, email, historial, objetivos);
    }

    public PacienteAdulto(int id, String nombre, String apellido, int edad, double peso, double altura,
                          String telefono, String email, String historial, String objetivos) {
        super(id, nombre, apellido, edad, peso, altura, telefono, email, historial, objetivos);
    }

    @Override
    public String tipoPaciente() {
        return "Adulto";
    }
}
