package foodiet.modelo;

public abstract class Paciente {

    protected int id;
    protected int idUsuario;
    protected String nombre;
    protected String apellido;
    protected int edad;
    protected double peso;
    protected double altura;
    protected String telefono;
    protected String email;
    protected String historialMedico;
    protected String objetivosNutricionales;

    public Paciente(String nombre, String apellido, int edad, double peso, double altura) {
        this.id = 0;
        this.idUsuario = 0;
        this.nombre = nombre;
        this.apellido = apellido;
        this.edad = edad;
        this.peso = peso;
        this.altura = altura;
        this.telefono = "";
        this.email = "";
        this.historialMedico = "";
        this.objetivosNutricionales = "";
    }

    public Paciente(int id, String nombre, String apellido, int edad, double peso, double altura,
                    String telefono, String email, String historial, String objetivos) {
        this.id = id;
        this.idUsuario = 1;
        this.nombre = nombre;
        this.apellido = apellido;
        this.edad = edad;
        this.peso = peso;
        this.altura = altura;
        this.telefono = telefono;
        this.email = email;
        this.historialMedico = historial;
        this.objetivosNutricionales = objetivos;
    }

    public double calcularIMC() {
        return peso / (altura * altura);
    }

    public abstract String tipoPaciente();

    public String nombreCompleto() {
        return nombre + " " + apellido;
    }

    public int getId() {
        return this.id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getIdUsuario() {
        return this.idUsuario;
    }

    public void setIdUsuario(int idUsuario) {
        this.idUsuario = idUsuario;
    }

    public String getNombre() {
        return this.nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellido() {
        return this.apellido;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    public int getEdad() {
        return this.edad;
    }

    public void setEdad(int edad) {
        this.edad = edad;
    }

    public double getPeso() {
        return this.peso;
    }

    public void setPeso(double peso) {
        this.peso = peso;
    }

    public double getAltura() {
        return this.altura;
    }

    public void setAltura(double altura) {
        this.altura = altura;
    }

    public String getTelefono() {
        return this.telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getEmail() {
        return this.email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getHistorialMedico() {
        return this.historialMedico;
    }

    public void setHistorialMedico(String historial) {
        this.historialMedico = historial;
    }

    public String getObjetivosNutricionales() {
        return this.objetivosNutricionales;
    }

    public void setObjetivosNutricionales(String objetivos) {
        this.objetivosNutricionales = objetivos;
    }

    @Override
    public String toString() {
        return nombre + " " + apellido + " (" + tipoPaciente() + ")";
    }
}
