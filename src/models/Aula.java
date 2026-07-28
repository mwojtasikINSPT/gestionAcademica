package models;

public class Aula {
    private String codigo;
    private int capacidad;
    
    // Relaciones
    //profesor Relación 1 a 1
    //estudiantes Relación 1 a N

    public Aula(String codigo, int capacidad) {
        this.codigo = codigo;
        this.capacidad = capacidad;
    }

    // --- Getters y Setters ---

    public String getCodigo() {
        return codigo;
    }
        
    public int getCapacidad() {
        return capacidad;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }
    
    public void setCapacidad(int capacidad) {
        this.capacidad = capacidad;
    }

    
}