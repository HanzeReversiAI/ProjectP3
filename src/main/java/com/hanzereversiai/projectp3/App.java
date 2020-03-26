package com.hanzereversiai.projectp3;

import com.hanzereversiai.projectp3.networking.Network;
import com.hanzereversiai.projectp3.ui.ConnectionPanel;
import com.hanzereversiai.projectp3.ui.events.ConnectionRaisedEvent;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public class App extends Application {
    Network network;
    StackPane rootStackPane;

    @Override
    public void start(Stage stage) {
        rootStackPane = new StackPane();

        ConnectionPanel connectionPanel = new ConnectionPanel();
        connectionPanel.addEventHandler(ConnectionRaisedEvent.CONNECTION_RAISED_EVENT_TYPE,
                e -> onConnectionRaised(e.getNetwork()));
        rootStackPane.getChildren().add(connectionPanel);

        stage.setScene(new Scene(rootStackPane));
        stage.setTitle("Reversi AI Project");
        stage.show();
    }

    public void onConnectionRaised(Network network) {
        this.network = network;
        rootStackPane.getChildren().remove(0);
    }

    public static void main(String[] args) {
        launch(args);
    }
}
