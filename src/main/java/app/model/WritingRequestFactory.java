package app.model;

import app.model.strategy.WritingStrategy;

/**
 * Factory to build WritingRequest objects from a strategy and user input.
 */
public class WritingRequestFactory {

    private WritingRequestFactory() {
        // prevent instantiation
    }

    public static WritingRequest fromStrategy(WritingStrategy strategy, String userInput) {
        String prompt = strategy.buildPrompt(userInput);
        double temperature = strategy.getTemperature();
        int maxTokens = strategy.getMaxTokens();
        return new WritingRequest(prompt, temperature, maxTokens);
    }
}
