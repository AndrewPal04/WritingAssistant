package app.service;

import app.model.APIClient;
import app.model.WritingRequest;
import app.model.WritingResponse;
import app.model.observer.ResponseListener;

import javax.swing.*;

/**
 * Service responsible for talking to the API asynchronously.
 */
public class APIService {

    private final APIClient apiClient;

    public APIService() {
        this.apiClient = APIClient.getInstance();
    }

    /**
     * Stub async method using SwingWorker.
     * Later: replace stubbed response with real HTTP call + JSON parsing.
     */
    public void generateTextAsync(WritingRequest request, ResponseListener listener) {

        SwingWorker<WritingResponse, Void> worker = new SwingWorker<>() {
            @Override
            protected WritingResponse doInBackground() {
                // For now: pretend we called an API and got a response
                String fakeContent = "Stub response for prompt:\n\n" + request.getPrompt();
                return new WritingResponse(fakeContent);
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
}
