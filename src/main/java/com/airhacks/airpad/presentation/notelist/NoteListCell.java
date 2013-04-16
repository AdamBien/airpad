package com.airhacks.airpad.presentation.notelist;

import com.airhacks.airpad.business.notes.entity.Note;
import javafx.beans.binding.StringExpression;
import javafx.beans.property.ReadOnlyStringProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.scene.control.ListCell;

/**
 *
 * @author adam-bien.com
 */
public class NoteListCell extends ListCell<Note> {

    public static final int MAX_LENGTH = 60;

    @Override
    protected void updateItem(Note t, boolean empty) {
        super.updateItem(t, empty);
        if (empty) {
            textProperty().unbind();
            setText(null);
            setGraphic(null);
        }
        if (t != null) {
            textProperty().bind(limited(t.toBindableString()));
        }
    }

    StringExpression limited(StringExpression exp) {
        if (exp.length().greaterThan(MAX_LENGTH).get()) {
            String txt = exp.getValue();
            final String substring = txt.substring(0, MAX_LENGTH);
            return new SimpleStringProperty(substring).concat("...");
        } else {
            return exp;
        }
    }
}
