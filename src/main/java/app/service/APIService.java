package app.service;

import app.model.APIClient;
import app.model.WritingRequest;
import app.model.WritingResponse;
import app.model.observer.ResponseListener;
import app.model.domain.Session;
import app.model.repository.SessionRepository;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import javax.swing.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class APIService {

    private final APIClient apiClient;
    private final Gson gson;
    private final List<ResponseListener> listeners = new ArrayList<>();
    private final int maxRetries = 2;
    private final SessionRepository sessionRepo = new SessionRepository();
    private WritingRequest lastRequest;

    public APIService() {
        this.apiClient = APIClient.getInstance();
        this.gson = new Gson();
    }

    public void addListener(ResponseListener l) {
        listeners.add(l);
    }
    public List<ResponseListener> getListeners() {
        return listeners;
}


    private void notifyStart() {
        listeners.forEach(ResponseListener::onRequestStarted);
    }

    private void notifySuccess(WritingResponse res) {
        if (lastRequest != null) {
            Session session = new Session(
                    lastRequest.getPrompt(),
                    res.getOutput(),
                    lastRequest.getMode()
            );
            try {
                sessionRepo.save(session);
            } catch (Exception e) {
                notifyError("Failed to save session: " + e.getMessage());
            }
        }
        listeners.forEach(l -> l.onRequestComplete(res));
    }

    private void notifyError(String msg) {
        listeners.forEach(l -> l.onRequestError(msg));
    }

    public void generateTextAsync(WritingRequest request) {
        this.lastRequest = request;

        SwingWorker<WritingResponse, Void> worker = new SwingWorker<>() {
            @Override
            protected WritingResponse doInBackground() throws Exception {
                notifyStart();
                return performRequestWithRetry(request);
            }

            @Override
            protected void done() {
                try {
                    notifySuccess(get());
                } catch (Exception e) {
                    notifyError(e.getMessage());
                }
            }
        };

        worker.execute();
    }

    private WritingResponse performRequestWithRetry(WritingRequest request) throws Exception {
        int retries = 0;
        int backoff = 1000;

        while (true) {
            try {
                String payloadJson = buildPayloadJson(request);
                String responseJson = apiClient.sendChatCompletionRequest(payloadJson);
                return parseResponseJson(responseJson);
            } catch (IOException e) {
                if (retries >= maxRetries) throw e;
                Thread.sleep(backoff);
                backoff *= 2;
                retries++;
            }
        }
    }

    private String buildPayloadJson(WritingRequest request) {
        JsonObject root = new JsonObject();
        root.addProperty("model", "gpt-4o-mini");
        root.addProperty("temperature", request.getTemperature());
        root.addProperty("max_tokens", request.getMaxTokens());

        JsonArray messages = new JsonArray();

        JsonObject systemMsg = new JsonObject();
        systemMsg.addProperty("role", "system");
        systemMsg.addProperty(
                "content",
                "You are a helpful AI writing assistant. Follow the requested style (creative, professional, academic) and be concise."
        );
        messages.add(systemMsg);

        JsonObject userMsg = new JsonObject();
        userMsg.addProperty("role", "user");
        userMsg.addProperty("content", request.getPrompt());
        messages.add(userMsg);

        root.add("messages", messages);

        return gson.toJson(root);
    }

    private WritingResponse parseResponseJson(String json) {
        JsonObject root = JsonParser.parseString(json).getAsJsonObject();

        if (root.has("error")) {
            JsonObject err = root.getAsJsonObject("error");
            String msg = err.has("message") ? err.get("message").getAsString() : "Unknown API error";
            throw new IllegalStateException(msg);
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

        return new WritingResponse(message.get("content").getAsString().trim());
    }
}
