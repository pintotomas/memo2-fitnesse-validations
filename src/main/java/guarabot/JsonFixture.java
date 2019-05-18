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

    protected String prepararRequestLlamada() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        Map<String,Object> data =  new HashMap<String, Object>();
        data.put("numero_origen", this.numeroOrigen);
        data.put("numero_destino", this.numeroDestino);
        data.put("fechahora_inicio", this.fechaHoraInicio);
        data.put("fechahora_fin", this.fechaHoraFin);
        return mapper.writeValueAsString(data);
    }

}
