package app;

import app.model.strategy.ProfessionalStrategy;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class ProfessionalStrategyTest {

    @Test
    void promptContainsUserInput() {
        ProfessionalStrategy strategy = new ProfessionalStrategy();
        String userInput = "Please fix this email.";
        String prompt = strategy.buildPrompt(userInput);
        assertTrue(prompt.contains(userInput));
    }

    @Test
    void professionalHasMediumTemperature() {
        ProfessionalStrategy strategy = new ProfessionalStrategy();
        double temp = strategy.getTemperature();
        assertTrue(temp >= 0.4 && temp <= 0.6);
    }

    @Test
    void professionalHasReasonableMaxTokens() {
        ProfessionalStrategy strategy = new ProfessionalStrategy();
        assertTrue(strategy.getMaxTokens() >= 300);
    }
}
