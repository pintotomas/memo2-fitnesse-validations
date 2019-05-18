package guarabot;

public class EnvFixture {

    private static final String DEFAULT_URL = "http://localhost:8080";

    public String targetUrl() {
        String baseUrl = System.getenv("base_url");
        if (baseUrl == null || baseUrl.isEmpty() ) {
            return DEFAULT_URL;
        }
        return baseUrl;
    }

}
