package gestionacademica;

import controllers.EstudianteController;
import utils.Mensajes;
import views.EstudianteView;

public class EstudianteMenu {
    
    private final EstudianteController controller;
    private final EstudianteView view;

    public EstudianteMenu(EstudianteController controller, EstudianteView view) {
        this.controller = controller;
        this.view = view;
    }   
    
    public void iniciar() {
        int opcion;
        do {
            opcion = view.mostrarMenu();
            switch (opcion) {
                case 1 ->
                    controller.registrarEstudiante();
                case 2 ->
                    controller.mostrarTodos();
                case 3 ->
                    controller.actualizarEstudiante();
                case 4 ->
                    controller.eliminarEstudiante();
                case 0 ->
                    view.mostrar(Mensajes.VOLVIENDO);
                default ->
                    view.mostrar(Mensajes.OPCION_INVALIDA);
            }
        } while (opcion != 0);
    }
}