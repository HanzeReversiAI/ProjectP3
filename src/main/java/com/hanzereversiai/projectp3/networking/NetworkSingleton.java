package com.hanzereversiai.projectp3.networking;

import java.io.IOException;

public class NetworkSingleton {
    private static Network networkInstance;

    public static synchronized Network createNetworkInstance(String username) {
        return createNetworkInstance("", 0, username);
    }

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

    public static Network getNetworkInstance() {
        return networkInstance;
    }
}
