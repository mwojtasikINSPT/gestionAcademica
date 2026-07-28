package views;

import dtos.ProfesorDTO;
import utils.Validaciones;
import java.util.List;
import java.util.Scanner;
import utils.Mensajes;
import utils.Mostrar;

public class ProfesorView {

    private final Scanner scanner = new Scanner(System.in);

    public int mostrarMenu() {
        String textoMenu = "\n--- GESTION DE PROFESORES ---\n"
                + "1. Registrar nuevo profesor\n"
                + "2. Ver todos los profesores\n"
                + "3. Actualizar profesor\n"
                + "4. Eliminar profesor\n"
                + "0. Salir";
        return Mostrar.Menu(textoMenu, scanner);
    }

    public String pedirId() {
        mostrar(Mensajes.PEDIR_DATO + " ID del profesor (ej. P0001): ");
        String idProfesor = scanner.nextLine();
        return Validaciones.normalizarTexto(idProfesor);
    }

    public ProfesorDTO pedirDatosNuevoProfesor() {
        String dni;
        do {
            mostrar(Mensajes.PEDIR_DATO + "DNI (exactamente 8 numeros): ");
            dni = scanner.nextLine().trim();
            if (!Validaciones.esDniValido(dni)) {
                mostrar(Mensajes.ERROR_DATO);
            }
        } while (!Validaciones.esDniValido(dni));

        String nombre;
        do {
            mostrar(Mensajes.PEDIR_DATO + "Nombre: ");
            nombre = scanner.nextLine().trim();
            if (!Validaciones.esTextoValido(nombre)) {
                mostrar(Mensajes.ERROR_DATO);
            }
        } while (!Validaciones.esTextoValido(nombre));

        String apellido;
        do {
            mostrar(Mensajes.PEDIR_DATO + "Apellido: ");
            apellido = scanner.nextLine().trim();
            if (!Validaciones.esTextoValido(apellido)) {
                mostrar(Mensajes.ERROR_DATO);
            }
        } while (!Validaciones.esTextoValido(apellido));

        return new ProfesorDTO(null, dni, nombre, apellido);
    }

    public void mostrarProfesores(List<ProfesorDTO> profesores) {
        Mostrar.Titulo("LISTA DE PROFESORES");
        if (profesores.isEmpty()) {
            mostrar(Mensajes.SIN_REGISTROS);
        } else {
            for (ProfesorDTO dto : profesores) {
                // La Vista decide cómo se ve, no el DTO
                mostrar("ID: " + dto.getId() + " | DNI: " + dto.getDni() + " | Nombre: " + dto.getNombre() + " " + dto.getApellido());
            }
        }
    }

    // Este es el único punto de salida a la consola (además de los menús fijos)
    public void mostrar(String mensaje) {
        System.out.println("-> " + mensaje);
    }
}