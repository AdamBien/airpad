package com.airhacks.airpad.presentation.notelist;

import com.airhacks.airpad.business.notes.boundary.NotesStore;
import com.airhacks.airpad.business.notes.entity.Note;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ListView;
import javax.inject.Inject;

/**
 *
 * @author adam-bien.com
 */
public class NoteListPresenter implements Initializable {

    @FXML
    ListView<Note> listView;
    @Inject
    NotesStore notesStore;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
    }
}
