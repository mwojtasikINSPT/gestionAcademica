package controllers;

import daos.AsignacionDAO;
import daos.ProfesorDAO;
import daos.AulaDAO;
import dtos.AsignacionDTO;
import models.Asignacion;
import models.Profesor;
import models.Aula;
import java.util.ArrayList;
import java.util.List;
import utils.*;
import views.AsignacionView;

// Controller: coordina la operación y decide qué mensaje corresponde para Asignaciones.
public class AsignacionController {

    private final AsignacionDAO dao;
    private final ProfesorDAO profesorDAO = new ProfesorDAO();
    private final AulaDAO aulaDAO = new AulaDAO();
    private final AsignacionView view;

    public AsignacionController(AsignacionDAO dao, AsignacionView view) {
        this.dao = dao;
        this.view = view;
    }

    public void registrarAsignacion() {
        try {
            AsignacionDTO nuevaAsignacionDTO = view.pedirDatosNuevaAsignacion();

            // 1. Validar que el profesor exista
            Profesor profesor = profesorDAO.obtenerPorId(nuevaAsignacionDTO.getIdProfesor());
            if (profesor == null) {
                view.mostrar(Mensajes.ERROR_ID + " (El profesor ingresado no existe)");
                return;
            }

            // 2. Validar que el aula exista
            Aula aula = aulaDAO.obtenerPorId(nuevaAsignacionDTO.getCodigoAula());
            if (aula == null) {
                view.mostrar(Mensajes.ERROR_ID + " (El aula ingresada no existe)");
                return;
            }

            // 3. Generar ID automático para la asignación (ej. AS001)
            List<Asignacion> listaActual = dao.obtenerRegistros();
            List<String> codigosActuales = new ArrayList<>();
            for (Asignacion a : listaActual) {
                codigosActuales.add(a.getIdAsignacion());
            }

            String nuevoId = Validaciones.generarSiguienteId(codigosActuales, "AS");
            Asignacion nuevaAsignacion = new Asignacion(nuevoId, nuevaAsignacionDTO.getIdProfesor(), nuevaAsignacionDTO.getCodigoAula());

            // 4. Guardar
            dao.agregar(nuevaAsignacion);
            view.mostrar(Mensajes.EXITO_GUARDAR + " (ID Asignación: " + nuevaAsignacion.getIdAsignacion() + ")");

        } catch (RuntimeException e) {
            view.mostrar(Mensajes.ERROR_GUARDAR);
        }
    }

    public void mostrarTodas() {
        try {
            List<Asignacion> entidades = dao.obtenerRegistros();
            List<AsignacionDTO> dtos = new ArrayList<>();

            for (Asignacion a : entidades) {
                dtos.add(new AsignacionDTO(a.getIdAsignacion(), a.getIdProfesor(), a.getCodigoAula()));
            }

            view.mostrarAsignaciones(dtos);
        } catch (RuntimeException e) {
            view.mostrar(Mensajes.ERROR_LECTURA);
        }
    }

    public void actualizarAsignacion() {
        try {
            String id = view.pedirIdAsignacion();
            Asignacion asignacionExistente = dao.obtenerPorId(id);

            if (asignacionExistente == null) {
                view.mostrar(Mensajes.ERROR_ID);
                return;
            }

            // Pedimos el nuevo código de aula al que se quiere reasignar
            String nuevoCodigoAula = view.pedirNuevoCodigoAula();

            // Validamos que el aula nueva exista
            Aula aula = aulaDAO.obtenerPorId(nuevoCodigoAula);
            if (aula == null) {
                view.mostrar(Mensajes.ERROR_ID + " (El aula nueva no existe)");
                return;
            }

            // Modificamos manteniendo el mismo ID y Profesor, cambiando solo el aula (o lo que corresponda)
            Asignacion asignacionModificada = new Asignacion(id, asignacionExistente.getIdProfesor(), nuevoCodigoAula);
            dao.modificar(asignacionModificada);

            view.mostrar(Mensajes.EXITO_ACTUALIZAR);
        } catch (RuntimeException e) {
            view.mostrar(Mensajes.ERROR_GUARDAR);
        }
    }

    public void eliminarAsignacion() {
        try {
            String id = view.pedirIdAsignacion();
            Asignacion asignacion = dao.obtenerPorId(id);

            if (asignacion == null) {
                view.mostrar(Mensajes.ERROR_ID);
                return;
            }

            boolean eliminado = dao.eliminar(id);
            if (eliminado) {
                view.mostrar(Mensajes.EXITO_ELIMINAR);
            }
        } catch (RuntimeException e) {
            view.mostrar(Mensajes.ERROR_ELIMINAR);
        }
    }
}
