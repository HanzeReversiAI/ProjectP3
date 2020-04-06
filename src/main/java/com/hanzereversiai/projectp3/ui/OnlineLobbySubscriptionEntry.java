package com.hanzereversiai.projectp3.ui;

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

    }
}
