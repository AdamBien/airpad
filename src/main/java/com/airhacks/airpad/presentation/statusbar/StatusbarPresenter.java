package com.airhacks.airpad.presentation.statusbar;

import com.airhacks.airpad.business.notes.boundary.NotesStore;
import com.airhacks.airpad.presentation.notelist.ParameterizedStringProperty;
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
    ParameterizedStringProperty status;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        installClusterListener();
        this.status = new ParameterizedStringProperty("#airhackers:{0}");
        this.statusLabel.textProperty().bind(this.status);
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
        this.status.set(String.valueOf(numberOf));
    }
}
