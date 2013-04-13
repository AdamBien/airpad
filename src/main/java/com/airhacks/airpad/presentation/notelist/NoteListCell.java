package com.airhacks.airpad.presentation.notelist;

import com.airhacks.airpad.business.notes.entity.Note;
import javafx.scene.control.ListCell;

/**
 *
 * @author adam-bien.com
 */
public class NoteListCell extends ListCell<Note> {
    
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
