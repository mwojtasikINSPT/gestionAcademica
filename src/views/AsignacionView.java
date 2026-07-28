package views;

import dtos.AsignacionDTO;
import utils.Validaciones;
import java.util.List;
import java.util.Scanner;
import utils.Mensajes;
import utils.Mostrar;

public class AsignacionView {

    private final Scanner scanner = new Scanner(System.in);

    public int mostrarMenu() {
        String textoMenu = """
                           
                           --- GESTION DE ASIGNACIONES ---
                           1. Asignar profesor a un aula
                           2. Ver todas las asignaciones
                           3. Modificar asignacion
                           4. Eliminar asignacion
                           0. Volver al menu principal""";
        return Mostrar.Menu(textoMenu, scanner);
    }

    public String pedirIdAsignacion() {
        mostrar(Mensajes.PEDIR_DATO + " ID de la asignacion (ej. AS001): ");
        String idAsignacion = scanner.nextLine();
        return Validaciones.normalizarTexto(idAsignacion);
    }

    public AsignacionDTO pedirDatosNuevaAsignacion() {
        mostrar(Mensajes.PEDIR_DATO + "ID del Profesor (ej. P0090): ");
        String idProfesor = Validaciones.normalizarTexto(scanner.nextLine());

        mostrar(Mensajes.PEDIR_DATO + "Codigo del Aula (ej. A0200): ");
        String codigoAula = Validaciones.normalizarTexto(scanner.nextLine());

        return new AsignacionDTO(null, idProfesor, codigoAula);
    }

    public void mostrarAsignaciones(List<AsignacionDTO> asignaciones) {
        System.out.println("\n--- LISTA DE ASIGNACIONES ---");
        if (asignaciones.isEmpty()) {
            mostrar(Mensajes.SIN_REGISTROS);
        } else {
            for (AsignacionDTO dto : asignaciones) {
                mostrar("ID Asignación: " + dto.getIdAsignacion() + " | Profesor: "
                        + dto.getIdProfesor() + " | Aula: " + dto.getCodigoAula());
            }
        }
    }

    public String pedirNuevoCodigoAula() {
        Mostrar.Mensaje(Mensajes.PEDIR_DATO + "codigo del aula (ej. A0926): ");
        String nuevoCodigo = scanner.nextLine();
        return Validaciones.normalizarTexto(nuevoCodigo);
    }

    public void MostrarErrorNoEncontrado(String entidad, String id) {
        Mostrar.ErrorNoEncontrado(entidad, id);
    }

    public void mostrarErrorOcupado(String entidad, String id, String motivo) {
        Mostrar.ErrorOcupado(entidad, id, motivo);
    }

    // Imprime mensaje
    public void mostrar(String mensaje) {
        System.out.println("-> " + mensaje);
    }
}
