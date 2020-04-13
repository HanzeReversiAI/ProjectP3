package com.hanzereversiai.projectp3.ui;

import com.hanzereversiai.projectp3.networking.Command;
import com.hanzereversiai.projectp3.networking.NetworkSingleton;
import javafx.application.Platform;
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
    public Label challengedLabel;

    private String game;
    private int challengeNumber;

    @FXML
    public void initialize() {
        hideChallengeAcceptButton();
        hideChallengedLabel();
        NetworkSingleton.getNetworkInstance().getOnlineLobbyPanelNetworkHandler().addOnlineLobbyPlayerEntryController(this);
    }

    public void onChallengeAcceptButtonActivated(ActionEvent actionEvent) {
        NetworkSingleton.getNetworkInstance().sendCommand(Command.ACCEPT_CHALLENGE, String.valueOf(challengeNumber));
    }

    public void onChallengeAcceptMenuButtonActivated(ActionEvent actionEvent, String game, String player) {
        NetworkSingleton.getNetworkInstance().sendCommand(Command.CHALLENGE, "\"" + player + "\" \"" + game + "\"");

        hideChallengeAcceptButton();
        showChallengedLabel();
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
        Platform.runLater(() -> challengeAcceptButton.setVisible(false));
    }

    public void showChallengeAcceptButton() {
        Platform.runLater(() -> challengeAcceptButton.setVisible(true));
    }

    public void hideChallengedLabel() {
        Platform.runLater(() -> challengedLabel.setVisible(false));
    }

    public void showChallengedLabel() {
        Platform.runLater(() -> challengedLabel.setVisible(true));
    }

    public void setChallengeNumber(int challengeNumber) {
        this.challengeNumber = challengeNumber;
    }
    public void setGame(String game) {
        this.game = game;
    }
}
