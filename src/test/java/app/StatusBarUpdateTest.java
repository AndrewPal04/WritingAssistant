package app;

import app.view.StatusBar;
import org.junit.jupiter.api.Test;

import javax.swing.*;
import java.awt.*;

import static org.junit.jupiter.api.Assertions.*;

public class StatusBarUpdateTest {

    @Test
    void statusBarUpdatesLabelText() {
        StatusBar bar = new StatusBar();
        bar.setStatus("Working...");

        // StatusBar adds a JLabel as its first (and only) component
        Component[] children = bar.getComponents();
        assertTrue(children.length > 0);
        assertTrue(children[0] instanceof JLabel);

        JLabel label = (JLabel) children[0];
        assertEquals("Working...", label.getText());
    }
}
