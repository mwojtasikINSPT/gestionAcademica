package dtos;

public class AsignacionDTO {

    private final String idAsignacion;
    private final String idProfesor;
    private final String codigoAula;

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
