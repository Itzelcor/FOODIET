package model;

// Control de accesos: mira si el numero de intentos fallidos es sospechoso
public class ControlAccesos extends ComponenteSeguridad {

    private int intentosFallidos;
    private int umbralMaximo; // a partir de aqui se considera sospechoso

    public ControlAccesos(String descripcion, int intentosFallidos, int umbralMaximo) {
        super(descripcion);
        this.intentosFallidos = intentosFallidos;
        this.umbralMaximo = umbralMaximo;
    }

    public String ejecutarControl() {
        System.out.println("Comprobando accesos -> " + descripcion + " (intentos: " + intentosFallidos + ")");
        if (intentosFallidos > umbralMaximo) {
            return "uso incorrecto";
        }
        return "uso correcto";
    }
}