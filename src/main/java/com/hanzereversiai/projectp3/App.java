package com.hanzereversiai.projectp3;

import com.hanzereversiai.projectp3.networking.Network;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;

public class App extends Application {
    @Override
    public void start(Stage stage) {
        VBox root = new VBox();

        Label button = new Label("This is an example stage");
        root.getChildren().add(button);

        stage.setScene(new Scene(root, 400, 200));
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
