package app.model.domain;

public class Session {

    private final String input;
    private final String output;
    private final String mode;

    public Session(String input, String output, String mode) {
        this.input = input;
        this.output = output;
        this.mode = mode;
    }

    public String getInput() {
        return input;
    }

    public String getOutput() {
        return output;
    }

    public String getMode() {
        return mode;
    }
}
