package views;

import dtos.EstudianteDTO;
import java.util.List;
import java.util.Scanner;
import utils.Validaciones;

// se encarga de imprimir y pedir datos. 
//No conoce a la clase Estudiante, solo maneja EstudianteDTO.
public class EstudianteView {

    private Scanner scanner = new Scanner(System.in);

    public int mostrarMenu() {
        String textoMenu = "\n--- GESTION DE ESTUDIANTES ---\n" +
                           "1. Registrar nuevo estudiante\n" +
                           "2. Ver todos los estudiantes\n" +
                           "3. Actualizar estudiante\n" +
                           "4. Eliminar estudiante\n" +
                           "0. Salir";
                           
        return Validaciones.mostrarMenu(textoMenu, scanner);
    }

    public String pedirId() {
        System.out.print("Ingrese el ID del estudiante (ej. E0001): ");
        String idEstudiante = scanner.nextLine();
        return Validaciones.normalizarTexto(idEstudiante);
    }

    public EstudianteDTO pedirDatosNuevoEstudiante() {
        String dni;
        do {
            System.out.print("DNI (exactamente 8 numeros): ");
            // El .trim() elimina cualquier espacio accidental al principio o al final
            dni = scanner.nextLine().trim();

            if (!Validaciones.esDniValido(dni)) {
                mostrarMensaje("Error: El DNI ingresado no es valido.");
            }
        } while (!Validaciones.esDniValido(dni));

        String nombre;
        do {
            System.out.print("Nombre: ");
            nombre = scanner.nextLine();

            if (!Validaciones.esTextoValido(nombre)) {
                mostrarMensaje("Error: El nombre no puede estar vacio.");
            }
        } while (!Validaciones.esTextoValido(nombre));

        String apellido;
        do {
            System.out.print("Apellido: ");
            apellido = scanner.nextLine();

            if (!Validaciones.esTextoValido(apellido)) {
                mostrarMensaje("Error: El apellido no puede estar vacio.");
            }
        } while (!Validaciones.esTextoValido(apellido));

        return new EstudianteDTO(null, dni, nombre, apellido);
    }

    public void mostrarEstudiantes(List<EstudianteDTO> estudiantes) {
        System.out.println("\n--- LISTA DE ESTUDIANTES ---");
        if (estudiantes.isEmpty()) {
            System.out.println("No hay estudiantes registrados.");
        } else {
            for (EstudianteDTO dto : estudiantes) {
                System.out.println(dto.toString());
            }
        }
    }

    public void mostrarMensaje(String mensaje) {
        System.out.println("-> " + mensaje);
    }
}
