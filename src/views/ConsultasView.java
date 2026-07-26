package views;

import utils.Validaciones;
import java.util.Scanner;

public class ConsultasView {

    private final Scanner scanner = new Scanner(System.in);

    public int mostrarMenuConsultas() {
        String textoMenu = "\n--- MENU DE CONSULTAS ---\n"
                + "1. Estudiante con su aula asignada\n"
                + "2. Estudiantes a cargo de un profesor\n"
                + "3. Profesor asignado a un estudiante\n"
                + "0. Volver al menu principal";
        return Validaciones.mostrarMenu(textoMenu, scanner);
    }

    public String pedirIdProfesor() {
        System.out.print("Ingrese el ID del profesor a consultar: ");
        return scanner.nextLine().trim();
    }

    public String pedirIdEstudiante() {
        System.out.print("Ingrese el ID del estudiante a consultar: ");
        return scanner.nextLine().trim();
    }

    public void mostrarResultado(String resultado) {
        System.out.println(resultado);
    }

    public void mostrarMensaje(String mensaje) {
        System.out.println("-> " + mensaje);
    }
}
