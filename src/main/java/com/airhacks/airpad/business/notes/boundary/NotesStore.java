package com.airhacks.airpad.business.notes.boundary;

import com.airhacks.airpad.business.notes.entity.Note;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
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

    @PostConstruct
    public void init() {
        this.notes = FXCollections.observableArrayList();
        this.title = new SimpleStringProperty();
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
