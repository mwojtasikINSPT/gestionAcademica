package views;

import dtos.InscripcionDTO;
import utils.Validaciones;
import java.util.List;
import java.util.Scanner;
import utils.Mensajes;
import utils.Mostrar;

public class InscripcionView {

    private final Scanner scanner = new Scanner(System.in);

    public int mostrarMenu() {
        String textoMenu = """
                           
                           --- GESTION DE INSCRIPCIONES ---
                           1. Asignar estudiante a un aula
                           2. Ver todas las inscripciones
                           3. Modificar inscripcion
                           4. Eliminar inscripcion
                           0. Volver al menu principal""";
        return Mostrar.Menu(textoMenu, scanner);
    }

    public String pedirIdInscripcion() {
        mostrar(Mensajes.PEDIR_DATO + " ID de la inscripcion (ej. I0001): ");
        String idInscripcion = scanner.nextLine();
        return Validaciones.normalizarTexto(idInscripcion);
    }

    public InscripcionDTO pedirDatosNuevaInscripcion() {
        mostrar(Mensajes.PEDIR_DATO + " ID del Estudiante: ");
        String idEstudiante = scanner.nextLine();
        idEstudiante = Validaciones.normalizarTexto(idEstudiante);

        mostrar(Mensajes.PEDIR_DATO + " Codigo del Aula: ");
        String codigoAula = scanner.nextLine();
        codigoAula = Validaciones.normalizarTexto(codigoAula);

        return new InscripcionDTO(null, idEstudiante, codigoAula);
    }

    public void mostrarInscripciones(List<InscripcionDTO> inscripciones) {
        Mostrar.Titulo("LISTA DE INSCRIPCIONES");
        if (inscripciones.isEmpty()) {
            mostrar(Mensajes.SIN_REGISTROS);
        } else {
            for (InscripcionDTO dto : inscripciones) {
                mostrar("ID Inscripción: " + dto.getIdInscripcion() + " | Estudiante: "
                        + dto.getIdEstudiante() + " | Aula: " + dto.getCodigoAula());
            }
        }
    }

    public void mostrar(String mensaje) {
        System.out.println("-> " + mensaje);
    }

    public String pedirNuevoCodigoAula() {
        mostrar(Mensajes.PEDIR_DATO + " codigo de la nueva aula (ej. A0926): ");
        String nuevoCodigo = scanner.nextLine();
        return Validaciones.normalizarTexto(nuevoCodigo);
    }
}
