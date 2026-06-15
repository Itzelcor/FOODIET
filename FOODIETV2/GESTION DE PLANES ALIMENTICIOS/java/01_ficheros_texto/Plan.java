public class Plan {
    String paciente;
    String objetivo;
    int calorias;

    public Plan(String paciente, String objetivo, int calorias) {
        this.paciente = paciente;
        this.objetivo = objetivo;
        this.calorias = calorias;
    }

    public String aLinea() {
        return paciente + ";" + objetivo + ";" + calorias;
    }

    public static Plan desdeLinea(String linea) {
        String[] partes = linea.split(";");
        return new Plan(partes[0], partes[1], Integer.parseInt(partes[2]));
    }

    public void mostrar() {
        System.out.println(paciente + " - " + objetivo + " - " + calorias + " kcal");
    }
}
