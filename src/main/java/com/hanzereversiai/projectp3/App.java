package com.hanzereversiai.projectp3;

import com.hanzereversiai.projectp3.ui.ConnectionPanel;
import com.hanzereversiai.projectp3.ui.events.ConnectionSucceededEvent;
import com.hanzereversiai.projectp3.networking.Network;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.io.IOException;

public class App extends Application {
    Stage stage;
    Scene connectionScene;

    @Override
    public void start(Stage stage) {
        this.stage = stage;

        ConnectionPanel connectionPanel = new ConnectionPanel();
        connectionPanel.addEventHandler(ConnectionSucceededEvent.CONNECTION_SUCCEEDED_EVENT_TYPE,
                e -> stage.setScene(new Scene(new Pane())));

        this.connectionScene = new Scene(connectionPanel);

        stage.setScene(connectionScene);
        stage.setTitle("Reversi AI Project");
        stage.show();


    }

    public static void main(String[] args) {

        try {
            Network network = new Network();
        } catch (IOException e) {
            e.printStackTrace();
        }

        launch(args);
    }
}
