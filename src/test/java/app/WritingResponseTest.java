package app;

import app.model.WritingResponse;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class WritingResponseTest {

    @Test
    void responseStoresContent() {
        WritingResponse resp = new WritingResponse("hello");
        assertEquals("hello", resp.getContent());
    }
}
