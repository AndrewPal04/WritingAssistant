package app;

import app.view.EditorPanel;
import org.junit.jupiter.api.Test;

import javax.swing.*;

import static org.junit.jupiter.api.Assertions.*;

public class EditorPanelTest {

    @Test
    void inputAndOutputTextAreStoredCorrectly() {
        EditorPanel panel = new EditorPanel();

        // Set input via the underlying text area
        // (simplest: use the public getter/setter)
        String input = "My test input";
        // There's no direct setter, so we have to hack a bit:
        // simulate user typing by accessing the first JScrollPane's viewport
        JScrollPane inputScroll = (JScrollPane) panel.getComponent(0);
        JTextArea inputArea = (JTextArea) inputScroll.getViewport().getView();
        inputArea.setText(input);

        assertEquals(input, panel.getInputText());

        String output = "My test output";
        panel.setOutputText(output);

        JScrollPane outputScroll = (JScrollPane) panel.getComponent(1);
        JTextArea outputArea = (JTextArea) outputScroll.getViewport().getView();
        assertEquals(output, outputArea.getText());
    }
}
