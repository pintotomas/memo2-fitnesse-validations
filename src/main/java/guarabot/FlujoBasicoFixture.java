package guarabot;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.*;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;

import java.io.IOException;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.util.Map;

public class FlujoBasicoFixture extends JsonFixture {

    protected final static String ALTA_MATERIA_PATH = "materias";
    protected final static String INSCRIBIR_ALUMNO_PATH = "alumnos";
    protected final static String CALIFICAR_ALUMNO_PATH = "calificar";
    protected final static String ESTADO_MATERIA_PATH = "materias/estado";

    protected String estado;
    protected Float notaFinal;

    public FlujoBasicoFixture() throws IOException {
        this.client = createHttpClient();
        this.targetUrl = new EnvFixture().targetUrl();
        this.apiToken = new EnvFixture().apiToken();
        this.reset();
}

    public boolean altaMateriaCodigoDocenteCupoModalidad(String materia, String codigoMateria, String docente, int cupo, String modalidad) throws IOException, KeyStoreException, NoSuchAlgorithmException, KeyManagementException {
      this.nombreMateria = materia;
      this.codigoMateria = codigoMateria;
      this.docente = docente;
      this.cupo = cupo;
      this.modalidad = modalidad;
      return this.submitPost(ALTA_MATERIA_PATH, this.prepararRequestAltaMateria());
    }

    public boolean inscribirAlumnoUsernameAlumnoMateria(String nombreAlumno, String usernameAlumno, String codigoMateria) throws IOException, KeyStoreException, NoSuchAlgorithmException, KeyManagementException {
      this.alumno = nombreAlumno;
      this.usernameAlumno = usernameAlumno;
      this.codigoMateria = codigoMateria;
      return this.submitPost(INSCRIBIR_ALUMNO_PATH, this.prepararInscripcionAlumno());
    }

    public boolean  calificarAlumnoMateriaNotas(String usernameAlumno, String codigoMateria, String notas) throws IOException, KeyStoreException, NoSuchAlgorithmException, KeyManagementException {
      this.usernameAlumno = usernameAlumno;
      this.codigoMateria = codigoMateria;
      this.notas = notas;
      return this.submitPost(CALIFICAR_ALUMNO_PATH, this.prepararCalificarAlumno());
    }

    public boolean obtenerEstadoAlumnoMateria(String usernameAlumno, String codigoMateria) throws IOException {
      String query = "?usernameAlumno=" + usernameAlumno + "&codigoMateria=" + codigoMateria;
      HttpGet request = new HttpGet(this.targetUrl + ESTADO_MATERIA_PATH + query);
      request.addHeader(this.getTokenHeader());
      HttpResponse response = client.execute(request);
      if (response == null) return false;
      if (response.getStatusLine().getStatusCode() >= 300) return false;
      ObjectMapper mapper = new ObjectMapper();
      Map<String, Object> result = mapper.readValue(response.getEntity().getContent(), Map.class);
      this.estado = String.valueOf(result.get("estdado").toString());
      this.notaFinal = null;
      if (result.get("nota_final") != null) {
        this.notaFinal = Float.parseFloat(result.get("nota_final").toString());
      }
      return true;
    }

    public String estado() {
      return this.estado;
    }

    public Float notaFinal() {
        return this.notaFinal;
    }

    public int status() throws IOException {
        return this.response.getStatusLine().getStatusCode();
    }

    public String error() throws IOException {
        if (this.response == null)
            return "ERROR_DESCONOCIDO";
        if (this.status() == 400 || this.status() == 401) {
            ObjectMapper mapper = new ObjectMapper();
            Map<String, Object> result = mapper.readValue(response.getEntity().getContent(), Map.class);
            return result.get("error").toString();
        }
        return response.getEntity().getContent().toString();
    }

    public String resultado() throws IOException {
        if (this.response == null)
            return "SIN_RESPUESTA";
        ObjectMapper mapper = new ObjectMapper();
        Map<String, Object> result = mapper.readValue(response.getEntity().getContent(), Map.class);
        return result.get("resultado").toString();
    }

    public void apiToken(String token) {
        this.apiToken = token;
    }
}
