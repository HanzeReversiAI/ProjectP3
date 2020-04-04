package com.hanzereversiai.projectp3.ui.skins;

import com.hanzereversiai.projectp3.ui.ConnectionPanel;
import com.hanzereversiai.projectp3.ui.behaviors.ConnectionPanelBehavior;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class ConnectionPanelSkin extends SkinBase<ConnectionPanel> {
    Label errorLabel;

    public ConnectionPanelSkin(ConnectionPanel connectionPanel) {
        super(connectionPanel);
        ConnectionPanelBehavior connectionPanelBehavior = connectionPanel.getConnectionPanelBehavior();

        // Layout
        BorderPane rootBorderPane = new BorderPane();
        rootBorderPane.getStyleClass().add("border-pane");

        VBox vBox = new VBox();
        vBox.setMaxSize(Control.USE_PREF_SIZE, Control.USE_PREF_SIZE);
        vBox.setPadding(new Insets(25, 25, 25, 25));
        vBox.getStyleClass().add("vertical-box");

        rootBorderPane.setCenter(vBox);
        getChildren().add(rootBorderPane);

        // Title label
        Label titleLabel = new Label("Online tournament framework");
        titleLabel.getStyleClass().add("title-label");
        GridPane.setHalignment(titleLabel, HPos.CENTER);
        vBox.getChildren().add(titleLabel);

        // Input fields
        TextField ipTextField = new TextField();
        ipTextField.setPromptText("IP address");
        vBox.getChildren().add(ipTextField);

        TextField portTextField = new TextField();
        portTextField.setPromptText("Port number");
        vBox.getChildren().add(portTextField);

        /*
        TextField usernameInputField = new TextField();
        usernameInputField.setPromptText("Username");
        vBox.getChildren().add(usernameInputField);
         */

        // Error label
        errorLabel = new Label("ERROR TEXT");
        errorLabel.getStyleClass().add("error-label");
        errorLabel.setVisible(false);
        vBox.getChildren().add(errorLabel);

        // Connect button and offline button
        // Layout
        HBox buttonsLayout = new HBox();
        buttonsLayout.setAlignment(Pos.CENTER);
        buttonsLayout.setSpacing(10);
        vBox.getChildren().add(buttonsLayout);

        // Connect button
        Button connectButton = new Button("Connect");
        connectButton.getStyleClass().add("connect-button");
        connectButton.setOnAction(e -> connectionPanelBehavior.onConnectButtonActivated(
                ipTextField.getText(), portTextField.getText()));
        GridPane.setHalignment(connectButton, HPos.CENTER);
        buttonsLayout.getChildren().add(connectButton);

        // Offline button
        Button offlineButton = new Button("Offline");
        offlineButton.getStyleClass().add("offline-button");
        offlineButton.setOnAction(e -> connectionPanelBehavior.onOfflineButtonActivated());
        GridPane.setHalignment(offlineButton, HPos.CENTER);
        buttonsLayout.getChildren().add(offlineButton);
    }

    public void setErrorLabel(String text) {
        errorLabel.setText(text);
        errorLabel.setVisible(true);
    }

    public void resetErrorLabel() {
        errorLabel.setText("");
        errorLabel.setVisible(false);
    }
}
