package app;

import app.model.WritingRequest;
import app.model.WritingRequestFactory;
import app.model.strategy.WritingStrategy;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class WritingRequestFactoryTest {

    @Test
    void factoryBuildsRequestFromStrategy() {
        WritingStrategy dummyStrategy = new WritingStrategy() {
            @Override
            public String buildPrompt(String userInput) {
                return "PREFIX: " + userInput;
            }

            @Override
            public double getTemperature() {
                return 0.42;
            }

            @Override
            public int getMaxTokens() {
                return 321;
            }
        };

        String input = "Hello world";
        WritingRequest request = WritingRequestFactory.fromStrategy(dummyStrategy, input);

        assertEquals("PREFIX: " + input, request.getPrompt());
        assertEquals(0.42, request.getTemperature());
        assertEquals(321, request.getMaxTokens());
    }
}
