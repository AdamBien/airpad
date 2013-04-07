package com.airhacks.airpad.presentation.airpad;

import com.airhacks.airpad.business.notes.boundary.NotesStore;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
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
    @FXML
    ListView<String> noteList;
    @Inject
    NotesStore store;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
    }

    public void noteNameEntered() {
        store.findOrCreate(noteName.getText());
    }
}
