package daos;

import models.Profesor;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class ProfesorDAO implements ICrud<Profesor, String> {

    private final String ARCHIVO = "profesores.txt";

    @Override
    public List<Profesor> obtenerRegistros() {
        List<Profesor> profesores = new ArrayList<>();
        File file = new File(ARCHIVO);
        if (!file.exists()) {
            return profesores;
        }

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String linea;
            while ((linea = br.readLine()) != null) {
                String[] partes = linea.split(";");
                if (partes.length >= 4) {
                    Profesor prof = new Profesor(partes[0], partes[1], partes[2], partes[3]);
                    profesores.add(prof);
                }
            }
        } catch (IOException e) {
            throw new RuntimeException("Error al leer el archivo", e);
        }
        return profesores;
    }

    public void guardarTodos(List<Profesor> profesores) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(ARCHIVO))) {
            for (Profesor prof : profesores) {
                // Uso metodo toLineaArchivo() de la entidad Profesor
                bw.write(prof.getId() + ";" + prof.getDni() + ";" + prof.getNombre() + ";" + prof.getApellido());
                bw.newLine();
            }
        } catch (IOException e) {
            throw new RuntimeException("Error al guardar", e);
        }
    }

    @Override
    public void agregar(Profesor profesor) {
        List<Profesor> lista = obtenerRegistros();
        lista.add(profesor);
        guardarTodos(lista);
    }

    @Override
    public void modificar(Profesor profeModificado) {
        List<Profesor> lista = obtenerRegistros();
        for (int i = 0; i < lista.size(); i++) {
            if (lista.get(i).getId().equals(profeModificado.getId())) {
                lista.set(i, profeModificado);
                guardarTodos(lista);
                return; 
            }
        }
    }

    @Override
    public Profesor obtenerPorId(String id) {
        // Recorremos la lista de profesores
        for (Profesor profe : obtenerRegistros()) {
            // Si el ID coincide, devolvemos el objeto completo
            if (profe.getId().equals(id)) {
                return profe;
            }
        }
        // Si no lo encuentra, devuelve null
        return null;
    }

    @Override
    public boolean eliminar(String id) {
        // 1. Instanciamos el DAO que necesitamos consultar
        AsignacionDAO asignacionDAO = new AsignacionDAO();

        // 2. Chequeamos si el profesor está asignado a un aula
        boolean enUso = asignacionDAO.obtenerRegistros().stream()
                .anyMatch(a -> a.getIdProfesor().equals(id));

        if (enUso) {
            return false;
        }

        // 3. Verificamos si el profesor realmente existe 
        Profesor profeAEliminar = obtenerPorId(id);

        // 4. Si existe (no es null), procedemos a borrarlo
        if (profeAEliminar != null) {
            List<Profesor> lista = obtenerRegistros();

            // Removemos de la lista de forma limpia usando removeIf
            lista.removeIf(p -> p.getId().equals(id));

            guardarTodos(lista); // Guardamos los cambios en el archivo
            return true;         // Avisamos que la baja fue un éxito
        }

        // 5. Si no existía en la lista, devolvemos false
        return false;
    }

}
