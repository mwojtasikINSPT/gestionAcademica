package daos;

import models.Aula;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class AulaDAO {
    private final String ARCHIVO = "aulas.txt";

    public List<Aula> obtenerTodas() {
        List<Aula> aulas = new ArrayList<>();
        File file = new File(ARCHIVO);
        if (!file.exists()) return aulas;

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
            System.out.println("Error al leer el archivo de aulas: " + e.getMessage());
        }
        return aulas;
    }

    public void guardarTodas(List<Aula> aulas) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(ARCHIVO))) {
            for (Aula aula : aulas) {
                bw.write(aula.getCodigo() + ";" + aula.getCapacidad());
                bw.newLine();
            }
        } catch (IOException e) {
            System.out.println("Error al guardar aulas: " + e.getMessage());
        }
    }

    public void agregar(Aula aula) {
        List<Aula> lista = obtenerTodas();
        lista.add(aula);
        guardarTodas(lista);
    }

    public boolean actualizar(Aula aulaModificada) {
        List<Aula> lista = obtenerTodas();
        for (int i = 0; i < lista.size(); i++) {
            if (lista.get(i).getCodigo().equals(aulaModificada.getCodigo())) {
                lista.set(i, aulaModificada);
                guardarTodas(lista);
                return true;
            }
        }
        return false;
    }

    public boolean eliminar(String codigo) {
        List<Aula> lista = obtenerTodas();
        for (int i = 0; i < lista.size(); i++) {
            if (lista.get(i).getCodigo().equals(codigo)) {
                lista.remove(i);
                guardarTodas(lista);
                return true;
            }
        }
        return false;
    }
}