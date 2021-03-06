package com.airhacks.airpad.presentation.notelist;

import com.airhacks.airpad.business.notes.entity.Note;
import javafx.beans.binding.StringExpression;
import javafx.beans.property.SimpleStringProperty;
import javafx.scene.control.ListCell;
import javafx.scene.control.OverrunStyle;

/**
 *
 * @author adam-bien.com
 */
public class NoteListCell extends ListCell<Note> {

    public NoteListCell() {
        setTextOverrun(OverrunStyle.ELLIPSIS);
        setWrapText(true);
    }

    @Override
    protected void updateItem(Note t, boolean empty) {
        super.updateItem(t, empty);
        if (empty) {
            textProperty().unbind();
            setText(null);
            setGraphic(null);
        }
        if (t != null) {
            textProperty().bind(t.toBindableString());
        }
    }
}
