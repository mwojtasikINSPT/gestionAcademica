package controllers;

import daos.ProfesorDAO;
import dtos.ProfesorDTO;
import models.Profesor;
import utils.Validaciones;
import views.ProfesorView;
import java.util.ArrayList;
import java.util.List;
import utils.Mensajes;
import utils.Mostrar;

public class ProfesorController {

    private final ProfesorDAO dao;
    private final ProfesorView view;

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
                    Mostrar.Mensaje(Mensajes.VOLVIENDO);
                    break;
                default:
                    Mostrar.Mensaje(Mensajes.OPCION_INVALIDA);
            }
        } while (opcion != 0); // Corta automaticamente cuando es 0
    }

    private void registrarProfesor() {
        ProfesorDTO datos = view.pedirDatosNuevoProfesor();

        try {
            List<Profesor> listaActual = dao.obtenerRegistros();
            List<String> idsActuales = new ArrayList<>();

            for (Profesor p : listaActual) {
                idsActuales.add(p.getId());
            }

            String nuevoId = Validaciones.generarSiguienteId(idsActuales, "P");
            Profesor nuevoProfesor = new Profesor(nuevoId, datos.getDni(), datos.getNombre(), datos.getApellido());

            dao.agregar(nuevoProfesor);
            Mostrar.Mensaje(Mensajes.EXITO_GUARDAR);
        } catch (RuntimeException e) {
            Mostrar.Mensaje(Mensajes.ERROR_GUARDAR);
        }
    }

    private void mostrarTodos() {
        
        try {
            List<Profesor> entidades = dao.obtenerRegistros();
            List<ProfesorDTO> dtos = new ArrayList<>();
            for (Profesor p : entidades) {
                dtos.add(new ProfesorDTO(p.getId(), p.getDni(), p.getNombre(), p.getApellido()));
            }
            view.mostrarProfesores(dtos);
        } catch (RuntimeException e) {
            Mostrar.Mensaje(Mensajes.ERROR_LECTURA);
        }
    }

    private void actualizarProfesor() {
        String id = view.pedirId();

        try {
            List<Profesor> listaActual = dao.obtenerRegistros();

            if (!Validaciones.existeProfesor(listaActual, id)) {
                Mostrar.Mensaje(Mensajes.ERROR_ID);
                return;
            }

            Mostrar.Mensaje("Ingrese los NUEVOS datos para el profesor:");
            ProfesorDTO datos = view.pedirDatosNuevoProfesor();
            Profesor profeModificado = new Profesor(id, datos.getDni(), datos.getNombre(), datos.getApellido());

            dao.modificar(profeModificado);
            Mostrar.Mensaje(Mensajes.EXITO_ACTUALIZAR);
        } catch (RuntimeException e) {
            Mostrar.Mensaje(Mensajes.ERROR_GUARDAR);
        }
    }

    private void eliminarProfesor() {
        String id = view.pedirId();
        
        try{
        // 1. Primero verificamos si el profesor existe usando el obtenerPorId
        Profesor profe = dao.obtenerPorId(id);

        if (profe == null) {
            // Si es null, mostramos el error de que no se encontró
            Mostrar.Mensaje(Mensajes.ERROR_ID);
        } else {
            // 2. Si existe, le decimos al DAO que intente eliminarlo.
            // Si está asignado, el DAO va a frenarlo, imprimir su propio mensaje y devolver false.
            boolean eliminado = dao.eliminar(id);

            // 3. Solo si el DAO logró eliminarlo de verdad (true), mostramos el éxito
            if (eliminado) {
                Mostrar.Mensaje(Mensajes.EXITO_ELIMINAR);
            }
        }}
        catch(RuntimeException e){
            Mostrar.Mensaje(Mensajes.ERROR_ELIMINAR);
        }
    }
}
