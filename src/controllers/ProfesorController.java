package controllers;

import daos.ProfesorDAO;
import dtos.ProfesorDTO;
import models.Profesor;
import utils.Validaciones;
import views.ProfesorView;
import java.util.ArrayList;
import java.util.List;
import utils.Mensajes;

public class ProfesorController {

    private ProfesorDAO dao;
    private ProfesorView view;

    public ProfesorController(ProfesorDAO dao, ProfesorView view) {
        this.dao = dao;
        this.view = view;
    }

    public void iniciar() {
        int opcion;
        do {
            opcion = view.mostrarMenu();
            switch (opcion) {
                case 1:
                    registrarProfesor();
                    break;
                case 2:
                    mostrarTodos();
                    break;
                case 3:
                    actualizarProfesor();
                    break;
                case 4:
                    eliminarProfesor();
                    break;
                case 0:
                    view.mostrarMensaje(Mensajes.VOLVIENDO);
                    break;
                default:
                    view.mostrarMensaje(Mensajes.OPCION_INVALIDA);
            }
        } while (opcion != 0); // Corta automaticamente cuando es 0
    }

    private void registrarProfesor() {
        ProfesorDTO datos = view.pedirDatosNuevoProfesor();
        List<Profesor> listaActual = dao.obtenerTodos();
        List<String> idsActuales = new ArrayList<>();

        for (Profesor p : listaActual) {
            idsActuales.add(p.getId());
        }

        String nuevoId = Validaciones.generarSiguienteId(idsActuales, "P");
        Profesor nuevoProfesor = new Profesor(nuevoId, datos.dni, datos.nombre, datos.apellido);

        dao.agregar(nuevoProfesor);
        view.mostrarMensaje(Mensajes.EXITO_GUARDAR);
    }

    private void mostrarTodos() {
        List<Profesor> entidades = dao.obtenerTodos();
        List<ProfesorDTO> dtos = new ArrayList<>();
        for (Profesor p : entidades) {
            dtos.add(new ProfesorDTO(p.getId(), p.getDni(), p.getNombre(), p.getApellido()));
        }
        view.mostrarProfesores(dtos);
    }

    private void actualizarProfesor() {
        String id = view.pedirId();
        List<Profesor> listaActual = dao.obtenerTodos();

        if (!Validaciones.existeProfesor(listaActual, id)) {
            view.mostrarMensaje(Mensajes.ERROR_ID);
            return;
        }

        view.mostrarMensaje("Ingrese los NUEVOS datos para el profesor:");
        ProfesorDTO datos = view.pedirDatosNuevoProfesor();
        Profesor profeModificado = new Profesor(id, datos.dni, datos.nombre, datos.apellido);

        if (dao.actualizar(profeModificado)) {
            view.mostrarMensaje(Mensajes.EXITO_ACTUALIZAR);
        } else {
            view.mostrarMensaje(Mensajes.ERROR_ACTUALIZAR);
        }
    }

    private void eliminarProfesor() {
        String id = view.pedirId();
        if (dao.eliminar(id)) {
            view.mostrarMensaje(Mensajes.EXITO_ELIMINAR);
        } else {
            view.mostrarMensaje(Mensajes.ERROR_ID);
        }
    }
}
