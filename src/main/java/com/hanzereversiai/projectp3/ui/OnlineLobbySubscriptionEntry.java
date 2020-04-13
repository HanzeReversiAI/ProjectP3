package com.hanzereversiai.projectp3.ui;

import com.hanzereversiai.projectp3.networking.Command;
import com.hanzereversiai.projectp3.networking.NetworkSingleton;
import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

/**
 * UI Controller that allows you to subscribe to a game type
 *
 * @author Mike
 */
public class OnlineLobbySubscriptionEntry {
    public Label subscriptionStatusLabel;
    public Label gameNameLabel;
    public Button subUnsubButton;

    /**
     * Set the game type
     * @param gameName game type provided by the server
     */
    public void setGameName(String gameName) {
        gameNameLabel.setText(gameName);
    }

    /**
     * Set the status of the subscription
     * @param status (un)subscribed
     */
    public void setStatusLabel(String status) {
        subscriptionStatusLabel.setText(status);
    }

    /**
     * Handle the subscribe button
     * @param actionEvent UI actionEvent
     */
    public void onSubUnsubButtonActivated(ActionEvent actionEvent) {
        if (subUnsubButton.getText().equals("Subscribe")) {
            setStatusLabel("Subscribed");
            subUnsubButton.setVisible(false);
            NetworkSingleton.getNetworkInstance().sendCommand(Command.SUBSCRIBE, gameNameLabel.getText());
        }
    }
}
