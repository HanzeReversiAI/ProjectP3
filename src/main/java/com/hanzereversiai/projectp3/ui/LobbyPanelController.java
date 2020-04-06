package com.hanzereversiai.projectp3.ui;

import com.hanzereversiai.projectp3.networking.NetworkSingleton;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;

import java.io.IOException;

public class LobbyPanelController {
    public StackPane contentStackPane;
    public Label welcomeLabel;

    @FXML
    public void initialize() {
        welcomeLabel.setText("Welcome back " + NetworkSingleton.getNetworkInstance().getUsername());
        loadUI("offline-lobby-panel");
    }

    private void loadUI(String ui) {
        try {
            Parent root = new FXMLLoader(getClass().getResource("/" + ui + ".fxml")).load();
            contentStackPane.getChildren().clear();
            contentStackPane.getChildren().add(root);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
