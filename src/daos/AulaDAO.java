package daos;

import models.Aula;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class AulaDAO implements ICrud<Aula, String> {

    AsignacionDAO asignacionDAO = new AsignacionDAO();
    InscripcionDAO inscripcionDAO = new InscripcionDAO();

    private final String ARCHIVO = "aulas.txt";

    @Override
    public List<Aula> obtenerRegistros() {
        List<Aula> aulas = new ArrayList<>();
        File file = new File(ARCHIVO);
        if (!file.exists()) {
            return aulas;
        }

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String linea;
            while ((linea = br.readLine()) != null) {
                String[] partes = linea.split(";");
                if (partes.length >= 2) {
                    Aula aula = new Aula(partes[0], Integer.parseInt(partes[1]));
                    aulas.add(aula);
                }
            }
        } catch (IOException e) {
            System.out.println("Error al leer el archivo de aulas: " + e.getMessage());
        }
        return aulas;
    }

    public void guardarTodas(List<Aula> aulas) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(ARCHIVO))) {
            for (Aula aula : aulas) {
                bw.write(aula.getCodigo() + ";" + aula.getCapacidad());
                bw.newLine();
            }
        } catch (IOException e) {
            System.out.println("Error al guardar aulas: " + e.getMessage());
        }
    }

    @Override
    public void agregar(Aula aula) {
        List<Aula> lista = obtenerRegistros();
        lista.add(aula);
        guardarTodas(lista);
    }

    @Override
    public void modificar(Aula aulaModificada) {
        List<Aula> lista = obtenerRegistros();
        for (int i = 0; i < lista.size(); i++) {
            if (lista.get(i).getCodigo().equals(aulaModificada.getCodigo())) {
                lista.set(i, aulaModificada);
                guardarTodas(lista);
                return; // Corta la ejecución del método una vez que guarda
            }
        }
    }

    @Override
    public Aula obtenerPorId(String codigo) {
        return obtenerRegistros().stream()
                .filter(a -> a.getCodigo().equals(codigo))
                .findFirst()
                .orElse(null);
    }

   
    /* 
    public boolean eliminar(String codigo) {
        // 1. Verificamos si el aula existe usando tu método con Streams
        Aula aulaAEliminar = obtenerPorId(codigo);

        // 2. Si la encuentra (no es null), procedemos a borrarla
        if (aulaAEliminar != null) {
            List<Aula> lista = obtenerRegistros();

            // Removemos de la lista usando removeIf
            lista.removeIf(a -> a.getCodigo().equals(codigo));

            guardarTodas(lista); // Guardamos los cambios en el archivo
            return true;         // Avisamos que fue un éxito
        }

        // 3. Si no existía, devolvemos false
        return false;
    }
    */

    @Override
    public boolean eliminar(String codigo) {

    // 1. Chequeamos si el aula está en alguna Asignación (si tiene profesor)
    boolean enUsoPorProfesor = asignacionDAO.obtenerRegistros().stream()
            .anyMatch(a -> a.getCodigoAula().equals(codigo));

    // 2. Chequeamos si el aula está en alguna Inscripción (si tiene alumnos)
    boolean enUsoPorAlumnos = inscripcionDAO.obtenerRegistros().stream()
            .anyMatch(i -> i.getCodigoAula().equals(codigo));

    // 3. Si está en uso, informamos y abortamos devolviendo false
    if (enUsoPorProfesor || enUsoPorAlumnos) {
        System.out.println("ERROR: No se puede eliminar el aula porque tiene un profesor o alumnos asignados.");
        return false; 
    }

    // 4. Si el aula está "limpia", procedemos con la lógica de eliminación normal
    Aula aulaAEliminar = obtenerPorId(codigo);

    if (aulaAEliminar != null) {
        List<Aula> lista = obtenerRegistros();
        
        lista.removeIf(a -> a.getCodigo().equals(codigo));
        guardarTodas(lista); 
        
        return true; // Éxito al eliminar
    }

    // 5. Si no se encontró el aula original
    return false;
}   

}
