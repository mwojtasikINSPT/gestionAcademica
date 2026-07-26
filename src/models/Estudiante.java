package models;

public class Estudiante {
    private String id;
    private String dni;
    private String nombre;
    private String apellido;
    
    // Relación: El estudiante pertenece a un aula
    private Aula aula; 

    // Constructor (sin el aula, ya que usualmente se asigna después de inscribirlo)
    public Estudiante(String id, String dni, String nombre, String apellido) {
        this.id = id;
        this.dni = dni;
        this.nombre = nombre;
        this.apellido = apellido;
    }
    
    //Método para guardar en .txt
    public String toLineaArchivo() {
        return id + ";" + dni + ";" + nombre + ";" + apellido;
    }

    // --- Getters y Setters ---

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDni() {
        return dni;
    }

    public void setDni(String dni) {
        this.dni = dni;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellido() {
        return apellido;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    public Aula getAula() {
        return aula;
    }

    public void setAula(Aula aula) {
        this.aula = aula;
    }
}