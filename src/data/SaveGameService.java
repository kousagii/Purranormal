package data;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.file.AtomicMoveNotSupportedException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;

/** Owns save IO; a failed write never replaces the last complete save. */
public final class SaveGameService {
    private final Path path;

    public SaveGameService(Path path) { this.path = path.toAbsolutePath(); }

    public boolean exists() { return Files.isRegularFile(path); }

    public DataStorage load() throws IOException {
        try (ObjectInputStream input = new ObjectInputStream(Files.newInputStream(path))) {
            Object value = input.readObject();
            if (!(value instanceof DataStorage data)) throw new IOException("Not a Purranormal save.");
            return data;
        } catch (ClassNotFoundException e) {
            throw new IOException("This save uses an unsupported format.", e);
        }
    }

    public void save(DataStorage data) throws IOException {
        Path temporary = Files.createTempFile(path.getParent(), "purranormal-", ".tmp");
        try {
            try (ObjectOutputStream output = new ObjectOutputStream(Files.newOutputStream(temporary))) {
                output.writeObject(data);
            }
            try {
                Files.move(temporary, path, StandardCopyOption.ATOMIC_MOVE, StandardCopyOption.REPLACE_EXISTING);
            } catch (AtomicMoveNotSupportedException e) {
                Files.move(temporary, path, StandardCopyOption.REPLACE_EXISTING);
            }
        } finally {
            Files.deleteIfExists(temporary);
        }
    }
}

