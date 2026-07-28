package views;

import dtos.AulaDTO;
import utils.Validaciones;
import utils.Mostrar;
import java.util.List;
import java.util.Scanner;
import utils.Mensajes;

//Vista: es la encargada de mostrar mensajes y captar entradas.
public class AulaView {

    private final Scanner scanner = new Scanner(System.in);

    public int mostrarMenu() {
        String textoMenu = """

                --- GESTION DE AULAS ---
                1. Registrar nueva aula
                2. Ver todas las aulas
                3. Actualizar aula
                4. Eliminar aula
                0. Volver al menu principal""";
        return Mostrar.Menu(textoMenu, scanner);
    }

    public String pedirCodigo() {
        mostrar(Mensajes.PEDIR_DATO + "codigo del aula (ej. A0001): ");
        String codigoAula = scanner.nextLine();
        return Validaciones.normalizarTexto(codigoAula);
    }

    public int pedirCapacidad() {
        String capacidad;
        do {
            mostrar(Mensajes.PEDIR_DATO + "Capacidad (numero mayor a 0): ");
            capacidad = scanner.nextLine().trim();
            if (!Validaciones.esNumeroPositivo(capacidad)) {
                mostrar(Mensajes.ERROR_DATO);
            }
        } while (!Validaciones.esNumeroPositivo(capacidad));
        return Integer.parseInt(capacidad);
    }

    public AulaDTO pedirDatosNuevaAula() {

        int capacidad = pedirCapacidad();
        return new AulaDTO(null, capacidad);
    }

    public void mostrarAulas(List<AulaDTO> aulas) {
        Mostrar.Titulo("LISTA DE AULAS");
        if (aulas == null || aulas.isEmpty()) {
            mostrar(Mensajes.SIN_REGISTROS);
        } else {
            for (AulaDTO dto : aulas) {
                mostrar("Codigo: " + dto.getCodigo() + " | Capacidad: " + dto.getCapacidad() + " estudiantes");
            }
        }
    }

    // Imprime mensaje
    public void mostrar(String mensaje) {
        System.out.println("-> " + mensaje);
    }

}
