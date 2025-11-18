package app.model;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Properties;

/**
 * Singleton responsible for low-level HTTP communication with the OpenAI API.
 */
public class APIClient {

    private static APIClient instance;

    private final HttpClient httpClient;
    private final String apiKey;

    // Chat Completions endpoint
    private static final String OPENAI_URL = "https://api.openai.com/v1/chat/completions";

    private APIClient() {
        this.httpClient = HttpClient.newHttpClient();

        // 1) Try environment variable
        String key = System.getenv("OPENAI_API_KEY");

        // 2) If not set, try config.properties on the classpath
        if (key == null || key.isBlank()) {
            try (InputStream input = getClass()
                    .getClassLoader()
                    .getResourceAsStream("config.properties")) {

                if (input != null) {
                    Properties props = new Properties();
                    props.load(input);
                    key = props.getProperty("OPENAI_API_KEY");
                }
            } catch (IOException e) {
                // Ignore here; we'll treat missing key below
            }
        }

        // 3) If still missing or blank, mark as null so controller can handle it
        if (key == null || key.isBlank()) {
            this.apiKey = null;
        } else {
            this.apiKey = key.trim();
        }
    }

    public static synchronized APIClient getInstance() {
        if (instance == null) {
            instance = new APIClient();
        }
        return instance;
    }

    /**
     * Allows other classes (MainController) to detect missing key.
     */
    public String getApiKey() {
        return apiKey;
    }

    /**
     * Sends a JSON request body to the OpenAI Chat Completions endpoint
     * and returns the raw JSON response.
     */
    public String sendChatCompletionRequest(String jsonBody)
            throws IOException, InterruptedException {

        if (apiKey == null) {
            throw new IllegalStateException(
                "No API key available. Please set the OPENAI_API_KEY environment variable " +
                "or create a config.properties file with OPENAI_API_KEY defined."
            );
        }

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(OPENAI_URL))
                .header("Authorization", "Bearer " + apiKey)
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(jsonBody))
                .build();

        HttpResponse<String> response =
                httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        int status = response.statusCode();
        if (status / 100 != 2) {  // not 2xx
            throw new IOException("OpenAI API HTTP " + status + ": " + response.body());
        }

        return response.body();
    }
}
