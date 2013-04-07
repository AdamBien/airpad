package com.airhacks.airpad.business.notes.entity;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 *
 * @author adam-bien.com
 */
public class Note {

    private StringProperty title;
    private StringProperty content;

    public Note(String title) {
        this.title = new SimpleStringProperty();
        this.title.set(title);
        this.content = new SimpleStringProperty();
    }

    public StringProperty titleProperty() {
        return this.title;
    }

    public StringProperty contentProperty() {
        return this.content;
    }
}
