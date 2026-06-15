package foodiet.modelo;

public class Usuario {

    private int idUsuario;
    private String nombreUsuario;
    private String email;
    private String contraseñaHash;
    private String rol;
    private boolean activo;

    public Usuario(int idUsuario, String nombreUsuario, String email, String contraseñaHash, String rol) {
        this.idUsuario = idUsuario;
        this.nombreUsuario = nombreUsuario;
        this.email = email;
        this.contraseñaHash = contraseñaHash;
        this.rol = rol;
        this.activo = true;
    }

    public int getIdUsuario() {
        return this.idUsuario;
    }

    public String getNombreUsuario() {
        return this.nombreUsuario;
    }

    public String getEmail() {
        return this.email;
    }

    public String getContraseñaHash() {
        return this.contraseñaHash;
    }

    public String getRol() {
        return this.rol;
    }

    public boolean isActivo() {
        return this.activo;
    }

    @Override
    public String toString() {
        return nombreUsuario + " (" + rol + ")";
    }
}
