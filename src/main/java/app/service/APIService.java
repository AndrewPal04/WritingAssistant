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

/**
 * Service responsible for talking to the OpenAI API asynchronously.
 */
public class APIService {

    private final APIClient apiClient;
    private final Gson gson;

    public APIService() {
        this.apiClient = APIClient.getInstance();
        this.gson = new Gson();
    }

    /**
     * Asynchronously generates text using the OpenAI Chat Completions API.
     */
    public void generateTextAsync(WritingRequest request, ResponseListener listener) {

        SwingWorker<WritingResponse, Void> worker = new SwingWorker<>() {
            @Override
            protected WritingResponse doInBackground() throws Exception {
                String payloadJson = buildPayloadJson(request);
                String responseJson = apiClient.sendChatCompletionRequest(payloadJson);
                return parseResponseJson(responseJson);
            }

            @Override
            protected void done() {
                try {
                    WritingResponse response = get();
                    listener.onSuccess(response);
                } catch (Exception e) {
                    listener.onError("Error generating text: " + e.getMessage());
                }
            }
        };

        worker.execute();
    }

    /**
     * Builds the JSON payload for the OpenAI Chat Completions API.
     */
    private String buildPayloadJson(WritingRequest request) {
        JsonObject root = new JsonObject();
        // You can change the model later if needed
        root.addProperty("model", "gpt-4o-mini");

        root.addProperty("temperature", request.getTemperature());
        root.addProperty("max_tokens", request.getMaxTokens());

        JsonArray messages = new JsonArray();

        JsonObject systemMsg = new JsonObject();
        systemMsg.addProperty("role", "system");
        systemMsg.addProperty(
                "content",
                "You are a helpful AI writing assistant. " +
                "Follow the requested style (creative, professional, academic) and be concise."
        );
        messages.add(systemMsg);

        JsonObject userMsg = new JsonObject();
        userMsg.addProperty("role", "user");
        userMsg.addProperty("content", request.getPrompt());
        messages.add(userMsg);

        root.add("messages", messages);

        return gson.toJson(root);
    }

    /**
     * Parses the OpenAI JSON response and extracts the assistant's message content.
     */
    private WritingResponse parseResponseJson(String json) {
        JsonObject root = JsonParser.parseString(json).getAsJsonObject();

        // Handle error payloads like: { "error": { "message": "...", ... } }
        if (root.has("error")) {
            JsonObject err = root.getAsJsonObject("error");
            String message = err.has("message") ? err.get("message").getAsString() : "Unknown API error";
            throw new IllegalStateException("OpenAI API returned an error: " + message);
        }

        JsonArray choices = root.getAsJsonArray("choices");
        if (choices == null || choices.size() == 0) {
            throw new IllegalStateException("No choices returned from OpenAI API.");
        }

        JsonObject firstChoice = choices.get(0).getAsJsonObject();
        JsonObject message = firstChoice.getAsJsonObject("message");
        if (message == null || !message.has("content")) {
            throw new IllegalStateException("No message content found in OpenAI response.");
        }

        String content = message.get("content").getAsString();
        return new WritingResponse(content.trim());
    }
}
