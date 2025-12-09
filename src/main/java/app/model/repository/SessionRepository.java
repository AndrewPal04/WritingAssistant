package app.model.repository;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import app.model.domain.Session;

import java.io.IOException;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.List;

public class SessionRepository {

    private final Path file = Paths.get("sessions.json");
    private final Gson gson = new Gson();

    public void save(Session session) throws IOException {
        String json = gson.toJson(session);

        Files.writeString(
                file,
                json + System.lineSeparator(),
                StandardOpenOption.CREATE,
                StandardOpenOption.APPEND
        );
    }

    public List<Session> loadAll() throws IOException {
        List<Session> sessions = new ArrayList<>();

        if (!Files.exists(file)) return sessions;

        for (String line : Files.readAllLines(file)) {
            try {
                Session s = gson.fromJson(line, Session.class);
                if (s != null) sessions.add(s);
            } catch (JsonSyntaxException ignored) {
                // Skip malformed lines instead of crashing
            }
        }

        return sessions;
    }
}

