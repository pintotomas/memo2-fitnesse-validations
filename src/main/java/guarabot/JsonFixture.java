package guarabot;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.*;
import org.apache.http.client.HttpClient;
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
import java.util.HashMap;
import java.util.Map;

public class JsonFixture {

    protected HttpClient client;
    protected HttpResponse response;
    protected static final String RESET_PATH = "reset";
    protected String targetUrl;
    protected String apiToken;

    protected String nombreMateria;
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
        data.put("nombreMateria", this.nombreMateria);
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


    protected boolean submitPost(String path, String body) throws IOException, KeyStoreException, NoSuchAlgorithmException, KeyManagementException {
        this.client = createHttpClient();
        System.out.println("target url:" + this.targetUrl);
        HttpPost request = new HttpPost(this.targetUrl + path);
        request.addHeader(this.getTokenHeader());
        HttpEntity entity = new StringEntity(body, ContentType.APPLICATION_JSON);
        request.setEntity(entity);
        this.response = client.execute(request);
        if (this.response == null) return false;
        return true;
    }

    protected Header getTokenHeader() {
        return new Header() {
            @Override
            public String getName() {
                return "api_token";
            }

            @Override
            public String getValue() {
                return apiToken;
            }

            @Override
            public HeaderElement[] getElements() throws ParseException {
                return new HeaderElement[0];
            }
        };
    }

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


    public void reset(){
        HttpPost request = new HttpPost(this.targetUrl + RESET_PATH);
        HttpEntity entity = new StringEntity("", ContentType.APPLICATION_JSON);
        request.setEntity(entity);
        try {
            HttpResponse response = client.execute(request);
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

}
