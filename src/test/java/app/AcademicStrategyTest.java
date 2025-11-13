package app;

import app.model.strategy.AcademicStrategy;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class AcademicStrategyTest {

    @Test
    void promptContainsUserInput() {
        AcademicStrategy strategy = new AcademicStrategy();
        String userInput = "Explain quantum computing.";
        String prompt = strategy.buildPrompt(userInput);
        assertTrue(prompt.contains(userInput));
    }

    @Test
    void academicHasLowerTemperature() {
        AcademicStrategy strategy = new AcademicStrategy();
        double temp = strategy.getTemperature();
        assertTrue(temp <= 0.5);
    }

    @Test
    void academicHasReasonableMaxTokens() {
        AcademicStrategy strategy = new AcademicStrategy();
        assertTrue(strategy.getMaxTokens() >= 400);
    }
}
