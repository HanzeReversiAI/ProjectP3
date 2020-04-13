package com.hanzereversiai.projectp3.networking;

import java.io.IOException;

/**
 * Network handles setting up a connection, keeps track of the current Player and handles in and outgoing commands.
 *
 * @author Mike
 */
public class Network {
    private static final String DEFAULT_HOSTNAME = "127.0.0.1";
    private static final int DEFAULT_PORT = 7789;
    private static final int DEFAULT_TIMEOUT = 30;

    public String hostname;
    public int port;
    public int timeout;
    private boolean isConnected;

    // User data
    private String username;
    private int aiDepthAmount = 5;

    private Connection connection;
    private DelegateInputHandler delegateInputHandler;

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
            delegateInputHandler = new DelegateInputHandler(this);
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

    /**
     * Get the username of the local player
     * @return The username of the local player
     */
    public String getUsername() {
        return username;
    }

    /**
     * Returns the amount of moves the AI should think ahead
     * @return The amount of moves the AI should think ahead
     */
    public int getAiDepthAmount() {
        return aiDepthAmount;
    }

    /**
     * Sets the amount of moves the AI should think ahead
     * @param aiDepthAmount The amount of moves the AI should think ahead
     */
    public void setAiDepthAmount(int aiDepthAmount) {
        this.aiDepthAmount = aiDepthAmount;
    }

    /**
     * Are we connected to a server?
     * @return Are we connected to a server?
     */
    public boolean isConnected() {
        return isConnected;
    }

    /**
     * Subscribe to the Connection's InputHandler
     * @param inputListener InputListener to notify
     */
    public void subscribe(InputListener inputListener) {
        connection.getInputHandler().subscribe(inputListener);
    }

    /**
     * Send a command through the Connection using the Command ENUM
     * @param command The command to send
     * @param parameter The parameter to use with the command
     */
    public void sendCommand(Command command, String parameter) {
        Command.sendCommand(connection, command, parameter);
    }

    /**
     * Send a command through the Connection using the Command ENUM
     * @param command The command to send
     */
    public void sendCommand(Command command) {
        Command.sendCommand(connection, command);
    }

    /**
     * Get the DelegateInputHandler belonging to the Network instance
     * @return DelegateInputHandler belonging to the Network instance
     */
    public DelegateInputHandler getDelegateInputHandler() {
        return delegateInputHandler;
    }
    /**
     * Get the NetworkHandler belonging to the Network instance
     * @return NetworkHandler belonging to the Network instance
     */
    public NetworkHandler getNetworkHandler() { return networkHandler; }
    /**
     * Get the OnlineLobbyPanelNetworkHandler belonging to the Network instance
     * @return OnlineLobbyPanelNetworkHandler belonging to the Network instance
     */
    public OnlineLobbyPanelNetworkHandler getOnlineLobbyPanelNetworkHandler() { return onlineLobbyPanelNetworkHandler; }
}
