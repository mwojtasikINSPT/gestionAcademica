package controllers;

import daos.AulaDAO;
import dtos.AulaDTO;
import models.Aula;
import utils.Validaciones;
import views.AulaView;
import java.util.ArrayList;
import java.util.List;
import utils.Mensajes;

public class AulaController {

    private AulaDAO dao;
    private AulaView view;

    public AulaController(AulaDAO dao, AulaView view) {
        this.dao = dao;
        this.view = view;
    }

    public void iniciar() {
        int opcion;
        do {
            opcion = view.mostrarMenu();
            switch (opcion) {
                case 1:
                    registrarAula();
                    break;
                case 2:
                    mostrarTodas();
                    break;
                case 3:
                    actualizarAula();
                    break;
                case 4:
                    eliminarAula();
                    break;
                case 0:
                    view.mostrarMensaje(Mensajes.VOLVIENDO);
                    break;
                default:
                    view.mostrarMensaje(Mensajes.OPCION_INVALIDA);
            }
        } while (opcion != 0);
    }

    private void registrarAula() {
        AulaDTO datos = view.pedirDatosNuevaAula();
        List<Aula> listaActual = dao.obtenerRegistros();
        List<String> codigosActuales = new ArrayList<>();

        for (Aula a : listaActual) {
            codigosActuales.add(a.getCodigo());
        }

        String nuevoCodigo = Validaciones.generarSiguienteId(codigosActuales, "A");
        Aula nuevaAula = new Aula(nuevoCodigo, datos.capacidad);

        dao.agregar(nuevaAula);
        view.mostrarMensaje(Mensajes.EXITO_GUARDAR);
    }

    private void mostrarTodas() {
        List<Aula> entidades = dao.obtenerRegistros();
        List<AulaDTO> dtos = new ArrayList<>();
        for (Aula a : entidades) {
            dtos.add(new AulaDTO(a.getCodigo(), a.getCapacidad()));
        }
        view.mostrarAulas(dtos);
    }

    private void actualizarAula() {
        String codigo = view.pedirCodigo();
        List<Aula> listaActual = dao.obtenerRegistros();

        if (!Validaciones.existeAula(listaActual, codigo)) {
            view.mostrarMensaje(Mensajes.ERROR_ID);
            return;
        }

        view.mostrarMensaje("Ingrese los NUEVOS datos para el aula:");
        AulaDTO datos = view.pedirDatosNuevaAula();
        Aula aulaModificada = new Aula(codigo, datos.capacidad);

        dao.modificar(aulaModificada);
        view.mostrarMensaje(Mensajes.EXITO_ACTUALIZAR);

    }

    private void eliminarAula() {
        String codigo = view.pedirCodigo();
        if (dao.eliminar(codigo)) {
            view.mostrarMensaje(Mensajes.EXITO_ELIMINAR);
        } else {
            view.mostrarMensaje(Mensajes.ERROR_ID);
        }
    }
}
