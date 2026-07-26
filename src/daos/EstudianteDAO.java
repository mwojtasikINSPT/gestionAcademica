package daos;

// Aisla la logica del archivo. Si el archivo no existe, lo crea
import models.Estudiante;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class EstudianteDAO {
    private final String ARCHIVO = "estudiantes.txt";

    // Carga los estudiantes del archivo a la memoria
    public List<Estudiante> obtenerTodos() {
        List<Estudiante> estudiantes = new ArrayList<>();
        File file = new File(ARCHIVO);
        
        if (!file.exists()) return estudiantes;

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String linea;
            while ((linea = br.readLine()) != null) {
                String[] partes = linea.split(";");
                if (partes.length == 4) {
                    Estudiante est = new Estudiante(partes[0], partes[1], partes[2], partes[3]);
                    estudiantes.add(est);
                }
            }
        } catch (IOException e) {
            System.out.println("Error al leer el archivo: " + e.getMessage());
        }
        return estudiantes;
    }

    // Guarda la lista completa sobreescribiendo el archivo
    public void guardarTodos(List<Estudiante> estudiantes) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(ARCHIVO))) {
            for (Estudiante est : estudiantes) {
                bw.write(est.toLineaArchivo());
                bw.newLine();
            }
        } catch (IOException e) {
            System.out.println("Error al guardar: " + e.getMessage());
        }
    }

    // Agrega un estudiante nuevo y guarda
    public void agregar(Estudiante estudiante) {
        List<Estudiante> lista = obtenerTodos();
        lista.add(estudiante);
        guardarTodos(lista);
    }
    
    // Actualiza un estudiante existente y guarda los cambios
    public boolean actualizar(Estudiante estudianteModificado) {
        List<Estudiante> lista = obtenerTodos();
        for (int i = 0; i < lista.size(); i++) {
            if (lista.get(i).getId().equals(estudianteModificado.getId())) {
                lista.set(i, estudianteModificado); // Reemplazamos el viejo por el nuevo
                guardarTodos(lista);
                return true;
            }
        }
        return false;
    }

    // Elimina un estudiante por su ID
    public boolean eliminar(String id) {
        List<Estudiante> lista = obtenerTodos();
        for (int i = 0; i < lista.size(); i++) {
            if (lista.get(i).getId().equals(id)) {
                lista.remove(i); // Lo quitamos de la lista
                guardarTodos(lista);
                return true;
            }
        }
        return false;
    }
}
