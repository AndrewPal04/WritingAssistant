package app.controller;

import app.model.WritingRequest;
import app.model.WritingRequestFactory;
import app.model.WritingResponse;
import app.model.domain.Session;
import app.model.repository.SessionRepository;
import app.model.strategy.AcademicStrategy;
import app.model.strategy.CreativeStrategy;
import app.model.strategy.ProfessionalStrategy;
import app.model.strategy.WritingStrategy;
import app.service.APIService;
import app.model.APIClient;
import app.view.HistoryPanel;

public class MainController {

    private final APIService apiService;
    private final SessionRepository repo = new SessionRepository();
    private HistoryPanel historyPanel;

    public MainController() {
        this.apiService = new APIService();
    }

    public APIService getApiService() {
        return apiService;
    }

    public void setHistoryPanel(HistoryPanel panel) {
        this.historyPanel = panel;

        try {
            historyPanel.loadSessions(repo.loadAll());
        } catch (Exception ignored) {}
    }
    public void handleGenerate(String userInput, String modeName) {

        if (userInput == null || userInput.trim().isEmpty()) {
            apiService.dispatchError("Input cannot be empty.");
            return;
        }

        if (APIClient.getInstance().getApiKey() == null) {
            apiService.dispatchError("No API key found. Please set OPENAI_API_KEY.");
            return;
        }

        WritingStrategy strategy = selectStrategy(modeName);
        WritingRequest request = WritingRequestFactory.fromStrategy(strategy, userInput);

        apiService.generateTextStream(request);
    }


    private WritingStrategy selectStrategy(String modeName) {
        if ("Professional".equalsIgnoreCase(modeName))
            return new ProfessionalStrategy();
        if ("Academic".equalsIgnoreCase(modeName))
            return new AcademicStrategy();
        return new CreativeStrategy();
    }
}
