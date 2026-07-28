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
        String textoMenu = "\n--- GESTION DE PROFESORES ---\n" +
                           "1. Registrar nuevo profesor\n" +
                           "2. Ver todos los profesores\n" +
                           "3. Actualizar profesor\n" +
                           "4. Eliminar profesor\n" +
                           "0. Salir";
        return Mostrar.Menu(textoMenu, scanner);
    }

    public String pedirId() {
        Mostrar.Mensaje(Mensajes.PEDIR_DATO +" ID del profesor (ej. P0001): ");
        String idProfesor = scanner.nextLine();
        return Validaciones.normalizarTexto(idProfesor);
    }

    public ProfesorDTO pedirDatosNuevoProfesor() {
        String dni;
        do {
            Mostrar.Mensaje(Mensajes.PEDIR_DATO +"DNI (exactamente 8 numeros): ");
            dni = scanner.nextLine().trim();
            if (!Validaciones.esDniValido(dni)) {
                Mostrar.Mensaje(Mensajes.ERROR_DATO);
            }
        } while (!Validaciones.esDniValido(dni));
        
        String nombre;
        do {
            Mostrar.Mensaje(Mensajes.PEDIR_DATO +"Nombre: ");
            nombre = scanner.nextLine().trim();
            if (!Validaciones.esTextoValido(nombre)) {
                Mostrar.Mensaje(Mensajes.ERROR_DATO);
            }
        } while (!Validaciones.esTextoValido(nombre));
        
        String apellido;
        do {
            Mostrar.Mensaje(Mensajes.PEDIR_DATO +"Apellido: ");
            apellido = scanner.nextLine().trim();
            if (!Validaciones.esTextoValido(apellido)) {
                Mostrar.Mensaje(Mensajes.ERROR_DATO);
            }
        } while (!Validaciones.esTextoValido(apellido));
        
        return new ProfesorDTO(null, dni, nombre, apellido);
    }

    public void mostrarProfesores(List<ProfesorDTO> profesores) {
        System.out.println("\n--- LISTA DE PROFESORES ---");
        if (profesores.isEmpty()) {
            Mostrar.Mensaje(Mensajes.SIN_REGISTROS);
        } else {
            for (ProfesorDTO dto : profesores) {
                Mostrar.Mensaje(dto.toString());
            }
        }
    }

   
}