package com.hanzereversiai.projectp3.ui;

import com.hanzereversiai.projectp3.networking.Network;
import com.hanzereversiai.projectp3.networking.NetworkSingleton;

/**
 * @author Thomas
 */
public class ConnectionPanelModel {
    /**
     * Check the given data and try to start a connection
     * @param ip IP that the connection should use
     * @param port Port that the connection should use
     * @param username Username used for logging in after a connection has been made
     * @return An error if something is wrong
     */
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

    /**
     * Check the given data and store this in the network object
     * @param username Username used for identification
     * @return An error if something is wrong
     */
    public String startOfflineConnection(String username) {
        // Do check to make sure filled in field is viable
        if (username.isEmpty())
            return "Please fill in a username.";

        NetworkSingleton.createNetworkInstance(username);

        return "";
    }
}
