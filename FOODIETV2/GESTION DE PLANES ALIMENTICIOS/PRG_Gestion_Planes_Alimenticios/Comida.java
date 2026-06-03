public class Comida {

    private String nombre;
    private int calorias;

    public Comida(String nombre, int calorias) {
        this.nombre = nombre;
        this.calorias = calorias;
    }

    public String getNombre() {
        return nombre;
    }

    public int getCalorias() {
        return calorias;
    }

    @Override
    public String toString() {
        return nombre + " - " + calorias + " kcal";
    }
}