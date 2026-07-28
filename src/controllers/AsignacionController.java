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
import utils.Mostrar;

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
                    Mostrar.Mensaje(Mensajes.VOLVIENDO);
                    break;
                default:
                    Mostrar.Mensaje(Mensajes.OPCION_INVALIDA);
            }
        } while (opcion != 0);
    }

    private void registrarAsignacion() {
        AsignacionDTO datos = view.pedirDatosNuevaAsignacion();

        try {
            // 1. Validar que el profesor exista
            if (!Validaciones.existeProfesor(profesorDAO.obtenerRegistros(), datos.getIdProfesor())) {
                view.MostrarErrorNoEncontrado("El profesor", datos.getIdProfesor());
                return;
            }

            // 2. Validar que el aula exista
            if (!Validaciones.existeAula(aulaDAO.obtenerRegistros(), datos.getCodigoAula())) {
                view.MostrarErrorNoEncontrado("El aula ", datos.getCodigoAula());
                return;
            }

            // 3. Obtenemos la lista actual de asignaciones de una sola vez
            List<Asignacion> listaActual = dao.obtenerRegistros();

            // 4. Validar si el profesor ya tiene un aula asignada
            if (Validaciones.profesorTieneAula(listaActual, datos.getIdProfesor())) {
                view.mostrarErrorOcupado("El profesor ", datos.getIdProfesor(), " ya tiene un aula asignada.");
                return;
            }

            // 5. Validar si el aula ya tiene un profesor asignado
            if (Validaciones.aulaOcupada(listaActual, datos.getCodigoAula())) {
                view.mostrarErrorOcupado("El aula ", datos.getCodigoAula(), " se encuentra ocupada por otro profesor.");
                return;
            }

            // 6. Generar ID y guardar (Usamos "AS" para diferenciar de las Aulas que usan "A")
            List<String> idsActuales = new ArrayList<>();
            for (Asignacion a : listaActual) {
                idsActuales.add(a.getIdAsignacion());
            }

            String nuevoId = Validaciones.generarSiguienteId(idsActuales, "AS");
            Asignacion nuevaAsignacion = new Asignacion(nuevoId, datos.getIdProfesor(), datos.getCodigoAula());

            dao.agregar(nuevaAsignacion);
            Mostrar.Mensaje(Mensajes.EXITO_GUARDAR + " (ID: " + nuevoId + ")");
        } catch (RuntimeException e) {
            Mostrar.Mensaje(Mensajes.ERROR_GUARDAR);
        }
    }

    private void mostrarTodas() {

        try {
            List<Asignacion> entidades = dao.obtenerRegistros();
            List<AsignacionDTO> dtos = new ArrayList<>();
            for (Asignacion a : entidades) {
                dtos.add(new AsignacionDTO(a.getIdAsignacion(), a.getIdProfesor(), a.getCodigoAula()));
            }
            view.mostrarAsignaciones(dtos);
        } catch (RuntimeException e) {
            Mostrar.Mensaje(Mensajes.ERROR_LECTURA);
        }
    }

    private void eliminarAsignacion() {
        String id = view.pedirIdAsignacion();

        try {
            if (dao.eliminar(id)) {
                Mostrar.Mensaje(Mensajes.EXITO_ELIMINAR);
            } else {
                Mostrar.Mensaje(Mensajes.ERROR_NO_ENCONTRADO);
            }
        } catch (RuntimeException e) {
            Mostrar.Mensaje(Mensajes.ERROR_ELIMINAR);
        }
    }

    private void modificarAsignacion() {
        // 1. Pedir el ID de la asignación que queremos modificar
        String idAsignacion = view.pedirIdAsignacion();
        // 2. Pedir el código de la nueva aula 
        String nuevoCodigoAula = view.pedirNuevoCodigoAula();

        try {
            // 3. Buscar la asignación actual
            Asignacion asignacionActual = dao.obtenerPorId(idAsignacion);
            for (Asignacion a : dao.obtenerRegistros()) {
                if (asignacionActual == null) {
                    Mostrar.ErrorNoEncontrado("La asignacion ", idAsignacion);
                    return;
                }
            }
            // 4. Validar que no lo estemos mandando a la misma aula donde ya está
            if (asignacionActual.getCodigoAula().equals(nuevoCodigoAula)) {
                Mostrar.Mensaje(Mensajes.ERROR_SIN_CAMBIOS);
                return;
            }

            // 5. Validar que la NUEVA aula exista
            Aula nuevaAula = aulaDAO.obtenerPorId(nuevoCodigoAula);
            if (nuevaAula == null) {
                Mostrar.Mensaje(Mensajes.ERROR_NO_ENCONTRADO);
                return;
            }

            // 6. Validar que la NUEVA aula no esté ocupada por otro profesor
            List<Asignacion> listaActual = dao.obtenerRegistros();
            if (Validaciones.aulaOcupada(listaActual, nuevoCodigoAula)) {
                Mostrar.ErrorOcupado("El aula ", nuevoCodigoAula, " ya tiene un profesor asignado.");
                return;
            }

            // 7. Aplicar el cambio y guardar
            asignacionActual.setCodigoAula(nuevoCodigoAula);

            // Al igual que en inscripciones, vas a necesitar un método actualizar en tu DAO de Asignaciones
            dao.modificar(asignacionActual);

            Mostrar.Mensaje(Mensajes.EXITO_REASIGNAR + nuevoCodigoAula);
        } catch (RuntimeException e) {
            Mostrar.Mensaje(Mensajes.ERROR_ACTUALIZAR);
        }
    }
}
