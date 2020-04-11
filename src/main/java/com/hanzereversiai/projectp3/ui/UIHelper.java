package com.hanzereversiai.projectp3.ui;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class UIHelper {
    public static FXMLLoader switchScene(ActionEvent actionEvent, String fxml) {
        return switchScene(((Node)actionEvent.getSource()).getScene(), fxml);
    }

    public static FXMLLoader switchScene(Scene scene, String fxml) {
        try {
            double width = scene.getWidth();
            double height = scene.getHeight();

            FXMLLoader loader = new FXMLLoader(UIHelper.class.getResource("/" + fxml + ".fxml"));
            Parent lobbyPanelParent = loader.load();
            Scene lobbyPanelScene = new Scene(lobbyPanelParent, width, height);

            Stage stage = (Stage) scene.getWindow();

            System.out.println("Stage: " + stage);
            System.out.println("LobbyPanelParent: " + lobbyPanelParent);

            Platform.runLater(() -> {
                stage.setScene(lobbyPanelScene);
                stage.show();
            });

            return loader;
        }
        catch (IOException e) {
            return null;
        }
    }
}
