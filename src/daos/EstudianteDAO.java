package daos;

import models.Estudiante;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class EstudianteDAO implements ICrud<Estudiante, String> {

    private final String ARCHIVO = "estudiantes.txt";

    // Aisla la logica del archivo. Si el archivo no existe, lo crea
    @Override
    public List<Estudiante> obtenerRegistros() {
        List<Estudiante> estudiantes = new ArrayList<>();
        File file = new File(ARCHIVO);

        if (!file.exists()) {
            return estudiantes;
        }

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
            throw new RuntimeException("Error al leer el archivo", e);
        }
        return estudiantes;
    }

    // Guarda la lista completa sobreescribiendo el archivo
    public void guardarTodos(List<Estudiante> estudiantes) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(ARCHIVO))) {
            for (Estudiante est : estudiantes) {
                bw.write(est.getId() + ";" + est.getDni() + ";" + est.getNombre() + ";" + est.getApellido());
                bw.newLine();
            }
        } catch (IOException e) {
            throw new RuntimeException("Error al guardar", e);
        }
    }

    // Agrega un estudiante nuevo y guarda
    @Override
    public void agregar(Estudiante estudiante) {
        List<Estudiante> lista = obtenerRegistros();
        lista.add(estudiante);
        guardarTodos(lista);
    }

    // Actualiza un estudiante existente y guarda los cambios
    @Override
    public void modificar(Estudiante estudianteModificado) {
        List<Estudiante> lista = obtenerRegistros();
        for (int i = 0; i < lista.size(); i++) {
            if (lista.get(i).getId().equals(estudianteModificado.getId())) {
                lista.set(i, estudianteModificado); // Reemplazamos el viejo por el nuevo
                guardarTodos(lista);
                return;
            }
        }
    }

    // Busca un estudiante por su ID
    @Override
    public Estudiante obtenerPorId(String id) {
        // Recorremos la lista de estudiantes
        for (Estudiante e : obtenerRegistros()) {
            // Si el ID coincide, devolvemos el objeto completo
            if (e.getId().equals(id)) {
                return e;
            }
        }
        // Si termina de buscar y no está, devuelve null
        return null;
    }

    // Elimina un estudiante por su ID
    @Override
    public boolean eliminar(String id) {
        List<Estudiante> lista = obtenerRegistros();
        // removeIf devuelve true si eliminó algo, false si no lo encontró
        boolean eliminado = lista.removeIf(e -> e.getId().equals(id));

        if (eliminado) {
            guardarTodos(lista);
        }
        return eliminado;
    }

}
