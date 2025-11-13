package app.model;

/**
 * Singleton stub for API client.
 * Later: implement HTTP calls to OpenAI or other APIs.
 */
public class APIClient {

    private static APIClient instance;

    private APIClient() {
        // Later: load API key, init HttpClient, etc.
    }

    public static synchronized APIClient getInstance() {
        if (instance == null) {
            instance = new APIClient();
        }
        return instance;
    }

    // Stub method for now
    public String sendRequest(String payload) {
        // Later: perform real HTTP request and return JSON
        return "{ \"stub\": true }";
    }
}
