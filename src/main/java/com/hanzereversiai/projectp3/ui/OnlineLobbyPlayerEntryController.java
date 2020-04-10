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
    public Label challengedLabel;

    private String game;
    private String opponent;
    private int challengeNumber;

    @FXML
    public void initialize() {
        hideChallengeAcceptButton();
        hideChallengedLabel();
        NetworkSingleton.getNetworkInstance().getDelegateInputListener().SUBSCRIBE_CHALLENGE(this::handleChallenge, this.hashCode());
    }

    public void onChallengeAcceptButtonActivated(ActionEvent actionEvent) {
        NetworkSingleton.getNetworkInstance().sendCommand(Command.ACCEPT_CHALLENGE, String.valueOf(challengeNumber));
    }

    public void onChallengeAcceptMenuButtonActivated(ActionEvent actionEvent, String game, String player) {
        NetworkSingleton.getNetworkInstance().sendCommand(Command.CHALLENGE, "\"" + player + "\" \"" + game + "\"");

        hideChallengeAcceptButton();
        showChallengedLabel();
    }

    // TODO: If the player is not in the overview no accept button will appear,
    // TODO: calling the getPlayerList command however causes the screen to not update in time.
    public void handleChallenge(String input) {
        Matcher matcher = challengerPattern.matcher(input);

        if (matcher.find() && matcher.group(1).equals(playerNameLabel.getText())) {
            hideChallengedLabel();
            showChallengeAcceptButton();

            matcher = gamePattern.matcher(input);

            if (matcher.find())
                game = matcher.group(1);
            else
                throw new IllegalArgumentException();

            matcher = numberPattern.matcher(input);

            if (matcher.find())
                challengeNumber = Integer.parseInt(matcher.group(1));
            else
                throw new IllegalArgumentException();
        }
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
}
