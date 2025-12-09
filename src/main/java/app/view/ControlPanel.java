package app.view;

import app.controller.MainController;

import javax.swing.*;
import java.awt.*;

public class ControlPanel extends JPanel {

    private final JComboBox<String> modeComboBox;
    private final JButton generateButton;

    public ControlPanel(MainController controller, EditorPanel editorPanel, StatusBar statusBar) {
        setLayout(new FlowLayout(FlowLayout.LEFT));

        modeComboBox = new JComboBox<>(new String[] {
                "Creative",
                "Professional",
                "Academic"
        });

        generateButton = new JButton("Generate");

        generateButton.addActionListener(e -> {
            String input = editorPanel.getInputText();
            String mode = (String) modeComboBox.getSelectedItem();
            statusBar.setMessage("Generating...");
            controller.handleGenerate(input, mode);
        });

        add(new JLabel("Mode:"));
        add(modeComboBox);
        add(generateButton);
    }
}
