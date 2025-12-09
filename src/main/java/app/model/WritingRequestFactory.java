package app.model;

import app.model.strategy.WritingStrategy;

public class WritingRequestFactory {

    private WritingRequestFactory() {}

    public static WritingRequest fromStrategy(WritingStrategy strategy, String userInput) {
        String prompt = strategy.buildPrompt(userInput);
        double temperature = strategy.getTemperature();
        int maxTokens = strategy.getMaxTokens();
        String modeName = strategy.getModeName();
        return new WritingRequest(prompt, temperature, maxTokens, modeName);
    }
}
