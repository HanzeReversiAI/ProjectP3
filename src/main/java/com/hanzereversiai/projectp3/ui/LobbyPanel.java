package com.hanzereversiai.projectp3.ui;

import javafx.scene.control.Control;
import javafx.scene.control.Skin;

public class LobbyPanel extends Control {
    private LobbyPanelBehavior lobbyPanelBehavior;
    private String username;

    // region Constructors
    public LobbyPanel(String username) {
        this.lobbyPanelBehavior = new LobbyPanelBehavior(this);
        this.username = username;
    }
    // endregion

    // region Overrides
    @Override
    public String getUserAgentStylesheet() {
        return LobbyPanel.class.getResource("/lobby-panel.css").toExternalForm();
    }

    @Override
    protected Skin<?> createDefaultSkin() {
        return new LobbyPanelSkin(this);
    }
    // endregion

    public void createOnlineContent() {

    }

    public void createOfflineContent() {
        getConnectionPanelSkin().createOfflineContent();
    }

    // region Getters and setters
    public LobbyPanelBehavior getConnectionPanelBehavior() {
        return lobbyPanelBehavior;
    }

    public LobbyPanelSkin getConnectionPanelSkin() {
        return (LobbyPanelSkin)getSkin();
    }

    public String getUsername() {
        return username;
    }

    // endregion
}
