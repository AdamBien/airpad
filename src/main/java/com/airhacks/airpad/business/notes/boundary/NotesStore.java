package com.airhacks.airpad.business.notes.boundary;

import com.airhacks.airpad.business.notes.entity.Note;
import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.ISet;
import com.hazelcast.core.ItemEvent;
import com.hazelcast.core.ItemListener;
import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.util.Set;
import javafx.application.Platform;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javax.annotation.PostConstruct;

/**
 *
 * @author adam-bien.com
 */
public class NotesStore {

    private ISet<Note> notes;
    private HazelcastInstance hazelcast;
    private ObjectProperty<Note> removed;
    private ObjectProperty<Note> added;

    @PostConstruct
    public void init() {
        this.added = new SimpleObjectProperty<>();
        this.removed = new SimpleObjectProperty<>();
        this.hazelcast = Hazelcast.newHazelcastInstance();
        this.notes = this.hazelcast.getSet("notes");
        this.notes.addItemListener(new ItemListener<Note>() {
            @Override
            public void itemAdded(ItemEvent<Note> item) {
                final Note note = item.getItem();
                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        added.set(note);
                    }
                });
            }

            @Override
            public void itemRemoved(ItemEvent<Note> item) {
                final Note note = item.getItem();
                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        removed.set(note);
                    }
                });
            }
        }, true);
        refill();
    }

    void refill() {
        for (Note note : notes) {
            this.added.set(note);
        }
    }

    public void create(Note note) {
        this.notes.add(note);
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

    public Set<Note> allNotes() {
        return this.notes;
    }

    public void save() {
        for (Note note : notes) {
            save(note);
        }
    }

    void save(Note note) {

        System.out.println("Saving: " + note);
        if (note.isDirty()) {
            this.notes.add(note);
            note.synced();
        }
        String title = note.titleProperty().get();
        String content = note.contentProperty().get();
        File file = new File(title + ".note");
        Charset charset = Charset.forName("UTF-8");
        try (BufferedWriter writer = Files.newBufferedWriter(file.toPath(), charset)) {
            writer.write(content, 0, content.length());
        } catch (IOException x) {
            System.err.format("IOException: %s%n", x);
        }
    }
}
