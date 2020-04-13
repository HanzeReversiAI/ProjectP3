package com.hanzereversiai.projectp3.networking;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;

/**
 * This class handles all incoming network commands.
 *
 * Other classes can subscribe to this class to be notified of ANY incoming command.
 * To only subscribe to a single command see DelegateInputHandler
 */
public class InputHandler implements Runnable {

    private BufferedReader reader;
    private ArrayList<InputListener> inputListeners;
    private DebugHandler debugHandler;

    /**
     * Constructor
     * @param reader The BufferedReader providing incoming commands
     */
    public InputHandler(BufferedReader reader) {
        this.reader = reader;
        inputListeners = new ArrayList<>();

        debugHandler = new DebugHandler();
        subscribe(debugHandler);
    }

    /**
     * Main loop, checks for incoming commands
     */
    @Override
    public void run() {
        String input;

        try {
            while(!Thread.interrupted() && (input = reader.readLine()) != null) {
                for (InputListener inputListener: inputListeners) {
                    inputListener.handleInput(input);
                }
            }
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Subscribe to be notified of any command
     * @param inputListener InputListener to notify of an incoming command
     */
    public void subscribe(InputListener inputListener) {
        inputListeners.add(inputListener);
    }

    /**
     * Debug InputListener used to print all incoming commands to a console
     */
    private class DebugHandler implements InputListener {
        @Override
        public void handleInput(String input) {
            System.out.println("NETWORK RECEIVED: " + input);
        }
    }
}
