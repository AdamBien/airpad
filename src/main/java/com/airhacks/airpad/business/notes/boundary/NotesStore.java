package com.airhacks.airpad.business.notes.boundary;

import com.airhacks.airpad.business.notes.entity.Note;
import java.util.ArrayList;
import java.util.List;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.ObservableMap;
import javax.annotation.PostConstruct;

/**
 *
 * @author adam-bien.com
 */
public class NotesStore {

    private StringProperty title;
    private ObservableList<Note> notes;
    private List<Note> matchingNotes;

    @PostConstruct
    public void init() {
        this.notes = FXCollections.observableArrayList();
        this.matchingNotes = new ArrayList<>();
        this.title = new SimpleStringProperty();
        this.title.addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> ov, String newValue, String oldValue) {
                notes.removeAll(matchingNotes);
                for (Note note : notes) {
                    if (note.matches(newValue)) {
                        matchingNotes.add(note);
                    }
                }
                notes.clear();
                notes.addAll(matchingNotes);
                matchingNotes.clear();
            }
        });
    }

    public void findOrCreate(String text) {
        this.notes.add(new Note(text));
    }

    public ObservableList<Note> notes() {
        return this.notes;
    }

    public StringProperty title() {
        return this.title;
    }
}
