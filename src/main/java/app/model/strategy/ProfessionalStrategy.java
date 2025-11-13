package app.model.strategy;

public class ProfessionalStrategy implements WritingStrategy {

    @Override
    public String buildPrompt(String userInput) {
        return """
               You are a professional writing assistant.
               Rewrite or continue this text in a clear, concise, and formal tone:
               """ + userInput;
    }

    @Override
    public double getTemperature() {
        return 0.5;
    }

    @Override
    public int getMaxTokens() {
        return 400;
    }
}
