package br.ufrn.ct.cronos.infrastructure.repository;

import org.json.JSONObject;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

public abstract class ApiUfrnAbstract {

	private static final String API_SINFO_URL_BASE_SISTEMAS = System.getenv("API_SINFO_URL_BASE_SISTEMAS");
	private static final String API_SINFO_URL_BASE_AUTENTICACAO = System.getenv("API_SINFO_URL_BASE_AUTENTICACAO");
	private static final String API_SINFO_VERSAO = System.getenv("API_SINFO_VERSAO");

	private static final String CRONOS_CLIENT_ID = System.getenv("CRONOS_CLIENT_ID");
    private static final String CRONOS_CLIENT_SECRET = System.getenv("CRONOS_CLIENT_SECRET");
	private static final String CRONOS_API_KEY = System.getenv("CRONOS_API_KEY");

	public ApiUfrnAbstract() {
		
	}

	protected ResponseEntity<String> getRespostaJSON(String url) {
		RestTemplate template = new RestTemplate();
		ResponseEntity<String> responseEntity = template.exchange(getURLAutenticacao(), HttpMethod.POST, null,
				String.class);
		JSONObject jsonObject = new JSONObject(responseEntity.getBody());
		HttpHeaders headers = new HttpHeaders();
		headers.add("Authorization", "bearer " + jsonObject.get("access_token"));
		headers.add("x-api-key", CRONOS_API_KEY);
		HttpEntity<String> requestEntity = new HttpEntity<String>("parameters", headers);
		return template.exchange(url, HttpMethod.GET, requestEntity, String.class);
	}

	protected ResponseEntity<String> getRespostaJSONPaginado(String url) {
		RestTemplate template = new RestTemplate();
		ResponseEntity<String> responseEntity = template.exchange(getURLAutenticacao(), HttpMethod.POST, null,
				String.class);
		JSONObject jsonObject = new JSONObject(responseEntity.getBody());
		HttpHeaders headers = new HttpHeaders();
		headers.add("Authorization", "bearer " + jsonObject.get("access_token"));
		headers.add("x-api-key", CRONOS_API_KEY);
		headers.add("paginado", "true");
		HttpEntity<String> requestEntity = new HttpEntity<String>("parameters", headers);
		return template.exchange(url, HttpMethod.GET, requestEntity, String.class);
	}

	private String getURLAutenticacao() {
		return API_SINFO_URL_BASE_AUTENTICACAO + "authz-server/oauth/token?client_id=" + CRONOS_CLIENT_ID + "&client_secret=" + CRONOS_CLIENT_SECRET
				+ "&grant_type=client_credentials";
	}

	protected String getUrlBaseSistemas() {
		return API_SINFO_URL_BASE_SISTEMAS;
	}

	protected String getVersao() {
		return API_SINFO_VERSAO;
	}

}
