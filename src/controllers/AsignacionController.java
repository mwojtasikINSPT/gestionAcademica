package controllers;

import daos.AsignacionDAO;
import daos.ProfesorDAO;
import daos.AulaDAO;
import dtos.AsignacionDTO;
import models.Asignacion;
import utils.Validaciones;
import utils.Mensajes;
import views.AsignacionView;
import java.util.ArrayList;
import java.util.List;
import models.Aula;

public class AsignacionController {

    private final AsignacionDAO dao;
    private final AsignacionView view;
    private final ProfesorDAO profesorDAO;
    private final AulaDAO aulaDAO;

    public AsignacionController(AsignacionDAO dao, AsignacionView view, ProfesorDAO profDao, AulaDAO aulaDao) {
        this.dao = dao;
        this.view = view;
        this.profesorDAO = profDao;
        this.aulaDAO = aulaDao;
    }

    public void iniciar() {
        int opcion;
        do {
            opcion = view.mostrarMenu();
            switch (opcion) {
                case 1:
                    registrarAsignacion();
                    break;
                case 2:
                    mostrarTodas();
                    break;
                case 3:
                    modificarAsignacion();
                    break;
                case 4:
                    eliminarAsignacion();
                    break;
                case 0:
                    view.mostrarMensaje(Mensajes.VOLVIENDO);
                    break;
                default:
                    view.mostrarMensaje(Mensajes.OPCION_INVALIDA);
            }
        } while (opcion != 0);
    }

    private void registrarAsignacion() {
        AsignacionDTO datos = view.pedirDatosNuevaAsignacion();

        // 1. Validar que el profesor exista
        if (!Validaciones.existeProfesor(profesorDAO.obtenerRegistros(), datos.idProfesor)) {
            view.mostrarMensaje("Error: El profesor con ID " + datos.idProfesor + " no existe.");
            return;
        }

        // 2. Validar que el aula exista
        if (!Validaciones.existeAula(aulaDAO.obtenerRegistros(), datos.codigoAula)) {
            view.mostrarMensaje("Error: El aula con código " + datos.codigoAula + " no existe.");
            return;
        }

        // 3. Obtenemos la lista actual de asignaciones de una sola vez
        List<Asignacion> listaActual = dao.obtenerRegistros();

        // 4. Validar si el profesor ya tiene un aula asignada
        if (Validaciones.profesorTieneAula(listaActual, datos.idProfesor)) {
            view.mostrarMensaje("Error: El profesor ya tiene un aula asignada.");
            return;
        }

        // 5. Validar si el aula ya tiene un profesor asignado
        if (Validaciones.aulaOcupada(listaActual, datos.codigoAula)) {
            view.mostrarMensaje("Error: El aula " + datos.codigoAula + " ya se encuentra ocupada por otro profesor.");
            return;
        }

        // 6. Generar ID y guardar (Usamos "AS" para diferenciar de las Aulas que usan "A")
        List<String> idsActuales = new ArrayList<>();
        for (Asignacion a : listaActual) {
            idsActuales.add(a.getIdAsignacion());
        }

        String nuevoId = Validaciones.generarSiguienteId(idsActuales, "AS");
        Asignacion nuevaAsignacion = new Asignacion(nuevoId, datos.idProfesor, datos.codigoAula);

        dao.agregar(nuevaAsignacion);
        view.mostrarMensaje(Mensajes.EXITO_GUARDAR + " (ID: " + nuevoId + ")");
    }

    private void mostrarTodas() {
        List<Asignacion> entidades = dao.obtenerRegistros();
        List<AsignacionDTO> dtos = new ArrayList<>();
        for (Asignacion a : entidades) {
            dtos.add(new AsignacionDTO(a.getIdAsignacion(), a.getIdProfesor(), a.getCodigoAula()));
        }
        view.mostrarAsignaciones(dtos);
    }

    private void eliminarAsignacion() {
        String id = view.pedirIdAsignacion();
        if (dao.eliminar(id)) {
            view.mostrarMensaje(Mensajes.EXITO_ELIMINAR);
        } else {
            view.mostrarMensaje(Mensajes.ERROR_NO_ENCONTRADO);
        }
    }

    private void modificarAsignacion() {
        // 1. Pedir el ID de la asignación que queremos modificar
        String idAsignacion = view.pedirIdAsignacion(); // Ajustá el nombre según tu vista

        // 2. Buscar la asignación actual
        Asignacion asignacionActual = null;
        for (Asignacion a : dao.obtenerRegistros()) {
            if (a.getIdAsignacion().equals(idAsignacion)) {
                asignacionActual = a;
                break;
            }
        }

        if (asignacionActual == null) {
            view.mostrarMensaje("Error: No se encontró la asignación con ID " + idAsignacion);
            return;
        }

        // 3. Pedir el código de la nueva aula 
        String nuevoCodigoAula = view.pedirNuevoCodigoAula();

        // 4. Validar que no lo estemos mandando a la misma aula donde ya está
        if (asignacionActual.getCodigoAula().equals(nuevoCodigoAula)) {
            view.mostrarMensaje("El profesor ya se encuentra asignado a esa aula. No hay cambios.");
            return;
        }

        // 5. Validar que la NUEVA aula exista (Reutilizamos tu método pro del DAO)
        Aula nuevaAula = aulaDAO.obtenerPorId(nuevoCodigoAula);
        if (nuevaAula == null) {
            view.mostrarMensaje(Mensajes.ERROR_NO_ENCONTRADO);
            return;
        }

        // 6. Validar que la NUEVA aula no esté ocupada por otro profesor
        List<Asignacion> listaActual = dao.obtenerRegistros();
        if (Validaciones.aulaOcupada(listaActual, nuevoCodigoAula)) {
            view.mostrarMensaje("Error: El aula " + nuevoCodigoAula + " ya tiene un profesor asignado.");
            return;
        }

        // 7. Aplicar el cambio y guardar
        asignacionActual.setCodigoAula(nuevoCodigoAula);

        // Al igual que en inscripciones, vas a necesitar un método actualizar en tu DAO de Asignaciones
        dao.modificar(asignacionActual);

        view.mostrarMensaje("Exito! El profesor fue reasignado al aula " + nuevoCodigoAula);
    }
}
