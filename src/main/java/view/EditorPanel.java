package app.view;

import javax.swing.*;
import java.awt.*;

/**
 * Panel holding input and output text areas.
 */
public class EditorPanel extends JPanel {

    private final JTextArea inputArea;
    private final JTextArea outputArea;

    public EditorPanel() {
        setLayout(new GridLayout(1, 2));

        inputArea = new JTextArea();
        outputArea = new JTextArea();
        outputArea.setEditable(false);

        JScrollPane inputScroll = new JScrollPane(inputArea);
        JScrollPane outputScroll = new JScrollPane(outputArea);

        inputScroll.setBorder(BorderFactory.createTitledBorder("Input"));
        outputScroll.setBorder(BorderFactory.createTitledBorder("Output"));

        add(inputScroll);
        add(outputScroll);
    }

    public String getInputText() {
        return inputArea.getText();
    }

    public void setOutputText(String text) {
        outputArea.setText(text);
    }
}
