package controllers;

import daos.InscripcionDAO;
import daos.EstudianteDAO;
import daos.AulaDAO;
import dtos.InscripcionDTO;
import models.Inscripcion;
import utils.Validaciones;
import utils.Mensajes;
import views.InscripcionView;
import java.util.ArrayList;
import java.util.List;
import models.Aula;
import utils.Mostrar;

public class InscripcionController {

    private final InscripcionDAO dao;
    private final InscripcionView view;
    // Necesitamos estos dos para validar que existan antes de inscribir
    private final EstudianteDAO estudianteDAO;
    private final AulaDAO aulaDAO;

    public InscripcionController(InscripcionDAO dao, InscripcionView view, EstudianteDAO estDao, AulaDAO aulaDao) {
        this.dao = dao;
        this.view = view;
        this.estudianteDAO = estDao;
        this.aulaDAO = aulaDao;
    }

    public void iniciar() {
        int opcion;
        do {
            opcion = view.mostrarMenu();
            switch (opcion) {
                case 1:
                    registrarInscripcion();
                    break;
                case 2:
                    mostrarTodas();
                    break;
                case 3:
                    modificarInscripcion();
                    break;
                case 4:
                    eliminarInscripcion();
                    break;
                case 0:
                    view.mostrarMensaje(Mensajes.VOLVIENDO);
                    break;
                default:
                    view.mostrarMensaje(Mensajes.OPCION_INVALIDA);
            }
        } while (opcion != 0);
    }

    private void registrarInscripcion() {
        InscripcionDTO datos = view.pedirDatosNuevaInscripcion();

        try {
            // 1. Validar que el estudiante exista
            if (!Validaciones.existeEstudiante(estudianteDAO.obtenerRegistros(), datos.getIdEstudiante())) {
                view.mostrarMensaje(Mensajes.ERROR_ID);
                return;
            }
            // 2. Buscar el aula y validar que exista
            Aula aulaDestino = aulaDAO.obtenerPorId(datos.getCodigoAula());
            if (aulaDestino == null) {
                view.mostrarMensaje(Mensajes.ERROR_NO_ENCONTRADO);
                return;
            }

            // 3. Validar la capacidad especifica de esa aula
            int ocupacionActual = contarInscriptos(datos.getCodigoAula());
            if (ocupacionActual >= aulaDestino.getCapacidad()) {
                view.mostrarMensaje("Error: El aula " + datos.getCodigoAula() + " esta llena. Capacidad maxima: " + aulaDestino.getCapacidad());
                return;
            }

            // 4. Obtenemos la lista actual de inscripciones
            List<Inscripcion> listaActual = dao.obtenerRegistros();
            // 5. Validar si el estudiante ya está inscripto
            if (Validaciones.estudianteInscripto(listaActual, datos.getIdEstudiante())) {
                view.mostrarMensaje("Error: El estudiante ya se encuentra inscripto en un aula.");
                return;
            }

            // 6. Generar ID y guardar
            List<String> idsActuales = new ArrayList<>();
            for (Inscripcion i : listaActual) {
                idsActuales.add(i.getIdInscripcion());
            }
            String nuevoId = Validaciones.generarSiguienteId(idsActuales, "I");
            Inscripcion nuevaInscripcion = new Inscripcion(nuevoId, datos.getIdEstudiante(), datos.getCodigoAula());

            dao.agregar(nuevaInscripcion);
            view.mostrarMensaje(Mensajes.EXITO_GUARDAR + " (ID: " + nuevoId + ")");
        } catch (RuntimeException e) {
            Mostrar.Mensaje(Mensajes.EXITO_ACTUALIZAR);
        }

    }

    private void mostrarTodas() {
        try {
            List<Inscripcion> entidades = dao.obtenerRegistros();
            List<InscripcionDTO> dtos = new ArrayList<>();
            for (Inscripcion i : entidades) {
                dtos.add(new InscripcionDTO(i.getIdInscripcion(), i.getIdEstudiante(), i.getCodigoAula()));
            }
            view.mostrarInscripciones(dtos);
        } catch (RuntimeException e) {
            {
                Mostrar.Mensaje(Mensajes.ERROR_LECTURA);
            }
        }
    }

    private void eliminarInscripcion() {
        String id = view.pedirIdInscripcion();
        try {
            if (dao.eliminar(id)) {
                view.mostrarMensaje(Mensajes.EXITO_ELIMINAR);
            } else {
                view.mostrarMensaje(Mensajes.ERROR_NO_ENCONTRADO);
            }
        } catch (RuntimeException e) {
            Mostrar.Mensaje(Mensajes.ERROR_ELIMINAR);
        }
    }

    private int contarInscriptos(String codigoAula) {
        return (int) dao.obtenerRegistros().stream()
                .filter(inscripcion -> inscripcion.getCodigoAula().equals(codigoAula))
                .count();
    }

    private void modificarInscripcion() {
        // 1. Pedir el ID de la inscripción que queremos modificar
        String idInscripcion = view.pedirIdInscripcion();

        try {
            // 2. Buscar la inscripción en la lista actual
            Inscripcion inscripcionActual = dao.obtenerPorId(idInscripcion);

            if (inscripcionActual == null) {
                view.mostrarMensaje("Error: No se encontro ninguna inscripcion con el ID " + idInscripcion);
                return;
            }

            // 3. Pedir a qué aula lo queremos mover
            String nuevoCodigoAula = view.pedirNuevoCodigoAula();

            // 4. Validar que no lo estemos mandando a la misma aula donde ya está
            if (inscripcionActual.getCodigoAula().equals(nuevoCodigoAula)) {
                view.mostrarMensaje("El alumno ya se encuentra inscripto en esa aula. No hay cambios.");
                return;
            }

            // 5. Validar que la NUEVA aula exista
            Aula nuevaAula = aulaDAO.obtenerPorId(nuevoCodigoAula);
            if (nuevaAula == null) {
                view.mostrarMensaje(Mensajes.ERROR_NO_ENCONTRADO);
                return;
            }

            // 6. Validar que la NUEVA aula tenga lugar
            int ocupacionActual = contarInscriptos(nuevoCodigoAula);
            if (ocupacionActual >= nuevaAula.getCapacidad()) {
                view.mostrarMensaje("Error: El aula destino " + nuevoCodigoAula + " esta llena. Capacidad maxima: " + nuevaAula.getCapacidad());
                return;
            }

            // 7. Aplicar el cambio y guardar
            inscripcionActual.setCodigoAula(nuevoCodigoAula);

            dao.modificar(inscripcionActual);

            view.mostrarMensaje("Exito! El estudiante fue reasignado al aula " + nuevoCodigoAula);
        } catch (RuntimeException e) {
            Mostrar.Mensaje(Mensajes.ERROR_ACTUALIZAR);
        }

    }
}
