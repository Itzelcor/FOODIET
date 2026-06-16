package stats.Pacientes;

/**
 * Clase abstracta que representa un paciente de la clínica FooDiet.
 * Subsistema: Gestión de Pacientes y Citas — Dani Dimitrov
 */
public abstract class Paciente {

    private int    idPaciente;
    private String dni;
    private String nombre;
    private String apellidos;
    private String telefono;
    private String email;
    private String direccion;
    private String fechaNac;
    private String sexo;
    private double altura;

    public Paciente(int idPaciente, String dni, String nombre, String apellidos,
                    String telefono, String email, String direccion,
                    String fechaNac, String sexo, double altura) {
        this.idPaciente = idPaciente;
        this.dni        = dni;
        this.nombre     = nombre;
        this.apellidos  = apellidos;
        this.telefono   = telefono;
        this.email      = email;
        this.direccion  = direccion;
        this.fechaNac   = fechaNac;
        this.sexo       = sexo;
        this.altura     = altura;
    }

    // Método abstracto — cada subtipo lo implementa
    public abstract String tipoPaciente();

    // Método concreto compartido por todos los subtipos
    public double calcularIMC(double peso) {
        return peso / (altura * altura);
    }

    public String nombreCompleto() {
        return nombre + " " + apellidos;
    }

    // Getters
    public int    getIdPaciente() { return idPaciente; }
    public String getDni()        { return dni; }
    public String getNombre()     { return nombre; }
    public String getApellidos()  { return apellidos; }
    public String getTelefono()   { return telefono; }
    public String getEmail()      { return email; }
    public String getDireccion()  { return direccion; }
    public String getFechaNac()   { return fechaNac; }
    public String getSexo()       { return sexo; }
    public double getAltura()     { return altura; }

    // Setters
    public void setIdPaciente(int id)       { this.idPaciente = id; }
    public void setTelefono(String t)       { this.telefono = t; }
    public void setEmail(String e)          { this.email = e; }
    public void setDireccion(String d)      { this.direccion = d; }

    @Override
    public String toString() {
        return "ID: " + idPaciente
             + " | " + nombreCompleto()
             + " | DNI: " + dni
             + " | Tipo: " + tipoPaciente()
             + " | Tel: " + telefono;
    }
}
