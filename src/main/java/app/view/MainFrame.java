package app.view;

import app.controller.MainController;

import javax.swing.*;
import java.awt.*;

/**
 * Main application window.
 */
public class MainFrame extends JFrame {

    private final MainController controller;
    private final EditorPanel editorPanel;
    private final StatusBar statusBar;

    public MainFrame(MainController controller) {
        super("WriteWise - AI Writing Assistant");
        this.controller = controller;

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(900, 600);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        editorPanel = new EditorPanel();
        statusBar = new StatusBar();
        ControlPanel controlPanel = new ControlPanel(controller, editorPanel, statusBar);

        add(controlPanel, BorderLayout.NORTH);
        add(editorPanel, BorderLayout.CENTER);
        add(statusBar, BorderLayout.SOUTH);
    }
}
