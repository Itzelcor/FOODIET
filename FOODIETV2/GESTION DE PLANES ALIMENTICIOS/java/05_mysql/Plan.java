public class Plan {
    int id;
    String paciente;
    String objetivo;
    int calorias;

    public Plan(int id, String paciente, String objetivo, int calorias) {
        this.id = id;
        this.paciente = paciente;
        this.objetivo = objetivo;
        this.calorias = calorias;
    }

    public void mostrar() {
        System.out.println(id + " - " + paciente + " - " + objetivo + " - " + calorias + " kcal");
    }
}
