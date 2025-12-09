package app.view;

import app.model.domain.Session;

import javax.swing.*;
import java.awt.*;

public class HistoryPanel extends JPanel {

    private final DefaultListModel<String> listModel;
    private final JList<String> list;

    public HistoryPanel() {
        setLayout(new BorderLayout());
        setPreferredSize(new Dimension(250, 600));

        listModel = new DefaultListModel<>();
        list = new JList<>(listModel);
        list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        add(new JLabel("Session History"), BorderLayout.NORTH);
        add(new JScrollPane(list), BorderLayout.CENTER);
    }

    public void addSession(Session session) {
        listModel.addElement("[" + session.getMode() + "] " + session.getInput());
    }

    public void loadSessions(java.util.List<Session> sessions) {
        listModel.clear();
        for (Session s : sessions) {
            addSession(s);
        }
    }
}
