import foodiet.FoodietApp;
import foodiet.modelo.Paciente;
import foodiet.modelo.PacienteJoven;
import foodiet.modelo.PacienteAdulto;
import foodiet.modelo.PacienteJubilado;
import foodiet.modelo.Cita;
import foodiet.modelo.CitaOnline;
import foodiet.modelo.CitaPresencial;
import foodiet.modelo.ProfesionalClinica;
import foodiet.modelo.ServicioClinica;
import foodiet.modelo.PlanAlimenticio;
import foodiet.gestion.GestionClinica;
import foodiet.gestion.GestorArchivos;
import foodiet.gestion.Estadisticas;
import foodiet.datos.ConexionBD;
import foodiet.datos.GestorBD;
import foodiet.presentacion.MenuPrincipal;
import hacer_main_comun.Estadisticas.src.modelo.*;
import hacer_main_comun.Gestion_del_Equipo_Profesional.src.model.*;

import java.util.ArrayList;

public class Main {

        public static void main(String[] args) {

                // ====================================================================
                // BLOQUE 1 - SUBSISTEMA DE SEGURIDAD Y AUDITORIA
                // (default package - PrincipalAdmin: GestorAuditoria, ComponenteSeguridad,
                // ControlAccesos, ControlModificaciones, SubsistemaAdministrador)
                // GestorAuditoria registra eventos en un ArrayList<String>.
                // ComponenteSeguridad es abstracta; ControlAccesos y ControlModificaciones
                // implementan ejecutarControl() con polimorfismo.
                // SubsistemaAdministrador persiste controles en BD y exporta alertas.
                // ====================================================================
                GestorAuditoria gestorAuditoria = new GestorAuditoria();
                gestorAuditoria.registrarAccion("Inicio del sistema FooDiet");

                Administrador admin = new Administrador(1, "Marina Soler",
                                "marina@foodiet.com", "ALTO");
                admin.revisarRegistros(gestorAuditoria);

                ControlAccesos ctrlAcceso = new ControlAccesos(
                                "Accesos de usuarios", 2, 3);
                ControlModificaciones ctrlMod = new ControlModificaciones(
                                "Modificacion en pacientes", true, "PACIENTES");

                SubsistemaAdministrador subsistemaAdmin = new SubsistemaAdministrador(
                                gestorAuditoria);
                subsistemaAdmin.procesarControlYPersistir(ctrlAcceso);
                subsistemaAdmin.procesarControlYPersistir(ctrlMod);

                // ====================================================================
                // BLOQUE 2 - GESTION CLINICA (foodiet.gestion)
                // GestionClinica maneja arrays estaticos con capacidad maxima fija
                // y variables N (pacientesN, profesionalesN, etc.) que indican
                // cuantas posiciones estan ocupadas en cada momento.
                // ====================================================================
                GestionClinica gestion = new GestionClinica();

                // ====================================================================
                // BLOQUE 3 - PACIENTES CON POLIMORFISMO (foodiet.modelo)
                // Paciente es abstracta con metodo tipoPaciente().
                // PacienteJoven, PacienteAdulto y PacienteJubilado heredan
                // y sobrescriben tipoPaciente() cada uno con su clasificacion.
                // ====================================================================
                Paciente pJoven = new PacienteJoven("Ana", "Lopez", 25, 68.5, 1.65);
                Paciente pAdulto = new PacienteAdulto("Carlos", "Mendez",
                                45, 85.0, 1.78);
                Paciente pJubilado = new PacienteJubilado("Rosa", "Garcia",
                                68, 72.3, 1.60);

                gestion.añadirPaciente(pJoven);
                gestion.añadirPaciente(pAdulto);
                gestion.añadirPaciente(pJubilado);

                // ====================================================================
                // BLOQUE 4 - PROFESIONALES DE LA CLINICA (foodiet.modelo)
                // ProfesionalClinica almacena datos del nutricionista y su especialidad.
                // ====================================================================
                ProfesionalClinica prof1 = new ProfesionalClinica(
                                "Laura", "Ruiz", "Nutricion deportiva",
                                8, "L-M-V 9-14", "600111222");
                ProfesionalClinica prof2 = new ProfesionalClinica(
                                "Pedro", "Diaz", "Nutricion clinica",
                                12, "L-X-V 15-20", "600333444");

                gestion.añadirProfesional(prof1);
                gestion.añadirProfesional(prof2);

                // ====================================================================
                // BLOQUE 5 - CITAS CON POLIMORFISMO (foodiet.modelo)
                // CitaOnline y CitaPresencial heredan de Cita y sobrescriben
                // mostrarCita() anadiendo datos especificos de cada modalidad.
                // ====================================================================
                Cita citaOnline = new CitaOnline(
                                pJoven, prof1, 15, 6, 2026,
                                "Revision trimestral", "Zoom");
                Cita citaPresencial = new CitaPresencial(
                                pAdulto, prof2, 16, 6, 2026,
                                "Primera consulta", "Sala 3");

                gestion.añadirCita(citaOnline);
                gestion.añadirCita(citaPresencial);

                // ====================================================================
                // BLOQUE 6 - PLANES ALIMENTICIOS (foodiet.modelo)
                // PlanAlimenticio vincula un paciente y un profesional
                // con objetivos, descripcion y calorias diarias.
                // ====================================================================
                PlanAlimenticio plan1 = new PlanAlimenticio(
                                pJoven.getId(), prof1.getId(),
                                "01/06/2026", "01/09/2026",
                                "Perdida de peso", "Dieta hipocalorica equilibrada", 1800);
                PlanAlimenticio plan2 = new PlanAlimenticio(
                                pAdulto.getId(), prof2.getId(),
                                "01/06/2026", "01/12/2026",
                                "Control glucemico", "Dieta para diabeticos tipo 2", 2000);

                gestion.añadirPlan(plan1);
                gestion.añadirPlan(plan2);

                // ====================================================================
                // BLOQUE 7 - SERVICIOS DE LA CLINICA (foodiet.modelo)
                // ServicioClinica.obtenerServicios() construye un array estatico
                // usando una variable auxiliar N que controla las posiciones ocupadas
                // dentro del bucle while.
                // ====================================================================
                ServicioClinica[] servicios = ServicioClinica.obtenerServicios();
                int nServicios = servicios.length;
                for (int i = 0; i < nServicios; i++) {
                        System.out.println(servicios[i].mostrarServicio());
                }

                // ====================================================================
                // BLOQUE 8 - METRICAS CON POLIMORFISMO (estadisticas)
                // Metrica es abstracta con metodos calcular() y getTipo().
                // MetricaPeso, MetricaIMC y MetricaGrasa implementan cada una
                // su propia logica de calculo y clasificacion.
                // ====================================================================
                Metrica metricaPeso = new MetricaPeso(
                                "Ana Lopez", 68.5, 65.2, "14/06/2026");
                Metrica metricaIMC = new MetricaIMC(
                                "Ana Lopez", 65.2, 1.65, "14/06/2026");
                Metrica metricaGrasa = new MetricaGrasa(
                                "Ana Lopez", 28.5, 'F', "14/06/2026");

                metricaPeso.mostrar();
                metricaIMC.mostrar();
                metricaGrasa.mostrar();

                // ====================================================================
                // BLOQUE 9 - MATRIZ DE ESTADISTICAS (estadisticas)
                // Estadistica recibe una matriz double[n][4] con columnas:
                // peso inicial, peso actual, IMC, diferencia de kg.
                // Calcula IMC medio, mejor paciente, etc.
                // ====================================================================
                String[] nombresPacientes = {
                                "Ana Lopez", "Carlos Mendez", "Rosa Garcia"
                };
                double[][] datosPacientes = {
                                { 68.5, 65.2, 23.9, -3.3 },
                                { 85.0, 82.1, 25.9, -2.9 },
                                { 72.3, 71.0, 27.7, -1.3 }
                };
                Estadistica estadistica = new Estadistica(
                                nombresPacientes, datosPacientes, 15, 2);

                // ====================================================================
                // BLOQUE 10 - GENERACION DE INFORME (estadisticas)
                // Informe asocia un titulo, fecha, tipo y una Estadistica
                // para generar un reporte completo con resumen y matriz.
                // ====================================================================
                Informe informe = new Informe(
                                "Informe mensual Junio 2026", "14/06/2026",
                                "MENSUAL", "Marina Soler", estadistica);
                informe.generarInforme();

                // ====================================================================
                // BLOQUE 11 - EQUIPO PROFESIONAL (gestion_del_equipo_profesional)
                // Profesional con enum Rol (DIRECTIVO, NUTRICIONISTA, ADMINISTRATIVO).
                // AsignacionPaciente vincula profesional-paciente.
                // Agenda usa TipoEvento (CONSULTA, VACACIONES, SUSTITUCION).
                // Sustitucion gestiona reemplazos entre profesionales.
                // ====================================================================
                Profesional profEquipo1 = new Profesional(
                                "Laura", "Ruiz Lopez", "Nutricion deportiva",
                                "Master Nutricion", "laura@foodiet.com",
                                "600111222", Rol.NUTRICIONISTA);
                Profesional profEquipo2 = new Profesional(
                                "Pedro", "Diaz Martin", "Nutricion clinica",
                                "Doctor Nutricion", "pedro@foodiet.com",
                                "600333444", Rol.NUTRICIONISTA);
                Profesional profDirectivo = new Profesional(
                                "Admin", "Sistema Foodiet", "Gestion",
                                "Administrador", "admin@foodiet.com",
                                "600000000", Rol.DIRECTIVO);

                AsignacionPaciente asig1 = new AsignacionPaciente(
                                1, 1, "14/06/2026");
                AsignacionPaciente asig2 = new AsignacionPaciente(
                                2, 2, "14/06/2026");

                Agenda eventoAgenda = new Agenda(
                                1, "20/06/2026",
                                TipoEvento.CONSULTA,
                                "Consulta con Ana Lopez");

                Sustitucion sustitucion = new Sustitucion(
                                1, 2, "01/07/2026", "15/07/2026",
                                "Vacaciones de Laura");

                // ====================================================================
                // BLOQUE 12 - GESTION DE ARCHIVOS (foodiet.gestion)
                // GestorArchivos escribe logs y lee configuracion desde
                // ficheros de texto en la carpeta archivos/.
                // ====================================================================
                GestorArchivos gestorArchivos = new GestorArchivos();
                gestorArchivos.escribirLog("Sistema inicializado correctamente");
                String configLeida = gestorArchivos.leerConfiguracion();

                // ====================================================================
                // BLOQUE 13 - EXPORTACION DE FICHEROS (estadisticas)
                // GestorFicheros.exportarInforme vuelca una lista de Progreso
                // a un fichero de texto con formato estructurado.
                // ====================================================================
                ArrayList<Progreso> listaProgresos = new ArrayList<Progreso>();
                Progreso prog1 = new Progreso(
                                1, 1, "14/06/2026", 65.2, 23.9,
                                "Buena evolucion");
                listaProgresos.add(prog1);
                GestorFicheros.exportarInforme(listaProgresos,
                                "informe_progreso.txt");

                // ====================================================================
                // BLOQUE 14 - RECORRIDO DE ARRAYS CON VARIABLE N AUXILIAR
                // Se obtiene el valor de N desde GestionClinica (getPacientesN(),
                // getProfesionalesN(), getCitasN()) para iterar sobre las
                // posiciones realmente ocupadas sin modificar el indice.
                // ====================================================================
                int nPac = gestion.getPacientesN();
                for (int i = 0; i < nPac; i++) {
                        Paciente p = gestion.getPaciente(i);
                        System.out.println(p.nombreCompleto()
                                        + " | Tipo: " + p.tipoPaciente()
                                        + " | IMC: " + String.format("%.1f", p.calcularIMC()));
                }

                int nProf = gestion.getProfesionalesN();
                for (int i = 0; i < nProf; i++) {
                        gestion.getProfesional(i).mostrarInfo();
                }

                int nCitas = gestion.getCitasN();
                for (int i = 0; i < nCitas; i++) {
                        System.out.println(gestion.getCita(i).mostrarCita());
                }

                int nPlanes = gestion.getPlanesN();
                for (int i = 0; i < nPlanes; i++) {
                        System.out.println(gestion.getPlan(i).mostrarPlan());
                }

                // ====================================================================
                // BLOQUE 15 - CIERRE DEL SISTEMA Y AUDITORIA FINAL
                // (default package - PrincipalAdmin)
                // Se registra el cierre, se revisan los logs y se exportan
                // las alertas de uso incorrecto a un fichero.
                // ====================================================================
                gestorAuditoria.registrarAccion(
                                "Sistema finalizado correctamente");
                admin.revisarRegistros(gestorAuditoria);
                admin.notificarIncidencia(
                                "Revision completada sin incidencias graves");
                subsistemaAdmin.exportarAlertasUsoIncorrecto();
        }
}
