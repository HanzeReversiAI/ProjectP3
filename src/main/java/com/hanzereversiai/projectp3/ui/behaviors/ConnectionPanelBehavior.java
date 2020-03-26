package com.hanzereversiai.projectp3.ui.behaviors;

import com.hanzereversiai.projectp3.ui.ConnectionPanel;
import com.hanzereversiai.projectp3.ui.events.ConnectionSucceededEvent;

public class ConnectionPanelBehavior {
    private ConnectionPanel connectionPanel;

    public ConnectionPanelBehavior(ConnectionPanel connectionPanel) {
        this.connectionPanel = connectionPanel;
    }

    public void onConnectButtonActivated() {
        connectionPanel.fireEvent(new ConnectionSucceededEvent(this, connectionPanel));
    }
}
