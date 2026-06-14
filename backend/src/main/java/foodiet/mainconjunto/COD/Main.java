import java.io.*;
import java.sql.*;

public class Main {

    public static void main(String[] args) {

        Plan p1 = new Plan("Ana", "Dieta", 1800);
        Plan p2 = new Plan("Carlos", "Volumen", 2400);

        // TEXTO

        try {

            FileWriter fw = new FileWriter("planes.txt");

            fw.write(p1.paciente + ";" + p1.objetivo + ";" + p1.calorias + "\n");
            fw.write(p2.paciente + ";" + p2.objetivo + ";" + p2.calorias + "\n");

            fw.close();

            BufferedReader br =
                    new BufferedReader(
                            new FileReader("planes.txt"));

            String linea;

            System.out.println("LECTURA TEXTO");

            while ((linea = br.readLine()) != null) {

                String v[] = linea.split(";");

                Plan p =
                        new Plan(
                                v[0],
                                v[1],
                                Integer.parseInt(v[2]));

                p.mostrar();
            }

            br.close();

        } catch (Exception e) {
            System.out.println("Error texto");
        }

        // BINARIO

        try {

            DataOutputStream salida =
                    new DataOutputStream(
                            new FileOutputStream("planes.dat"));

            salida.writeUTF(p1.paciente);
            salida.writeUTF(p1.objetivo);
            salida.writeInt(p1.calorias);

            salida.close();

            DataInputStream entrada =
                    new DataInputStream(
                            new FileInputStream("planes.dat"));

            Plan p =
                    new Plan(
                            entrada.readUTF(),
                            entrada.readUTF(),
                            entrada.readInt());

            entrada.close();

            System.out.println("\nLECTURA BINARIA");

            p.mostrar();

        } catch (Exception e) {
            System.out.println("Error binario");
        }

        // OBJETOS

        try {

            ObjectOutputStream oos =
                    new ObjectOutputStream(
                            new FileOutputStream("plan.obj"));

            oos.writeObject(p1);

            oos.close();

            ObjectInputStream ois =
                    new ObjectInputStream(
                            new FileInputStream("plan.obj"));

            Plan p = (Plan) ois.readObject();

            ois.close();

            System.out.println("\nLECTURA OBJETO");

            p.mostrar();

        } catch (Exception e) {
            System.out.println("Error objetos");
        }

        // POLIMORFISMO

        System.out.println("\nPOLIMORFISMO");

        PlanAlimentacion planes[] =
                new PlanAlimentacion[2];

        planes[0] =
                new PlanPerdidaPeso(
                        "Ana",
                        1800);

        planes[1] =
                new PlanMantenimiento(
                        "Carlos",
                        2200);

        for (int i = 0; i < planes.length; i++) {
            planes[i].mostrarPlan();
        }

        // MYSQL

        try {

            String url =
                    "jdbc:mysql://localhost:3306/foodiet_planes";

            String usuario = "root";
            String clave = "";

            Connection con =
                    DriverManager.getConnection(
                            url,
                            usuario,
                            clave);

            Statement s =
                    con.createStatement();

            ResultSet r =
                    s.executeQuery(
                            "SELECT pl.id_plan,p.nombre,pl.objetivo,pl.calorias_dia "
                                    +
                                    "FROM plan_alimentacion pl "
                                    +
                                    "JOIN paciente p "
                                    +
                                    "ON pl.id_paciente=p.id_paciente");

            System.out.println("\nMYSQL");

            while (r.next()) {

                Plan p =
                        new Plan(
                                r.getInt("id_plan"),
                                r.getString("nombre"),
                                r.getString("objetivo"),
                                r.getInt("calorias_dia"));

                p.mostrar();
            }

            r.close();
            s.close();
            con.close();

        } catch (Exception e) {

            System.out.println("Error MySQL");
        }
    }
}