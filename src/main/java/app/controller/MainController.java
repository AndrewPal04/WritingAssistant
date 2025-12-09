package app.controller;

import app.model.WritingRequest;
import app.model.WritingRequestFactory;
import app.model.strategy.AcademicStrategy;
import app.model.strategy.CreativeStrategy;
import app.model.strategy.ProfessionalStrategy;
import app.model.strategy.WritingStrategy;
import app.service.APIService;
import app.model.APIClient;

public class MainController {

    private final APIService apiService;

    public MainController() {
        this.apiService = new APIService();
    }

    public APIService getApiService() {
        return apiService;
    }

    public void handleGenerate(String userInput, String modeName) {

        if (userInput == null || userInput.trim().isEmpty()) {
            apiService.getListeners().forEach(
                    l -> l.onRequestError("Input cannot be empty.")
            );
            return;
        }

        if (APIClient.getInstance().getApiKey() == null) {
            apiService.getListeners().forEach(
                    l -> l.onRequestError("No API key found. Please set OPENAI_API_KEY.")
            );
            return;
        }

        WritingStrategy strategy = selectStrategy(modeName);
        WritingRequest request = WritingRequestFactory.fromStrategy(strategy, userInput);

        apiService.generateTextAsync(request);
    }

    private WritingStrategy selectStrategy(String modeName) {
        if ("Professional".equalsIgnoreCase(modeName)) {
            return new ProfessionalStrategy();
        } else if ("Academic".equalsIgnoreCase(modeName)) {
            return new AcademicStrategy();
        } else {
            return new CreativeStrategy();
        }
    }
}
