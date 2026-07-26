package controllers;

import daos.EstudianteDAO;
import dtos.EstudianteDTO;
import models.Estudiante;
import java.util.ArrayList;
import java.util.List;
import utils.Mensajes;
import utils.Validaciones;
import views.EstudianteView;

public class EstudianteController {

    private EstudianteDAO dao;
    private EstudianteView view;

    public EstudianteController(EstudianteDAO dao, EstudianteView view) {
        this.dao = dao;
        this.view = view;
    }

    public void iniciar() {
        int opcion; 
        do{
            opcion = view.mostrarMenu();
            switch (opcion) {
                case 1:
                    registrarEstudiante();
                    break;
                case 2:
                    mostrarTodos();
                    break;
                case 3:
                    actualizarEstudiante();
                    break;
                case 4:
                    eliminarEstudiante();
                    break;
                case 0:
                    view.mostrarMensaje(Mensajes.VOLVIENDO);
                    break;
                default:
                    view.mostrarMensaje(Mensajes.OPCION_INVALIDA);
            }
        }while(opcion !=0);
    }

    private void registrarEstudiante() {
        // 1. La vista nos da los datos base en un DTO
        EstudianteDTO datos = view.pedirDatosNuevoEstudiante();

        // 2. Extraemos los IDs existentes para generar el nuevo
        List<Estudiante> listaActual = dao.obtenerTodos();
        List<String> idsActuales = new ArrayList<>();
        for (Estudiante e : listaActual) {
            idsActuales.add(e.getId());
        }

        // 3. Usamos Utils/Validaciones para el ID autoincremental
        String nuevoId = Validaciones.generarSiguienteId(idsActuales, "E");

        // 4. Transformamos el DTO en la Entidad real
        Estudiante nuevoEstudiante = new Estudiante(nuevoId, datos.dni, datos.nombre, datos.apellido);

        // 5. Guardamos mediante el DAO
        dao.agregar(nuevoEstudiante);
        view.mostrarMensaje(Mensajes.EXITO_GUARDAR);
    }

    private void mostrarTodos() {
        // 1. Pedimos al DAO las entidades
        List<Estudiante> entidades = dao.obtenerTodos();

        // 2. Las mapeamos a DTOs para mandarlas a la vista
        List<EstudianteDTO> dtos = new ArrayList<>();
        for (Estudiante e : entidades) {
            dtos.add(new EstudianteDTO(e.getId(), e.getDni(), e.getNombre(), e.getApellido()));
        }

        // 3. La vista los muestra
        view.mostrarEstudiantes(dtos);
    }

    private void actualizarEstudiante() {
        String id = view.pedirId();
        List<Estudiante> listaActual = dao.obtenerTodos();

        if (!Validaciones.existeEstudiante(listaActual, id)) {
            view.mostrarMensaje(Mensajes.ERROR_ID);
            return;
        }

        view.mostrarMensaje("Ingrese los NUEVOS datos para el estudiante:");
        // Reutilizamos el metodo de la vista porque ya tiene las validaciones de DNI, nombre, etc.
        EstudianteDTO datos = view.pedirDatosNuevoEstudiante();

        // Creamos la entidad respetando el ID original, pero con los datos nuevos
        Estudiante estudianteModificado = new Estudiante(id, datos.dni, datos.nombre, datos.apellido);

        if (dao.actualizar(estudianteModificado)) {
            view.mostrarMensaje(Mensajes.EXITO_ACTUALIZAR);
        } else {
            view.mostrarMensaje(Mensajes.ERROR_ACTUALIZAR);
        }
    }

    private void eliminarEstudiante() {
        String id = view.pedirId();

        // El DAO ya nos devuelve true si lo elimino o false si no lo encontro
        if (dao.eliminar(id)) {
            view.mostrarMensaje(Mensajes.EXITO_ELIMINAR);
        } else {
            view.mostrarMensaje(Mensajes.ERROR_ID);
        }
    }
}
