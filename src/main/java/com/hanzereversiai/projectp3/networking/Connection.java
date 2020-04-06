package com.hanzereversiai.projectp3.networking;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.net.Socket;

public class Connection {

    private Socket socket;
    private BufferedReader reader;
    private PrintWriter writer;

    private InputHandler inputHandler;

    public Connection(String hostname, int port, int timeout) throws IOException {
        socket = new Socket();
        socket.connect(new InetSocketAddress(hostname, port), timeout);

        reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        writer = new PrintWriter(socket.getOutputStream(), true);

        inputHandler = new InputHandler(reader);
        Thread inputHandlerThread = new Thread(inputHandler);
        inputHandlerThread.start();

        DebugOutputHandler debugOutputHandler = new DebugOutputHandler(writer);
        Thread debugOutputHandlerThread = new Thread(debugOutputHandler);
        debugOutputHandlerThread.start();

        System.out.println("DEBUG: Connection initialized");
    }

    void send(String commandString) {
        writer.println(commandString);
    }

    public InputHandler getInputHandler() {
        return inputHandler;
    }
}
