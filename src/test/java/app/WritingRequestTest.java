package app;

import app.model.WritingRequest;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class WritingRequestTest {

    @Test
    void gettersReturnConstructorValues() {
        WritingRequest req = new WritingRequest("prompt", 0.7, 250);
        assertEquals("prompt", req.getPrompt());
        assertEquals(0.7, req.getTemperature());
        assertEquals(250, req.getMaxTokens());
    }
}
