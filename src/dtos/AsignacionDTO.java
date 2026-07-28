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

    public String getIdAsignacion() {
        return idAsignacion;
    }

}
