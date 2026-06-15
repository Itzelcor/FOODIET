import java.io.Serializable;

public class Plan implements Serializable {
    private static final long serialVersionUID = 1L;

    String paciente;
    String objetivo;
    int calorias;

    public Plan(String paciente, String objetivo, int calorias) {
        this.paciente = paciente;
        this.objetivo = objetivo;
        this.calorias = calorias;
    }

    public void mostrar() {
        System.out.println(paciente + " - " + objetivo + " - " + calorias + " kcal");
    }
}
