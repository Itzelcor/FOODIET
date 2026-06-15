package foodiet.modelo;

/**
 * Representa a un profesional de la clínica FooDiet.
 * Roles posibles: DIRECTIVO, NUTRICIONISTA, ADMINISTRATIVO
 */
public class Profesional {

    private int id;
    private String nombre;
    private String apellidos;
    private String email;
    private String telefono;
    private String especialidad;
    private String rol; // DIRECTIVO | NUTRICIONISTA | ADMINISTRATIVO
    private String formacion;

    public Profesional() {}

    public Profesional(int id, String nombre, String apellidos,
                       String email, String telefono,
                       String especialidad, String rol, String formacion) {
        this.id          = id;
        this.nombre      = nombre;
        this.apellidos   = apellidos;
        this.email       = email;
        this.telefono    = telefono;
        this.especialidad = especialidad;
        this.rol         = rol;
        this.formacion   = formacion;
    }

    // ---------- Getters y Setters ----------

    public int getId()                     { return id; }
    public void setId(int id)              { this.id = id; }

    public String getNombre()              { return nombre; }
    public void setNombre(String nombre)   { this.nombre = nombre; }

    public String getApellidos()                   { return apellidos; }
    public void setApellidos(String apellidos)     { this.apellidos = apellidos; }

    public String getEmail()               { return email; }
    public void setEmail(String email)     { this.email = email; }

    public String getTelefono()                    { return telefono; }
    public void setTelefono(String telefono)       { this.telefono = telefono; }

    public String getEspecialidad()                        { return especialidad; }
    public void setEspecialidad(String especialidad)       { this.especialidad = especialidad; }

    public String getRol()                 { return rol; }
    public void setRol(String rol)         { this.rol = rol; }

    public String getFormacion()                   { return formacion; }
    public void setFormacion(String formacion)     { this.formacion = formacion; }

    @Override
    public String toString() {
        return "Profesional{id=" + id + ", nombre='" + nombre + "', rol='" + rol + "'}";
    }
}
