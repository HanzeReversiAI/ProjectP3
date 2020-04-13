package com.hanzereversiai.projectp3.networking;

import java.io.IOException;

/**
 * NetworkSingleton used by the UI to access the Network instance
 *
 * @author Mike
 */
public class NetworkSingleton {
    private static Network networkInstance;

    /**
     * Create a Network instance
     * @param username The username to connect with
     * @return Returns the initialized Network instance
     */
    public static synchronized Network createNetworkInstance(String username) {
        return createNetworkInstance("", 0, username);
    }

    /**
     * Create a Network instance
     * @param ip The ip to connect to
     * @param port The port to connect to
     * @param username The username to connect with
     * @return Returns the initialized Network instance
     */
    public static synchronized Network createNetworkInstance(String ip, Integer port, String username) {
        try {
            if (ip.isEmpty())
                networkInstance = new Network(username, false);
            else
                networkInstance = new Network(ip, port, username);
        } catch (IOException e) {
            return null;
        }

        return networkInstance;
    }

    /**
     * Get the Network instance
     * @return The Network instance
     */
    public static Network getNetworkInstance() {
        return networkInstance;
    }
}
