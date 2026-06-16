package stats.Profesionales;

/**
 * Clase abstracta que representa un profesional de la clínica FooDiet.
 * Subsistema: Gestión del Equipo Profesional — Andrei Veres
 */
public abstract class ProfesionalClinica {

    private int    idProfesional;
    private String nombre;
    private String apellidos;
    private String email;
    private String telefono;
    private int    anosExp;
    private boolean activo;

    public ProfesionalClinica(int idProfesional, String nombre, String apellidos,
                              String email, String telefono, int anosExp, boolean activo) {
        this.idProfesional = idProfesional;
        this.nombre        = nombre;
        this.apellidos     = apellidos;
        this.email         = email;
        this.telefono      = telefono;
        this.anosExp       = anosExp;
        this.activo        = activo;
    }

    // Método abstracto — cada subtipo define su rol
    public abstract String getRol();

    public String nombreCompleto() {
        return nombre + " " + apellidos;
    }

    public void atenderCita(int idCita) {
        System.out.println(getRol() + " " + nombreCompleto()
                         + " atiende la cita ID: " + idCita);
    }

    // Getters
    public int     getIdProfesional() { return idProfesional; }
    public String  getNombre()        { return nombre; }
    public String  getApellidos()     { return apellidos; }
    public String  getEmail()         { return email; }
    public String  getTelefono()      { return telefono; }
    public int     getAnosExp()       { return anosExp; }
    public boolean isActivo()         { return activo; }

    // Setters
    public void setIdProfesional(int id) { this.idProfesional = id; }
    public void setActivo(boolean activo) { this.activo = activo; }
    public void setEmail(String email)    { this.email = email; }

    @Override
    public String toString() {
        return "ID: " + idProfesional
             + " | " + nombreCompleto()
             + " | Rol: " + getRol()
             + " | Exp: " + anosExp + " años"
             + " | Activo: " + activo;
    }
}
