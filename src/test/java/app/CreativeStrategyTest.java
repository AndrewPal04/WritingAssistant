package app;

import app.model.strategy.CreativeStrategy;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class CreativeStrategyTest {

    @Test
    public void testBuildPromptContainsUserInput() {
        CreativeStrategy strategy = new CreativeStrategy();
        String userInput = "Test text";
        String prompt = strategy.buildPrompt(userInput);
        assertTrue(prompt.contains(userInput));
    }
}
