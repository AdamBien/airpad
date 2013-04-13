package com.airhacks.airpad.business.notes.entity;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.io.Serializable;
import java.util.Objects;
import javafx.beans.property.ReadOnlyBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 *
 * @author adam-bien.com
 */
public class Note implements Externalizable {

    private StringProperty title;
    private StringProperty content;
    private String beforeImage;

    public Note() {
        this.title = new SimpleStringProperty();
        this.content = new SimpleStringProperty();
        this.beforeImage = "";
    }

    public Note(String title) {
        this();
        this.title.set(title);
        this.content.set("");
    }

    public StringProperty titleProperty() {
        return this.title;
    }

    public StringProperty contentProperty() {
        return this.content;
    }

    public boolean matches(String newValue) {
        if (newValue.trim().isEmpty()) {
            return true;
        }
        String titleValue = title.get();
        String contentValue = content.get();
        return (titleValue.contains(newValue) || contentValue.contains(newValue));
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 79 * hash + Objects.hashCode(this.title);
        hash = 79 * hash + Objects.hashCode(this.content);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Note other = (Note) obj;
        if (!Objects.equals(this.title, other.title)) {
            return false;
        }
        if (!Objects.equals(this.content, other.content)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return title.get() + ":" + content.get();
    }

    @Override
    public void writeExternal(ObjectOutput out) throws IOException {
        out.writeUTF(title.get());
        out.writeUTF(content.get());
    }

    @Override
    public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
        String titleValue = in.readUTF();
        String contentValue = in.readUTF();
        this.title.set(titleValue);
        this.content.set(contentValue);
    }

    public void synced() {
        this.beforeImage = computeFingerprint();
    }

    public boolean isDirty() {
        return (!this.beforeImage.equals(computeFingerprint()));
    }

    private String computeFingerprint() {
        return this.title.concat(this.content).get();
    }
}
