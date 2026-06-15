package model;

// Almacen en memoria de las acciones que va registrando el sistema
public class GestorAuditoria {

    private ArrayList<String> logs;

    public GestorAuditoria() {
        logs = new ArrayList<String>();
    }

    public void registrarAccion(String accion) {
        logs.add(accion);
        System.out.println("[LOG] " + accion);
    }

    public void mostrarLogsActivos() {
        System.out.println("---- Registro de actividad (" + logs.size() + " entradas) ----");
        if (logs.size() == 0) {
            System.out.println("No hay nada registrado todavia.");
        } else {
            int i = 1;
            for (String l : logs) {
                System.out.println(i + ") " + l);
                i++;
            }
        }
        System.out.println("------------------------------------------------");
    }

    public ArrayList<String> getLogs() {
        return logs;
    }
}