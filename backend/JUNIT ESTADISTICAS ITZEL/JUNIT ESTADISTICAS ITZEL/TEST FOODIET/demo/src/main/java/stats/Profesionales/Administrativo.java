package stats.Profesionales;

public class Administrativo extends ProfesionalClinica {
    public Administrativo(int id, String nombre, String apellidos,
                          String email, String telefono, int anosExp, boolean activo) {
        super(id, nombre, apellidos, email, telefono, anosExp, activo);
    }

    @Override
    public String getRol() { return "Administrativo"; }
}
