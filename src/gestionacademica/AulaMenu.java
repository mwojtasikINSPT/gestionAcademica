package gestionacademica;

import controllers.AulaController;
import dtos.AulaDTO;
import utils.Mensajes;
import views.AulaView;

public class AulaMenu {
    private final AulaView view;
    private final AulaController controller;

    public AulaMenu(AulaController controller, AulaView view) {
        this.controller = controller;
        this.view = view;
    }

    public void iniciar() {
        int opcion;
        do {
            opcion = view.mostrarMenu();
            switch (opcion) {
                case 1:
                    registrar();
                    break;
                case 2:
                    listar();
                    break;
                case 3:
                    actualizar();
                    break;
                case 4:
                    eliminar();
                    break;
                case 0:
                    view.mostrar(Mensajes.VOLVIENDO);
                    break;
                default:
                    view.mostrar(Mensajes.OPCION_INVALIDA);
            }
        } while (opcion != 0);
    }

    private void registrar() {
        AulaDTO aula = controller.registrarAula(view.pedirCapacidad());
        if (aula != null) {
            view.mostrar("Aula registrada: " + aula.getCodigo());
        } else {
            view.mostrar(Mensajes.ERROR_GUARDAR);
        }
    }

    private void listar() {
        view.mostrarAulas(controller.mostrarTodas());
    }

    private void actualizar() {
        boolean ok = controller.actualizarAula(view.pedirCodigo(), view.pedirCapacidad());
        view.mostrar(ok ? Mensajes.EXITO_ACTUALIZAR : Mensajes.ERROR_ID);
    }

    private void eliminar() {
        boolean ok = controller.eliminarAula(view.pedirCodigo());
        view.mostrar(ok ? Mensajes.EXITO_ELIMINAR : Mensajes.ERROR_ID);
    }
}
