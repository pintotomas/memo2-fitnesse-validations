package guarabot;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.ssl.SSLContextBuilder;

import javax.net.ssl.SSLContext;
import java.io.IOException;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.util.Map;

public class FlujoBasicoFixture extends JsonFixture {

    protected static final String RESET_PATH = "reset";
    protected final String targetUrl;
    protected final static String LLAMADAS_PATH = "llamadas";
    protected final static String FACTURACION_PATH = "facturacion";
    protected HttpClient client;
    protected int cantidadLlamadas;
    protected float costoTotal;

    protected final static String ALTA_MATERIA_PATH = "materias";
    protected final static String INSCRIBIR_ALUMNO_PATH = "alumnos";
    protected final static String CALIFICAR_ALUMNO_PATH = "calificar";
    protected final static String ESTADO_MATERIA_PATH = "materias/estado";

    protected boolean aprobo;
    protected float notaFinal;

    protected HttpClient createHttpClient() {
        SSLContext sslContext = null;
        try {
            sslContext = new SSLContextBuilder()
                    .loadTrustMaterial(null, (x509CertChain, authType) -> true)
                    .build();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (KeyManagementException e) {
            e.printStackTrace();
        } catch (KeyStoreException e) {
            e.printStackTrace();
        }

        return HttpClientBuilder.create()
                .setSslcontext(sslContext)
                .setConnectionManager(
                        new PoolingHttpClientConnectionManager(
                                RegistryBuilder.<ConnectionSocketFactory>create()
                                        .register("http", PlainConnectionSocketFactory.INSTANCE)
                                        .register("https", new SSLConnectionSocketFactory(sslContext,
                                                NoopHostnameVerifier.INSTANCE))
                                        .build()
                        ))
                .build();
    }

    public FlujoBasicoFixture() throws IOException {
        client = createHttpClient();
        this.targetUrl = new EnvFixture().targetUrl();
        this.reset();
    }

    public boolean altaMateriaCodigoDocenteCupoModalidad(String materia, String codigoMateria, String docente, int cupo, String modalidad) throws IOException, KeyStoreException, NoSuchAlgorithmException, KeyManagementException {
      this.materia = materia;
      this.codigoMateria = codigoMateria;
      this.docente = docente;
      this.cupo = cupo;
      this.modalidad = modalidad;
      return this.submitPost(ALTA_MATERIA_PATH, this.prepararRequestAltaMateria());
    }

    public boolean inscribirAlumnoUsernameMateria(String alumno, String username, String codigoMateria) throws IOException, KeyStoreException, NoSuchAlgorithmException, KeyManagementException {
      this.alumno = alumno;
      this.username = username;
      this.codigoMateria = codigoMateria;
      return this.submitPost(INSCRIBIR_ALUMNO_PATH, this.prepararInscripcionAlumno());
    }

    public boolean calificarMateriaNotas(String username, String codigoMateria, String notas) throws IOException, KeyStoreException, NoSuchAlgorithmException, KeyManagementException {
      this.username = username;
      this.codigoMateria = codigoMateria;
      this.notas = notas;
      return this.submitPost(CALIFICAR_ALUMNO_PATH, this.prepararCalificarAlumno());
    }

    public boolean aprobo() throws IOException {
      String query = "?username=" + this.username + "&codigoMateria=" + this.codigoMateria;
      HttpGet request = new HttpGet(this.targetUrl + ESTADO_MATERIA_PATH + query);
      HttpResponse response = client.execute(request);
      if (response == null) return false;
      if (response.getStatusLine().getStatusCode() >= 300) return false;
      ObjectMapper mapper = new ObjectMapper();
      Map<String, Object> result = mapper.readValue(response.getEntity().getContent(), Map.class);
      this.aprobo = Boolean.valueOf(result.get("aprobada").toString());
      this.notaFinal = Float.parseFloat(result.get("nota_final").toString());
      return true;
    }

    public float notaFinal() {
        return this.notaFinal;
    }

    private boolean submitPost(String path, String body) throws IOException, KeyStoreException, NoSuchAlgorithmException, KeyManagementException {
      this.client = createHttpClient();
      HttpPost request = new HttpPost(this.targetUrl + path);
      HttpEntity entity = new StringEntity(body, ContentType.APPLICATION_JSON);
      request.setEntity(entity);
      HttpResponse response = client.execute(request);
      if (response == null) return false;
      return true;
    }

    public boolean numero(String numero) {
        this.numeroOrigen = numero.trim();
        return true;
    }

    public boolean destinoInicioFin(String numero, String inicio, String fin) throws IOException, KeyStoreException, NoSuchAlgorithmException, KeyManagementException {
        this.numeroDestino = numero.trim();
        this.fechaHoraInicio = inicio;
        this.fechaHoraFin = fin;
        this.client = createHttpClient();
        HttpPost request = new HttpPost(this.targetUrl + LLAMADAS_PATH);
        HttpEntity entity = new StringEntity(this.prepararRequestLlamada(), ContentType.APPLICATION_JSON);
        request.setEntity(entity);
        HttpResponse response = client.execute(request);
        if (response == null) return false;
        return true;
    }


    protected void reset() throws IOException {
        HttpPost request = new HttpPost(this.targetUrl + RESET_PATH);
        HttpEntity entity = new StringEntity("", ContentType.APPLICATION_JSON);
        request.setEntity(entity);
        HttpResponse response = client.execute(request);
    }

    public boolean facturar(String mes) throws IOException {
        String query = "?numero=" + java.net.URLEncoder.encode(this.numeroOrigen, "UTF-8") + "&mes=" + mes;
        HttpGet request = new HttpGet(this.targetUrl + FACTURACION_PATH + query);
        HttpResponse response = client.execute(request);
        if (response == null) return false;
        if (response.getStatusLine().getStatusCode() >= 300) return false;
        ObjectMapper mapper = new ObjectMapper();
        Map<String, Object> result = mapper.readValue(response.getEntity().getContent(), Map.class);
        this.cantidadLlamadas = Integer.parseInt(result.get("cantidad_llamadas").toString());
        this.costoTotal = Float.parseFloat(result.get("costo_total").toString());
        return true;
    }

    public float montoAPagar() {
        return this.costoTotal;
    }

    public float cantidadLlamadas() {
        return this.cantidadLlamadas;
    }
}