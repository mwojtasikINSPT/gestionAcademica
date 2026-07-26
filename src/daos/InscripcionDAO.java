package daos;

import models.Inscripcion;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class InscripcionDAO {

    private final String ARCHIVO = "inscripciones.txt";

    public List<Inscripcion> obtenerTodas() {
        List<Inscripcion> inscripciones = new ArrayList<>();
        File file = new File(ARCHIVO);
        if (!file.exists()) {
            return inscripciones;
        }

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String linea;
            while ((linea = br.readLine()) != null) {
                String[] partes = linea.split(";");
                if (partes.length >= 3) {
                    inscripciones.add(new Inscripcion(partes[0], partes[1], partes[2]));
                }
            }
        } catch (IOException e) {
            System.out.println("Error al leer el archivo de inscripciones: " + e.getMessage());
        }
        return inscripciones;
    }

    public void guardarTodas(List<Inscripcion> inscripciones) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(ARCHIVO))) {
            for (Inscripcion ins : inscripciones) {
                bw.write(ins.getIdInscripcion() + ";" + ins.getIdEstudiante() + ";" + ins.getCodigoAula());
                bw.newLine();
            }
        } catch (IOException e) {
            System.out.println("Error al guardar inscripciones: " + e.getMessage());
        }
    }

    public void agregar(Inscripcion inscripcion) {
        List<Inscripcion> lista = obtenerTodas();
        lista.add(inscripcion);
        guardarTodas(lista);
    }

    public boolean eliminar(String id) {
        List<Inscripcion> lista = obtenerTodas();
        for (int i = 0; i < lista.size(); i++) {
            if (lista.get(i).getIdInscripcion().equals(id)) {
                lista.remove(i);
                guardarTodas(lista);
                return true;
            }
        }
        return false;
    }
}
