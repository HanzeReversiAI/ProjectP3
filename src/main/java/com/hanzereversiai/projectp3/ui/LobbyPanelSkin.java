package com.hanzereversiai.projectp3.ui;

import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.SkinBase;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

public class LobbyPanelSkin extends SkinBase<LobbyPanel> {
    LobbyPanel lobbyPanel;
    StackPane lobbyContentStackPane;

    public LobbyPanelSkin(LobbyPanel lobbyPanel) {
        super(lobbyPanel);
        this.lobbyPanel = lobbyPanel;

        VBox rootVBox = new VBox();
        rootVBox.getStyleClass().add("background");
        getChildren().add(rootVBox);

        // Header
        HBox centerHBox = new HBox();
        centerHBox.getStyleClass().add("header");
        centerHBox.setAlignment(Pos.CENTER);
        rootVBox.getChildren().add(centerHBox);

        VBox centerVBox = new VBox();
        centerVBox.getStyleClass().add("header-inner");
        centerVBox.setAlignment(Pos.CENTER);
        centerHBox.getChildren().add(centerVBox);

        Label titleLabel = new Label("Tournament framework");
        titleLabel.getStyleClass().add("title-label");
        centerVBox.getChildren().add(titleLabel);

        Label welcomeLabel = new Label("Welcome back " + lobbyPanel.getUsername());
        welcomeLabel.getStyleClass().add("welcome-label");
        centerVBox.getChildren().add(welcomeLabel);

        // Content
        lobbyContentStackPane = new StackPane();
        lobbyContentStackPane.getStyleClass().add("content");
        VBox.setVgrow(lobbyContentStackPane, Priority.ALWAYS);
        rootVBox.getChildren().add(lobbyContentStackPane);
    }

    public void createOnlineContent() {

    }

    public void createOfflineContent() {
        lobbyContentStackPane.getChildren().add(new OfflineLobbyContent(lobbyPanel));
    }
}
