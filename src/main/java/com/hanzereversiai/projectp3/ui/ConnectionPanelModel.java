package com.hanzereversiai.projectp3.ui;

import com.hanzereversiai.projectp3.networking.Network;
import com.hanzereversiai.projectp3.networking.NetworkSingleton;

public class ConnectionPanelModel {
    public String startOnlineConnection(String ip, String port, String username) {
        int portInteger;

        // Do checks to make sure filled in fields are viable
        if (ip.isEmpty() || port.isEmpty() || username.isEmpty())
            return "Please fill in all fields.";
        else
            try {
                portInteger = Integer.parseInt(port);
            }
            catch (NumberFormatException e) {
                return "Port number must only consist of numbers.";
            }

        // Try to create a connection object, if successful raise an event
        Network network = NetworkSingleton.createNetworkInstance(ip, portInteger, username);
        if (network == null)
            return "Something went wrong, please try again.";

        return "";
    }

    public String startOfflineConnection(String username) {
        // Do check to make sure filled in field is viable
        if (username.isEmpty())
            return "Please fill in a username.";

        NetworkSingleton.createNetworkInstance(username);

        return "";
    }
}
