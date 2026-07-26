package utils;

import models.Aula;
import models.Estudiante;
import models.Profesor;
import java.util.List;
import java.util.Scanner;


public class Validaciones {

// Método para generar IDs alfanuméricos (ej: P0001, E0001). Static: pertenece a la clase    
    public static String generarSiguienteId(List<String> idsExistentes, String prefijo) {
        int maxId = 0;

        for (String id : idsExistentes) {
            if (id.startsWith(prefijo)) {
                try {
                    String numeroStr = id.substring(prefijo.length());
                    int numero = Integer.parseInt(numeroStr);

                    if (numero > maxId) {
                        maxId = numero;
                    }
                } catch (NumberFormatException e) {
                    // Ignora formatos corruptos
                }
            }
        }

        // "%04d" asegura que siempre haya al menos 4 dígitos, rellenando con ceros.
        // Ej: maxId 5 -> "P0006". maxId 1500 -> "P1501".
        return prefijo + String.format("%04d", maxId + 1);
    }

    // Valida si un estudiante existe en la lista usando su ID
    public static boolean existeEstudiante(List<Estudiante> listaEstudiantes, String idBuscado) {
        for (Estudiante est : listaEstudiantes) {
            // Si tu ID de estudiante quedó como int, cambia el .equals por un ==
            if (est.getId().equals(idBuscado)) {
                return true;
            }
        }
        return false;
    }

    // Valida si un aula existe en la lista usando su código
    public static boolean existeAula(List<Aula> listaAulas, String codigoBuscado) {
        for (Aula aula : listaAulas) {
            if (aula.getCodigo().equals(codigoBuscado)) {
                return true;
            }
        }
        return false;
    }

// Valida si un profesor existe en la lista usando su ID
    public static boolean existeProfesor(List<Profesor> listaProfesores, String idBuscado) {
        for (Profesor prof : listaProfesores) {
            if (prof.getId().equals(idBuscado)) {
                return true;
            }
        }
        return false;
    }
    
    // Valida que el DNI tenga exactamente 8 numeros
    public static boolean esDniValido(String dni) {
        if (dni == null) {
            return false;
        }
        // \\d significa "digito numerico", y {8} significa "exactamente 8 veces"
        return dni.matches("\\d{8}");
    }
    
    // Valida que el texto no este vacio ni compuesto solo por espacios
    public static boolean esTextoValido(String texto) {
        if (texto == null) {
            return false;
        }
        // trim() quita los espacios al principio y al final
        return !texto.trim().isEmpty();
    }
    
    // Imprime un menu multilinea y devuelve la opcion elegida
    public static int mostrarMenu(String mensajeMenu, Scanner scanner) {
        System.out.println(mensajeMenu);
        System.out.print("Opcion: ");
        int opcion = scanner.nextInt();
        scanner.nextLine(); // Limpiar el buffer del teclado
        return opcion;
    }
    
    // Valida que el texto ingresado sea un numero entero mayor a 0
    public static boolean esNumeroPositivo(String texto) {
        if (texto == null || texto.trim().isEmpty()) {
            return false;
        }
        try {
            int numero = Integer.parseInt(texto.trim());
            return numero > 0;
        } catch (NumberFormatException e) {
            return false; // Si tiene letras, cae aca y devuelve falso
        }
    }
    
    

}
