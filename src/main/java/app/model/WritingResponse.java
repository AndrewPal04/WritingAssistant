package app.model;

/**
 * Represents a response returned by the writing API.
 */
public class WritingResponse {

    private final String content;

    public WritingResponse(String content) {
        this.content = content;
    }

    public String getContent() {
        return content;
    }
}
