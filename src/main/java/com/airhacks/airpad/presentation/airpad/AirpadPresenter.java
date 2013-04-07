package com.airhacks.airpad.presentation.airpad;

import com.airhacks.airpad.business.notes.boundary.NotesStore;
import com.airhacks.airpad.business.notes.entity.Note;
import com.airhacks.airpad.presentation.notelist.NoteListView;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javax.inject.Inject;

/**
 *
 * @author adam-bien.com
 */
public class AirpadPresenter implements Initializable {

    @FXML
    TextField noteName;
    @FXML
    TextArea noteContent;
    @Inject
    NotesStore store;
    @FXML
    AnchorPane noteList;
    private NoteListView noteListView;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        this.noteListView = new NoteListView();
        Parent view = this.noteListView.getView();
        this.noteList.getChildren().add(view);
        store.title().bind(this.noteName.textProperty());
    }

    public void noteNameEntered() {
        store.findOrCreate(noteName.getText());
    }
}
