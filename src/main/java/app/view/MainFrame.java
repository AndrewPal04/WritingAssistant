package app.view;

import app.controller.MainController;
import app.model.WritingResponse;
import app.model.observer.ResponseListener;

import javax.swing.*;
import java.awt.*;

public class MainFrame extends JFrame implements ResponseListener {

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

        controller.getApiService().addListener(this);
    }

    @Override
    public void onRequestStarted() {
        statusBar.setMessage("Generating...");
    }

    @Override
    public void onRequestComplete(WritingResponse response) {
        editorPanel.setOutputText(response.getOutput());
        statusBar.setMessage("Done");
    }

    @Override
    public void onRequestError(String message) {
        statusBar.setMessage("Error: " + message);
    }
}
