package models;

//Entidad Transaccional
public class Asignacion {

    private String idAsignacion;
    private String idProfesor;
    private String codigoAula;

    public Asignacion(String idAsignacion, String idProfesor, String codigoAula) {
        this.idAsignacion = idAsignacion;
        this.idProfesor = idProfesor;
        this.codigoAula = codigoAula;
    }

    // --- Getters y Setters ---
    public String getIdAsignacion() {
        return idAsignacion;
    }

    public void setIdAsignacion(String idAsignacion) {
        this.idAsignacion = idAsignacion;
    }

    public String getIdProfesor() {
        return idProfesor;
    }

    public void setIdProfesor(String idProfesor) {
        this.idProfesor = idProfesor;
    }

    public String getCodigoAula() {
        return codigoAula;
    }

    public void setCodigoAula(String codigoAula) {
        this.codigoAula = codigoAula;
    }
}
