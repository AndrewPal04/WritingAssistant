package app;

import app.model.strategy.CreativeStrategy;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class CreativeStrategyTest {

    @Test
    void promptContainsUserInput() {
        CreativeStrategy strategy = new CreativeStrategy();
        String userInput = "Test text";
        String prompt = strategy.buildPrompt(userInput);
        assertTrue(prompt.contains(userInput));
    }

    @Test
    void creativeHasHighTemperature() {
        CreativeStrategy strategy = new CreativeStrategy();
        assertTrue(strategy.getTemperature() > 0.7);
    }

    @Test
    void creativeHasReasonableMaxTokens() {
        CreativeStrategy strategy = new CreativeStrategy();
        assertTrue(strategy.getMaxTokens() >= 400);
    }
}
