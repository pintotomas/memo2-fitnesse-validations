package guarabot;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.stream.Collectors;

public class OfertaAcademicaFixture extends FlujoBasicoFixture {

	private List<Materia> ofertaAcademica;
	private final static String CONSULTA_MATERIAS_PATH = "materias";

	public OfertaAcademicaFixture() throws IOException {
		// TODO, pasar al constructor de JsonFixture
		this.client = createHttpClient();
		this.targetUrl = new EnvFixture().targetUrl();
		this.apiToken = new EnvFixture().apiToken();
	}

	// TODO, eliminar codigo repetido
	public boolean consultarOferta(String usernameAlumno) throws IOException {
        String query = "?usernameAlumno=" + usernameAlumno;
		HttpGet request = new HttpGet(this.targetUrl + CONSULTA_MATERIAS_PATH + query);
		request.addHeader(this.getTokenHeader());
		HttpResponse response = client.execute(request);
		if (response == null)
			return false;
		if (response.getStatusLine().getStatusCode() >= 300)
			return false;
		ObjectMapper mapper = new ObjectMapper();
		JsonNode json = mapper.readTree(response.getEntity().getContent()).get("oferta");
		ofertaAcademica = mapper.convertValue(json, mapper.getTypeFactory().constructCollectionType(List.class, Materia.class));
		return true;
	}

	public int cantidadDeMateriasEnLaOfertaAcademica() {
		return this.ofertaAcademica.size();
	}

	public boolean ofertaAcademicaIncluyeCodigo(String codigo) {
		return this.ofertaAcademica.stream()
				.anyMatch(materia -> materia.getCodigo().equals(codigo));
	}

	public boolean ofertaAcademicaIncluyeNombre(String nombre) {
		return this.ofertaAcademica.stream()
				.anyMatch(materia -> materia.getNombre().equals(nombre));
	}

	public boolean ofertaAcademicaIncluyeModalidad(String modalidad) {
		return this.ofertaAcademica.stream()
				.anyMatch(materia -> materia.getModalidad().equals(modalidad));
	}

	public boolean ofertaAcademicaIncluyeCupo(String cupo) {
		return this.ofertaAcademica.stream()
				.anyMatch(materia -> materia.getCupo().equals(cupo));
	}


	public String cuposDisponibles(String codigo) {
		return this.ofertaAcademica.stream()
		.filter(materia -> materia.getCodigo().equals(codigo)).collect(Collectors.toList()).get(0).getCupo();
	}
	
}
