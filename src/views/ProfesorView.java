package views;

import dtos.ProfesorDTO;
import utils.Validaciones;
import java.util.List;
import java.util.Scanner;

public class ProfesorView {
    private Scanner scanner = new Scanner(System.in);

    public int mostrarMenu() {
        String textoMenu = "\n--- GESTION DE PROFESORES ---\n" +
                           "1. Registrar nuevo profesor\n" +
                           "2. Ver todos los profesores\n" +
                           "3. Actualizar profesor\n" +
                           "4. Eliminar profesor\n" +
                           "0. Salir";
        return Validaciones.mostrarMenu(textoMenu, scanner);
    }

    public String pedirId() {
        System.out.print("Ingrese el ID del profesor (ej. P0001): ");
        String idProfesor = scanner.nextLine();
        return Validaciones.normalizarTexto(idProfesor);
    }

    public ProfesorDTO pedirDatosNuevoProfesor() {
        String dni;
        do {
            System.out.print("DNI (exactamente 8 numeros): ");
            dni = scanner.nextLine().trim();
            if (!Validaciones.esDniValido(dni)) {
                mostrarMensaje("Error: El DNI ingresado no es valido.");
            }
        } while (!Validaciones.esDniValido(dni));
        
        String nombre;
        do {
            System.out.print("Nombre: ");
            nombre = scanner.nextLine().trim();
            if (!Validaciones.esTextoValido(nombre)) {
                mostrarMensaje("Error: El nombre no puede estar vacio.");
            }
        } while (!Validaciones.esTextoValido(nombre));
        
        String apellido;
        do {
            System.out.print("Apellido: ");
            apellido = scanner.nextLine().trim();
            if (!Validaciones.esTextoValido(apellido)) {
                mostrarMensaje("Error: El apellido no puede estar vacio.");
            }
        } while (!Validaciones.esTextoValido(apellido));
        
        return new ProfesorDTO(null, dni, nombre, apellido);
    }

    public void mostrarProfesores(List<ProfesorDTO> profesores) {
        System.out.println("\n--- LISTA DE PROFESORES ---");
        if (profesores.isEmpty()) {
            System.out.println("No hay profesores registrados.");
        } else {
            for (ProfesorDTO dto : profesores) {
                System.out.println(dto.toString());
            }
        }
    }

    public void mostrarMensaje(String mensaje) {
        System.out.println("-> " + mensaje);
    }
}