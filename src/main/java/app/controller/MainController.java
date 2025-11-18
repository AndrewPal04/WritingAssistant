package app.controller;

import app.model.WritingRequest;
import app.model.WritingRequestFactory;
import app.model.observer.ResponseListener;
import app.model.strategy.AcademicStrategy;
import app.model.strategy.CreativeStrategy;
import app.model.strategy.ProfessionalStrategy;
import app.model.strategy.WritingStrategy;
import app.service.APIService;
import app.model.APIClient;

/**
 * Main controller that coordinates between the view and the service/model.
 */
public class MainController {

    private final APIService apiService;

    public MainController() {
        this.apiService = new APIService();
    }

    /**
     * Called by the view when the user clicks "Generate".
     * modeName is a simple String for now ("Creative", "Professional", "Academic").
     */
    public void handleGenerate(String userInput, String modeName, ResponseListener listener) {

        // Basic input validation
        if (userInput == null || userInput.trim().isEmpty()) {
            listener.onError("Input cannot be empty. Please enter some text first.");
            return;
        }

        // Check for missing API key BEFORE making any request
        if (APIClient.getInstance().getApiKey() == null) {
            listener.onError(
                "Error: No valid API key found.\n" +
                "Please set the OPENAI_API_KEY environment variable."
            );
            return;
        }

        // Normal flow: select strategy + build request + generate
        WritingStrategy strategy = selectStrategy(modeName);
        WritingRequest request = WritingRequestFactory.fromStrategy(strategy, userInput);

        apiService.generateTextAsync(request, listener);
    }

    private WritingStrategy selectStrategy(String modeName) {
        if ("Professional".equalsIgnoreCase(modeName)) {
            return new ProfessionalStrategy();
        } else if ("Academic".equalsIgnoreCase(modeName)) {
            return new AcademicStrategy();
        } else {
            // default to Creative
            return new CreativeStrategy();
        }
    }
}
