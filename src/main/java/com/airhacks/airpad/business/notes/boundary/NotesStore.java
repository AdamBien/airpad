package com.airhacks.airpad.business.notes.boundary;

import com.airhacks.airpad.business.notes.entity.Note;
import com.hazelcast.core.EntryEvent;
import com.hazelcast.core.EntryListener;
import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.IMap;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collection;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

/**
 *
 * @author adam-bien.com
 */
public class NotesStore {

    private IMap<String, Note> notes;
    private HazelcastInstance hazelcast;
    private ObjectProperty<Note> removed;
    private ObjectProperty<Note> added;
    private ObjectProperty<Note> updated;
    private String notesDirectory = "notes";
    private Charset charset;

    @PostConstruct
    public void init() {
        this.charset = Charset.forName("UTF-8");
        this.added = new SimpleObjectProperty<>();
        this.removed = new SimpleObjectProperty<>();
        this.updated = new SimpleObjectProperty<>();
        this.hazelcast = Hazelcast.newHazelcastInstance();
        this.notes = this.hazelcast.getMap("notes");
        this.notes.addEntryListener(new EntryListener<String, Note>() {
            @Override
            public void entryAdded(EntryEvent<String, Note> event) {
                System.out.println("Added: " + event);
                add(event);
            }

            @Override
            public void entryRemoved(EntryEvent<String, Note> event) {
                System.out.println("Removed: " + event);
                remove(event);
            }

            @Override
            public void entryUpdated(EntryEvent<String, Note> event) {
                System.out.println("Updated: " + event);
                update(event);
            }

            @Override
            public void entryEvicted(EntryEvent<String, Note> event) {
            }
        }, true);
        try {
            loadFromDisk();
        } catch (IOException ex) {
            Logger.getLogger(NotesStore.class.getName()).log(Level.SEVERE, null, ex);
        }
        refill();
    }

    public void add(EntryEvent<String, Note> event) {
        final Note note = event.getValue();
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                added.set(note);
                save(note);
            }
        });
    }

    public void update(EntryEvent<String, Note> event) {
        final Note note = event.getValue();
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                updated.set(note);
                save(note);
            }
        });
    }

    public void remove(EntryEvent<String, Note> event) {
        final Note note = event.getValue();
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                removed.set(note);
            }
        });
    }

    void refill() {
        for (Note note : notes.values()) {
            this.added.set(note);
        }
    }

    public void create(Note note) {
        this.notes.put(note.titleProperty().get(), note);
        save();
    }

    public void delete(Note note) {
        this.notes.remove(note);
    }

    public ReadOnlyObjectProperty<Note> addedProperty() {
        return added;
    }

    public ReadOnlyObjectProperty<Note> removedProperty() {
        return removed;
    }

    public ReadOnlyObjectProperty<Note> updatedProperty() {
        return updated;
    }

    public Collection<Note> allNotes() {
        return this.notes.values();
    }

    public void save() {
        for (Note note : notes.values()) {
            save(note);
        }
    }

    void save(Note note) {
        System.out.println("Saving: " + note);
        String title = note.titleProperty().get();
        String content = note.contentProperty().get();
        try {
            final Path directory = Paths.get(this.notesDirectory);
            if (!Files.exists(directory)) {
                Files.createDirectories(directory);
            }
        } catch (IOException ex) {
            System.err.format("IOException: %s%n", ex);
        }
        try (BufferedWriter writer = Files.newBufferedWriter(getNotePath(title), charset)) {
            writer.write(content, 0, content.length());
        } catch (IOException ex) {
            System.err.format("IOException: %s%n", ex);
        }
    }

    public void update(Note note) {
        this.notes.put(note.titleProperty().get(), note);
    }

    void loadFromDisk() throws IOException {
        Path notesDirectory = Paths.get(this.notesDirectory);
        DirectoryStream<Path> directories = Files.newDirectoryStream(notesDirectory);
        for (Path path : directories) {
            BufferedReader reader = Files.newBufferedReader(path, this.charset);
            StringBuilder content = new StringBuilder();
            String line = null;
            while ((line = reader.readLine()) != null) {
                content.append(line).append("/n");
            }
            final Path fileName = path.getFileName();
            Note note = new Note(stripEnding(fileName.toString()));
            note.contentProperty().set(content.toString());
            this.notes.put(note.titleProperty().get(), note);
        }
    }

    String stripEnding(String withEnding) {
        int lastIndexOf = withEnding.lastIndexOf('.');
        return withEnding.substring(0, lastIndexOf);
    }

    public void remove(Note note) {
        this.notes.remove(note.getIdentifier());
        try {
            Files.deleteIfExists(getNotePath(note.getIdentifier()));
        } catch (IOException ex) {
            Logger.getLogger(NotesStore.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    Path getNotePath(String title) {
        return Paths.get(this.notesDirectory, title + ".note");
    }

    @PreDestroy
    public void shutdown() {
        Hazelcast.shutdownAll();
    }
}
