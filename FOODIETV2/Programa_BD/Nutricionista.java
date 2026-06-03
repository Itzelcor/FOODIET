public class Nutricionista extends Persona {

    public Nutricionista(int id, String nombre) {
        super(id, nombre);
    }

    @Override
    public void mostrarInformacion() {
        System.out.println("Nutricionista: " + nombre);
    }
}