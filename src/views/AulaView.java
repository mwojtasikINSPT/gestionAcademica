package views;

import dtos.AulaDTO;
import utils.Validaciones;
import java.util.List;
import java.util.Scanner;

public class AulaView {
    private Scanner scanner = new Scanner(System.in);

    public int mostrarMenu() {
        String textoMenu = "\n--- GESTION DE AULAS ---\n" +
                           "1. Registrar nueva aula\n" +
                           "2. Ver todas las aulas\n" +
                           "3. Actualizar aula\n" +
                           "4. Eliminar aula\n" +
                           "0. Volver al menu principal";
        return Validaciones.mostrarMenu(textoMenu, scanner);
    }

    public String pedirCodigo() {
        System.out.print("Ingrese el codigo del aula (ej. A0001): ");
        String codigoAula = scanner.nextLine();
        return Validaciones.normalizarTexto(codigoAula);
    }

    public AulaDTO pedirDatosNuevaAula() {
        String capacidadStr;
        do {
            System.out.print("Capacidad (numero mayor a 0): ");
            capacidadStr = scanner.nextLine().trim();
            if (!Validaciones.esNumeroPositivo(capacidadStr)) {
                mostrarMensaje("Error: Ingrese un numero entero valido.");
            }
        } while (!Validaciones.esNumeroPositivo(capacidadStr));
        
        int capacidad = Integer.parseInt(capacidadStr);
        return new AulaDTO(null, capacidad);
    }

    public void mostrarAulas(List<AulaDTO> aulas) {
        System.out.println("\n--- LISTA DE AULAS ---");
        if (aulas.isEmpty()) {
            System.out.println("No hay aulas registradas.");
        } else {
            for (AulaDTO dto : aulas) {
                System.out.println(dto.toString());
            }
        }
    }

    public void mostrarMensaje(String mensaje) {
        System.out.println("-> " + mensaje);
    }
}