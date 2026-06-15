package main;

import model.*;
import dao.*;

public class PrincipalAdmin {
        public static void main(String[] args) {

                System.out.println("=== Subsistema de Administrador - FooDiet ===");

                Administrador admin = new Administrador(1, "Marina Soler", "marina.soler@foodiet.com", "ALTO");

                GestorAuditoria gestor = new GestorAuditoria();

                // Cargamos un par de eventos iniciales como si vinieran de otros modulos
                gestor.registrarAccion("Login correcto del usuario nutricionista_jgomez");
                gestor.registrarAccion("Acceso al modulo de pacientes fuera del horario habitual (23:47h)");

                admin.revisarRegistros(gestor);

                SubsistemaAdministrador subsistema = new SubsistemaAdministrador(gestor);

                // Control de accesos: este usuario ha fallado el login 5 veces, umbral son 3
                ComponenteSeguridad cAcceso = new ControlAccesos("Accesos del usuario nutricionista_jgomez", 5, 3);

                // Modificacion no autorizada del historial medico del paciente 204
                ComponenteSeguridad cModSinAutorizar = new ControlModificaciones(
                                "Cambio en alergias del paciente PAC-204", false, "HISTORIAL_MEDICO");

                // Modificacion normal y autorizada de un plan nutricional
                ComponenteSeguridad cModOk = new ControlModificaciones(
                                "Actualizacion de calorias del plan PAC-310", true, "PLAN_NUTRICIONAL");

                System.out.println("\n-- Lanzando controles de seguridad --");
                subsistema.procesarControlYPersistir(cAcceso);
                subsistema.procesarControlYPersistir(cModSinAutorizar);
                subsistema.procesarControlYPersistir(cModOk);

                admin.revisarRegistros(gestor);

                admin.notificarIncidencia(
                                "Hay accesos y modificaciones marcadas como uso incorrecto, revisar antes de cerrar el turno.");

                System.out.println("\n-- Exportando informe de alertas --");
                subsistema.exportarAlertasUsoIncorrecto();

                System.out.println("\n=== Fin de la ejecucion ===");
        }
}