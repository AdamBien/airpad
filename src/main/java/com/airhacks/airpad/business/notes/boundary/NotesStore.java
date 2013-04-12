package com.airhacks.airpad.business.notes.boundary;

import com.airhacks.airpad.business.notes.entity.Note;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.ObservableMap;
import javafx.collections.ObservableSet;
import javax.annotation.PostConstruct;

/**
 *
 * @author adam-bien.com
 */
public class NotesStore {

    private StringProperty title;
    private ObservableList<Note> notes;
    private ObservableList<Note> filteredNotes;

    @PostConstruct
    public void init() {
        this.notes = FXCollections.observableArrayList();
        this.filteredNotes = FXCollections.observableArrayList();
        this.title = new SimpleStringProperty();
        this.title.addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> ov, String oldValue, String newValue) {
                System.out.println("Newvalue: " + newValue + " old value: " + oldValue);
                filteredNotes.clear();
                for (Note note : notes) {
                    if (note.matches(newValue)) {
                        filteredNotes.add(note);
                    }
                }
            }
        });
    }

    public void findOrCreate(String text) {
        final Note note = new Note(text);
        this.notes.add(note);
    }

    public ObservableList<Note> notes() {
        return this.filteredNotes;
    }

    public StringProperty title() {
        return this.title;
    }
}
