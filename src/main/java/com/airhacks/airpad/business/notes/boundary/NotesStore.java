package com.airhacks.airpad.business.notes.boundary;

import com.airhacks.airpad.business.notes.entity.Note;
import javafx.collections.FXCollections;
import javafx.collections.ObservableMap;
import javax.annotation.PostConstruct;

/**
 *
 * @author adam-bien.com
 */
public class NotesStore {

    private ObservableMap<String, Note> notes;

    @PostConstruct
    public void init() {
        this.notes = FXCollections.observableHashMap();
    }

    public void findOrCreate(String text) {
        this.notes.put(text, new Note(text));
    }
}
