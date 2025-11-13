package app;

import app.controller.MainController;
import app.view.MainFrame;

import javax.swing.SwingUtilities;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            MainController controller = new MainController();
            MainFrame frame = new MainFrame(controller);
            frame.setVisible(true);
        });
    }
}
