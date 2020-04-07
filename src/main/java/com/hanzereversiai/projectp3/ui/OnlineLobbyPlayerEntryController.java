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

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class OnlineLobbyPlayerEntryController {
    private final static Pattern gamePattern = Pattern.compile("GAMETYPE: \"(.*?)\"");
    private final static Pattern challengerPattern = Pattern.compile("CHALLENGER: \"(.*?)\",");
    private final static Pattern numberPattern = Pattern.compile("CHALLENGENUMBER: \"(.*?)\",");


    public BorderPane rootBorderPane;
    public Label playerNameLabel;
    public Button challengeAcceptButton;
    public MenuButton challengeMenuButton;

    private String game;
    private String opponent;
    private int challengeNumber;

    @FXML
    public void initialize() {
        hideChallengeAcceptButton();
        NetworkSingleton.getNetworkInstance().getDelegateInputListener().SUBSCRIBE_CHALLENGE(this::handleChallenge);
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
        NetworkSingleton.getNetworkInstance().SendCommand(Command.ACCEPT_CHALLENGE, String.valueOf(challengeNumber));
    }

    public void onChallengeAcceptMenuButtonActivated(ActionEvent actionEvent, String game, String player) {
        NetworkSingleton.getNetworkInstance().SendCommand(Command.CHALLENGE, "\"" + player + "\" \"" + game + "\"");
        challengeMenuButton.setVisible(false);
    }

    // TODO: If the player is not in the overview no accept button will appear,
    // TODO: calling the getPlayerList command however causes the screen to not update in time.
    public void handleChallenge(String input) {
        Matcher m = challengerPattern.matcher(input);
        if (m.find()) {
            if (m.group(1).equals(playerNameLabel.getText())) {
                Platform.runLater(
                        this::showChallengeAcceptButton
                );
                m = gamePattern.matcher(input);
                if (m.find()) {
                    game = m.group(1);
                } else {
                    throw new IllegalArgumentException();
                }
                m = numberPattern.matcher(input);
                if (m.find()) {
                    challengeNumber = Integer.parseInt(m.group(1));
                } else {
                    throw new IllegalArgumentException();
                }

            }
        }
    }
}
