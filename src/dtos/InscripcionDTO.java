package dtos;

public class InscripcionDTO {
    private final String idInscripcion;
    private final String idEstudiante;
    private final String codigoAula;

    public InscripcionDTO(String idInscripcion, String idEstudiante, String codigoAula) {
        this.idInscripcion = idInscripcion;
        this.idEstudiante = idEstudiante;
        this.codigoAula = codigoAula;
    }

    public String getIdInscripcion() {
        return idInscripcion;
    }

    public String getIdEstudiante() {
        return idEstudiante;
    }

    public String getCodigoAula() {
        return codigoAula;
    }
   
    
    
    @Override
    public String toString() {
        return "Inscripcion: " + idInscripcion + " | Estudiante ID: " + idEstudiante + " | Aula Codigo: " + codigoAula;
    }
}