package model;

// Contrato comun para los distintos chequeos de seguridad que puede lanzar el admin
public abstract class ComponenteSeguridad {

    protected String descripcion;

    public ComponenteSeguridad(String descripcion) {
        this.descripcion = descripcion;
    }

    public abstract String ejecutarControl();

    public String getDescripcion() {
        return descripcion;
    }
}