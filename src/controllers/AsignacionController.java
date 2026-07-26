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
                case 1: registrarAsignacion(); break;
                case 2: mostrarTodas(); break;
                case 3: eliminarAsignacion(); break;
                case 0: 
                    view.mostrarMensaje(Mensajes.VOLVIENDO);
                    break;
                default: view.mostrarMensaje(Mensajes.OPCION_INVALIDA);
            }
        } while (opcion != 0);
    }

    private void registrarAsignacion() {
        AsignacionDTO datos = view.pedirDatosNuevaAsignacion();
        
        // 1. Validar que el profesor exista
        if (!Validaciones.existeProfesor(profesorDAO.obtenerTodos(), datos.idProfesor)) {
            view.mostrarMensaje("Error: El profesor con ID " + datos.idProfesor + " no existe.");
            return;
        }
        
        // 2. Validar que el aula exista
        if (!Validaciones.existeAula(aulaDAO.obtenerTodas(), datos.codigoAula)) {
            view.mostrarMensaje("Error: El aula con codigo " + datos.codigoAula + " no existe.");
            return;
        }

        // 3. Generar ID y guardar (Usamos "AS" para diferenciar de las Aulas que usan "A")
        List<Asignacion> listaActual = dao.obtenerTodas();
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
        List<Asignacion> entidades = dao.obtenerTodas();
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
}