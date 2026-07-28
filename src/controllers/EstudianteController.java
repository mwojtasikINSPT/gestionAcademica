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

    private final EstudianteDAO dao;
    private final EstudianteView view;

    public EstudianteController(EstudianteDAO dao, EstudianteView view) {
        this.dao = dao;
        this.view = view;
    }

    public void registrarEstudiante() {

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
            view.mostrar(Mensajes.EXITO_GUARDAR);
        } catch (RuntimeException e) {
            view.mostrar(Mensajes.ERROR_GUARDAR);
        }
    }

    public void mostrarTodos() {

        try {
            // 1. Pedimos al DAO las entidades
            List<Estudiante> entidades = dao.obtenerRegistros();
            // 2. Las mapeamos a DTOs para mandarlas a la vista
            List<EstudianteDTO> dtos = new ArrayList<>();
            for (Estudiante e : entidades) {
                dtos.add(new EstudianteDTO(e.getId(), e.getDni(), e.getNombre(), e.getApellido()));
            }
            // 3. La vista los muestra
            view.mostrarEstudiantes(dtos);
        } catch (RuntimeException e) {
            view.mostrar(Mensajes.ERROR_LECTURA);
        }
    }

    public void actualizarEstudiante() {

        String id = view.pedirId();

        try {
            List<Estudiante> listaActual = dao.obtenerRegistros();

            if (!Validaciones.existeEstudiante(listaActual, id)) {
                view.mostrar(Mensajes.ERROR_ID);
                return;
            }

            view.mostrar(Mensajes.PEDIR_NUEVOS_DATOS);

            EstudianteDTO datos = view.pedirDatosNuevoEstudiante();
            // Creamos la entidad respetando el ID original, pero con los datos nuevos
            Estudiante estudianteModificado = new Estudiante(id, datos.getDni(), datos.getNombre(), datos.getApellido());

            dao.modificar(estudianteModificado);
            view.mostrar(Mensajes.EXITO_ACTUALIZAR);
        } catch (RuntimeException e) {
            view.mostrar(Mensajes.ERROR_GUARDAR);
        }

    }

    public boolean eliminarEstudiante() {
        String id = view.pedirId();

        try {
            // 1. Primero verificamos si el estudiante existe usando el obtenerPorId
            Estudiante est = dao.obtenerPorId(id);

            if (est == null) {
                view.mostrar(Mensajes.ERROR_ID);
                return false;
            }

            // 2. Regla de negocio: Chequeamos si el estudiante está inscripto en un aula
            daos.InscripcionDAO inscripcionDAO = new daos.InscripcionDAO();
            boolean enUso = inscripcionDAO.obtenerRegistros().stream()
                    .anyMatch(i -> i.getIdEstudiante().equals(id));

            if (enUso) {
                view.mostrar(Mensajes.ERROR_ELIMINAR_EN_USO);
                return false;
            }
            // 3. Si existe y no está en uso, eliminamos
            boolean eliminado = dao.eliminar(id);

            if (eliminado) {
                view.mostrar(Mensajes.EXITO_ELIMINAR);
                return true;
            }
            return false;
        } catch (RuntimeException e) {
            view.mostrar(Mensajes.ERROR_ELIMINAR);
            return false;
        }
    }
}
