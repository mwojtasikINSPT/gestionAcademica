package gestionacademica;

import controllers.InscripcionController;
import utils.Mensajes;
import views.InscripcionView;

public class InscripcionMenu {

// Declaro la vista y el controlador para que el menú sepa a quién llamar y cómo interactuar con el usuario.
    private final InscripcionView view;
    private final InscripcionController controller;

    // Instancio ambos por inyección de dependencias en el constructor para mantener la arquitectura desacoplada.
    public InscripcionMenu(InscripcionController controller, InscripcionView view) {
        this.controller = controller;
        this.view = view;
    }

    public void iniciar() {
        int opcion;
        do {
            opcion = view.mostrarMenu();
            switch (opcion) {
                case 1:
                    controller.registrarInscripcion();
                    break;
                case 2:
                    controller.mostrarTodas();
                    break;
                case 3:
                    controller.modificarInscripcion();
                    break;
                case 4:
                    controller.eliminarInscripcion();
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
