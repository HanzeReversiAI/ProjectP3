package com.hanzereversiai.projectp3.networking;

import java.io.IOException;

/**
 * Network handles setting up a connection, keeps track of the current Player and handles in and outgoing commands.
 */
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

    private NetworkHandler networkHandler;
    private OnlineLobbyPanelNetworkHandler onlineLobbyPanelNetworkHandler;

    /**
     * Constructor
     * @param username Username to use
     * @param shouldCreateConnection Are we playing online?
     * @throws IOException Throws an IOException if no connection can be made
     */
    public Network(String username, boolean shouldCreateConnection) throws IOException {
        this(DEFAULT_HOSTNAME, DEFAULT_PORT, username, shouldCreateConnection,
                DEFAULT_TIMEOUT);
    }

    /**
     * Constructor
     * @param hostname Hostname to connect to
     * @param port Port to connect to
     * @param username Username to use
     * @throws IOException Throws an IOException if no connection can be made
     */
    public Network(String hostname, int port, String username) throws IOException {
        this(hostname, port, username, true, DEFAULT_TIMEOUT);
    }

    /**
     * Constructor
     * @param hostname Hostname to connect to
     * @param port Port to connect to
     * @param username Username to use
     * @param shouldCreateConnection Are we playing online?
     * @param timeout The server timeout
     * @throws IOException Throws an IOException if no connection can be made
     */
    public Network(String hostname, int port, String username, boolean shouldCreateConnection, int timeout) throws IOException{
        this.hostname = hostname;
        this.port = port;
        this.username = username;
        this.timeout = timeout;
        isConnected = false;

        if (shouldCreateConnection) {
            createConnection();
            delegateInputListener = new DelegateInputListener(this);
            networkHandler = new NetworkHandler(this);
            onlineLobbyPanelNetworkHandler = new OnlineLobbyPanelNetworkHandler(this);
        }
    }

    /**
     * Sets up the connection and sends a login command.
     * @throws IOException Throws an IOException if no connection can be made
     */
    public void createConnection() throws IOException {
        connection = new Connection(hostname, port, timeout);
        isConnected = true;

        sendCommand(Command.LOGIN, username);
    }

    /**
     * Sends a logout command and stops the connection.
     * @throws IOException Throws an IOException if the connection couldn't be closed
     */
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
    public NetworkHandler getNetworkHandler() { return networkHandler; }
    public OnlineLobbyPanelNetworkHandler getOnlineLobbyPanelNetworkHandler() { return onlineLobbyPanelNetworkHandler; }
}
