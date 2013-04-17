package com.airhacks.airpad.presentation.airpad;

import com.airhacks.airpad.business.notes.boundary.NotesStore;
import com.airhacks.airpad.business.notes.entity.Note;
import com.airhacks.airpad.presentation.notelist.NoteListPresenter;
import com.airhacks.airpad.presentation.notelist.NoteListView;
import java.net.URL;
import java.util.HashSet;
import java.util.ResourceBundle;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.ObservableSet;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
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
    private StringProperty title;
    private ObservableList<Note> filteredNotes;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        this.initModel();
        this.selectedNote = new SimpleObjectProperty<>();
        this.noteListView = new NoteListView();
        this.noteListPresenter = (NoteListPresenter) this.noteListView.getPresenter();
        this.noteListPresenter.bind(this.filteredNotes);
        Parent view = this.noteListView.getView();
        this.noteList.getChildren().add(view);
        this.title.bind(this.noteName.textProperty());
        this.selectedNote.bind(this.noteListPresenter.selectedNote());
        installSelectedNoteListener();
        installNoteContentListener();
        installTextAreaFocusHandler();
        installFocusListeners();
        installModelListeners();
        installNoteContentEnabling();
    }

    void installFocusListeners() {
        this.noteName.focusedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> ov, Boolean focusedBefore, Boolean focusNow) {
                if (focusedBefore && !focusNow) {
                    noteListPresenter.requestFocus();
                }
            }
        });
    }

    void installModelListeners() {
        this.store.addedProperty().addListener(new ChangeListener<Note>() {
            @Override
            public void changed(ObservableValue<? extends Note> ov, Note old, Note newNote) {
                System.out.println("Added: " + newNote);
                filteredNotes.add(newNote);
            }
        });

        this.store.removedProperty().addListener(new ChangeListener<Note>() {
            @Override
            public void changed(ObservableValue<? extends Note> ov, Note old, Note newNote) {
                System.out.println("Removed: " + newNote);
                filteredNotes.remove(newNote);
            }
        });

        this.store.updatedProperty().addListener(new ChangeListener<Note>() {
            @Override
            public void changed(ObservableValue<? extends Note> ov, Note old, Note newNote) {
                handleModelUpdate(newNote);
            }
        });
    }

    void initModel() {
        this.filteredNotes = FXCollections.observableArrayList();
        this.title = new SimpleStringProperty();
        this.title.addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> ov, String oldValue, String newValue) {
                refreshList(newValue);
            }
        });

    }

    void refreshList(String newValue) {
        filteredNotes.clear();
        for (Note note : store.allNotes()) {
            if (note.matches(newValue)) {
                filteredNotes.add(note);
            }
        }
    }

    void reload() {
        filteredNotes.setAll(store.allNotes());
    }

    void handleModelUpdate(Note newNote) {
        for (Note note : filteredNotes) {
            if (note.getIdentifier().equals(newNote.getIdentifier())) {
                note.from(newNote);
            }
        }
    }

    public void noteNameEntered() {
        create(noteName.getText());
        noteListPresenter.requestFocus();
    }

    public void create(String text) {
        final Note note = new Note(text);
        this.store.create(note);
    }

    public void save() {
        this.store.save();
    }

    void installSelectedNoteListener() {
        this.selectedNote.addListener(new ChangeListener<Note>() {
            @Override
            public void changed(ObservableValue<? extends Note> ov, Note old, Note newNote) {
                System.out.println("Note selected: " + ov + " old " + old + " new " + newNote);
                if (newNote == null) {
                    return;
                }
                noteContent.setText(newNote.contentProperty().get());
            }
        });
    }

    void installNoteContentListener() {
        noteContent.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> ov, String old, String newValue) {
                Note note = selectedNote.getValue();
                if (note != null && newValue != null) {
                    note.contentProperty().set(newValue);
                    store.update(note);
                }
            }
        });
    }

    void installTextAreaFocusHandler() {
        this.noteListPresenter.nodeSelected().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> ov, Boolean old, Boolean newValue) {
                if (newValue) {
                    noteContent.requestFocus();
                }
            }
        });
    }

    void installNoteContentEnabling() {
        this.noteContent.disableProperty().bind(this.selectedNote.isNull());
    }
}
