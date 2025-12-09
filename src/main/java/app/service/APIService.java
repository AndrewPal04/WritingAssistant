package app.service;

import app.model.APIClient;
import app.model.WritingRequest;
import app.model.WritingResponse;
import app.model.observer.ResponseListener;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import javax.swing.*;
import java.io.IOException;

public class APIService {

    private ResponseListener uiListener; // MainFrame listener
    private final APIClient apiClient = APIClient.getInstance();
    private final Gson gson = new Gson();

    public void addListener(ResponseListener l) {
        this.uiListener = l;
    }

    // --- event dispatch helpers ---
    public void dispatchStart() {
        if (uiListener != null) uiListener.onRequestStarted();
    }

    public void dispatchSuccess(WritingResponse response) {
        if (uiListener != null) uiListener.onRequestComplete(response);
    }

    public void dispatchError(String message) {
        if (uiListener != null) uiListener.onRequestError(message);
    }

    // --- async API call with callback ---
    public void generateTextAsync(WritingRequest request, ResponseListener callback) {

        SwingWorker<WritingResponse, Void> worker = new SwingWorker<>() {

            @Override
            protected WritingResponse doInBackground() throws Exception {
                callback.onRequestStarted();

                String payload = buildPayloadJson(request);
                String responseJson = apiClient.sendChatCompletionRequest(payload);
                return parseResponseJson(responseJson);
            }

            @Override
            protected void done() {
                try {
                    WritingResponse response = get();
                    callback.onRequestComplete(response);
                } catch (Exception e) {
                    callback.onRequestError(e.getMessage());
                }
            }
        };

        worker.execute();
    }

    // --- JSON builder ---
    private String buildPayloadJson(WritingRequest request) {
        JsonObject root = new JsonObject();
        root.addProperty("model", "gpt-4o-mini");
        root.addProperty("temperature", request.getTemperature());
        root.addProperty("max_tokens", request.getMaxTokens());

        JsonArray messages = new JsonArray();

        JsonObject systemMsg = new JsonObject();
        systemMsg.addProperty("role", "system");
        systemMsg.addProperty("content", "You are a helpful AI writing assistant.");
        messages.add(systemMsg);

        JsonObject userMsg = new JsonObject();
        userMsg.addProperty("role", "user");
        userMsg.addProperty("content", request.getPrompt());
        messages.add(userMsg);

        root.add("messages", messages);

        return gson.toJson(root);
    }

    // --- parse OpenAI response ---
    private WritingResponse parseResponseJson(String json) {
        JsonObject root = JsonParser.parseString(json).getAsJsonObject();

        if (root.has("error")) {
            throw new IllegalStateException(root.getAsJsonObject("error")
                    .get("message").getAsString());
        }

        JsonArray choices = root.get("choices").getAsJsonArray();
        JsonObject msg = choices.get(0).getAsJsonObject().get("message").getAsJsonObject();
        String content = msg.get("content").getAsString().trim();

        return new WritingResponse(content);
    }
}
