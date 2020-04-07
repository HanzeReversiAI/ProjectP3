package com.hanzereversiai.projectp3.ui;

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
            FXMLLoader loader = new FXMLLoader(UIHelper.class.getResource("/" + fxml + ".fxml"));
            System.out.println(loader);
            Parent lobbyPanelParent = loader.load();
            Scene lobbyPanelScene = new Scene(lobbyPanelParent);

            Stage stage = (Stage) scene.getWindow();

            System.out.println(scene.getWindow());

            stage.setScene(lobbyPanelScene);
            stage.show();

            return loader;
        } catch (IOException e) {
            return null;
        }
    }
}
