package stats.Administracion;

/**
 * Clase que representa una factura de la clínica FooDiet.
 * Subsistema: Administración
 */
public class Factura {

    private int    idFactura;
    private int    idCita;
    private int    idPaciente;
    private double importe;
    private String estadoPago;
    private String metodoPago;
    private String fechaEmision;
    private String fechaPago;

    public Factura(int idFactura, int idCita, int idPaciente,
                   double importe, String estadoPago, String metodoPago,
                   String fechaEmision, String fechaPago) {
        this.idFactura    = idFactura;
        this.idCita       = idCita;
        this.idPaciente   = idPaciente;
        this.importe      = importe;
        this.estadoPago   = estadoPago;
        this.metodoPago   = metodoPago;
        this.fechaEmision = fechaEmision;
        this.fechaPago    = fechaPago;
    }

    // Getters
    public int    getIdFactura()    { return idFactura; }
    public int    getIdCita()       { return idCita; }
    public int    getIdPaciente()   { return idPaciente; }
    public double getImporte()      { return importe; }
    public String getEstadoPago()   { return estadoPago; }
    public String getMetodoPago()   { return metodoPago; }
    public String getFechaEmision() { return fechaEmision; }
    public String getFechaPago()    { return fechaPago; }

    // Setters
    public void setIdFactura(int id)          { this.idFactura = id; }
    public void setEstadoPago(String estado)  { this.estadoPago = estado; }
    public void setFechaPago(String fecha)    { this.fechaPago = fecha; }

    public String toCSV() {
        return idFactura + "," + idCita + "," + idPaciente + ","
             + importe + "," + estadoPago + "," + metodoPago + ","
             + fechaEmision + "," + fechaPago;
    }

    @Override
    public String toString() {
        return "Factura ID: " + idFactura
             + " | Cita: " + idCita
             + " | Paciente: " + idPaciente
             + " | Importe: " + importe + " €"
             + " | Estado: " + estadoPago
             + " | Método: " + metodoPago
             + " | Emitida: " + fechaEmision;
    }
}