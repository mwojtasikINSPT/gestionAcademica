package views;

import dtos.EstudianteDTO;
import java.util.List;
import java.util.Scanner;
import utils.Mensajes;
import utils.Mostrar;
import utils.Validaciones;

// se encarga de imprimir y pedir datos. 
//No conoce a la clase Estudiante, solo maneja EstudianteDTO.
public class EstudianteView {

    private final Scanner scanner = new Scanner(System.in);

    public int mostrarMenu() {
        String textoMenu = """
                           
                           --- GESTION DE ESTUDIANTES ---
                           1. Registrar nuevo estudiante
                           2. Ver todos los estudiantes
                           3. Actualizar estudiante
                           4. Eliminar estudiante
                           0. Salir""";

        return Mostrar.Menu(textoMenu, scanner);
    }

    public String pedirId() {
        mostrar(Mensajes.PEDIR_DATO + "ID del estudiante (ej. E0001): ");
        String idEstudiante = scanner.nextLine();
        return Validaciones.normalizarTexto(idEstudiante);
    }

    public EstudianteDTO pedirDatosNuevoEstudiante() {
        String dni;
        do {
            mostrar(Mensajes.PEDIR_DATO + "DNI (exactamente 8 numeros): ");
            // El .trim() elimina cualquier espacio accidental al principio o al final
            dni = scanner.nextLine().trim();

            if (!Validaciones.esDniValido(dni)) {
                mostrar(Mensajes.ERROR_DATO);
            }
        } while (!Validaciones.esDniValido(dni));

        String nombre;
        do {
            mostrar(Mensajes.PEDIR_DATO + "Nombre: ");
            nombre = scanner.nextLine();

            if (!Validaciones.esTextoValido(nombre)) {
                mostrar(Mensajes.ERROR_DATO);
            }
        } while (!Validaciones.esTextoValido(nombre));

        String apellido;
        do {
            mostrar(Mensajes.PEDIR_DATO + "Apellido: ");
            apellido = scanner.nextLine();

            if (!Validaciones.esTextoValido(apellido)) {
                mostrar(Mensajes.ERROR_DATO);
            }
        } while (!Validaciones.esTextoValido(apellido));

        return new EstudianteDTO(null, dni, nombre, apellido);
    }

    public void mostrarEstudiantes(List<EstudianteDTO> estudiantes) {
        Mostrar.Titulo("LISTA DE ESTUDIANTES");
        if (estudiantes.isEmpty()) {
            mostrar(Mensajes.SIN_REGISTROS);
        } else {
            for (EstudianteDTO dto : estudiantes) {
                mostrar("ID: " + dto.getId() + " | DNI: " + dto.getDni()
                        + " | Nombre: " + dto.getNombre() + " " + dto.getApellido());
            }
        }
    }

    // Imprime mensaje
    public void mostrar(String mensaje) {
        System.out.println("-> " + mensaje);
    }

}
