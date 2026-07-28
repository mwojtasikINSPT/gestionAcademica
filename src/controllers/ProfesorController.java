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

    public void registrarProfesor() {
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
            view.mostrar(Mensajes.EXITO_GUARDAR);
        } catch (RuntimeException e) {
            view.mostrar(Mensajes.ERROR_GUARDAR);
        }
    }

    public void mostrarTodos() {

        try {
            List<Profesor> entidades = dao.obtenerRegistros();
            List<ProfesorDTO> dtos = new ArrayList<>();
            for (Profesor p : entidades) {
                dtos.add(new ProfesorDTO(p.getId(), p.getDni(), p.getNombre(), p.getApellido()));
            }
            view.mostrarProfesores(dtos);
        } catch (RuntimeException e) {
            view.mostrar(Mensajes.ERROR_LECTURA);
        }
    }

    public void actualizarProfesor() {
        String id = view.pedirId();

        try {
            List<Profesor> listaActual = dao.obtenerRegistros();

            if (!Validaciones.existeProfesor(listaActual, id)) {
                Mostrar.Mensaje(Mensajes.ERROR_ID);
                return;
            }

            view.mostrar(Mensajes.PEDIR_NUEVOS_DATOS);
            ProfesorDTO datos = view.pedirDatosNuevoProfesor();
            Profesor profeModificado = new Profesor(id, datos.getDni(), datos.getNombre(), datos.getApellido());

            dao.modificar(profeModificado);
            view.mostrar(Mensajes.EXITO_ACTUALIZAR);
        } catch (RuntimeException e) {
            view.mostrar(Mensajes.ERROR_GUARDAR);
        }
    }

    public boolean eliminarProfesor() {
        String id = view.pedirId();

        try {
            // 1. Verificamos si el profesor existe
            Profesor profe = dao.obtenerPorId(id);

            if (profe == null) {
                view.mostrar(Mensajes.ERROR_ID);
                return false;
            }

            // 2. Verificamos si está en uso (Regla de negocio)
            daos.AsignacionDAO asignacionDAO = new daos.AsignacionDAO();
            boolean enUso = asignacionDAO.obtenerRegistros().stream()
                    .anyMatch(a -> a.getIdProfesor().equals(id));

            if (enUso) {
                view.mostrar(Mensajes.ERROR_ELIMINAR_EN_USO);
                return false;
            }

            // 3. Si existe y no está en uso, ejecutamos la eliminación
            boolean eliminado = dao.eliminar(id);

            if (eliminado) {
                view.mostrar(Mensajes.EXITO_ELIMINAR);
                return true;
            }

            // Si por algún motivo el DAO falla de forma silenciosa
            return false;

        } catch (RuntimeException e) {
            view.mostrar(Mensajes.ERROR_ELIMINAR);
            return false;
        }
    }
}
}
