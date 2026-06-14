import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

public class Main {
    public static void main(String[] args) {
        Plan plan = new Plan("Ana Garcia", "Perdida de peso", 1800);

        try {
            DataOutputStream salida = new DataOutputStream(new FileOutputStream("planes.dat"));
            salida.writeUTF(plan.paciente);
            salida.writeUTF(plan.objetivo);
            salida.writeInt(plan.calorias);
            salida.close();

            DataInputStream entrada = new DataInputStream(new FileInputStream("planes.dat"));
            String paciente = entrada.readUTF();
            String objetivo = entrada.readUTF();
            int calorias = entrada.readInt();
            entrada.close();

            Plan planLeido = new Plan(paciente, objetivo, calorias);
            planLeido.mostrar();
        } catch (IOException e) {
            System.out.println("Error con el fichero binario");
        }
    }
}
