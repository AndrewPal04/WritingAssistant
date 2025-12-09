package app.service;

import app.model.APIClient;
import app.model.WritingRequest;
import app.model.WritingResponse;
import com.google.gson.*;

import java.io.*;
import java.net.HttpURLConnection;

public class APIService {

    private final Gson gson = new Gson();

    private app.model.observer.ResponseListener uiListener;

    public void addListener(app.model.observer.ResponseListener l) {
        this.uiListener = l;
    }

    public void dispatchStart() {
        if (uiListener != null) uiListener.onRequestStarted();
    }

    public void dispatchChunk(String text) {
        if (uiListener != null) uiListener.onStreamChunk(text);
    }

    public void dispatchSuccess(String finalText) {
        if (uiListener != null)
            uiListener.onRequestComplete(new WritingResponse(finalText));
    }

    public void dispatchError(String msg) {
        if (uiListener != null) uiListener.onRequestError(msg);
    }


    public void generateTextStream(
            WritingRequest request,
            java.util.function.Consumer<String> onChunk,
            java.util.function.Consumer<String> onComplete,
            java.util.function.Consumer<String> onError
    ) {

        dispatchStart();

        new Thread(() -> {
            try {

                HttpURLConnection conn = APIClient.getInstance().openStreamConnection();

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

                try (OutputStream os = conn.getOutputStream()) {
                    os.write(root.toString().getBytes());
                }

                StringBuilder full = new StringBuilder();

                try (BufferedReader reader =
                             new BufferedReader(new InputStreamReader(conn.getInputStream()))) {

                    String line;

                    while ((line = reader.readLine()) != null) {

                        if (!line.startsWith("data: ")) continue;

                        String json = line.substring(6).trim();

                        if (json.equals("[DONE]"))
                            break;

                        JsonObject chunk = JsonParser.parseString(json).getAsJsonObject();

                        JsonObject delta =
                                chunk.getAsJsonArray("choices")
                                        .get(0).getAsJsonObject()
                                        .getAsJsonObject("delta");

                        if (delta != null && delta.has("content")) {

                            String text = delta.get("content").getAsString();
                            full.append(text);

                            // controller's callback
                            onChunk.accept(text);

                            // UI callback
                            dispatchChunk(text);
                        }
                    }
                }

                // Streaming completed 
                String finalText = full.toString();

                onComplete.accept(finalText);
                dispatchSuccess(finalText);

            } catch (Exception e) {
                onError.accept(e.getMessage());
                dispatchError("Streaming error: " + e.getMessage());
            }

        }).start();
    }

    // Not used anymore
    public void generateTextAsync(WritingRequest request) {}
}
