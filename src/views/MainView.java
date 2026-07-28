package views;

import utils.Mostrar;
import java.util.Scanner;

public class MainView {

    private Scanner scanner = new Scanner(System.in);

    public int mostrarMenuPrincipal() {
        String menu = "\n=== SISTEMA DE GESTION EDUCATIVA ===\n"
                + "1. Gestionar Estudiantes\n"
                + "2. Gestionar Profesores\n"
                + "3. Gestionar Aulas\n"
                + "4. Gestionar Inscripciones\n"
                + "5. Gestionar Asignaciones\n"
                + "6. Consultas\n"
                + "0. Salir del sistema";

        return Mostrar.Menu(menu, scanner);
    }

    public void mostrarMensaje(String mensaje) {
        System.out.println("-> " + mensaje);
    }
}
