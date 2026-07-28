package dtos;

// Objeto plano que viajará entre la Vista y el Controlador
public class EstudianteDTO {
    private final String id;
    private final String dni;
    private final String nombre;
    private final String apellido;

    public EstudianteDTO(String id, String dni, String nombre, String apellido) {
        this.id = id;
        this.dni = dni;
        this.nombre = nombre;
        this.apellido = apellido;
    }

    public String getId() {
        return id;
    }

    public String getDni() {
        return dni;
    }

    public String getNombre() {
        return nombre;
    }

    public String getApellido() {
        return apellido;
    }
            
    @Override
    public String toString() {
        return "ID: " + id + " | DNI: " + dni + " | Nombre: " + nombre + " " + apellido;
    }
}