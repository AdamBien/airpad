package com.airhacks.airpad.presentation.notelist;

import com.airhacks.airpad.business.notes.boundary.NotesStore;
import com.airhacks.airpad.business.notes.entity.Note;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ReadOnlyBooleanProperty;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.MultipleSelectionModel;
import javafx.scene.control.SelectionMode;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.util.Callback;
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
    private MultipleSelectionModel<Note> selectionModel;
    private BooleanProperty noteSelected;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        this.noteSelected = new SimpleBooleanProperty();
        listView.setEditable(false);
        this.selectionModel = this.listView.getSelectionModel();
        selectionModel.setSelectionMode(SelectionMode.SINGLE);
        listView.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent t) {
                if (t.getCode().equals(KeyCode.ENTER)) {
                    editSelectedNote();
                }
            }
        });
        listView.setOnKeyReleased(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent t) {
                if (t.getCode().equals(KeyCode.ENTER)) {
                    noteSelected.set(false);
                }
                if (t.getCode().equals(KeyCode.BACK_SPACE)) {
                    deleteSelectedNote();
                }

            }
        });
        listView.setCellFactory(new Callback<ListView<Note>, ListCell<Note>>() {
            @Override
            public ListCell<Note> call(ListView<Note> p) {
                return new NoteListCell();
            }
        });
    }

    void deleteSelectedNote() {
        Note selectedNote = selectedNote().get();
        if (selectedNote != null) {
            notesStore.remove(selectedNote);
        }
    }

    public ReadOnlyObjectProperty<Note> selectedNote() {
        return selectionModel.selectedItemProperty();
    }

    public ReadOnlyBooleanProperty nodeSelected() {
        return this.noteSelected;
    }

    void editSelectedNote() {
        if (selectionModel.getSelectedItems().isEmpty()) {
            selectionModel.selectFirst();
        }
        noteSelected.set(true);
    }

    public void requestFocus() {
        selectionModel.selectFirst();
        this.listView.requestFocus();
    }

    public void bind(ObservableList<Note> notes) {
        this.listView.setItems(notes);
    }

    public void update(Note note) {
        ObservableList<Note> items = this.listView.getItems();
        for (Note current : items) {
            if (current.getIdentifier().equals(note.getIdentifier())) {
                current.from(note);
            }
        }
    }
}
