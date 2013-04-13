package com.airhacks.airpad.presentation.notelist;

import com.airhacks.airpad.business.notes.entity.Note;
import javafx.scene.control.ListCell;

/**
 *
 * @author adam-bien.com
 */
public class NoteListCell extends ListCell<Note> {
    
    @Override
    protected void updateItem(Note t, boolean bln) {
        super.updateItem(t, bln);
        if (t != null) {
            setText(t.toString());
        }
    }
}
