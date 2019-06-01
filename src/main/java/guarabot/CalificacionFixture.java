package guarabot;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.util.Map;

public class CalificacionFixture extends JsonFixture{

    public CalificacionFixture() {
        this.client = createHttpClient();
        this.targetUrl = new EnvFixture().targetUrl();
        this.apiToken = new EnvFixture().apiToken();
    }

    public void reset(){
        super.reset();
        try {
            FlujoBasicoFixture fb = new FlujoBasicoFixture();
            fb.altaMateriaCodigoDocenteCupoModalidad("Algo1","7541","Luis", 30, "parciales");
            fb.inscribirAlumnoUsernameAlumnoMateria("Juan Perez", "juanperez","7541");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void setMateria(String materia) {
        this.codigoMateria = materia;
    }

    public void setAlumno(String alumno) {
        this.usernameAlumno = alumno;
    }

    public void setNotas(String notas) {
        this.notas = notas;
    }

    protected final static String CALIFICAR_ALUMNO_PATH = "calificar";

    public String resultado() {
        try {
            FlujoBasicoFixture fb = new FlujoBasicoFixture();
            fb.calificarAlumnoMateriaNotas(this.usernameAlumno, this.codigoMateria, this.notas);
            this.response = fb.response;
            ObjectMapper mapper = new ObjectMapper();
            Map<String, Object> result = mapper.readValue(response.getEntity().getContent(), Map.class);
            return result.get("resultado").toString();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
