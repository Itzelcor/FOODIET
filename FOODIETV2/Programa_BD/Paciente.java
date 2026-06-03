public class Paciente extends Persona {

    public Paciente(int id, String nombre) {
        super(id, nombre);
    }

    @Override
    public void mostrarInformacion() {
        System.out.println("Paciente: " + nombre);
    }

    @Override
    public String toString() {
        return "Paciente [id=" + id +
                ", nombre=" + nombre + "]";
    }
}