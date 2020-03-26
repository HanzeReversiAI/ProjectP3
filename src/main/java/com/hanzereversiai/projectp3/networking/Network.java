package com.hanzereversiai.projectp3.networking;

import java.io.IOException;

public class Network {

    private static final String defaultHostname = "127.0.0.1";
    private static final int defaultPort = 7789;
    private static final int defaultTimeout = 30;

    public String hostname;
    public int port;
    public int timeout;

    private Connection connection;
    private Thread thread;

    public Network() throws IOException {
        this(defaultHostname, defaultPort, defaultTimeout);
    }

    public Network(String hostname, int port, int timeout) throws IOException{
        this.hostname = hostname;
        this.port = port;
        this.timeout = timeout;

        connection = new Connection(hostname, port, timeout);
        thread = new Thread(connection);
        thread.start();
    }





}
