package app.model;

/**
 * Represents a request to the writing API (prompt + params).
 */
public class WritingRequest {

    private final String prompt;
    private final double temperature;
    private final int maxTokens;

    public WritingRequest(String prompt, double temperature, int maxTokens) {
        this.prompt = prompt;
        this.temperature = temperature;
        this.maxTokens = maxTokens;
    }

    public String getPrompt() {
        return prompt;
    }

    public double getTemperature() {
        return temperature;
    }

    public int getMaxTokens() {
        return maxTokens;
    }
}
