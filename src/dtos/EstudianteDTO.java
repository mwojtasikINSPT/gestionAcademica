package dtos;

// Objeto plano que viajará entre la Vista y el Controlador
public class EstudianteDTO {
    public String id;
    public String dni;
    public String nombre;
    public String apellido;

    public EstudianteDTO(String id, String dni, String nombre, String apellido) {
        this.id = id;
        this.dni = dni;
        this.nombre = nombre;
        this.apellido = apellido;
    }
    
    @Override
    public String toString() {
        return "ID: " + id + " | DNI: " + dni + " | Nombre: " + nombre + " " + apellido;
    }
}