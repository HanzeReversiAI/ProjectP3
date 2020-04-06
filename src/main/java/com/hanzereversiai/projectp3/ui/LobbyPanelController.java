package com.hanzereversiai.projectp3.ui;

import com.hanzereversiai.projectp3.networking.NetworkSingleton;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;

import java.io.IOException;

public class LobbyPanelController {
    Parent offlineLobbyPane;
    Parent onlineLobbyPane;

    public Button onlineButton;
    public Button offlineButton;
    public StackPane contentStackPane;
    public Label welcomeLabel;

    @FXML
    public void initialize() {
        welcomeLabel.setText("Welcome back " + NetworkSingleton.getNetworkInstance().getUsername());
        offlineLobbyPane = loadUI("offline-lobby-panel");

        if (NetworkSingleton.getNetworkInstance().isConnected())
            onlineLobbyPane = loadUI("online-lobby-panel");
    }

    public void onOnlineButtonActivated(ActionEvent actionEvent) {
        if (onlineLobbyPane != null)
            switchContentStackPaneContent(onlineLobbyPane);
        else
            UIHelper.switchScene(actionEvent, "connection-panel");
    }

    public void onOfflineButtonActivated(ActionEvent actionEvent) {
        if (offlineLobbyPane != null)
            switchContentStackPaneContent(offlineLobbyPane);
    }

    private Parent loadUI(String ui) {
        try {
            Parent content = new FXMLLoader(getClass().getResource("/" + ui + ".fxml")).load();
            switchContentStackPaneContent(content);
            return content;
        } catch (IOException e) {
            return null;
        }
    }

    private void switchContentStackPaneContent(Parent content) {
        contentStackPane.getChildren().clear();
        contentStackPane.getChildren().add(content);
    }
}
