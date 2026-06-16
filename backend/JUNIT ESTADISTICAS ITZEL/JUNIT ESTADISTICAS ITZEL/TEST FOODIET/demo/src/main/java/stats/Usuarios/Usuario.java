package stats.Usuarios;


/**
 * Clase que representa un usuario del sistema FooDiet.
 * Gestiona la autenticación y el rol de acceso.
 */
public class Usuario {

    private int     idUsuario;
    private String  email;
    private String  passwordHash;
    private String  rol;
    private boolean activo;
    private String  fechaAlta;

    public Usuario(int idUsuario, String email, String passwordHash,
                   String rol, boolean activo, String fechaAlta) {
        this.idUsuario    = idUsuario;
        this.email        = email;
        this.passwordHash = passwordHash;
        this.rol          = rol;
        this.activo       = activo;
        this.fechaAlta    = fechaAlta;
    }

    // Getter for password hash (needed by DAO)
    public String getPasswordHash() { return passwordHash; }

    public boolean login(String emailIntroducido, String passIntroducida) {
        boolean acceso = false;
        if (this.email.equals(emailIntroducido)
                && this.passwordHash.equals(passIntroducida)
                && this.activo) {
            System.out.println("Sesión iniciada como: " + rol);
            acceso = true;
        } else {
            System.out.println("Credenciales incorrectas o usuario inactivo.");
        }
        return acceso;
    }

    public void logout() {
        System.out.println("Sesión cerrada para: " + email);
    }

    // Getters
    public int     getIdUsuario()    { return idUsuario; }
    public String  getEmail()        { return email; }
    public String  getRol()          { return rol; }
    public boolean isActivo()        { return activo; }
    public String  getFechaAlta()    { return fechaAlta; }

    // Setters
    public void setIdUsuario(int id)       { this.idUsuario = id; }
    public void setActivo(boolean activo)  { this.activo = activo; }
    public void setRol(String rol)         { this.rol = rol; }

    @Override
    public String toString() {
        return "ID: " + idUsuario
             + " | Email: " + email
             + " | Rol: " + rol
             + " | Activo: " + activo;
    }
}