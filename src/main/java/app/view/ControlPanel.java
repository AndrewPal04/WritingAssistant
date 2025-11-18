package app.view;

import app.controller.MainController;
import app.model.WritingResponse;
import app.model.observer.ResponseListener;

import javax.swing.*;
import java.awt.*;

/**
 * Panel for controls: mode selection + generate button.
 */
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
            statusBar.setStatus("Generating...");

            controller.handleGenerate(input, mode, new ResponseListener() {
                @Override
                public void onSuccess(WritingResponse response) {
                    editorPanel.setOutputText(response.getContent());
                    statusBar.setStatus("Done");
                }

                @Override
                public void onError(String errorMessage) {
                    statusBar.setStatus("Error");
                    // Show the error directly in the Output panel
                    editorPanel.setOutputText(errorMessage);
                    // If you still want a popup, you can uncomment this:
                    /*
                    JOptionPane.showMessageDialog(
                            ControlPanel.this,
                            errorMessage,
                            "Error",
                            JOptionPane.ERROR_MESSAGE
                    );
                    */
                }
            });
        });

        add(new JLabel("Mode:"));
        add(modeComboBox);
        add(generateButton);
    }
}
