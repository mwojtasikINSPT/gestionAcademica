package daos;

import models.Asignacion;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class AsignacionDAO implements ICrud<Asignacion, String> {

    private final String ARCHIVO = "asignaciones.txt";

    @Override
    public List<Asignacion> obtenerRegistros() {
        List<Asignacion> asignaciones = new ArrayList<>();
        File file = new File(ARCHIVO);
        if (!file.exists()) {
            return asignaciones;
        }

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

    @Override
    public void agregar(Asignacion asignacion) {
        List<Asignacion> lista = obtenerRegistros();
        lista.add(asignacion);
        guardarTodas(lista);
    }

    @Override
    public void modificar(Asignacion asignacionActual) {
        //Obtengo lista de todas las asignaciones
        List<Asignacion> listaAsignaciones = obtenerRegistros();

        //Recorro lista para buscar si hay asignacion que coincida con id pasado
        for (int i = 0; i < listaAsignaciones.size(); i++) {
            if (listaAsignaciones.get(i).getIdAsignacion().equals(asignacionActual.getIdAsignacion())) {
                //Si encuentro, reemplazo
                listaAsignaciones.set(i, asignacionActual);
                //Guardo
                guardarTodas(listaAsignaciones);
                return;
            }
        }
    }

    @Override
    public Asignacion obtenerPorId(String id) {
        // Recorremos la lista de asignaciones
        for (Asignacion a : obtenerRegistros()) {
            // Si el ID coincide, devolvemos el objeto completo
            if (a.getIdAsignacion().equals(id)) {
                return a;
            }
        }
        // Si no lo encuentra, devuelve null
        return null;
    }

    @Override
    public boolean eliminar(String id) {
        // 1. Verificamos si la asignación realmente existe
        Asignacion asignacionAEliminar = obtenerPorId(id);

        // 2. Si existe (no es null), procedemos a borrarla
        if (asignacionAEliminar != null) {
            List<Asignacion> lista = obtenerRegistros();

            // Forma limpia de remover el objeto coincidente
            lista.removeIf(a -> a.getIdAsignacion().equals(id));

            guardarTodas(lista); // Guardamos los cambios
            return true;         // Avisamos que fue un éxito
        }
        // 3. Si no existía, devolvemos false
        return false;
    }
}