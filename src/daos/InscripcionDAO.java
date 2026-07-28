package daos;

import models.Inscripcion;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class InscripcionDAO implements ICrud<Inscripcion, String> {

    private final String ARCHIVO = "inscripciones.txt";

    @Override
    public List<Inscripcion> obtenerRegistros() {
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
            throw new RuntimeException("Error al leer el archivo de inscripciones ", e);
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
            throw new RuntimeException("Error al guardar inscripciones", e);
        }
    }

    @Override
    public void agregar(Inscripcion inscripcion) {
        List<Inscripcion> lista = obtenerRegistros();
        lista.add(inscripcion);
        guardarTodas(lista);
    }

    @Override
    public void modificar(Inscripcion inscripcionActual) {
        // Obtenemos la lista de todas las inscripciones
        List<Inscripcion> listaInscripciones = obtenerRegistros();

        // Recorremos la lista buscando la inscripción que coincida con el ID
        for (int i = 0; i < listaInscripciones.size(); i++) {
            if (listaInscripciones.get(i).getIdInscripcion().equals(inscripcionActual.getIdInscripcion())) {
                // Cuando la encontramos, la reemplazamos por el objeto actualizado
                listaInscripciones.set(i, inscripcionActual);
                guardarTodas(listaInscripciones);
                return; // Cortamos la ejecución porque ya terminamos el reemplazo
            }
        }
    }

    @Override
    public Inscripcion obtenerPorId(String id) {
        // Recorremos la lista de inscripciones
        for (Inscripcion inscripcion : obtenerRegistros()) {
            // Si el ID coincide, devolvemos el objeto completo
            if (inscripcion.getIdInscripcion().equals(id)) {
                return inscripcion;
            }
        }
        // Si no lo encuentra, devuelve null
        return null;
    }

    @Override
    public boolean eliminar(String id) {
        // 1. Verificamos si la inscripción realmente existe
        Inscripcion inscripcionAEliminar = obtenerPorId(id);

        // 2. Si existe (no es null), procedemos a borrarla
        if (inscripcionAEliminar != null) {
            List<Inscripcion> lista = obtenerRegistros();

            // Removemos de la lista limpiamente usando removeIf
            lista.removeIf(i -> i.getIdInscripcion().equals(id));

            guardarTodas(lista); // Guardamos los cambios en el archivo
            return true;         // Avisamos que fue un éxito
        }
        // 3. Si no existía, devolvemos false
        return false;
    }

}
