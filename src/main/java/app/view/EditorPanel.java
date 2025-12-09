package app.view;

import javax.swing.*;
import java.awt.*;

public class EditorPanel extends JPanel {

    private final JTextArea inputArea;
    private final JTextArea outputArea;

    public EditorPanel() {
        setLayout(new GridLayout(1, 2));

        inputArea = new JTextArea();
        outputArea = new JTextArea();
        outputArea.setEditable(false);

        add(new JScrollPane(inputArea));
        add(new JScrollPane(outputArea));
    }

    public String getInputText() {
        return inputArea.getText();
    }

    public void setOutputText(String text) {
        outputArea.setText(text);
    }

    public void appendOutputText(String text) {
        outputArea.append(text);
    }
}
