package com.hanzereversiai.projectp3.ui;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;

/**
 * @author Thomas
 */
public class ConnectionPanelController {
    private ConnectionPanelModel connectionPanelModel;
    public BorderPane rootBorderPane;
    public TextField ipTextField;
    public TextField portTextField;
    public TextField usernameTextField;
    public Label errorLabel;

    /**
     * Set the model of this controller.
     */
    @FXML
    public void initialize() {
        connectionPanelModel = new ConnectionPanelModel();
    }

    /**
     * Start an online connection and switch the scene or display an error.
     * @param actionEvent The event that fired this method
     */
    public void onConnectButtonActivated(ActionEvent actionEvent) {
        String result = connectionPanelModel.startOnlineConnection(
                ipTextField.getText(), portTextField.getText(), usernameTextField.getText());

        if (result.isEmpty())
            UIHelper.switchScene(actionEvent, "lobby-panel");

        errorLabel.setText(result);
    }

    /**
     * Start an offline connection by only storing data in the network object and switch the scene or display an error.
     * @param actionEvent The event that fired this method
     */
    public void onOfflineButtonActivated(ActionEvent actionEvent) {
        String result = connectionPanelModel.startOfflineConnection(usernameTextField.getText());

        if (result.isEmpty()) {
            UIHelper.switchScene(actionEvent, "lobby-panel");
        }

        errorLabel.setText(result);
    }
}
