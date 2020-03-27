package com.hanzereversiai.projectp3.ui;

import com.hanzereversiai.projectp3.ui.behaviors.ConnectionPanelBehavior;
import com.hanzereversiai.projectp3.ui.skins.ConnectionPanelSkin;
import javafx.scene.control.Control;
import javafx.scene.control.Skin;

public class ConnectionPanel extends Control {
    private ConnectionPanelBehavior connectionPanelBehavior;

    // region Constructors
    public ConnectionPanel() {
        this.connectionPanelBehavior = new ConnectionPanelBehavior(this);
    }
    // endregion

    // region Overrides
    @Override
    public String getUserAgentStylesheet() {
        return ConnectionPanel.class.getResource("/connection-panel.css").toExternalForm();
    }

    @Override
    protected Skin<?> createDefaultSkin() {
        return new ConnectionPanelSkin(this);
    }
    // endregion

    // region Getters and setters
    public ConnectionPanelBehavior getConnectionPanelBehavior() {
        return connectionPanelBehavior;
    }

    public ConnectionPanelSkin getConnectionPanelSkin() {
        return (ConnectionPanelSkin)getSkin();
    }
    // endregion
}
