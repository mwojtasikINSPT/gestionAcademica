package daos;

// Aisla la logica del archivo. Si el archivo no existe, lo crea
import models.Estudiante;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class EstudianteDAO implements ICrud<Estudiante, String> {

    private final String ARCHIVO = "estudiantes.txt";

    // Carga los estudiantes del archivo a la memoria
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
        InscripcionDAO inscripcionDAO = new InscripcionDAO();
        // Chequeamos si el estudiante está inscripto en alguna materia/aula
        boolean enUso = inscripcionDAO.obtenerRegistros().stream()
                .anyMatch(i -> i.getIdEstudiante().equals(id));

        if (enUso) {
            System.out.println("ERROR: No se puede eliminar el estudiante porque esta inscripto en un aula.");
            return false;
        }
        //1. Busco x id
        Estudiante estudianteAEliminar = obtenerPorId(id);

        // 2. Si NO es null, significa que lo encontró y procedemos a borrarlo
        if (estudianteAEliminar != null) {
            List<Estudiante> lista = obtenerRegistros();

            // Forma rápida de borrarlo (removeIf busca el ID y lo quita automáticamente)
            lista.removeIf(e -> e.getId().equals(id));

            guardarTodos(lista); // Guardamos los cambios
            return true;         // Avisamos que fue un éxito
        }

        // 3. Si era null (no existía), directamente devolvemos false
        return false;
    }

}
