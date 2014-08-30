package com.airhacks.airpad;

import com.airhacks.afterburner.injection.Injector;
import com.airhacks.airpad.presentation.airpad.AirpadPresenter;
import com.airhacks.airpad.presentation.airpad.AirpadView;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.stage.Stage;

/**
 *
 * @author adam-bien.com
 */
public class App extends Application {

    @Override
    public void start(Stage stage) throws Exception {
        setUserAgentStylesheet(STYLESHEET_MODENA);
        AirpadView appView = new AirpadView();
        Scene scene = new Scene(appView.getView());
        registerAccelerators(
                appView, scene);
        stage.setTitle("airpad");
        final String uri = getClass().getResource("app.css").toExternalForm();
        scene.getStylesheets().add(uri);
        stage.setScene(scene);
        stage.show();
    }

    @Override
    public void stop() throws Exception {
        Injector.forgetAll();
    }

    public static void main(String[] args) {
        launch(args);
    }

    private void registerAccelerators(AirpadView appView, Scene scene) {
        final AirpadPresenter presenter = (AirpadPresenter) appView.getPresenter();
        scene.getAccelerators().put(
                new KeyCodeCombination(KeyCode.S, KeyCombination.CONTROL_ANY),
                new Runnable() {
                    @Override
                    public void run() {
                        presenter.save();
                    }
                });
    }
}
