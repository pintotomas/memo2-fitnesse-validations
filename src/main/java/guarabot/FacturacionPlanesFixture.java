package guarabot;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class FacturacionPlanesFixture extends FacturacionFixture {

    private static final String PLAN_PATH = "numero";
    private String plan;
    private String numerosAmigos;
    private String pais;

    public FacturacionPlanesFixture() throws IOException {
        super();
    }

    public boolean planNumeros(String plan, String numeros) throws IOException {
        this.plan = plan;
        this.numerosAmigos = numeros;
        this.client = createHttpClient();
        HttpPut request = new HttpPut(this.targetUrl + PLAN_PATH);
        HttpEntity entity = new StringEntity(this.prepararRequestPlanAmigos(), ContentType.APPLICATION_JSON);
        request.setEntity(entity);
        HttpResponse response = client.execute(request);
        if (response == null) return false;
        return true;
    }

    protected String prepararRequestPlanAmigos() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        Map<String,Object> data =  new HashMap<String, Object>();
        data.put("numero", this.numeroOrigen);
        data.put("plan", this.plan);
        data.put("amigos", this.numerosAmigos);
        return mapper.writeValueAsString(data);
    }

    protected String prepararRequestPlanPais() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        Map<String,Object> data =  new HashMap<String, Object>();
        data.put("numero", this.numeroOrigen);
        data.put("plan", this.plan);
        data.put("pais", this.pais);
        return mapper.writeValueAsString(data);
    }

    public boolean planPais(String plan, String pais) throws IOException {
        this.plan = plan;
        this.pais = pais;
        this.client = createHttpClient();
        HttpPut request = new HttpPut(this.targetUrl + PLAN_PATH);
        HttpEntity entity = new StringEntity(this.prepararRequestPlanPais(), ContentType.APPLICATION_JSON);
        request.setEntity(entity);
        HttpResponse response = client.execute(request);
        if (response == null) return false;
        return true;
    }
}