package app.model;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

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

        // DO NOT crash here — allow GUI to load and let controller handle the error
        String key = System.getenv("OPENAI_API_KEY");

        if (key == null || key.isBlank()) {
            this.apiKey = null;  // Mark as missing
        } else {
            this.apiKey = key;
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
                "No API key available. Please set the OPENAI_API_KEY environment variable."
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
