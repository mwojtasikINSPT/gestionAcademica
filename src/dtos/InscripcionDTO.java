package dtos;

public class InscripcionDTO {
    public String idInscripcion;
    public String idEstudiante;
    public String codigoAula;

    public InscripcionDTO(String idInscripcion, String idEstudiante, String codigoAula) {
        this.idInscripcion = idInscripcion;
        this.idEstudiante = idEstudiante;
        this.codigoAula = codigoAula;
    }
   
    
    @Override
    public String toString() {
        return "Inscripcion: " + idInscripcion + " | Estudiante ID: " + idEstudiante + " | Aula Codigo: " + codigoAula;
    }
}