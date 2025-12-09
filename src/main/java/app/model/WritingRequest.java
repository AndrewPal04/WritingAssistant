package app.model;

public class WritingRequest {

    private final String prompt;
    private final double temperature;
    private final int maxTokens;
    private final String mode;

    public WritingRequest(String prompt, double temperature, int maxTokens, String mode) {
        this.prompt = prompt;
        this.temperature = temperature;
        this.maxTokens = maxTokens;
        this.mode = mode;
    }

    public String getPrompt() { return prompt; }
    public double getTemperature() { return temperature; }
    public int getMaxTokens() { return maxTokens; }
    public String getMode() { return mode; }
}
