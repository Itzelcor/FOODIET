package hacer_main_comun.Gestion_del_Equipo_Profesional.src.model;
/**
 * Representa a un profesional de la clínica FooDiet.
 *
 * Campos principales:
 * - id : clave primaria (generada por la BD)
 * - nombre : nombre de pila del profesional
 * - apellidos : apellidos del profesional
 * - especialidad: área de trabajo (ej. "Nutrición clínica", "Dietética
 * deportiva")
 * - formacion : titulación o estudios relevantes
 * - email : correo de contacto (usado también para identificarse en el sistema)
 * - telefono : teléfono de contacto
 * - rol : nivel de acceso al sistema (DIRECTIVO, NUTRICIONISTA, ADMINISTRATIVO)
 */
public class Profesional {

    private int id;
    private String nombre;
    private String apellidos;
    private String especialidad;
    private String formacion;
    private String email;
    private String telefono;
    private Rol rol;

    // -------------------------------------------------------------------------
    // Constructor vacío (necesario para instanciar desde el DAO)
    // -------------------------------------------------------------------------
    public Profesional() {
    }

    // -------------------------------------------------------------------------
    // Constructor completo (sin id, porque la BD lo genera automáticamente)
    // -------------------------------------------------------------------------
    public Profesional(String nombre, String apellidos, String especialidad,
            String formacion, String email, String telefono, Rol rol) {
        this.nombre = nombre;
        this.apellidos = apellidos;
        this.especialidad = especialidad;
        this.formacion = formacion;
        this.email = email;
        this.telefono = telefono;
        this.rol = rol;
    }

    // -------------------------------------------------------------------------
    // Getters y Setters
    // -------------------------------------------------------------------------
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellidos() {
        return apellidos;
    }

    public void setApellidos(String apellidos) {
        this.apellidos = apellidos;
    }

    public String getEspecialidad() {
        return especialidad;
    }

    public void setEspecialidad(String especialidad) {
        this.especialidad = especialidad;
    }

    public String getFormacion() {
        return formacion;
    }

    public void setFormacion(String formacion) {
        this.formacion = formacion;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public Rol getRol() {
        return rol;
    }

    public void setRol(Rol rol) {
        this.rol = rol;
    }

    // -------------------------------------------------------------------------
    // toString — útil para mostrar datos por consola
    // -------------------------------------------------------------------------
    @Override
    public String toString() {
        return "Profesional {" +
                "\n  ID          : " + id +
                "\n  Nombre      : " + nombre + " " + apellidos +
                "\n  Especialidad: " + especialidad +
                "\n  Formación   : " + formacion +
                "\n  Email       : " + email +
                "\n  Teléfono    : " + telefono +
                "\n  Rol         : " + rol +
                "\n}";
    }
}
