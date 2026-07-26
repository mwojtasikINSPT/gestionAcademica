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

        // 1. Validar que el estudiante exista
        if (!Validaciones.existeEstudiante(estudianteDAO.obtenerTodos(), datos.idEstudiante)) {
            view.mostrarMensaje(Mensajes.ERROR_ID);
            return;
        }

        // 2. Buscar el aula y validar que exista
        Aula aulaDestino = null;
        for (Aula a : aulaDAO.obtenerTodas()) {
            if (a.getCodigo().equals(datos.codigoAula)) {
                aulaDestino = a;
                break;
            }
        }

        if (aulaDestino == null) {
            view.mostrarMensaje(Mensajes.ERROR_NO_ENCONTRADO);
            return;
        }

        // 3. Validar la capacidad especifica de esa aula
        int ocupacionActual = contarInscriptos(datos.codigoAula);
        if (ocupacionActual >= aulaDestino.getCapacidad()) {
            view.mostrarMensaje("Error: El aula " + datos.codigoAula + " esta llena. Capacidad maxima: " + aulaDestino.getCapacidad());
            return;
        }

        // 4. Generar ID y guardar
        List<Inscripcion> listaActual = dao.obtenerTodas();
        List<String> idsActuales = new ArrayList<>();
        for (Inscripcion i : listaActual) {
            idsActuales.add(i.getIdInscripcion());
        }

        String nuevoId = Validaciones.generarSiguienteId(idsActuales, "I");
        Inscripcion nuevaInscripcion = new Inscripcion(nuevoId, datos.idEstudiante, datos.codigoAula);

        dao.agregar(nuevaInscripcion);
        view.mostrarMensaje(Mensajes.EXITO_GUARDAR + " (ID: " + nuevoId + ")");
    }

    private void mostrarTodas() {
        List<Inscripcion> entidades = dao.obtenerTodas();
        List<InscripcionDTO> dtos = new ArrayList<>();
        for (Inscripcion i : entidades) {
            dtos.add(new InscripcionDTO(i.getIdInscripcion(), i.getIdEstudiante(), i.getCodigoAula()));
        }
        view.mostrarInscripciones(dtos);
    }
/*
    private void mostrarTodas() {
        List<Inscripcion> entidades = dao.obtenerTodas();
        List<InscripcionDTO> dtos = new ArrayList<>();
        for (Inscripcion i : entidades) {
            InscripcionDTO dto = new InscripcionDTO(i.getIdInscripcion(), i.getIdEstudiante(), i.getCodigoAula());
            // DEPURACIÓN: ¿Qué imprime esto por consola?
            System.out.println("DEBUG DTO -> ID: " + dto.getIdInscripcion() + " | Est: " + dto.getIdEstudiante());
            dtos.add(dto);
        }
        view.mostrarInscripciones(dtos);
    }
*/
    private void eliminarInscripcion() {
        String id = view.pedirIdInscripcion();
        if (dao.eliminar(id)) {
            view.mostrarMensaje(Mensajes.EXITO_ELIMINAR);
        } else {
            view.mostrarMensaje(Mensajes.ERROR_NO_ENCONTRADO);
        }
    }

    //Para ver capacidad aula inscriptos
    private int contarInscriptos(String codigoAula) {
        int cantidad = 0;
        for (Inscripcion i : dao.obtenerTodas()) {
            if (i.getCodigoAula().equals(codigoAula)) {
                cantidad++;
            }
        }
        return cantidad;
    }
}
