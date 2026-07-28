package controllers;

import daos.AsignacionDAO;
import daos.AulaDAO;
import daos.InscripcionDAO;
import dtos.AulaDTO;
import models.Aula;
import java.util.ArrayList;
import java.util.List;
import utils.*;
import views.AulaView;

//Controller: coordina la operación y decide qué mensaje corresponde.
public class AulaController {

    private final AsignacionDAO asignacionDAO = new AsignacionDAO();
    private final InscripcionDAO inscripcionDAO = new InscripcionDAO();
    private final AulaDAO dao;
    private final AulaView view;

    public AulaController(AulaDAO dao, AulaView view) {
        this.dao = dao;
        this.view = view;
    }

    public void registrarAula() {
        try {
            // El controlador le pide el dato a la vista 
            int capacidad = view.pedirCapacidad();
            List<Aula> listaActual = dao.obtenerRegistros();

            List<String> codigosActuales = new ArrayList<>();

            for (Aula a : listaActual) {
                codigosActuales.add(a.getCodigo());
            }

            String nuevoCodigo = Validaciones.generarSiguienteId(codigosActuales, "A");
            Aula nuevaAula = new Aula(nuevoCodigo, capacidad);

            dao.agregar(nuevaAula);
            // Mostramos ok a través de la vista
            view.mostrar(Mensajes.EXITO_GUARDAR + " (Código: " + nuevaAula.getCodigo() + ")");
        } catch (RuntimeException e) {
            view.mostrar(Mensajes.ERROR_GUARDAR);
        }
    }

    public void mostrarTodas() {
        try {
            // 1. Intento obtener los registros
            List<Aula> entidades = dao.obtenerRegistros();

            // 2. Si accede a registros,
            List<AulaDTO> dtos = new ArrayList<>();
            for (Aula a : entidades) {
                dtos.add(new AulaDTO(a.getCodigo(), a.getCapacidad()));
            }
            // 3.Devuelvo (paso directamente a la vista)
            view.mostrarAulas(dtos);
        } catch (RuntimeException e) {
            // 4. Si el DAO fallo
            view.mostrar(Mensajes.ERROR_LECTURA);
        }
    }

    public void actualizarAula() {

        try {
            //Pido codigo
            String codigo = view.pedirCodigo();
            Aula aulaExistente = dao.obtenerPorId(codigo);

            if (aulaExistente == null) {
                view.mostrar(Mensajes.ERROR_ID);
                return;
            }

            // Si existe, pido capacidad a la vista
            int capacidad = view.pedirCapacidad();

            dao.modificar(new Aula(codigo, capacidad));
            view.mostrar(Mensajes.EXITO_ACTUALIZAR);
        } catch (RuntimeException e) {
            view.mostrar(Mensajes.ERROR_GUARDAR);
        }
    }

    public void eliminarAula() {
        try {
            String codigo = view.pedirCodigo();
            Aula aula = dao.obtenerPorId(codigo);

            if (aula == null) {
                view.mostrar(Mensajes.ERROR_ID);
                return; // Cortamos la ejecución acá
            }
            //Reviso si el aula tiene prof asignado o alumnos    
            boolean enUsoPorProfesor = asignacionDAO.obtenerRegistros().stream()
                    .anyMatch(a -> a.getCodigoAula().equals(codigo));

            boolean enUsoPorAlumnos = inscripcionDAO.obtenerRegistros().stream()
                    .anyMatch(i -> i.getCodigoAula().equals(codigo));

            if (enUsoPorProfesor || enUsoPorAlumnos) {
                view.mostrar(Mensajes.ERROR_ELIMINAR_EN_USO);
                return; // Cortamos la ejecución acá
            }

            boolean eliminado = dao.eliminar(codigo);
            if (eliminado) {
                view.mostrar(Mensajes.EXITO_ELIMINAR);
            }

        } catch (RuntimeException e) {
            view.mostrar(Mensajes.ERROR_ELIMINAR);
        }
    }
}
