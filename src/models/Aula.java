package models;

import java.util.ArrayList;
import java.util.List;

public class Aula {
    private String codigo;
    private int capacidad;
    
    // Relaciones
    private Profesor profesor; // Relación 1 a 1
    private List<Estudiante> estudiantes; // Relación 1 a N

    public Aula(String codigo, int capacidad) {
        this.codigo = codigo;
        this.capacidad = capacidad;
        this.estudiantes = new ArrayList<>(); // Inicializamos la lista de estudiantes
    }

    // --- Getters y Setters ---

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public int getCapacidad() {
        return capacidad;
    }

    public void setCapacidad(int capacidad) {
        this.capacidad = capacidad;
    }

    public Profesor getProfesor() {
        return profesor;
    }

    public void setProfesor(Profesor profesor) {
        this.profesor = profesor;
    }

    public List<Estudiante> getEstudiantes() {
        return estudiantes;
    }

    public void setEstudiantes(List<Estudiante> estudiantes) {
        this.estudiantes = estudiantes;
    }
    
    // Método de utilidad para agregar un estudiante fácilmente
    public void agregarEstudiante(Estudiante estudiante) {
        this.estudiantes.add(estudiante);
    }
}