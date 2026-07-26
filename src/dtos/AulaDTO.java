package dtos;

public class AulaDTO {
    public String codigo;
    public int capacidad;

    public AulaDTO(String codigo, int capacidad) {
        this.codigo = codigo;
        this.capacidad = capacidad;
    }
    
    @Override
    public String toString() {
        return "Codigo: " + codigo + " | Capacidad: " + capacidad + " estudiantes";
    }
}