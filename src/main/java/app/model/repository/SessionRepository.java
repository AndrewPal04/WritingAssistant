package app.model.repository;

import app.model.domain.Session;

import java.io.IOException;
import java.nio.file.*;
import java.util.List;

public class SessionRepository {

    private final Path file = Paths.get("sessions.json");

    public void save(Session session) throws IOException {
        String json = String.format(
                "{ \"input\": \"%s\", \"output\": \"%s\", \"mode\": \"%s\" }",
                session.getInput().replace("\"", "'"),
                session.getOutput().replace("\"", "'"),
                session.getMode()
        );

        Files.writeString(
                file,
                json + System.lineSeparator(),
                StandardOpenOption.CREATE,
                StandardOpenOption.APPEND
        );
    }

    public List<String> loadAll() throws IOException {
        if (!Files.exists(file)) return List.of();
        return Files.readAllLines(file);
    }
}
