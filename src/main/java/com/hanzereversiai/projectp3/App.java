package com.hanzereversiai.projectp3;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

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
        launch(args);
    }
}
