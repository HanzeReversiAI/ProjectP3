package com.hanzereversiai.projectp3.ui;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;

public class ConnectionPanelController {
    private ConnectionPanelModel connectionPanelModel;
    public BorderPane rootBorderPane;
    public TextField ipTextField;
    public TextField portTextField;
    public TextField usernameTextField;
    public Label errorLabel;

    @FXML
    public void initialize() {
        connectionPanelModel = new ConnectionPanelModel();
    }

    public void onConnectButtonActivated(ActionEvent actionEvent) {
        String result = connectionPanelModel.startOnlineConnection(
                ipTextField.getText(), portTextField.getText(), usernameTextField.getText());

        if (result.isEmpty())
            createLobbyPanel(actionEvent);

        errorLabel.setText(result);
    }

    public void onOfflineButtonActivated(ActionEvent actionEvent) {
        String result = connectionPanelModel.startOfflineConnection(usernameTextField.getText());

        if (result.isEmpty())
            createLobbyPanel(actionEvent);

        errorLabel.setText(result);
    }

    private void createLobbyPanel(ActionEvent actionEvent) {
        UIHelper.switchScene(actionEvent, "lobby-panel");
    }

    public void setModel(ConnectionPanelModel connectionPanelModel) {
        this.connectionPanelModel = connectionPanelModel;
    }
}
