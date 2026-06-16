package stats.Profesionales;

public class Nutricionista extends ProfesionalClinica {
    public Nutricionista(int id, String nombre, String apellidos,
                         String email, String telefono, int anosExp, boolean activo) {
        super(id, nombre, apellidos, email, telefono, anosExp, activo);
    }

    @Override
    public String getRol() { return "Nutricionista"; }
}
