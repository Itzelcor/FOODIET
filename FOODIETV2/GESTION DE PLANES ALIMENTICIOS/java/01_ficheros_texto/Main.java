import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class Main {
    public static void main(String[] args) {
        Plan plan1 = new Plan("Ana Garcia", "Perdida de peso", 1800);
        Plan plan2 = new Plan("Carlos Ruiz", "Ganancia muscular", 2400);

        try {
            FileWriter fw = new FileWriter("planes.txt");
            fw.write(plan1.aLinea() + "\n");
            fw.write(plan2.aLinea() + "\n");
            fw.close();

            BufferedReader br = new BufferedReader(new FileReader("planes.txt"));
            String linea;
            while ((linea = br.readLine()) != null) {
                Plan plan = Plan.desdeLinea(linea);
                plan.mostrar();
            }
            br.close();
        } catch (IOException e) {
            System.out.println("Error con el fichero");
        }
    }
}
