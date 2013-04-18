package com.airhacks.airpad.presentation.statusbar;

import com.airhacks.airpad.business.notes.boundary.NotesStore;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javax.inject.Inject;

/**
 *
 * @author adam-bien.com
 */
public class StatusbarPresenter implements Initializable {

    @Inject
    NotesStore store;
    @FXML
    Label statusLabel;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        installClusterListener();
    }

    void installClusterListener() {
        this.store.otherAirpads().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> ov, Number old, Number recent) {
                clusterSizeChanged(recent.intValue());
            }
        });
    }

    void clusterSizeChanged(int numberOf) {
        statusLabel.setText("Collaborators:" + numberOf);
    }
}
