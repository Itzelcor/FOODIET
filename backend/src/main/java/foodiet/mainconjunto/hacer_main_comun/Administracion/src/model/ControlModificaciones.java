package model;

// Control de modificaciones sobre datos de pacientes/planes nutricionales
public class ControlModificaciones extends ComponenteSeguridad {

    private boolean autorizada;
    private String tablaAfectada; // ej: PACIENTES, PLAN_NUTRICIONAL, HISTORIAL_MEDICO

    public ControlModificaciones(String descripcion, boolean autorizada, String tablaAfectada) {
        super(descripcion);
        this.autorizada = autorizada;
        this.tablaAfectada = tablaAfectada;
    }

    public String ejecutarControl() {
        System.out.println("Comprobando modificacion en " + tablaAfectada + " -> " + descripcion);
        if (!autorizada) {
            return "uso incorrecto";
        }
        return "uso correcto";
    }
}