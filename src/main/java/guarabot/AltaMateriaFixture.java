package guarabot;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;

import java.io.IOException;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.util.Map;

public class AltaMateriaFixture extends JsonFixture {

    private static final String ERROR_INESPERADO = "ERROR_INESPERADO";

    protected final static String ALTA_MATERIA_PATH = "materias";

    public AltaMateriaFixture() throws IOException {
        this.client = createHttpClient();
        this.targetUrl = new EnvFixture().targetUrl();
        this.apiToken = new EnvFixture().apiToken();
    }

    public void setCodigo(String codigo) {
        this.codigoMateria = codigo;
    }

    public void setNombre(String nombre) {
        this.nombreMateria = nombre;
    }

    public void setDocente(String docente) {
        this.docente = docente;
    }

    public void setCupo(int cupo) {
        this.cupo = cupo;
    }

    public void setModalidad(String modalidad) {
        this.modalidad = modalidad;
    }

    public void setConProyector(String conProyector) {
        this.conProyector = "si".equals(conProyector);
    }

    public void setConLaboratorio(String conLaboratorio) {
        this.conLaboratorio = "si".equals(conLaboratorio);
    }

    public String valido() throws RuntimeException, IOException, KeyManagementException, NoSuchAlgorithmException, KeyStoreException {
        this.submitPost(ALTA_MATERIA_PATH, this.prepararRequestAltaMateria());
        String resultado = response.getStatusLine().getStatusCode() == 201 ? "si,": "no,";
        ObjectMapper mapper = new ObjectMapper();
        Map<String, Object> result = mapper.readValue(response.getEntity().getContent(), Map.class);
        resultado += result.get("resultado").toString();
        return resultado;
    }

}
