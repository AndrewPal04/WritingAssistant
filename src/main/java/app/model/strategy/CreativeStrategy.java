package app.model.strategy;

public class CreativeStrategy implements WritingStrategy {

    @Override
    public String buildPrompt(String userInput) {
        return """
               You are a creative writing assistant. 
               Rewrite or continue this text in a vivid, imaginative style:
               """ + userInput;
    }
    @Override
    public String getModeName() {
        return "Creative";
}


    @Override
    public double getTemperature() {
        return 0.9;
    }

    @Override
    public int getMaxTokens() {
        return 500;
    }
}
