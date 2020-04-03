package com.hanzereversiai.projectp3.networking;

import java.io.IOException;

public class Network {

    private static final String DEFAULT_HOSTNAME = "127.0.0.1";
    private static final int DEFAULT_PORT = 7789;
    private static final int DEFAULT_TIMEOUT = 30;

    public String hostname;
    public int port;
    public int timeout;

    private Connection connection;

    public Network() throws IOException {
        this(DEFAULT_HOSTNAME, DEFAULT_PORT, DEFAULT_TIMEOUT);
    }

    public Network(String hostname, int port) throws IOException {
        this(hostname, port, DEFAULT_TIMEOUT);
    }

    public Network(String hostname, int port, int timeout) throws IOException{
        this.hostname = hostname;
        this.port = port;
        this.timeout = timeout;

        connection = new Connection(hostname, port, timeout);
    }

    public void Subscribe(InputListener inputListener) {
        connection.getInputHandler().subscribe(inputListener);
    }
    public void SendCommand(Command command, String argument) {
        Command.sendCommand(connection, command, argument);
    }
}
