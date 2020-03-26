package com.hanzereversiai.projectp3.ui.skins;

import com.hanzereversiai.projectp3.ui.ConnectionPanel;
import com.hanzereversiai.projectp3.ui.behaviors.ConnectionPanelBehavior;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
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
        TextField portTextField = new TextField();

        ipTextField.setPromptText("IP address");
        portTextField.setPromptText("Port number");

        vBox.getChildren().add(ipTextField);
        vBox.getChildren().add(portTextField);

        // Error label
        errorLabel = new Label("ERROR TEXT");
        errorLabel.setVisible(false);
        errorLabel.getStyleClass().add("error-label");
        vBox.getChildren().add(errorLabel);

        // Connect button
        Button connectButton = new Button("Connect");
        connectButton.setOnAction(e -> connectionPanelBehavior.onConnectButtonActivated(
                ipTextField.getText(), portTextField.getText()));
        GridPane.setHalignment(connectButton, HPos.CENTER);
        vBox.getChildren().add(connectButton);
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
