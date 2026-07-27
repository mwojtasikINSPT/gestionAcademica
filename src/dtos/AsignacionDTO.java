package dtos;

public class AsignacionDTO {
    public String idAsignacion;
    public String idProfesor;
    public String codigoAula;

    public AsignacionDTO(String idAsignacion, String idProfesor, String codigoAula) {
        this.idAsignacion = idAsignacion;
        this.idProfesor = idProfesor;
        this.codigoAula = codigoAula;
    }

    public String getIdProfesor() {
        return idProfesor;
    }

    public String getCodigoAula() {
        return codigoAula;
    }
    
    
    
    @Override
    public String toString() {
        return "Asignacion: " + idAsignacion + " | Profesor ID: " + idProfesor + " | Aula Codigo: " + codigoAula;
    }
}
