package app.view;

import javax.swing.*;
import java.awt.*;

public class StatusBar extends JPanel {

    private final JLabel statusLabel;

    public StatusBar() {
        setLayout(new BorderLayout());
        statusLabel = new JLabel("Ready");
        add(statusLabel, BorderLayout.WEST);
    }

    public void setStatus(String status) {
        statusLabel.setText(status);
    }

    public void setMessage(String message) {
        statusLabel.setText(message);
    }
}
