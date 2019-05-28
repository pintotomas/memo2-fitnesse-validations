package guarabot;

public class EnvFixture {

    private static final String DEFAULT_URL = "http://localhost:8080";
    private static final String DEFAULT_TOKEN = "guarabottoken";

    public String targetUrl() {
        String baseUrl = System.getenv("base_url");
        if (baseUrl == null || baseUrl.isEmpty() ) {
            return DEFAULT_URL;
        }
        return baseUrl;
    }

    public String apiToken() {
        String apiToken = System.getenv("api_token");
        if (apiToken == null || apiToken.isEmpty() ) {
            return DEFAULT_TOKEN;
        }
        return apiToken;
    }

}
