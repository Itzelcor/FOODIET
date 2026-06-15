import java.io.Serializable;

public class Plan implements Serializable {

    int id;
    String paciente;
    String objetivo;
    int calorias;

    public Plan(String paciente, String objetivo, int calorias) {
        this.paciente = paciente;
        this.objetivo = objetivo;
        this.calorias = calorias;
    }

    public Plan(int id, String paciente, String objetivo, int calorias) {
        this.id = id;
        this.paciente = paciente;
        this.objetivo = objetivo;
        this.calorias = calorias;
    }

    public void mostrar() {
        System.out.println(
                paciente + " - " +
                        objetivo + " - " +
                        calorias + " kcal");
    }
}