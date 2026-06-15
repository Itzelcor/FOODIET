package model;

public class Usuario {
    protected int idUsuario;
    protected String nombre;
    protected String email;

    public Usuario(int idUsuario, String nombre, String email) {
        this.idUsuario = idUsuario;
        this.nombre = nombre;
        this.email = email;
    }

    public int getIdUsuario() {
        return idUsuario;
    }

    public String getNombre() {
        return nombre;
    }

    public String getEmail() {
        return email;
    }

    public String toString() {
        return "Usuario [" + idUsuario + "] " + nombre + " - " + email;
    }
}
