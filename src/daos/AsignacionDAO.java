package daos;

import models.Asignacion;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class AsignacionDAO {
    private final String ARCHIVO = "asignaciones.txt";

    public List<Asignacion> obtenerTodas() {
        List<Asignacion> asignaciones = new ArrayList<>();
        File file = new File(ARCHIVO);
        if (!file.exists()) return asignaciones;

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String linea;
            while ((linea = br.readLine()) != null) {
                String[] partes = linea.split(";");
                if (partes.length >= 3) {
                    asignaciones.add(new Asignacion(partes[0], partes[1], partes[2]));
                }
            }
        } catch (IOException e) {
            System.out.println("Error al leer asignaciones: " + e.getMessage());
        }
        return asignaciones;
    }

    public void guardarTodas(List<Asignacion> asignaciones) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(ARCHIVO))) {
            for (Asignacion asig : asignaciones) {
                bw.write(asig.getIdAsignacion() + ";" + asig.getIdProfesor() + ";" + asig.getCodigoAula());
                bw.newLine();
            }
        } catch (IOException e) {
            System.out.println("Error al guardar asignaciones: " + e.getMessage());
        }
    }

    public void agregar(Asignacion asignacion) {
        List<Asignacion> lista = obtenerTodas();
        lista.add(asignacion);
        guardarTodas(lista);
    }

    public boolean eliminar(String id) {
        List<Asignacion> lista = obtenerTodas();
        for (int i = 0; i < lista.size(); i++) {
            if (lista.get(i).getIdAsignacion().equals(id)) {
                lista.remove(i);
                guardarTodas(lista);
                return true;
            }
        }
        return false;
    }
}