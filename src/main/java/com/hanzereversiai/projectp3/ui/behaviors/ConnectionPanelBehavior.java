package com.hanzereversiai.projectp3.ui.behaviors;

import com.hanzereversiai.projectp3.networking.Network;
import com.hanzereversiai.projectp3.ui.ConnectionPanel;
import com.hanzereversiai.projectp3.ui.events.ConnectionRaisedEvent;

import java.io.IOException;

public class ConnectionPanelBehavior {
    private ConnectionPanel connectionPanel;

    public ConnectionPanelBehavior(ConnectionPanel connectionPanel) {
        this.connectionPanel = connectionPanel;
    }

    public void onConnectButtonActivated(String ip, String port) {
        int portInteger = 0;

        connectionPanel.getConnectionPanelSkin().resetErrorLabel();

        // Do checks to make sure filled in fields are viable
        if (ip.isEmpty() || port.isEmpty()) {
            connectionPanel.getConnectionPanelSkin().setErrorLabel("Please fill in all fields.");
            return;
        }
        else {
            try {
                portInteger = Integer.parseInt(port);
            }
            catch (NumberFormatException e) {
                connectionPanel.getConnectionPanelSkin().setErrorLabel("Port number must only consist of numbers.");
                return;
            }
        }

        // Try to create a connection object, if successful raise an event
        try {
            Network network = new Network(ip, portInteger);
            connectionPanel.fireEvent(new ConnectionRaisedEvent(this, connectionPanel, network));
        }
        catch (IOException e) {
            connectionPanel.getConnectionPanelSkin().setErrorLabel("Something went wrong, please try again.");
        }
    }
}
