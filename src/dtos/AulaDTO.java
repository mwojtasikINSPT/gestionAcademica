package dtos;

public class AulaDTO {
    private final String codigo;
    private final int capacidad;

    public AulaDTO(String codigo, int capacidad) {
        this.codigo = codigo;
        this.capacidad = capacidad;
    }

    public int getCapacidad() {
        return capacidad;
    }

    public String getCodigo() {
        return codigo;
    } 
 }