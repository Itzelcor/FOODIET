package stats.Planes;

public class Comida {

    private int idComida;
    private int idMenu;
    private String nombre;
    private String tipo;
    private int calorias;

    public Comida(int idComida, int idMenu, String nombre, String tipo, int calorias) {
        this.idComida = idComida;
        this.idMenu = idMenu;
        this.nombre = nombre;
        this.tipo = tipo;
        this.calorias = calorias;
    }

    public double calcularCalorias() {
        return calorias;
    }

    public int getIdComida() {
        return idComida;
    }

    public int getIdMenu() {
        return idMenu;
    }

    public String getNombre() {
        return nombre;
    }

    public String getTipo() {
        return tipo;
    }

    public int getCalorias() {
        return calorias;
    }

    @Override
    public String toString() {
        return tipo.toUpperCase() + ": " + nombre + " (" + calorias + " kcal)";
    }
}
