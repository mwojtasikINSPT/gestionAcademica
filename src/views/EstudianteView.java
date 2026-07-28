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
        Mostrar.Mensaje(Mensajes.PEDIR_DATO + "ID del estudiante (ej. E0001): ");
        String idEstudiante = scanner.nextLine();
        return Validaciones.normalizarTexto(idEstudiante);
    }

    public EstudianteDTO pedirDatosNuevoEstudiante() {
        String dni;
        do {
            Mostrar.Mensaje(Mensajes.PEDIR_DATO + "DNI (exactamente 8 numeros): ");
            // El .trim() elimina cualquier espacio accidental al principio o al final
            dni = scanner.nextLine().trim();

            if (!Validaciones.esDniValido(dni)) {
                 Mostrar.Mensaje(Mensajes.ERROR_DATO);
            }
        } while (!Validaciones.esDniValido(dni));

        String nombre;
        do {
            Mostrar.Mensaje(Mensajes.PEDIR_DATO + "Nombre: ");
            nombre = scanner.nextLine();

            if (!Validaciones.esTextoValido(nombre)) {
                 Mostrar.Mensaje(Mensajes.ERROR_DATO);
            }
        } while (!Validaciones.esTextoValido(nombre));

        String apellido;
        do {
            Mostrar.Mensaje(Mensajes.PEDIR_DATO + "Apellido: ");
            apellido = scanner.nextLine();

            if (!Validaciones.esTextoValido(apellido)) {
                Mostrar.Mensaje(Mensajes.ERROR_DATO);
            }
        } while (!Validaciones.esTextoValido(apellido));

        return new EstudianteDTO(null, dni, nombre, apellido);
    }

    public void mostrarEstudiantes(List<EstudianteDTO> estudiantes) {
        System.out.println("\n--- LISTA DE ESTUDIANTES ---");
        if (estudiantes.isEmpty()) {
            Mostrar.Mensaje(Mensajes.SIN_REGISTROS);
        } else {
            for (EstudianteDTO dto : estudiantes) {
                System.out.println(dto.toString());
            }
        }
    }


}
