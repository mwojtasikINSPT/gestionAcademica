package daos;

import models.Profesor;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class ProfesorDAO {
    private final String ARCHIVO = "profesores.txt";

    public List<Profesor> obtenerTodos() {
        List<Profesor> profesores = new ArrayList<>();
        File file = new File(ARCHIVO);
        if (!file.exists()) return profesores;

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
            System.out.println("Error al leer el archivo de profesores: " + e.getMessage());
        }
        return profesores;
    }

    public void guardarTodos(List<Profesor> profesores) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(ARCHIVO))) {
            for (Profesor prof : profesores) {
                // Asumiendo que le agregaste el metodo toLineaArchivo() a la entidad Profesor
                bw.write(prof.getId() + ";" + prof.getDni() + ";" + prof.getNombre() + ";" + prof.getApellido());
                bw.newLine();
            }
        } catch (IOException e) {
            System.out.println("Error al guardar profesores: " + e.getMessage());
        }
    }

    public void agregar(Profesor profesor) {
        List<Profesor> lista = obtenerTodos();
        lista.add(profesor);
        guardarTodos(lista);
    }

    public boolean actualizar(Profesor profeModificado) {
        List<Profesor> lista = obtenerTodos();
        for (int i = 0; i < lista.size(); i++) {
            if (lista.get(i).getId().equals(profeModificado.getId())) {
                lista.set(i, profeModificado);
                guardarTodos(lista);
                return true;
            }
        }
        return false;
    }

    public boolean eliminar(String id) {
        List<Profesor> lista = obtenerTodos();
        for (int i = 0; i < lista.size(); i++) {
            if (lista.get(i).getId().equals(id)) {
                lista.remove(i);
                guardarTodos(lista);
                return true;
            }
        }
        return false;
    }
}