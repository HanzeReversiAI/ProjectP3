package com.hanzereversiai.projectp3.ui;

import com.hanzereversiai.projectp3.networking.Command;
import com.hanzereversiai.projectp3.networking.NetworkSingleton;
import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

public class OnlineLobbySubscriptionEntry {
    public Label subscriptionStatusLabel;
    public Label gameNameLabel;
    public Button subUnsubButton;

    public void setGameName(String gameName) {
        gameNameLabel.setText(gameName);
    }

    public void setStatusLabel(String status) {
        subscriptionStatusLabel.setText(status);
    }

    public void onSubUnsubButtonActivated(ActionEvent actionEvent) {
        if (subUnsubButton.getText().equals("Subscribe")) {
            setStatusLabel("Subscribed");
            subUnsubButton.setVisible(false);
            NetworkSingleton.getNetworkInstance().SendCommand(Command.SUBSCRIBE, gameNameLabel.getText());
        }
    }
}
