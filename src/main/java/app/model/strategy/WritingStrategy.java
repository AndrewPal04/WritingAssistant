package app.model.strategy;

/**
 * Strategy interface for different writing modes.
 */
public interface WritingStrategy {

    String buildPrompt(String userInput);

    double getTemperature();

    int getMaxTokens();
    
    String getModeName();
}
