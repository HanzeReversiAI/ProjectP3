package com.hanzereversiai.projectp3.ui;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.BorderPane;

public class OnlineLobbyPlayerEntryController {
    public BorderPane rootBorderPane;
    public Label playerNameLabel;
    public Button challengeAcceptButton;
    public MenuButton challengeMenuButton;

    @FXML
    public void initialize() {
        hideChallengeAcceptButton();
    }

    public void setPlayerName(String playerName) {
        playerNameLabel.setText(playerName);
    }

    public void addChallengeOptions(MenuItem menuItem) {
        challengeMenuButton.getItems().add(menuItem);
    }

    public void addChallengeOptions(MenuItem[] menuItems) {
        challengeMenuButton.getItems().addAll(menuItems);
    }

    public void hideChallengeAcceptButton() {
        challengeAcceptButton.setVisible(false);
    }

    public void showChallengeAcceptButton() {
        challengeAcceptButton.setVisible(true);
    }

    public void onChallengeAcceptButtonActivated(ActionEvent actionEvent) {

    }
}
