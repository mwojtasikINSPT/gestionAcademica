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
    public void modificar(Profesor profModificado) {
        List<Profesor> lista = obtenerRegistros();
        for (int i = 0; i < lista.size(); i++) {
            if (lista.get(i).getId().equals(profModificado.getId())) {
                lista.set(i, profModificado);
                guardarTodos(lista);
                return;
            }
        }
    }

    @Override
    public Profesor obtenerPorId(String id) {
        // Recorremos la lista de profesores
        for (Profesor profesor : obtenerRegistros()) {
            // Si el ID coincide, devolvemos el objeto completo
            if (profesor.getId().equals(id)) {
                return profesor;
            }
        }
        // Si no lo encuentra, devuelve null
        return null;
    }

    @Override
    public boolean eliminar(String id) {
        List<Profesor> lista = obtenerRegistros();

        // removeIf devuelve true si encontró el ID y lo borró, false si no existía
        boolean eliminado = lista.removeIf(p -> p.getId().equals(id));

        if (eliminado) {
            guardarTodos(lista); // Solo guardamos si realmente se borró algo
        }
        return eliminado;
    }
}
