package model;

// El administrador hereda de Usuario y añade el nivel de acceso al panel de seguridad
public class Administrador extends Usuario {

    private String nivelSeguridad; // ALTO / MEDIO / BAJO segun permisos del panel

    public Administrador(int idUsuario, String nombre, String email, String nivelSeguridad) {
        super(idUsuario, nombre, email);
        this.nivelSeguridad = nivelSeguridad;
    }

    public String getNivelSeguridad() {
        return nivelSeguridad;
    }

    // Revisa los logs que hay cargados en el gestor (paso del diagrama de
    // actividades)
    public void revisarRegistros(GestorAuditoria gestor) {
        System.out.println(
                "\nEl admin " + nombre + " (" + nivelSeguridad + ") entra a revisar el registro de actividad...");
        gestor.mostrarLogsActivos();
    }

    // Aviso rapido cuando el admin detecta algo raro durante la revision
    public void notificarIncidencia(String mensaje) {
        System.out.println(">> Aviso de " + nombre + ": " + mensaje);
    }
}