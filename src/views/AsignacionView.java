package views;

import dtos.AsignacionDTO;
import utils.Validaciones;
import java.util.List;
import java.util.Scanner;

public class AsignacionView {

    private Scanner scanner = new Scanner(System.in);

    public int mostrarMenu() {
        String textoMenu = "\n--- GESTION DE ASIGNACIONES ---\n"
                + "1. Asignar profesor a un aula\n"
                + "2. Ver todas las asignaciones\n"
                + "3. Modificar asignacion\n"
                + "4. Eliminar asignacion\n"
                + "0. Volver al menu principal";
        return Validaciones.mostrarMenu(textoMenu, scanner);
    }

    public String pedirIdAsignacion() {
        System.out.print("Ingrese el ID de la asignacion (ej. AS001): ");
        String idAsignacion = scanner.nextLine();
        return Validaciones.normalizarTexto(idAsignacion);
    }

    public AsignacionDTO pedirDatosNuevaAsignacion() {
        System.out.print("Ingrese el ID del Profesor (ej. P0090): ");
        String idProfesor = Validaciones.normalizarTexto(scanner.nextLine());

        System.out.print("Ingrese el Codigo del Aula (ej. A0200): ");
        String codigoAula = Validaciones.normalizarTexto(scanner.nextLine());

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

    public String pedirNuevoCodigoAula() {
        System.out.print("Ingrese el codigo del aula (ej. A0926): ");
        String nuevoCodigo = scanner.nextLine();
        return Validaciones.normalizarTexto(nuevoCodigo);
    }

   
    
    public void mostrarMensaje(String mensaje) {
        System.out.println("-> " + mensaje);
    }
}
