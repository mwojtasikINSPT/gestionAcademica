package views;

import dtos.InscripcionDTO;
import utils.Validaciones;
import java.util.List;
import java.util.Scanner;

public class InscripcionView {
    private Scanner scanner = new Scanner(System.in);

    public int mostrarMenu() {
        String textoMenu = "\n--- GESTION DE INSCRIPCIONES ---\n" +
                           "1. Asignar estudiante a un aula\n" +
                           "2. Ver todas las inscripciones\n" +
                           "3. Eliminar inscripcion\n" +
                           "0. Volver al menu principal";
        return Validaciones.mostrarMenu(textoMenu, scanner);
    }

    public String pedirIdInscripcion() {
        System.out.print("Ingrese el ID de la inscripcion (ej. I0001): ");
        return scanner.nextLine().trim();
    }

    public InscripcionDTO pedirDatosNuevaInscripcion() {
        System.out.print("Ingrese el ID del Estudiante: ");
        String idEstudiante = scanner.nextLine().trim();
        
        System.out.print("Ingrese el Codigo del Aula: ");
        String codigoAula = scanner.nextLine().trim();
        
        return new InscripcionDTO(null, idEstudiante, codigoAula);
    }

    public void mostrarInscripciones(List<InscripcionDTO> inscripciones) {
        System.out.println("\n--- LISTA DE INSCRIPCIONES ---");
        if (inscripciones.isEmpty()) {
            System.out.println("No hay inscripciones registradas.");
        } else {
            for (InscripcionDTO dto : inscripciones) {
                System.out.println(dto.toString());
            }
        }
    }

    public void mostrarMensaje(String mensaje) {
        System.out.println("-> " + mensaje);
    }
}