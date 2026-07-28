package daos;

import models.Aula;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

//DAO: solo accede y modifica datos. No debe imprimir mensajes de usuario
public class AulaDAO implements ICrud<Aula, String> {

    private final String ARCHIVO = "aulas.txt";

    @Override
    public List<Aula> obtenerRegistros() {
        List<Aula> aulas = new ArrayList<>();
        File file = new File(ARCHIVO);
        if (!file.exists()) {
            return aulas;
        }
        //Try-catch, interactua con archivos
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String linea;
            while ((linea = br.readLine()) != null) {
                String[] partes = linea.split(";");
                if (partes.length >= 2) {
                    Aula aula = new Aula(partes[0], Integer.parseInt(partes[1]));
                    aulas.add(aula);
                }
            }
        } catch (IOException e) {
            throw new RuntimeException("Error al leer el archivo de aulas. ", e);
        }
        return aulas;
    }

    public void guardarTodas(List<Aula> aulas) {
        //Try-catch, interactua con archivos
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(ARCHIVO))) {
            for (Aula aula : aulas) {
                bw.write(aula.getCodigo() + ";" + aula.getCapacidad());
                bw.newLine();
            }
        } catch (IOException e) {
            throw new RuntimeException("Error al sobreescribir archivo ", e);
        }
    }

    @Override
    public void agregar(Aula aula) {
        List<Aula> lista = obtenerRegistros();
        lista.add(aula);
        guardarTodas(lista);
    }

    @Override
    public void modificar(Aula aulaModificada) {
        List<Aula> lista = obtenerRegistros();
        for (int i = 0; i < lista.size(); i++) {
            if (lista.get(i).getCodigo().equals(aulaModificada.getCodigo())) {
                lista.set(i, aulaModificada);
                guardarTodas(lista);
                return; // Corta la ejecución del método una vez que guarda
            }
        }
    }

    @Override
    public Aula obtenerPorId(String codigo) {
        return obtenerRegistros().stream()
                .filter(a -> a.getCodigo().equals(codigo))
                .findFirst()
                .orElse(null);
    }

    @Override
    public boolean eliminar(String codigo) {
        List<Aula> lista = obtenerRegistros();
        // removeIf devuelve true si eliminó algo, false si no lo encontró
        boolean eliminado = lista.removeIf(a -> a.getCodigo().equals(codigo));

        if (eliminado) {
            guardarTodas(lista);
        }
        return eliminado;
    }

}
