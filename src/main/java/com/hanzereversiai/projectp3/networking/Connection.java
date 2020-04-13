package com.hanzereversiai.projectp3.networking;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.net.Socket;

/**
 * Connection class
 *
 * This class keeps track of a socket connection to the game server, and sets up in and output.
 *
 * @author Mike
 */
public class Connection {

    private Socket socket;
    private BufferedReader reader;
    private PrintWriter writer;

    private InputHandler inputHandler;
    private Thread inputHandlerThread;
    private Thread debugOutputHandlerThread;

    /**
     * Constructor
     * @param hostname The hostname to connect to
     * @param port The port to connect to
     * @param timeout The connection timeout
     * @throws IOException Throws an IOException if no connection can be made
     */
    public Connection(String hostname, int port, int timeout) throws IOException {
        socket = new Socket();
        socket.connect(new InetSocketAddress(hostname, port), timeout);

        reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        writer = new PrintWriter(socket.getOutputStream(), true);

        inputHandler = new InputHandler(reader);
        inputHandlerThread = new Thread(inputHandler);
        inputHandlerThread.start();

        DebugOutputHandler debugOutputHandler = new DebugOutputHandler(writer);
        debugOutputHandlerThread = new Thread(debugOutputHandler);
        debugOutputHandlerThread.start();

        System.out.println("NETWORK: Connection initialized");
    }

    /**
     * Close the Connection
     * @throws IOException Throws an IOException if the connection couldn't be closed.
     */
    public void close() throws IOException {
        inputHandlerThread.interrupt();
        debugOutputHandlerThread.interrupt();

        reader.close();
        writer.close();
        socket.close();

        System.out.println("NETWORK: Connection closed");
    }

    /**
     * Send a string over the network
     * @param commandString The string to send
     */
    void send(String commandString) {
        writer.println(commandString);
    }

    /**
     * Get the InputHandler belonging to this Connection
     * @return InputHandler
     */
    public InputHandler getInputHandler() {
        return inputHandler;
    }
}
