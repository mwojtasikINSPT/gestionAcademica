package gestionacademica;

import controllers.AulaController;
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
                    controller.registrarAula();
                    break;
                case 2:
                    controller.mostrarTodas();
                    break;
                case 3:
                    controller.actualizarAula();
                    break;
                case 4:
                    controller.eliminarAula();
                    break;
                case 0:
                    view.mostrar(Mensajes.VOLVIENDO);
                    break;
                default:
                    view.mostrar(Mensajes.OPCION_INVALIDA);
            }
        } while (opcion != 0);
    }
}