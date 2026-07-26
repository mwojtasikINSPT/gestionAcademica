package views;

import dtos.AsignacionDTO;
import utils.Validaciones;
import java.util.List;
import java.util.Scanner;

public class AsignacionView {
    private Scanner scanner = new Scanner(System.in);

    public int mostrarMenu() {
        String textoMenu = "\n--- GESTION DE ASIGNACIONES ---\n" +
                           "1. Asignar profesor a un aula\n" +
                           "2. Ver todas las asignaciones\n" +
                           "3. Eliminar asignacion\n" +
                           "0. Volver al menu principal";
        return Validaciones.mostrarMenu(textoMenu, scanner);
    }

    public String pedirIdAsignacion() {
        System.out.print("Ingrese el ID de la asignacion (ej. AS001): ");
        return scanner.nextLine().trim();
    }

    public AsignacionDTO pedirDatosNuevaAsignacion() {
        System.out.print("Ingrese el ID del Profesor: ");
        String idProfesor = scanner.nextLine().trim();
        
        System.out.print("Ingrese el Codigo del Aula: ");
        String codigoAula = scanner.nextLine().trim();
        
        return new AsignacionDTO(null, idProfesor, codigoAula);
    }

    public void mostrarAsignaciones(List<AsignacionDTO> asignaciones) {
        System.out.println("\n--- LISTA DE ASIGNACIONES ---");
        if (asignaciones.isEmpty()) {
            System.out.println("No hay asignaciones registradas.");
        } else {
            for (AsignacionDTO dto : asignaciones) {
                System.out.println(dto.toString());
            }
        }
    }

    public void mostrarMensaje(String mensaje) {
        System.out.println("-> " + mensaje);
    }
}