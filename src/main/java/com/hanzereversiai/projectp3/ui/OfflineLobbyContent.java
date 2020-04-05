package com.hanzereversiai.projectp3.ui;

import javafx.scene.control.Control;
import javafx.scene.control.Skin;

public class OfflineLobbyContent extends Control {
    private OfflineLobbyContentBehavior offlineLobbyContentBehavior;

    // region Constructors
    public OfflineLobbyContent(LobbyPanel lobbyPanel) {
        this.offlineLobbyContentBehavior = new OfflineLobbyContentBehavior(this, lobbyPanel);
    }
    // endregion

    // region Overrides
    @Override
    public String getUserAgentStylesheet() {
        return OfflineLobbyContent.class.getResource("/offline-content.css").toExternalForm();
    }

    @Override
    protected Skin<?> createDefaultSkin() {
        return new OfflineLobbyContentSkin(this);
    }
    // endregion

    // region Getters and setters
    public OfflineLobbyContentBehavior getOfflineLobbyContentBehavior() {
        return offlineLobbyContentBehavior;
    }

    public OfflineLobbyContentSkin getOfflineLobbyContentSkin() {
        return (OfflineLobbyContentSkin)getSkin();
    }
    // endregion
}
