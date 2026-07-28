package controllers;

import daos.AsignacionDAO;
import daos.AulaDAO;
import daos.InscripcionDAO;
import dtos.AulaDTO;
import models.Aula;
import java.util.ArrayList;
import java.util.List;
import utils.*;

//Controller: coordina la operación y decide qué mensaje corresponde.
public class AulaController {

    private final AsignacionDAO asignacionDAO = new AsignacionDAO();
    private final InscripcionDAO inscripcionDAO = new InscripcionDAO();
    private final AulaDAO dao;

    public AulaController(AulaDAO dao) {
        this.dao = dao;
    }

    public AulaDTO registrarAula(int capacidad) {
        try {
            List<Aula> listaActual = dao.obtenerRegistros();
            List<String> codigosActuales = new ArrayList<>();

            for (Aula a : listaActual) {
                codigosActuales.add(a.getCodigo());
            }

            String nuevoCodigo = Validaciones.generarSiguienteId(codigosActuales, "A");
            Aula nuevaAula = new Aula(nuevoCodigo, capacidad);

            dao.agregar(nuevaAula);
            return new AulaDTO(nuevaAula.getCodigo(), nuevaAula.getCapacidad());
        } catch (RuntimeException e) {
            return null;
        }
    }

    public List<AulaDTO> mostrarTodas() {
        try {
            // 1. Intento obtener los registros
            List<Aula> entidades = dao.obtenerRegistros();

            // 2. Si accede a registros,
            List<AulaDTO> dtos = new ArrayList<>();
            for (Aula a : entidades) {
                dtos.add(new AulaDTO(a.getCodigo(), a.getCapacidad()));
            }

            // 3.Devuelvo
            return dtos;
        } catch (RuntimeException e) {
            // 4. Si el DAO fallo
            return null;
        }
    }

    public boolean actualizarAula(String codigo, int capacidad) {
        try {
            Aula aulaExistente = dao.obtenerPorId(codigo);

            if (aulaExistente == null) {
                return false;
            }
            dao.modificar(new Aula(codigo, capacidad));
            return true;
        } catch (RuntimeException e) {
            return false;
        }
    }

    public boolean eliminarAula(String codigo) {
        try {
            // 1. Verificamos si el aula existe
            Aula aula = dao.obtenerPorId(codigo);

            if (aula == null) {
                return false;
            } 
            
            // 2. Lógica de negocio 
            boolean enUsoPorProfesor = asignacionDAO.obtenerRegistros().stream()
                    .anyMatch(a -> a.getCodigoAula().equals(codigo));

            boolean enUsoPorAlumnos = inscripcionDAO.obtenerRegistros().stream()
                    .anyMatch(i -> i.getCodigoAula().equals(codigo));

            if (enUsoPorProfesor || enUsoPorAlumnos) {
                return false;
            }

            // 3. Si existe y no está en uso, le damos la orden al DAO de eliminarla
            return dao.eliminar(codigo);
            
        } catch (RuntimeException e) {
            return false;
        }
    }
}
