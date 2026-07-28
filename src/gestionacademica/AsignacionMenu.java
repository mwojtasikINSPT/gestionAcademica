package gestionacademica;

import controllers.AsignacionController;
import utils.Mensajes;
import views.AsignacionView;

public class AsignacionMenu {

    // Declaro la vista y el controlador para que el menú sepa a quién llamar y cómo interactuar con el usuario.
    private final AsignacionView view;
    private final AsignacionController controller;

    // Instancio ambos por inyección de dependencias en el constructor
    public AsignacionMenu(AsignacionController controller, AsignacionView view) {
        this.controller = controller;
        this.view = view;
    }

    public void iniciar() {
        int opcion;
        do {
            opcion = view.mostrarMenu();
            switch (opcion) {
                case 1 -> controller.registrarAsignacion();
                case 2 -> controller.mostrarTodas();
                case 3 -> controller.actualizarAsignacion();
                case 4 -> controller.eliminarAsignacion();
                case 0 -> view.mostrar(Mensajes.VOLVIENDO);
                default -> view.mostrar(Mensajes.OPCION_INVALIDA);
            }
        } while (opcion != 0);
    }
}