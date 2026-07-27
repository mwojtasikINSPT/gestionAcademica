package controllers;

import daos.AsignacionDAO;
import daos.EstudianteDAO;
import daos.InscripcionDAO;
import daos.ProfesorDAO;
import java.util.ArrayList;
import java.util.List;
import models.Asignacion;
import models.Estudiante;
import models.Inscripcion;
import models.Profesor;
import utils.Mensajes;
import views.ConsultasView;

public class ConsultasController {

    private final ConsultasView view;
    private final EstudianteDAO estudianteDAO;
    private final ProfesorDAO profesorDAO;
    private final InscripcionDAO inscripcionDAO;
    private final AsignacionDAO asignacionDAO;

    public ConsultasController(ConsultasView view, EstudianteDAO estDao, ProfesorDAO profDao, InscripcionDAO insDao, AsignacionDAO asigDao) {
        this.view = view;
        this.estudianteDAO = estDao;
        this.profesorDAO = profDao;
        this.inscripcionDAO = insDao;
        this.asignacionDAO = asigDao;
    }

    public void iniciar() {
        int opcion;
        do {
            opcion = view.mostrarMenuConsultas();
            switch (opcion) {
                case 1:
                    consultarEstudiantesConAula();
                    break;
                case 2:
                    consultarEstudiantesDeProfesor();
                    break;
                case 3:
                    consultarProfesorDeEstudiante();
                    break;
                case 0:
                    view.mostrarMensaje(Mensajes.VOLVIENDO);
                    break;
                default:
                    view.mostrarMensaje(Mensajes.OPCION_INVALIDA);
            }
        } while (opcion != 0);
    }

    private void consultarEstudiantesConAula() {
        StringBuilder reporte = new StringBuilder("\n--- REPORTE: ESTUDIANTES Y SUS AULAS ---\n");
        var inscripciones = inscripcionDAO.obtenerTodas();
        var estudiantes = estudianteDAO.obtenerTodos();

        if (inscripciones.isEmpty()) {
            reporte.append("No hay inscripciones registradas en el sistema.");
        } else {
            for (Inscripcion ins : inscripciones) {
                // Buscamos el nombre del estudiante correspondiente a la inscripcion
                String nombreEstudiante = "Estudiante desconocido";
                for (Estudiante est : estudiantes) {
                    if (est.getId().equals(ins.getIdEstudiante())) {
                        nombreEstudiante = est.getNombre() + " " + est.getApellido();
                        break;
                    }
                }
                reporte.append("Estudiante: ").append(nombreEstudiante)
                        .append(" | Aula Asignada: ").append(ins.getCodigoAula()).append("\n");
            }
        }
        view.mostrarResultado(reporte.toString());
    }

    private void consultarEstudiantesDeProfesor() {
        String idProfesor = view.pedirIdProfesor();

        // 1. Verificar si el profesor existe
        Profesor profesorEncontrado = null;
        for (Profesor p : profesorDAO.obtenerTodos()) {
            if (p.getId().equals(idProfesor)) {
                profesorEncontrado = p;
                break;
            }
        }

        if (profesorEncontrado == null) {
            view.mostrarMensaje("Error: No se encontro un profesor con el ID " + idProfesor);
            return;
        }

        // 2. Buscar qué aulas tiene asignadas este profesor
        List<String> aulasDelProfesor = new ArrayList<>();
        for (Asignacion a : asignacionDAO.obtenerTodas()) {
            if (a.getIdProfesor().equals(idProfesor)) {
                aulasDelProfesor.add(a.getCodigoAula());
            }
        }

        if (aulasDelProfesor.isEmpty()) {
            view.mostrarMensaje("El profesor " + profesorEncontrado.getNombre() + " no tiene aulas asignadas.");
            return;
        }

        // 3. Buscar qué estudiantes están inscriptos en esas aulas
        StringBuilder reporte = new StringBuilder("\n--- ESTUDIANTES A CARGO DE: " + profesorEncontrado.getNombre() + " " + profesorEncontrado.getApellido() + " ---\n");
        boolean hayEstudiantes = false;

        for (Inscripcion ins : inscripcionDAO.obtenerTodas()) {
            if (aulasDelProfesor.contains(ins.getCodigoAula())) {
                // Encontramos un estudiante en un aula del profesor, buscamos sus datos
                for (Estudiante est : estudianteDAO.obtenerTodos()) {
                    if (est.getId().equals(ins.getIdEstudiante())) {
                        reporte.append(" - ").append(est.getNombre()).append(" ").append(est.getApellido())
                                .append(" (Aula: ").append(ins.getCodigoAula()).append(")\n");
                        hayEstudiantes = true;
                        break;
                    }
                }
            }
        }

        if (!hayEstudiantes) {
            reporte.append("No hay estudiantes inscriptos en las aulas de este profesor.");
        }

        view.mostrarResultado(reporte.toString());
    }
    
    private void consultarProfesorDeEstudiante() {
        String idEstudiante = view.pedirIdEstudiante();
        
        // 1. Validar que el estudiante exista
        Estudiante estudianteEncontrado = null;
        for (Estudiante e : estudianteDAO.obtenerTodos()) {
            if (e.getId().equals(idEstudiante)) {
                estudianteEncontrado = e;
                break;
            }
        }

        if (estudianteEncontrado == null) {
            view.mostrarMensaje("Error: No se encontro un estudiante con el ID " + idEstudiante);
            return;
        }

        // 2. Buscar en qué aula está inscripto el estudiante
        String codigoAulaInscripto = null;
        for (Inscripcion i : inscripcionDAO.obtenerTodas()) {
            if (i.getIdEstudiante().equals(idEstudiante)) {
                codigoAulaInscripto = i.getCodigoAula();
                break;
            }
        }

        if (codigoAulaInscripto == null) {
            view.mostrarMensaje("El estudiante " + estudianteEncontrado.getNombre() + " no esta inscripto en ninguna aula.");
            return;
        }

        // 3. Buscar qué profesor está asignado a esa aula
        String idProfesorAsignado = null;
        for (Asignacion a : asignacionDAO.obtenerTodas()) {
            if (a.getCodigoAula().equals(codigoAulaInscripto)) {
                idProfesorAsignado = a.getIdProfesor();
                break;
            }
        }

        if (idProfesorAsignado == null) {
            view.mostrarMensaje("El estudiante esta en el aula " + codigoAulaInscripto + ", pero esa aula no tiene un profesor asignado.");
            return;
        }

        // 4. Buscar los datos reales del profesor para mostrar su nombre
        Profesor profesorAsignado = null;
        for (Profesor p : profesorDAO.obtenerTodos()) {
            if (p.getId().equals(idProfesorAsignado)) {
                profesorAsignado = p;
                break;
            }
        }

        // 5. Mostrar el resultado final
        StringBuilder reporte = new StringBuilder("\n--- PROFESOR ASIGNADO AL ESTUDIANTE ---\n");
        reporte.append("Estudiante: ").append(estudianteEncontrado.getNombre()).append(" ").append(estudianteEncontrado.getApellido()).append("\n");
        reporte.append("Aula: ").append(codigoAulaInscripto).append("\n");
        
        if (profesorAsignado != null) {
            reporte.append("Profesor a cargo: ").append(profesorAsignado.getNombre()).append(" ").append(profesorAsignado.getApellido())
                   .append(" (ID: ").append(profesorAsignado.getId()).append(")\n");
        } else {
            reporte.append("Profesor a cargo: ID ").append(idProfesorAsignado).append(" (Datos no encontrados)\n");
        }

        view.mostrarResultado(reporte.toString());
    }
}
