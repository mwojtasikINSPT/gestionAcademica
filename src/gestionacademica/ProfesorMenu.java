package gestionacademica;

import controllers.ProfesorController;
import utils.Mensajes;
import views.ProfesorView;

public class ProfesorMenu {
    private final ProfesorController controller;
    private final ProfesorView view;

    public ProfesorMenu(ProfesorController controller, ProfesorView view) {
        this.controller = controller;
        this.view = view;
    }

    public void iniciar() {
        int opcion;
        do {
            opcion = view.mostrarMenu();
            switch (opcion) {
                case 1 -> controller.registrarProfesor();
                case 2 -> controller.mostrarTodos();
                case 3 -> controller.actualizarProfesor();
                case 4 -> controller.eliminarProfesor();
                case 0 -> view.mostrar(Mensajes.VOLVIENDO);
                default -> view.mostrar(Mensajes.OPCION_INVALIDA);
            }
        } while (opcion != 0);
    }
}
