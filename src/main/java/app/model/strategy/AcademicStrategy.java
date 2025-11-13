package app.model.strategy;

public class AcademicStrategy implements WritingStrategy {

    @Override
    public String buildPrompt(String userInput) {
        return """
               You are an academic writing assistant.
               Rewrite or continue this text in an academic style with a clear thesis and logical structure:
               """ + userInput;
    }

    @Override
    public double getTemperature() {
        return 0.4;
    }

    @Override
    public int getMaxTokens() {
        return 450;
    }
}
