package app.service;

import app.model.APIClient;
import app.model.WritingRequest;
import app.model.WritingResponse;
import app.model.observer.ResponseListener;
import com.google.gson.*;

import java.io.*;
import java.net.HttpURLConnection;

public class APIService {

    private ResponseListener uiListener;
    private final Gson gson = new Gson();

    public void addListener(ResponseListener l) {
        this.uiListener = l;
    }

    public void dispatchStart() {
        if (uiListener != null) uiListener.onRequestStarted();
    }

    public void dispatchSuccess(WritingResponse r) {
        if (uiListener != null) uiListener.onRequestComplete(r);
    }

    public void dispatchError(String msg) {
        if (uiListener != null) uiListener.onRequestError(msg);
    }

    public void generateTextStream(WritingRequest request) {

        dispatchStart();

        new Thread(() -> {
            try {
                HttpURLConnection conn =
                        APIClient.getInstance().openStreamConnection();

                // ---- Build payload ----
                JsonObject root = new JsonObject();
                root.addProperty("model", "gpt-4o-mini");
                root.addProperty("stream", true);
                root.addProperty("temperature", request.getTemperature());
                root.addProperty("max_tokens", request.getMaxTokens());

                JsonArray messages = new JsonArray();
                JsonObject user = new JsonObject();
                user.addProperty("role", "user");
                user.addProperty("content", request.getPrompt());
                messages.add(user);

                root.add("messages", messages);

                // ---- Send request ----
                try (OutputStream os = conn.getOutputStream()) {
                    os.write(root.toString().getBytes());
                }

                // ---- Read streaming chunks ----
                StringBuilder full = new StringBuilder();

                try (BufferedReader reader = new BufferedReader(
                        new InputStreamReader(conn.getInputStream()))) {

                    String line;

                    while ((line = reader.readLine()) != null) {

                        if (!line.startsWith("data: ")) continue;

                        String json = line.substring(6).trim();

                        if (json.equals("[DONE]"))
                            break;

                        JsonObject chunk =
                                JsonParser.parseString(json).getAsJsonObject();

                        JsonObject delta = chunk
                                .getAsJsonArray("choices")
                                .get(0).getAsJsonObject()
                                .getAsJsonObject("delta");

                        if (delta != null && delta.has("content")) {

                            String text = delta.get("content").getAsString();
                            full.append(text);

                            if (uiListener != null)
                                uiListener.onStreamChunk(text);
                        }
                    }
                }

                dispatchSuccess(new WritingResponse(full.toString()));

            } catch (Exception e) {
                dispatchError("Streaming error: " + e.getMessage());
            }

        }).start();
    }

    public void generateTextAsync(WritingRequest request) {
    }
}
