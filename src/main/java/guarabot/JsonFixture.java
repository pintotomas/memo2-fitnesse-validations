package guarabot;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.HashMap;
import java.util.Map;

public class JsonFixture {

    protected String numeroOrigen;
    protected String numeroDestino;
    protected String fechaHoraInicio;
    protected String fechaHoraFin;

    protected String materia;
    protected String codigoMateria;
    protected String docente;
    protected int cupo;
    protected String modalidad;

    protected String alumno;
    protected String usernameAlumno;
    protected String notas;


    protected String prepararRequestAltaMateria() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        Map<String,Object> data =  new HashMap<String, Object>();
        data.put("materia", this.materia);
        data.put("codigo", this.codigoMateria);
        data.put("docente", this.docente);
        data.put("cupo", this.cupo);
        data.put("modalidad", this.modalidad);
        return mapper.writeValueAsString(data);
    }

    protected String prepararInscripcionAlumno() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        Map<String,Object> data =  new HashMap<String, Object>();
        data.put("nombre_completo", this.alumno);
        data.put("username_alumno", this.usernameAlumno);
        data.put("codigo_materia", this.codigoMateria);
        return mapper.writeValueAsString(data);
    }

    protected String prepararCalificarAlumno() throws JsonProcessingException {
      ObjectMapper mapper = new ObjectMapper();
      Map<String,Object> data =  new HashMap<String, Object>();
      data.put("username_alumno", this.usernameAlumno);
      data.put("codigo_materia", this.codigoMateria);
      data.put("notas", this.notas);
      return mapper.writeValueAsString(data);
  }
}
