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

/**
 * @author Thomas
 */
public class LobbyPanelController {
    Parent offlineLobbyPanel;
    Parent onlineLobbyPanel;

    public Button onlineButton;
    public Button offlineButton;
    public StackPane contentStackPane;
    public Label welcomeLabel;

    /**
     * Set the welcome label and load the UI components.
     */
    @FXML
    public void initialize() {
        welcomeLabel.setText("Welcome back " + NetworkSingleton.getNetworkInstance().getUsername());

        // Load offline UI
        offlineLobbyPanel = loadUI("offline-lobby-panel");

        // Load online UI
        if (NetworkSingleton.getNetworkInstance().isConnected())
            onlineLobbyPanel = loadUI("online-lobby-panel");
    }

    /**
     * Switch the content panel to the online content.
     * @param actionEvent The event that fired this method
     */
    public void onOnlineButtonActivated(ActionEvent actionEvent) {
        if (onlineLobbyPanel != null)
            switchContentStackPaneContent(onlineLobbyPanel);
        else
            UIHelper.switchScene(actionEvent, "connection-panel");
    }

    /**
     * Switch the content panel to the offline content.
     * @param actionEvent The event that fired this method
     */
    public void onOfflineButtonActivated(ActionEvent actionEvent) {
        if (offlineLobbyPanel != null)
            switchContentStackPaneContent(offlineLobbyPanel);
    }

    /**
     * Switch the content of the content stack pane by loading the given fxml.
     * @param ui The fxml that has to be loaded
     * @return The parent object created from the fxml
     */
    private Parent loadUI(String ui) {
        try {
            Parent content = new FXMLLoader(getClass().getResource("/" + ui + ".fxml")).load();
            switchContentStackPaneContent(content);
            return content;
        } catch (IOException e) {
            return null;
        }
    }

    /**
     * Switch the content presented in the content stack pane.
     * @param content The content to be displayed
     */
    private void switchContentStackPaneContent(Parent content) {
        contentStackPane.getChildren().clear();
        contentStackPane.getChildren().add(content);
    }
}
