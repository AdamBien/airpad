package com.airhacks.airpad.presentation.notelist;

import com.airhacks.airpad.business.notes.boundary.NotesStore;
import com.airhacks.airpad.business.notes.entity.Note;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ListView;
import javafx.scene.control.MultipleSelectionModel;
import javafx.scene.control.SelectionMode;
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

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        listView.setItems(notesStore.notes());
        this.selectionModel = this.listView.getSelectionModel();
        selectionModel.setSelectionMode(SelectionMode.SINGLE);
    }

    public ReadOnlyObjectProperty<Note> selectedNote() {
        return selectionModel.selectedItemProperty();
    }
}
