package controllers;

import daos.EstudianteDAO;
import dtos.EstudianteDTO;
import models.Estudiante;
import java.util.ArrayList;
import java.util.List;
import utils.Mensajes;
import utils.Mostrar;
import utils.Validaciones;
import views.EstudianteView;

public class EstudianteController {

    private final EstudianteDAO dao;
    private final EstudianteView view;

    public EstudianteController(EstudianteDAO dao, EstudianteView view) {
        this.dao = dao;
        this.view = view;
    }

    public void iniciar() {
        int opcion;
        do {
            opcion = view.mostrarMenu();
            switch (opcion) {
                case 1 ->
                    registrarEstudiante();
                case 2 ->
                    mostrarTodos();
                case 3 ->
                    actualizarEstudiante();
                case 4 ->
                    eliminarEstudiante();
                case 0 ->
                    Mostrar.Mensaje(Mensajes.VOLVIENDO);
                default ->
                    Mostrar.Mensaje(Mensajes.OPCION_INVALIDA);
            }
        } while (opcion != 0);
    }

    private void registrarEstudiante() {

        // 1. La vista nos da los datos base en un DTO
        EstudianteDTO datos = view.pedirDatosNuevoEstudiante();

        try {
            // 2. Extraemos los IDs existentes para generar el nuevo
            List<Estudiante> listaActual = dao.obtenerRegistros();
            List<String> idsActuales = new ArrayList<>();
            for (Estudiante e : listaActual) {
                idsActuales.add(e.getId());
            }

            // 3. Usamos Utils/Validaciones para el ID autoincremental
            String nuevoId = Validaciones.generarSiguienteId(idsActuales, "E");

            // 4. Transformamos el DTO en la Entidad real
            Estudiante nuevoEstudiante = new Estudiante(nuevoId, datos.getDni(), datos.getNombre(), datos.getApellido());

            // 5. Guardamos mediante el DAO
            dao.agregar(nuevoEstudiante);
            Mostrar.Mensaje(Mensajes.EXITO_GUARDAR);
        } catch (RuntimeException e) {
            Mostrar.Mensaje(Mensajes.ERROR_GUARDAR);
        }
    }

    private void mostrarTodos() {
        // 1. Pedimos al DAO las entidades
        List<Estudiante> entidades = dao.obtenerRegistros();

        try {
            // 2. Las mapeamos a DTOs para mandarlas a la vista
            List<EstudianteDTO> dtos = new ArrayList<>();
            for (Estudiante e : entidades) {
                dtos.add(new EstudianteDTO(e.getId(), e.getDni(), e.getNombre(), e.getApellido()));
            }

            // 3. La vista los muestra
            view.mostrarEstudiantes(dtos);
        } catch (RuntimeException e) {
            Mostrar.Mensaje(Mensajes.ERROR_LECTURA);
        }
    }

    private void actualizarEstudiante() {

        String id = view.pedirId();

        try {
            List<Estudiante> listaActual = dao.obtenerRegistros();

            if (!Validaciones.existeEstudiante(listaActual, id)) {
                Mostrar.Mensaje(Mensajes.ERROR_ID);
                return;
            }

            Mostrar.Mensaje(Mensajes.PEDIR_NUEVOS_DATOS);
            // Reutilizamos el metodo de la vista porque ya tiene las validaciones de DNI, nombre, etc.
            EstudianteDTO datos = view.pedirDatosNuevoEstudiante();

            // Creamos la entidad respetando el ID original, pero con los datos nuevos
            Estudiante estudianteModificado = new Estudiante(id, datos.getDni(), datos.getNombre(), datos.getApellido());

            dao.modificar(estudianteModificado);
            Mostrar.Mensaje(Mensajes.EXITO_ACTUALIZAR);
        } catch (RuntimeException e) {
            Mostrar.Mensaje(Mensajes.ERROR_GUARDAR);
        }

    }

    private void eliminarEstudiante() {
        String id = view.pedirId();

        try {
            // 1. Primero verificamos si el estudiante existe usando el obtenerPorId
            Estudiante est = dao.obtenerPorId(id);

            if (est == null) {
                Mostrar.Mensaje(Mensajes.ERROR_ID);
            } else {
                // 2. Si existe, le decimos al DAO que intente eliminarlo.
                boolean eliminado = dao.eliminar(id);

                // 3. Solo si el DAO logró eliminarlo de verdad (true), mostramos el éxito
                if (eliminado) {
                    Mostrar.Mensaje(Mensajes.EXITO_ELIMINAR);
                } else {
                    // Si está en uso, imprimo mensaje Error
                    Mostrar.Mensaje(Mensajes.ERROR_ELIMINAR_EN_USO);
                }
            }
        } catch (RuntimeException e) {
             Mostrar.Mensaje(Mensajes.ERROR_ELIMINAR);
        }
    }
}
