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

/**
 * UI Controller for the playerlist items
 *
 * Each one of these shows a player, a challenge button and optionally a accept button.
 *
 * @author Mike
 */
public class OnlineLobbyPlayerEntryController {

    public BorderPane rootBorderPane;
    public Label playerNameLabel;
    public Button challengeAcceptButton;
    public MenuButton challengeMenuButton;
    public Label challengedLabel;

    private String game;
    private int challengeNumber;

    /**
     * Clean UI and tell the Network about ourselfs
     */
    @FXML
    public void initialize() {
        hideChallengeAcceptButton();
        hideChallengedLabel();
        NetworkSingleton.getNetworkInstance().getOnlineLobbyPanelNetworkHandler().addOnlineLobbyPlayerEntryController(this);
    }

    /**
     * When pressing the Challenge accept button
     * @param actionEvent JavaFX actionEvent
     */
    public void onChallengeAcceptButtonActivated(ActionEvent actionEvent) {
        NetworkSingleton.getNetworkInstance().sendCommand(Command.ACCEPT_CHALLENGE, String.valueOf(challengeNumber));
    }

    /**
     * hen pressing the Challenge accept button
     * @param actionEvent JavaFX actionEvent
     * @param game The game that is being accepted
     * @param player The player that challenged us
     */
    public void onChallengeAcceptMenuButtonActivated(ActionEvent actionEvent, String game, String player) {
        NetworkSingleton.getNetworkInstance().sendCommand(Command.CHALLENGE, "\"" + player + "\" \"" + game + "\"");

        hideChallengeAcceptButton();
        showChallengedLabel();
    }

    /**
     * Sets the player name of the playerlist item
     * @param playerName The playername to set
     */
    public void setPlayerName(String playerName) {
        playerNameLabel.setText(playerName);
    }

    /**
     * Add the Challenge button
     * @param menuItem MenuItem menuItem
     */
    public void addChallengeOptions(MenuItem menuItem) {
        challengeMenuButton.getItems().add(menuItem);
    }

    /**
     * Add Challenge buttons
     * @param menuItems MenuItem[] menuItems
     */
    public void addChallengeOptions(MenuItem[] menuItems) {
        challengeMenuButton.getItems().addAll(menuItems);
    }

    /**
     * Hide the Challenge Accept button
     */
    public void hideChallengeAcceptButton() {
        Platform.runLater(() -> challengeAcceptButton.setVisible(false));
    }

    /**
     * Show the Challenge Accept button
     */
    public void showChallengeAcceptButton() {
        Platform.runLater(() -> challengeAcceptButton.setVisible(true));
    }

    /**
     * Hide the challenge Label
     */
    public void hideChallengedLabel() {
        Platform.runLater(() -> challengedLabel.setVisible(false));
    }

    /**
     * Show the challenge Label
     */
    public void showChallengedLabel() {
        Platform.runLater(() -> challengedLabel.setVisible(true));
    }

    /**
     * Set the number of the challenge
     * @param challengeNumber The challenge number provided by the server
     */
    public void setChallengeNumber(int challengeNumber) {
        this.challengeNumber = challengeNumber;
    }

    /**
     * Set the game type of the challenge
     * @param game The game type of the challenge provided by the server
     */
    public void setGame(String game) {
        this.game = game;
    }
}
