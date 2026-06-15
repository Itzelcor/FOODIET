package foodiet.modelo;

public class ServicioClinica {

    private String nombre;
    private String descripcion;
    private double precio;
    private String modalidad;

    public ServicioClinica(String nombre, String descripcion, double precio, String modalidad) {
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.precio = precio;
        this.modalidad = modalidad;
    }

    public String mostrarServicio() {
        return nombre + " - " + String.format("%.2f", precio) + "\u20ac (" + modalidad + ")";
    }

    public static ServicioClinica[] obtenerServicios() {
        int N = 0;
        ServicioClinica[] lista = new ServicioClinica[6];

        String[][] datos = {
                { "Consulta Presencial", "Evaluacion completa con mediciones antropometricas", "50.00", "presencial" },
                { "Consulta Online", "Sesion por videollamada con nutricionista", "40.00", "online" },
                { "Plan de Alimentacion", "Plan personalizado con seguimiento quincenal", "120.00", "presencial" },
                { "Nutricion Deportiva", "Planes para deportistas y mejora del rendimiento", "70.00", "presencial" },
                { "Control de Peso", "Programa supervisado de perdida o ganancia de peso", "90.00", "online" },
                { "Seguimiento Mensual", "Revision periodica con ajustes al plan nutricional", "35.00", "online" }
        };

        while (N < lista.length) {
            lista[N] = new ServicioClinica(datos[N][0], datos[N][1], Double.parseDouble(datos[N][2]), datos[N][3]);
            N++;
        }

        return lista;
    }

    public static void mostrarTodos() {
        ServicioClinica[] servicios = obtenerServicios();
        int total = servicios.length;
        System.out.println("\n--- SERVICIOS FOODIET ---");
        for (int i = 0; i < total; i++) {
            System.out.println((i + 1) + ". " + servicios[i].mostrarServicio());
        }
    }
}
