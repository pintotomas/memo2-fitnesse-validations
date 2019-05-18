package guarabot;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
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
import java.util.Map;

/**
 * Created by nicopaez on 10/02/2019.
 */
public class TelcoBillingFixture extends JsonFixture{

    private final String targetUrl;
    private final static String LLAMADAS_PATH = "llamadas";
    private final static String FACTURACION_PATH = "facturacion";


    public TelcoBillingFixture() {
        this.targetUrl = new EnvFixture().targetUrl();
    }

    public void setCaso(String id) {

    }

    public void setNumeroOrigen(String numero) {
        this.numeroOrigen = numero;
    }

    public void setNumeroDestino(String numero) {
        this.numeroDestino = numero;
    }

    public void setInicio(String fechaHora) {
        this.fechaHoraInicio = fechaHora;
    }

    public void setFin(String fechaHora) {
        this.fechaHoraFin = fechaHora;
    }

    public float costo() throws Exception {
        final SSLContext sslContext = new SSLContextBuilder()
                .loadTrustMaterial(null, (x509CertChain, authType) -> true)
                .build();

        HttpClient client = HttpClientBuilder.create()
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
        HttpPost request = new HttpPost(this.targetUrl + LLAMADAS_PATH);
        HttpEntity entity = new StringEntity(this.prepararRequestLlamada(), ContentType.APPLICATION_JSON);
        request.setEntity(entity);
        HttpResponse response = client.execute(request);

        if (response == null) {
            return -1;
        }
        ObjectMapper mapper = new ObjectMapper();
        Map<String, Object> result = mapper.readValue(response.getEntity().getContent(), Map.class);
        return Float.parseFloat(result.get("costo_llamada").toString());
    }

}
