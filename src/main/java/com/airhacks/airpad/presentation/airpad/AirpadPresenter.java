package com.airhacks.airpad.presentation.airpad;

import com.airhacks.airpad.business.notes.boundary.NotesStore;
import com.airhacks.airpad.business.notes.entity.Note;
import com.airhacks.airpad.presentation.notelist.NoteListPresenter;
import com.airhacks.airpad.presentation.notelist.NoteListView;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
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
    private NoteListPresenter noteListPresenter;
    private ObjectProperty<Note> selectedNote;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        this.selectedNote = new SimpleObjectProperty<>();
        this.noteListView = new NoteListView();
        this.noteListPresenter = (NoteListPresenter) this.noteListView.getPresenter();
        Parent view = this.noteListView.getView();
        this.noteList.getChildren().add(view);
        store.title().bind(this.noteName.textProperty());
        this.selectedNote.bind(this.noteListPresenter.selectedNote());
        this.selectedNote.addListener(new ChangeListener<Note>() {
            @Override
            public void changed(ObservableValue<? extends Note> ov, Note old, Note newNote) {
                if (old != null) {
                    old.contentProperty().unbind();
                }
                noteContent.textProperty().set(newNote.contentProperty().get());
                noteContent.textProperty().unbind();
                newNote.contentProperty().bind(noteContent.textProperty());
            }
        });
        this.noteListPresenter.nodeSelected().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> ov, Boolean old, Boolean newValue) {
                noteContent.requestFocus();
            }
        });
    }

    public void noteNameEntered() {
        store.findOrCreate(noteName.getText());
    }
}
