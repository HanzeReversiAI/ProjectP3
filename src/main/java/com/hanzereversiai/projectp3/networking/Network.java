package com.hanzereversiai.projectp3.networking;

import java.io.IOException;

public class Network {
    private static final String DEFAULT_HOSTNAME = "127.0.0.1";
    private static final int DEFAULT_PORT = 7789;
    private static final int DEFAULT_TIMEOUT = 30;

    public String hostname;
    public int port;
    private String username;
    public int timeout;
    private boolean isConnected;

    private Connection connection;
    private DelegateInputListener delegateInputListener;

    public Network(String username, boolean shouldCreateConnection) throws IOException {
        this(DEFAULT_HOSTNAME, DEFAULT_PORT, username, shouldCreateConnection,
                DEFAULT_TIMEOUT);
    }

    public Network(String hostname, int port, String username) throws IOException {
        this(hostname, port, username, true, DEFAULT_TIMEOUT);
    }

    public Network(String hostname, int port, String username, boolean shouldCreateConnection, int timeout) throws IOException{
        this.hostname = hostname;
        this.port = port;
        this.username = username;
        this.timeout = timeout;
        isConnected = false;

        if (shouldCreateConnection) {
            createConnection();
            delegateInputListener = new DelegateInputListener(this);
        }
    }

    public void createConnection() throws IOException {
        connection = new Connection(hostname, port, timeout);
        isConnected = true;

        sendCommand(Command.LOGIN, username);
    }

    public void stopConnection() throws IOException {
        if (isConnected) {
            sendCommand(Command.LOGOUT);
            connection.close();
            isConnected = false;
        }
    }

    public String getUsername() {
        return username;
    }

    public boolean isConnected() {
        return isConnected;
    }

    public void subscribe(InputListener inputListener) {
        connection.getInputHandler().subscribe(inputListener);
    }

    public void sendCommand(Command command, String argument) {
        Command.sendCommand(connection, command, argument);
    }

    public void sendCommand(Command command) {
        Command.sendCommand(connection, command);
    }

    public DelegateInputListener getDelegateInputListener() {
        return delegateInputListener;
    }
}
