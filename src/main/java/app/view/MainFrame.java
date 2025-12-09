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
    private final HistoryPanel historyPanel;

    public MainFrame(MainController controller) {
        super("WriteWise - AI Writing Assistant");
        this.controller = controller;

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1200, 600);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        editorPanel = new EditorPanel();
        statusBar = new StatusBar();
        historyPanel = new HistoryPanel();

        ControlPanel controlPanel =
                new ControlPanel(controller, editorPanel, statusBar);

        add(controlPanel, BorderLayout.NORTH);
        add(editorPanel, BorderLayout.CENTER);
        add(statusBar, BorderLayout.SOUTH);
        add(historyPanel, BorderLayout.EAST);

        controller.getApiService().addListener(this);
        controller.setHistoryPanel(historyPanel);
    }

    @Override
    public void onRequestStarted() {
        editorPanel.setOutputText("");
        statusBar.setMessage("Generating (streaming)...");
    }

    // NEW — streaming chunk
    @Override
    public void onStreamChunk(String text) {
        editorPanel.appendOutputText(text);
    }

    @Override
    public void onRequestComplete(WritingResponse response) {
        statusBar.setMessage("Done");
    }

    @Override
    public void onRequestError(String message) {
        statusBar.setMessage("Error: " + message);
    }
}
