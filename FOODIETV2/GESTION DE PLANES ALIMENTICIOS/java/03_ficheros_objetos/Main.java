import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class Main {
    public static void main(String[] args) {
        Plan plan = new Plan("Ana Garcia", "Perdida de peso", 1800);

        try {
            ObjectOutputStream salida = new ObjectOutputStream(new FileOutputStream("plan.obj"));
            salida.writeObject(plan);
            salida.close();

            ObjectInputStream entrada = new ObjectInputStream(new FileInputStream("plan.obj"));
            Plan planLeido = (Plan) entrada.readObject();
            entrada.close();

            planLeido.mostrar();
        } catch (IOException e) {
            System.out.println("Error guardando el objeto");
        } catch (ClassNotFoundException e) {
            System.out.println("No se encontro la clase");
        }
    }
}
