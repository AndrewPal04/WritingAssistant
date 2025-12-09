package app.model;

import com.google.gson.JsonObject;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Properties;

public class APIClient {

    private static APIClient instance;
    private String apiKey;

    private APIClient() {
        loadApiKey();
    }

    public static synchronized APIClient getInstance() {
        if (instance == null)
            instance = new APIClient();
        return instance;
    }

    public String getApiKey() {
        return apiKey;
    }

    private void loadApiKey() {
        apiKey = System.getenv("OPENAI_API_KEY");
        if (apiKey != null && !apiKey.isBlank()) {
            return;
        }

        try (InputStream in = getClass().getClassLoader().getResourceAsStream("config.properties")) {
            if (in != null) {
                Properties props = new Properties();
                props.load(in);
                apiKey = props.getProperty("OPENAI_API_KEY");
                if (apiKey != null && !apiKey.isBlank()) {
                    return;
                }
            }
        } catch (IOException ignored) {}

        try {
            Properties props = new Properties();
            FileInputStream in = new FileInputStream("config.properties");
            props.load(in);
            in.close();
            apiKey = props.getProperty("OPENAI_API_KEY");
        } catch (IOException ignored) {}
    }



    public HttpURLConnection openStreamConnection() throws IOException {
        URL url = new URL("https://api.openai.com/v1/chat/completions");
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();

        conn.setRequestMethod("POST");
        conn.setDoOutput(true);

        conn.setRequestProperty("Authorization", "Bearer " + apiKey);
        conn.setRequestProperty("Content-Type", "application/json");

        return conn;
    }


    public String sendChatCompletionRequest(String jsonPayload) throws IOException {
        URL url = new URL("https://api.openai.com/v1/chat/completions");
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();

        conn.setRequestMethod("POST");
        conn.setRequestProperty("Authorization", "Bearer " + apiKey);
        conn.setRequestProperty("Content-Type", "application/json");
        conn.setDoOutput(true);

        // Send JSON body
        conn.getOutputStream().write(jsonPayload.getBytes());

        // Read full response
        return new String(conn.getInputStream().readAllBytes());
    }
}
