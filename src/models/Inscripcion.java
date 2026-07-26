package models;

//Entidad Transaccional
public class Inscripcion {

    private String idInscripcion;
    private String idEstudiante;
    private String codigoAula;

    public Inscripcion(String idInscripcion, String idEstudiante, String codigoAula) {
        this.idInscripcion = idInscripcion;
        this.idEstudiante = idEstudiante;
        this.codigoAula = codigoAula;
    }

    // --- Getters y Setters ---
    public String getIdInscripcion() {
        return idInscripcion;
    }

    public void setIdInscripcion(String idInscripcion) {
        this.idInscripcion = idInscripcion;
    }

    public String getIdEstudiante() {
        return idEstudiante;
    }

    public void setIdEstudiante(String idEstudiante) {
        this.idEstudiante = idEstudiante;
    }

    public String getCodigoAula() {
        return codigoAula;
    }

    public void setCodigoAula(String codigoAula) {
        this.codigoAula = codigoAula;
    }
}
